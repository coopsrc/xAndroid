package com.coopsrc.xandroid.downloader.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.coopsrc.xandroid.downloader.model.Segment
import com.coopsrc.xandroid.downloader.utils.Logger
import com.squareup.sqlbrite3.BriteDatabase
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function

/**
 * Created by tingkuo.
 * Date: 2018-08-02
 * Time: 16:53
 */
internal class SegmentsAction(private val briteDatabase: BriteDatabase) : DatabaseAction<Segment> {

    private var segmentsMapper: Function<Cursor, Segment> = Function { getSegment(it) }
    private val tag = "SegmentsAction"


    private fun getSegment(@NonNull cursor: Cursor): Segment {
        Logger.v(tag, "getSegment: $cursor")

        val tag = DatabaseUtils.getString(cursor, SegmentColumns.COLUMN_NAME_TAG)
        val index = DatabaseUtils.getLong(cursor, SegmentColumns.COLUMN_NAME_INDEX)
        val start = DatabaseUtils.getLong(cursor, SegmentColumns.COLUMN_NAME_START)
        val position = DatabaseUtils.getLong(cursor, SegmentColumns.COLUMN_NAME_POSITION)
        val end = DatabaseUtils.getLong(cursor, SegmentColumns.COLUMN_NAME_END)

        return Segment(tag, index, start, position, end)
    }

    fun exist(tag: String): Boolean {
        Logger.i(tag, "exist: $tag")

        briteDatabase.query(SegmentColumns.COUNT_QUERY, tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            if (segmentsMapper.apply(cursor) != null) {
                return true
            }
        }

        return false
    }

    fun list(tag: String): MutableList<Segment> {
        Logger.i(tag, "list: $tag")

        val segments = mutableListOf<Segment>()

        briteDatabase.query(SegmentColumns.LIST_QUERY, tag).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                segments.add(segmentsMapper.apply(cursor))
                cursor.moveToNext()
            }
        }
        return segments
    }

    override fun exist(data: Segment): Boolean {
        Logger.i(tag, "exist: $data")

        briteDatabase.query(SegmentColumns.COUNT_QUERY, data.tag).use { cursor ->
            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            if (segmentsMapper.apply(cursor) != null) {
                return true
            }
        }

        return false
    }

    override fun create(data: Segment): Long {
        Logger.i(tag, "create: $data")

        return briteDatabase.insert(
            SegmentColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(data)
        )
    }

    override fun read(data: Segment): Boolean {
        Logger.i(tag, "read: $data")

        briteDatabase.query(SegmentColumns.READ_QUERY, data.tag, data.index).use { cursor ->

            if (cursor.count <= 0) {
                return false
            }
            cursor.moveToFirst()
            val cache = segmentsMapper.apply(cursor)
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
            is Segment -> updateSegment(data)
            else -> -1
        }
    }

    private fun updateSegment(segment: Segment): Int {
        Logger.i(tag, "updateSegment: $segment")

        val whereArgs = arrayOf(segment.tag, segment.index.toString())

        return briteDatabase.update(
            SegmentColumns.TABLE_NAME,
            SQLiteDatabase.CONFLICT_REPLACE, provideValues(segment),
            SegmentColumns.DELETE_CLAUSE, *whereArgs
        )
    }

    fun delete(tag: String): Int {
        Logger.i(tag, "delete: $tag")

        return briteDatabase.delete(SegmentColumns.TABLE_NAME, SegmentColumns.CLEAN_CLAUSE, tag)
    }

    override fun delete(data: Segment): Int {
        Logger.i(tag, "delete: $data")

        val whereArgs = arrayOf(data.tag, data.index.toString())

        return briteDatabase.delete(
            SegmentColumns.TABLE_NAME,
            SegmentColumns.DELETE_CLAUSE, *whereArgs
        )
    }

    override fun clear(): Int {
        Logger.i(tag, "clear:")

        return briteDatabase.delete(SegmentColumns.TABLE_NAME, null)
    }

    private fun provideValues(segment: Segment): ContentValues {
        Logger.v(tag, "provideValues: $segment")
        return ValuesBuilder()
            .segment(segment)
            .build()
    }

    private inner class ValuesBuilder(private val values: ContentValues = ContentValues()) {

        fun tag(tag: String): ValuesBuilder {
            values.put(SegmentColumns.COLUMN_NAME_TAG, tag)
            return this
        }

        fun index(index: Long): ValuesBuilder {
            values.put(SegmentColumns.COLUMN_NAME_INDEX, index)
            return this
        }

        fun start(start: Long): ValuesBuilder {
            values.put(SegmentColumns.COLUMN_NAME_START, start)
            return this
        }

        fun position(position: Long): ValuesBuilder {
            values.put(SegmentColumns.COLUMN_NAME_POSITION, position)
            return this
        }

        fun end(end: Long): ValuesBuilder {
            values.put(SegmentColumns.COLUMN_NAME_END, end)
            return this
        }

        fun segment(segment: Segment): ValuesBuilder {

            return this.tag(segment.tag)
                .index(segment.index)
                .start(segment.start)
                .position(segment.position)
                .end(segment.end)
        }

        fun build(): ContentValues {
            return values
        }
    }
}