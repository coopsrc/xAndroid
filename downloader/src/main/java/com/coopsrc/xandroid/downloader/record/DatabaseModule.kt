package com.coopsrc.xandroid.downloader.record

import android.content.Context
import com.coopsrc.xandroid.downloader.db.DownloadDatabase
import com.coopsrc.xandroid.downloader.utils.Logger

/**
 * Created by tingkuo.
 * Date: 2018-08-01
 * Time: 09:48
 */
internal class DatabaseModule private constructor() {

    private val tag = "DatabaseModule"

    lateinit var taskInfoAction: TaskInfoAction
    lateinit var mSegmentInfoAction: SegmentInfoAction
    lateinit var downloadDatabase: DownloadDatabase

    private object Holder {
        val INSTANCE = DatabaseModule()
    }

    companion object {
        val instance: DatabaseModule by lazy { Holder.INSTANCE }
    }

    fun init(context: Context) {
        Logger.i(tag, "init: $context")

        downloadDatabase = DownloadDatabase.getInstance(context)

        taskInfoAction = TaskInfoAction(downloadDatabase.taskInfoDao())
        mSegmentInfoAction = SegmentInfoAction(downloadDatabase.segmentInfoDao())
    }

}