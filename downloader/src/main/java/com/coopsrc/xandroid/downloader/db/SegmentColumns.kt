package com.coopsrc.xandroid.downloader.db

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:42
 */
object SegmentColumns {
    const val TABLE_NAME = "segments"

    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_TAG = "tag"
    const val COLUMN_NAME_INDEX = "_index"
    const val COLUMN_NAME_START = "start"
    const val COLUMN_NAME_POSITION = "position"
    const val COLUMN_NAME_END = "end"


    const val COUNT_QUERY = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_NAME_TAG + " = ?" +
            " ORDER BY " + COLUMN_NAME_INDEX

    const val LIST_QUERY = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_NAME_TAG + " = ?"

    const val READ_QUERY = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_NAME_TAG + " = ?" +
            " AND " + COLUMN_NAME_INDEX + " = ?"

    const val DELETE_CLAUSE = COLUMN_NAME_TAG + " = ?" +
            " AND " + COLUMN_NAME_INDEX + " = ?"

    const val CLEAN_CLAUSE = COLUMN_NAME_TAG + " = ?"

}