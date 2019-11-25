package com.coopsrc.xandroid.downloader

import android.util.Log
import com.coopsrc.xandroid.downloader.core.DownloadCore
import com.coopsrc.xandroid.downloader.core.DownloaderConfig
import com.coopsrc.xandroid.downloader.core.ITaskRepo
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:11
 */
object ExDownloader : ITaskRepo {
    lateinit var downloadCore: DownloadCore

    private var initialized: Boolean = false

    fun init() {
        Log.i("ExDownloader", "init：")
        downloadCore = DownloadCore(DownloaderConfig())
        initialized = true
    }

    fun init(config: DownloaderConfig) {
        Log.i("ExDownloader", "init：$config")
        downloadCore = DownloadCore(config)
        initialized = true
    }

    override fun taskList(): Maybe<List<TaskInfo>> {
        Logger.i("taskList: ")
        checkInit()
        return downloadCore.taskList()
    }

    override fun isExist(taskInfo: TaskInfo): Maybe<Boolean> {
        Logger.i("isExist: $taskInfo")
        checkInit()
        return downloadCore.isExist(taskInfo)
    }

    override fun create(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i("create: $taskInfo")
        checkInit()
        return downloadCore.create(taskInfo)
    }

    override fun resume(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i("resume: $taskInfo")
        checkInit()
        return downloadCore.resume(taskInfo)
    }

    override fun start(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i("start: $taskInfo")
        checkInit()
        return downloadCore.start(taskInfo)
    }

    override fun startAll(): Maybe<Any> {
        Logger.i("startAll: ")
        checkInit()
        return downloadCore.startAll()
    }

    override fun stop(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i("stop: $taskInfo")
        checkInit()
        return downloadCore.stop(taskInfo)
    }

    override fun stopAll(): Maybe<Any> {
        Logger.i("stopAll: ")
        checkInit()
        return downloadCore.stopAll()
    }

    override fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any> {
        Logger.i("remove: $taskInfo, $delete")
        checkInit()
        return downloadCore.remove(taskInfo, delete)
    }

    override fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any> {
        Logger.i("remove: $taskInfo, $delete")
        checkInit()
        return downloadCore.remove(taskInfo, delete)
    }

    override fun targetFile(taskInfo: TaskInfo): Maybe<File> {
        Logger.i("targetFile: $taskInfo")
        checkInit()
        return downloadCore.targetFile(taskInfo)
    }

    private fun checkInit() {
        if (!initialized) {
            throw RuntimeException("ExDownloader has not been initialized !")
        }
    }

    internal fun withDebug(): Boolean {
        return if (initialized) {
            downloadCore.config.withDebug
        } else {
            BuildConfig.DEBUG
        }
    }
}