package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.model.TaskInfo
import io.reactivex.Maybe
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:20
 */
internal abstract class TaskRepo : ITaskRepo {
    private val taskQueue = LinkedList<DownloadTask>()

    protected val taskList: LinkedList<DownloadTask>
        get() = taskQueue

    override fun taskList(): Maybe<List<TaskInfo>> {
        return Maybe.just(taskQueue.map {
            it.taskInfo
        })
    }

    protected fun find(taskInfo: TaskInfo): DownloadTask? {
        return taskQueue.find {
            it.taskInfo == taskInfo
        }
    }

    protected fun add(downloadTask: DownloadTask): Boolean {
        return taskQueue.add(downloadTask)
    }

    protected fun remove(downloadTask: DownloadTask): Boolean {
        return taskQueue.remove(downloadTask)
    }
}