package com.coopsrc.xandroid.downloader.db

import androidx.room.*
import com.coopsrc.xandroid.downloader.model.Status

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 19:55
 */
@Dao
interface TaskInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskInfo: TaskInfoEntry): Long

    @Query("SELECT * FROM tasks WHERE tag=:tag")
    fun load(tag: String): TaskInfoEntry?

    @Update
    fun updateTaskInfo(taskInfo: TaskInfoEntry): Int

    @Query("UPDATE tasks SET p_download_size=:downloadSize and p_total_size=:totalSize and p_last_modified=:lastModified and p_status=:status WHERE tag=:tag")
    fun updateProgress(
        tag: String,
        downloadSize: Long,
        totalSize: Long,
        lastModified: Long,
        status: Status
    ): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE tag=:tag")
    fun count(tag: String): Int

    @Query("DELETE FROM tasks WHERE tag=:tag")
    fun delete(tag: String): Int

    @Query("DELETE FROM tasks")
    fun clear(): Int
}