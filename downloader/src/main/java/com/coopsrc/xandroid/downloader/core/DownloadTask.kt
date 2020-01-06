package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.api.DownloadApiProxy
import com.coopsrc.xandroid.downloader.core.impl.NormalDownloader
import com.coopsrc.xandroid.downloader.core.impl.RangeDownloader
import com.coopsrc.xandroid.downloader.core.impl.SingleDownloader
import com.coopsrc.xandroid.downloader.db.DatabaseModule
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.model.Type
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.DownloaderUtils
import com.coopsrc.xandroid.downloader.utils.HttpUtils
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.File
import java.util.concurrent.Semaphore

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:22
 */
internal class DownloadTask(var taskInfo: TaskInfo, private val semaphore: Semaphore) {

    private val mProgressProcessor: FlowableProcessor<Progress> =
        BehaviorProcessor.create<Progress>().toSerialized()

    private var disposable: Disposable? = null
    private lateinit var downloader: Downloader
    private lateinit var mDownloadFlowable: Flowable<Progress>

    private val autoStart = ExDownloader.downloadCore.config.autoStart
    private val databaseAction = DatabaseModule.instance.taskInfoAction

    private var semaphoreRequired = false

    private val tag = "DownloadTask"

    init {
        Logger.i(tag, "init")

        Maybe.create<Any> { emitter ->
            createFlowable()
            initTask()
            emitter.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread()).doOnError {
            Logger.e(tag, "update error! $it")
        }.subscribe {
            emitProgress()
            if (autoStart) {
                subscribe()
            }
        }
    }

    private fun createFlowable() {
        Logger.i(tag, "createFlowable")

        mDownloadFlowable = Flowable.just(Constants.any)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                emitStatus(Status.Waiting)

                if (!databaseAction.exist(taskInfo)) {
                    databaseAction.create(taskInfo)
                }

                semaphore.acquire()
                semaphoreRequired = true
            }.subscribeOn(Schedulers.newThread())
            .flatMap {
                detectAndDownload()
            }.doOnError {
                Logger.e(tag, "Download task error! ${it.message}")
                emitStatus(Status.Failed)
            }.doOnCancel {
                Logger.d(tag, "Download task cancel!")
                if (taskInfo.progress.status != Status.Complete) {
                    emitStatus(Status.Suspend)
                }
            }.doOnComplete {
                Logger.d(tag, "Download task complete!")
                emitStatus(Status.Complete)
            }.doFinally {
                Logger.d("DownloadTask", "Download task finally!")
                databaseAction.update(taskInfo)
                disposable = null
                if (semaphoreRequired) {
                    semaphore.release()
                    semaphoreRequired = false
                }
            }
    }

    private fun detectAndDownload(): Flowable<Progress> {
        emitStatus(Status.Starting)
        Logger.i(tag, "detectAndDownload")
        return detect().flatMapPublisher {
            emitStatus(Status.Prepared)
            download()
        }
    }

    private fun detect(): Maybe<Any> {
        Logger.i(tag, "detect")

        return DownloadApiProxy.detect(taskInfo.url).flatMap {
            taskInfo.progress.update(updated = HttpUtils.lastModified(it))
            if (taskInfo.type == null) {
                setupResponse(it)
            } else {
                Maybe.just(Constants.any)
            }
        }
    }

    private fun download(): Flowable<out Progress> {
        Logger.i(tag, "download")
        emitStatus(Status.Downloading)
        return downloader.download()
    }

    private fun setupResponse(response: Response<Void>): Maybe<Any> {
        Logger.i(tag, "setupResponse: $response")

        taskInfo.savePath = if (taskInfo.savePath.isEmpty()) {
            ExDownloader.downloadCore.config.workPath
        } else {
            taskInfo.savePath
        }

        taskInfo.saveName = HttpUtils.fileName(taskInfo.saveName, taskInfo.url, response)

        if (HttpUtils.isSupportRange(response)) {
            taskInfo.progress.totalSize = HttpUtils.contentRangeLength(response)

            if (ExDownloader.downloadCore.config.rangeMode.count > 1) {
                taskInfo.update(type = Type.Range)
            } else {
                taskInfo.update(type = Type.Single)
            }
        } else {
            taskInfo.progress.totalSize = HttpUtils.contentLength(response)
            taskInfo.update(type = Type.Normal)
        }

        taskInfo.progress.update(lastModified = HttpUtils.lastModified(response))
        databaseAction.update(taskInfo)

        downloader = generateDownloader()

        return Maybe.just(Constants.any)
    }

    private fun generateDownloader(): Downloader {
        Logger.i(tag, "generateDownloader: $taskInfo")

        return when (taskInfo.type) {
            Type.Normal -> NormalDownloader(this)
            Type.Single -> SingleDownloader(this)
            Type.Range -> RangeDownloader(this)
            else -> NormalDownloader(this)
        }
    }

    private fun initTask() {
        Logger.i(tag, "initTask")

        databaseAction.read(taskInfo)

        Logger.i(tag, "initTask: $taskInfo")

        if (taskInfo.progress.status.isWorking()) {
            taskInfo.progress.update(status = Status.Idle)
        }

        resumeDownloader()
    }

    private fun resumeDownloader() {
        Logger.i(tag, "resumeDownloader: $taskInfo")

        when (taskInfo.type) {
            Type.Normal -> downloader = NormalDownloader(this)
            Type.Single -> downloader = SingleDownloader(this)
            Type.Range -> downloader = RangeDownloader(this)
        }
    }

    private fun emitStatus(status: Status) {
        Logger.v(tag, "emitStatus $status")
        taskInfo.progress.update(status = status)
        mProgressProcessor.onNext(taskInfo.progress)
    }

    private fun emitProgress(progress: Progress) {
        Logger.v(tag, "emitProgress $progress")
        taskInfo.progress.update(progress)
        mProgressProcessor.onNext(taskInfo.progress)
    }

    private fun emitProgress() {
        Logger.v(tag, "emitProgress ${taskInfo.progress}")
        mProgressProcessor.onNext(taskInfo.progress)
    }

    fun getFlowable(): Flowable<Progress> {
        return mProgressProcessor
    }

    fun start(): Maybe<Any> {
        Logger.i(tag, "start")

        return Maybe.create<Any> {
            subscribe()
            it.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread())
    }

    private fun subscribe() {
        Logger.i(tag, "subscribe")

        if (disposable == null) {
            disposable = mDownloadFlowable.subscribe {
                Logger.v(tag, "update download progress: $it")
                databaseAction.update(it)
                emitProgress(it)
            }
        }
    }

    fun stop(): Maybe<Any> {
        Logger.i(tag, "stop")

        return Maybe.create<Any> {
            unSubscribe()
            it.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread())
    }

    private fun unSubscribe() {
        Logger.i(tag, "unSubscribe")

        DownloaderUtils.dispose(disposable)
        disposable = null
    }

    fun remove(delete: Boolean): Maybe<Any> {
        Logger.i(tag, "stop: $delete")

        return Maybe.create<Any> {
            unSubscribe()

            if (delete) {
                downloader.remove()
            }
            emitStatus(Status.Removed)

            it.onSuccess(Constants.any)
        }.subscribeOn(Schedulers.newThread())
    }

    fun targetFile(): Maybe<File> {
        Logger.i(tag, "targetFile")

        return Maybe.create<File> {
            val file = downloader.targetFile()
            if (file == null) {
                it.onError(RuntimeException("No such file"))
            } else {
                it.onSuccess(file)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadTask

        if (taskInfo != other.taskInfo) return false

        return true
    }

    override fun hashCode(): Int {
        return taskInfo.hashCode()
    }

    override fun toString(): String {
        return "DownloadTask(taskInfo=$taskInfo, semaphore=$semaphore, autoStart=$autoStart, databaseAction=$databaseAction)"
    }

}