package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.internal.operators.maybe.MaybeToPublisher
import java.io.File
import java.util.concurrent.Semaphore

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:19
 */
internal class LocalTaskRepo(maxTask: Int) : TaskRepo() {
    private val semaphore = Semaphore(maxTask, true)

    private val tag = "LocalTaskRepo"

    override fun isExist(taskInfo: TaskInfo): Maybe<Boolean> {
        Logger.i(tag, "isExist: $taskInfo")

        return Maybe.create {
            find(taskInfo) != null
        }
    }

    override fun create(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i(tag, "create: $taskInfo")

        var downloadTask: DownloadTask? = find(taskInfo)
        if (downloadTask == null) {
            downloadTask = DownloadTask(taskInfo, semaphore)
            add(downloadTask)
        }

        return downloadTask.getFlowable()
    }

    override fun resume(taskInfo: TaskInfo): Flowable<Progress> {
        Logger.i(tag, "resume: $taskInfo")
        return create(taskInfo)
    }

    override fun start(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i(tag, "start: $taskInfo")

        val downloadTask = find(taskInfo) ?: return Maybe.error(taskNotAdded())

        return downloadTask.start()
    }

    override fun startAll(): Maybe<Any> {
        Logger.i(tag, "startAll:")

        return Flowable.fromIterable(taskList.filter {
            it.taskInfo.progress.status.canStart()
        }.map {
            it.start()
        }).flatMap(MaybeToPublisher.INSTANCE).lastElement()
    }

    override fun stop(taskInfo: TaskInfo): Maybe<Any> {
        Logger.i(tag, "stop: $taskInfo")

        val downloadTask = find(taskInfo) ?: return Maybe.error(taskNotAdded())

        return downloadTask.stop()
    }

    override fun stopAll(): Maybe<Any> {
        Logger.i(tag, "stopAll:")

        return Flowable.fromIterable(taskList.filter {
            it.taskInfo.progress.status.canStop()
        }.map {
            it.stop()
        }).flatMap(MaybeToPublisher.INSTANCE).lastElement()
    }

    override fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any> {
        Logger.i(tag, "remove: $taskInfo")

        val downloadTask = find(taskInfo) ?: return Maybe.error(taskNotAdded())

        remove(downloadTask)

        return downloadTask.remove(delete)
    }

    override fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any> {
        Logger.i(tag, "remove: $taskInfo")

        return Flowable.fromIterable(taskInfo.map {
            find(it) as DownloadTask
        }.map {
            it.remove(delete)
        }).flatMap(MaybeToPublisher.INSTANCE).lastElement()
    }

    override fun targetFile(taskInfo: TaskInfo): Maybe<File> {
        Logger.i(tag, "targetFile: $taskInfo")

        val downloadTask = find(taskInfo) ?: return Maybe.error(taskNotAdded())

        return downloadTask.targetFile()
    }

    private fun taskNotAdded(): Exception {
        return NoSuchElementException("Task not added!")
    }
}