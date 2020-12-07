package com.coopsrc.xandroid.downloader.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.coopsrc.xandroid.downloader.model.SegmentInfo
import com.coopsrc.xandroid.downloader.utils.Logger
import com.squareup.sqlbrite3.BriteDatabase
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function

/**
 * Created by tingkuo.
 * Date: 2018-08-02
 * Time: 16:53
 */
internal class SegmentInfoAction(private val briteDatabase: BriteDatabase) : DatabaseAction<SegmentInfo> {

    private var mSegmentsMapper: Function<Cursor, SegmentInfo> = Function { getSegment(it) }
    private val tag = "SegmentInfoAction"


    private fun getSegment(@NonNull cursor: Cursor): SegmentInfo {
        Logger.v(tag, "getSegment: $cursor")

        val tag = DatabaseUtils.getString(cursor, SegmentInfoColumns.COLUMN_NAME_TAG)
        val index = DatabaseUtils.getLong(cursor, SegmentInfoColumns.COLUMN_NAME_INDEX)
        val start = DatabaseUtils.getLong(cursor, SegmentInfoColumns.COLUMN_NAME_START)
        val position = DatabaseUtils.getLong(cursor, SegmentInfoColumns.COLUMN_NAME_POSITION)
        val end = DatabaseUtils.getLong(cursor, SegmentInfoColumns.COLUMN_NAME_END)

        return SegmentInfo(tag, index, start, position, end)
    }

    fun exist(tag: String): Boolean {
        Logger.i(tag, "exist: $tag")

        briteDatabase.query(SegmentInfoColumns.COUNT_QUERY, tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            if (mSegmentsMapper.apply(cursor) != null) {
                return true
            }
        }

        return false
    }

    fun list(tag: String): MutableList<SegmentInfo> {
        Logger.i(tag, "list: $tag")

        val segments = mutableListOf<SegmentInfo>()

        briteDatabase.query(SegmentInfoColumns.LIST_QUERY, tag).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                segments.add(mSegmentsMapper.apply(cursor))
                cursor.moveToNext()
            }
        }
        return segments
    }

    override fun exist(data: SegmentInfo): Boolean {
        Logger.i(tag, "exist: $data")

        briteDatabase.query(SegmentInfoColumns.COUNT_QUERY, data.tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            if (mSegmentsMapper.apply(cursor) != null) {
                return true
            }
        }

        return false
    }

    override fun create(data: SegmentInfo): Long {
        Logger.i(tag, "create: $data")

        return briteDatabase.insert(
            SegmentInfoColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(data)
        )
    }

    override fun read(data: SegmentInfo): Boolean {
        Logger.i(tag, "read: $data")

        briteDatabase.query(SegmentInfoColumns.READ_QUERY, data.tag, data.index).use { cursor ->

            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            val cache = mSegmentsMapper.apply(cursor)
            if (cache != null) {
                data.update(cache)
                return true
            }
        }

        return false
    }

    override fun update(data: Any): Int {
        Logger.i(tag, "update: $data")
        return when (data) {
            is SegmentInfo -> updateSegment(data)
            else -> -1
        }
    }

    private fun updateSegment(segmentInfo: SegmentInfo): Int {
        Logger.i(tag, "updateSegment: $segmentInfo")

        val whereArgs = arrayOf(segmentInfo.tag, segmentInfo.index.toString())

        return briteDatabase.update(
            SegmentInfoColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(segmentInfo),
            SegmentInfoColumns.DELETE_CLAUSE, *whereArgs
        )
    }

    fun delete(tag: String): Int {
        Logger.i(tag, "delete: $tag")

        return briteDatabase.delete(SegmentInfoColumns.TABLE_NAME, SegmentInfoColumns.CLEAN_CLAUSE, tag)
    }

    override fun delete(data: SegmentInfo): Int {
        Logger.i(tag, "delete: $data")

        val whereArgs = arrayOf(data.tag, data.index.toString())

        return briteDatabase.delete(
            SegmentInfoColumns.TABLE_NAME,
            SegmentInfoColumns.DELETE_CLAUSE, *whereArgs
        )
    }

    override fun clear(): Int {
        Logger.i(tag, "clear:")

        return briteDatabase.delete(SegmentInfoColumns.TABLE_NAME, null)
    }

    private fun provideValues(segmentInfo: SegmentInfo): ContentValues {
        Logger.v(tag, "provideValues: $segmentInfo")
        return ValuesBuilder()
            .segment(segmentInfo)
            .build()
    }

    private inner class ValuesBuilder(private val values: ContentValues = ContentValues()) {

        fun tag(tag: String): ValuesBuilder {
            values.put(SegmentInfoColumns.COLUMN_NAME_TAG, tag)
            return this
        }

        fun index(index: Long): ValuesBuilder {
            values.put(SegmentInfoColumns.COLUMN_NAME_INDEX, index)
            return this
        }

        fun start(start: Long): ValuesBuilder {
            values.put(SegmentInfoColumns.COLUMN_NAME_START, start)
            return this
        }

        fun position(position: Long): ValuesBuilder {
            values.put(SegmentInfoColumns.COLUMN_NAME_POSITION, position)
            return this
        }

        fun end(end: Long): ValuesBuilder {
            values.put(SegmentInfoColumns.COLUMN_NAME_END, end)
            return this
        }

        fun segment(segmentInfo: SegmentInfo): ValuesBuilder {

            return this.tag(segmentInfo.tag)
                .index(segmentInfo.index)
                .start(segmentInfo.start)
                .position(segmentInfo.position)
                .end(segmentInfo.end)
        }

        fun build(): ContentValues {
            return values
        }
    }
}