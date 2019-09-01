package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.TaskInfo
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:14
 */
interface ITaskRepo {

    fun taskList(): Maybe<List<TaskInfo>>

    fun isExist(taskInfo: TaskInfo): Maybe<Boolean>

    fun create(taskInfo: TaskInfo): Flowable<Progress>

    fun resume(taskInfo: TaskInfo): Flowable<Progress>

    fun start(taskInfo: TaskInfo): Maybe<Any>

    fun startAll(): Maybe<Any>

    fun stop(taskInfo: TaskInfo): Maybe<Any>

    fun stopAll(): Maybe<Any>

    fun remove(taskInfo: TaskInfo, delete: Boolean): Maybe<Any>

    fun remove(taskInfo: List<TaskInfo>, delete: Boolean): Maybe<Any>

    fun targetFile(taskInfo: TaskInfo): Maybe<File>
}