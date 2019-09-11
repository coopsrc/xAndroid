package com.coopsrc.xandroid.downloader.db

import android.database.Cursor

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:45
 */
internal object DatabaseUtils {
    private const val BOOLEAN_FALSE = 0
    private const val BOOLEAN_TRUE = 1

    fun getString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName)) ?: ""
    }

    fun getBoolean(cursor: Cursor, columnName: String): Boolean {
        return getInt(cursor, columnName) == BOOLEAN_TRUE
    }

    fun getLong(cursor: Cursor, columnName: String): Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName))
    }
}