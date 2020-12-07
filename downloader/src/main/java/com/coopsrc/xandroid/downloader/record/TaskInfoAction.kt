package com.coopsrc.xandroid.downloader.record

import com.coopsrc.xandroid.downloader.db.DatabaseAction
import com.coopsrc.xandroid.downloader.db.TaskInfoDao
import com.coopsrc.xandroid.downloader.db.TaskInfoEntry
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Logger

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:03
 */
internal class TaskInfoAction(private val taskInfoDao: TaskInfoDao) : DatabaseAction<TaskInfo> {
    private val tag = "TaskInfoAction"

    override fun exist(data: TaskInfo): Boolean {
        Logger.i(tag, "exist: $data")

        return taskInfoDao.count(data.tag) > 0
    }

    override fun create(data: TaskInfo): Long {
        Logger.i(tag, "create: $data")

        return taskInfoDao.insert(TaskInfoEntry(data))
    }

    override fun read(data: TaskInfo): Boolean {
        Logger.i(tag, "read: $data")

        val taskInfoEntry = taskInfoDao.load(data.tag)
        if (taskInfoEntry == null) {
            return false
        } else {
            data.update(taskInfoEntry)
            return true
        }
    }

    override fun update(data: Any): Int {
        Logger.i(tag, "update: $data")
        return when (data) {
            is TaskInfo -> updateTaskInfo(data)
            is Progress -> updateProgress(data)
            else -> -1
        }
    }

    private fun updateTaskInfo(taskInfo: TaskInfo): Int {
        Logger.i(tag, "updateTaskInfo: $taskInfo")

        return taskInfoDao.updateTaskInfo(TaskInfoEntry(taskInfo))
    }

    private fun updateProgress(progress: Progress): Int {
        Logger.i(tag, "updateProgress: $progress")

        return taskInfoDao.updateProgress(
            progress.tag,
            progress.downloadSize,
            progress.totalSize,
            progress.lastModified,
            progress.status
        )
    }

    override fun delete(data: TaskInfo): Int {
        Logger.i(tag, "delete: $data")

        return taskInfoDao.delete(data.tag)
    }

    override fun clear(): Int {
        Logger.i(tag, "clear:")
        return taskInfoDao.clear()
    }

}