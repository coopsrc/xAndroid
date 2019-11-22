package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.plugins.RxJavaPlugins
import java.io.File
import java.io.InterruptedIOException
import java.net.SocketException

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 08:14
 */
class DownloadCore(var config: DownloaderConfig) : ITaskRepo {

    init {
        initRxJavaPlugin()
    }

    private fun initRxJavaPlugin() {
        RxJavaPlugins.setErrorHandler {
            when (it) {
                is InterruptedException -> Logger.e("InterruptedException $it")
                is InterruptedIOException -> Logger.e("InterruptedIOException $it")
                is SocketException -> Logger.e("SocketException $it")
            }
        }
    }

    override fun taskList(): Maybe<List<TaskInfo>> {
        return config.taskRepo.taskList()
    }

    override fun isExist(taskInfo: TaskInfo): Maybe<Boolean> {
        return config.taskRepo.isExist(taskInfo)
    }

    override fun create(taskInfo: TaskInfo): Flowable<Progress> {
        return config.taskRepo.create(taskInfo)
    }

    override fun resume(taskInfo: TaskInfo): Flowable<Progress> {
        return config.taskRepo.resume(taskInfo)
    }

    override fun start(taskInfo: TaskInfo): Maybe<Any> {
        return config.taskRepo.start(taskInfo)
    }

    override fun startAll(): Maybe<Any> {
        return config.taskRepo.startAll()
    }

    override fun stop(taskInfo: TaskInfo): Maybe<Any> {
        return config.taskRepo.stop(taskInfo)
    }

    override fun stopAll(): Maybe<Any> {
        return config.taskRepo.stopAll()
    }

    override fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any> {
        return config.taskRepo.remove(taskInfo, delete)
    }

    override fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any> {
        return config.taskRepo.remove(taskInfo, delete)
    }

    override fun targetFile(taskInfo: TaskInfo): Maybe<File> {
        return config.taskRepo.targetFile(taskInfo)
    }
}