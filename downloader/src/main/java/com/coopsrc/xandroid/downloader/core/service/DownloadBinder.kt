package com.coopsrc.xandroid.downloader.core.service

import android.os.Binder
import com.coopsrc.xandroid.downloader.core.ITaskRepo
import com.coopsrc.xandroid.downloader.core.LocalTaskRepo
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Constants
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-08-08
 * Time: 10:36
 */
class DownloadBinder(maxTask: Int = Constants.Config.maxTask) : Binder(), ITaskRepo {
    private val taskRepo: ITaskRepo = LocalTaskRepo(maxTask)

    override fun taskList(): Maybe<List<TaskInfo>> {
        return taskRepo.taskList()
    }

    override fun isExist(taskInfo: TaskInfo): Maybe<Boolean> {
        return taskRepo.isExist(taskInfo)
    }

    override fun create(taskInfo: TaskInfo): Flowable<Progress> {
        return taskRepo.create(taskInfo)
    }

    override fun resume(taskInfo: TaskInfo): Flowable<Progress> {
        return taskRepo.resume(taskInfo)
    }

    override fun start(taskInfo: TaskInfo): Maybe<Any> {
        return taskRepo.start(taskInfo)
    }

    override fun startAll(): Maybe<Any> {
        return taskRepo.startAll()
    }

    override fun stop(taskInfo: TaskInfo): Maybe<Any> {
        return taskRepo.stop(taskInfo)
    }

    override fun stopAll(): Maybe<Any> {
        return taskRepo.stopAll()
    }

    override fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any> {
        return taskRepo.remove(taskInfo, delete)
    }

    override fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any> {
        return taskRepo.remove(taskInfo, delete)
    }

    override fun targetFile(taskInfo: TaskInfo): Maybe<File> {
        return taskRepo.targetFile(taskInfo)
    }
}