package com.coopsrc.xandroid.downloader.db

import androidx.room.*

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 19:56
 */
@Dao
interface SegmentInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(segmentInfo: SegmentInfoEntry): Long

    @Query("SELECT * FROM segments WHERE tag=:tag")
    fun load(tag: String): List<SegmentInfoEntry>

    @Query("SELECT * FROM segments WHERE tag=:tag ORDER BY _index")
    fun loadWithOrder(tag: String): List<SegmentInfoEntry>

    @Query("SELECT * FROM segments WHERE tag=:tag AND _index=:index")
    fun read(tag: String, index: Long): SegmentInfoEntry?

    @Update
    fun update(segmentInfo: SegmentInfoEntry): Int

    @Query("SELECT COUNT(*) FROM segments WHERE tag=:tag")
    fun count(tag: String): Int

    @Query("DELETE FROM segments WHERE tag=:tag")
    fun delete(tag: String): Int

    @Query("DELETE FROM segments WHERE tag=:tag AND _index=:index")
    fun delete(tag: String, index: Long): Int

    @Query("DELETE FROM segments")
    fun clear(): Int
}