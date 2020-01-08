package com.coopsrc.xandroid.downloader.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.model.Type
import com.coopsrc.xandroid.downloader.utils.Logger
import com.squareup.sqlbrite3.BriteDatabase

import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:03
 */
internal class TaskInfoAction(private val briteDatabase: BriteDatabase) : DatabaseAction<TaskInfo> {

    private var taskInfoMapper: Function<Cursor, TaskInfo> = Function { getTaskInfo(it) }

    private val tag = "TaskInfoAction"

    private fun getTaskInfo(@NonNull cursor: Cursor): TaskInfo {
        Logger.v(tag, "getTaskInfo: $cursor")

        cursor.moveToFirst()

        val url = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_URL)
        val saveName = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_NAME)
        val savePath = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_PATH)
        val type = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_TYPE)
        val tag = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_TAG)

        val downloadSize = DatabaseUtils.getLong(cursor, TaskInfoColumns.COLUMN_NAME_DOWNLOAD)
        val totalSize = DatabaseUtils.getLong(cursor, TaskInfoColumns.COLUMN_NAME_TOTAL_SIZE)
        val status = DatabaseUtils.getString(cursor, TaskInfoColumns.COLUMN_NAME_STATUS)
        val lastModified = DatabaseUtils.getLong(cursor, TaskInfoColumns.COLUMN_NAME_MODIFIED)

        val taskInfo = TaskInfo(url, saveName, savePath, Type.Parser.parse(type), tag)
        taskInfo.progress.update(downloadSize, totalSize, Status.Parser.parse(status), lastModified)

        return taskInfo
    }

    override fun exist(data: TaskInfo): Boolean {
        Logger.i(tag, "exist: $data")

        briteDatabase.query(TaskInfoColumns.READ_QUERY, data.tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            if (taskInfoMapper.apply(cursor) != null) {
                return true
            }
        }
        return false
    }

    override fun create(data: TaskInfo): Long {
        Logger.i(tag, "create: $data")

        return briteDatabase.insert(
            TaskInfoColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(data)
        )
    }

    override fun read(data: TaskInfo): Boolean {
        Logger.i(tag, "read: $data")

        briteDatabase.query(TaskInfoColumns.READ_QUERY, data.tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            val cacheInfo = taskInfoMapper.apply(cursor)
            if (cacheInfo != null) {
                data.update(cacheInfo)
                return true
            }
        }

        return false
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

        return briteDatabase.update(
            TaskInfoColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(taskInfo),
            TaskInfoColumns.UD_CLAUSE, taskInfo.tag
        )
    }

    private fun updateProgress(progress: Progress): Int {
        Logger.i(tag, "updateProgress: $progress")

        return briteDatabase.update(
            TaskInfoColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(progress),
            TaskInfoColumns.UD_CLAUSE, progress.tag
        )
    }

    override fun delete(data: TaskInfo): Int {
        Logger.i(tag, "delete: $data")

        return briteDatabase.delete(
            TaskInfoColumns.TABLE_NAME,
            TaskInfoColumns.UD_CLAUSE, data.tag
        )
    }

    override fun clear(): Int {
        Logger.i(tag, "clear:")
        return briteDatabase.delete(TaskInfoColumns.TABLE_NAME, null)
    }

    private fun provideValues(taskInfo: TaskInfo): ContentValues {
        Logger.v(tag, "provideValues: $taskInfo")
        return ValuesBuilder()
            .taskInfo(taskInfo)
            .build()
    }

    private fun provideValues(progress: Progress): ContentValues {
        Logger.v(tag, "provideValues: $progress")
        return ValuesBuilder()
            .downloadInfo(progress)
            .build()
    }

    private inner class ValuesBuilder(private val values: ContentValues = ContentValues()) {

        fun url(url: String): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_URL, url)
            return this
        }

        fun saveName(saveName: String): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_NAME, saveName)
            return this
        }

        fun savePath(savePath: String): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_PATH, savePath)
            return this
        }

        fun type(type: Type?): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_TYPE, type?.name)
            return this
        }

        fun tag(tag: String): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_TAG, tag)
            return this
        }

        fun downloadSize(downloadSize: Long): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_DOWNLOAD, downloadSize)
            return this
        }

        fun totalSize(totalSize: Long): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_TOTAL_SIZE, totalSize)
            return this
        }

        fun lastModified(lastModified: Long): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_MODIFIED, lastModified)
            return this
        }

        fun status(status: Status): ValuesBuilder {
            values.put(TaskInfoColumns.COLUMN_NAME_STATUS, status.name)
            return this
        }

        fun taskInfo(taskInfo: TaskInfo): ValuesBuilder {

            return url(taskInfo.url)
                .saveName(taskInfo.saveName)
                .savePath(taskInfo.savePath)
                .type(taskInfo.type)
                .tag(taskInfo.tag)
                .downloadInfo(taskInfo.progress)
        }

        fun downloadInfo(progress: Progress): ValuesBuilder {
            return tag(progress.tag)
                .downloadSize(progress.downloadSize)
                .totalSize(progress.totalSize)
                .lastModified(progress.lastModified)
                .status(progress.status)
        }

        fun build(): ContentValues {
            return values
        }
    }
}