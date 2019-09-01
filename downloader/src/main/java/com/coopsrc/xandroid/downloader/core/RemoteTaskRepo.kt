package com.coopsrc.xandroid.downloader.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.coopsrc.xandroid.downloader.core.service.DownloadBinder
import com.coopsrc.xandroid.downloader.core.service.DownloadService
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-08-08
 * Time: 10:24
 */
internal class RemoteTaskRepo(private val context: Context, private val maxTask: Int) : TaskRepo() {
    private val tag = "RemoteTaskRepo"

    private var downloadBinder: DownloadBinder? = null

    init {
        bindService {}
    }

    override fun isExist(taskInfo: TaskInfo): Maybe<Boolean> {
        Logger.i(tag, "isExist: $taskInfo")
        return Maybe.create<Boolean> { emitter ->
            bindService { downloadBinder ->
                downloadBinder.isExist(taskInfo).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun create(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i(tag, "create: $taskInfo")
        return Flowable.create<Progress>({ emitter ->
            bindService { downloadBinder ->
                downloadBinder.create(taskInfo).subscribe({
                    emitter.onNext(it)
                }, {
                    emitter.onError(it)
                }, {
                    emitter.onComplete()
                })
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.newThread())
    }

    override fun resume(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i(tag, "resume: $taskInfo")
        return Flowable.create<Progress>({ emitter ->
            bindService { binder ->
                binder.resume(taskInfo).subscribe({
                    emitter.onNext(it)
                }, {
                    emitter.onError(it)
                }, {
                    emitter.onComplete()
                })
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.newThread())
    }

    override fun start(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i(tag, "start: $taskInfo")
        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.start(taskInfo).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun startAll(): Maybe<Any> {
        Logger.i(tag, "startAll:")

        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.startAll().subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun stop(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i(tag, "stop: $taskInfo")
        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.stop(taskInfo).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun stopAll(): Maybe<Any> {
        Logger.i(tag, "stopAll:")

        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.stopAll().subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any> {
        Logger.i(tag, "remove: $taskInfo")
        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.remove(taskInfo, delete).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any> {
        Logger.i(tag, "remove: $taskInfo")

        return Maybe.create<Any> { emitter ->
            bindService { binder ->
                binder.remove(taskInfo, delete).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    override fun targetFile(taskInfo: TaskInfo): Maybe<File> {
        Logger.i(tag, "targetFile: $taskInfo")
        return Maybe.create<File> { emitter ->
            bindService { binder ->
                binder.targetFile(taskInfo).subscribe({
                    emitter.onSuccess(it)
                }, {
                    emitter.onError(it)
                })
            }
        }.subscribeOn(Schedulers.newThread())
    }

    @Synchronized
    private fun bindService(onServiceConnected: (binder: DownloadBinder) -> Unit) {
        if (downloadBinder != null) {
            onServiceConnected(downloadBinder!!)
        } else {
            val intent = Intent(context, DownloadService::class.java)
            intent.putExtra("maxTask", maxTask)
            context.startService(intent)
            context.bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    Logger.i(tag, "ServiceConnected")
                    downloadBinder = service as DownloadBinder
                    onServiceConnected(downloadBinder!!)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    Logger.w(tag, "ServiceDisconnected")
                    downloadBinder = null
                }
            }, Context.BIND_AUTO_CREATE)
        }
    }
}