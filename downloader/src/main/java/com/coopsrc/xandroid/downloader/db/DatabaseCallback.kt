package com.coopsrc.xandroid.downloader.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.coopsrc.xandroid.downloader.BuildConfig
import com.coopsrc.xandroid.downloader.utils.Logger


/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:18
 */
internal class DatabaseCallback : SupportSQLiteOpenHelper.Callback(DB_VERSION) {

    override fun onCreate(db: SupportSQLiteDatabase?) {
        Logger.i("DatabaseCallback", "onCreate: $db, $SQL_CREATE_TASK_INFO")
        Logger.i("DatabaseCallback", "onCreate: $db, $SQL_CREATE_SEGMENTS")
        db?.execSQL(SQL_CREATE_TASK_INFO)
        db?.execSQL(SQL_CREATE_SEGMENTS)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        private const val DB_VERSION = BuildConfig.VERSION_CODE
        const val DB_NAME = "downloader.db"

        private const val TEXT_TYPE = " TEXT"
        private const val INTEGER_TYPE = " INTEGER"
        private const val SIZE_DEFAULT = " NOT NULL DEFAULT 0"
        private const val COMMA_SEP = ","

        private const val SQL_CREATE_TASK_INFO =
            "CREATE TABLE " + TaskInfoColumns.TABLE_NAME + " (" +
                    TaskInfoColumns.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_DOWNLOAD + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_TOTAL_SIZE + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_TAG + TEXT_TYPE + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_MODIFIED + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                    TaskInfoColumns.COLUMN_NAME_STATUS + TEXT_TYPE +
                    " )"

        private const val SQL_CREATE_SEGMENTS = "CREATE TABLE " + SegmentColumns.TABLE_NAME + " (" +
                SegmentColumns.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                SegmentColumns.COLUMN_NAME_TAG + TEXT_TYPE + COMMA_SEP +
                SegmentColumns.COLUMN_NAME_INDEX + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                SegmentColumns.COLUMN_NAME_START + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                SegmentColumns.COLUMN_NAME_POSITION + INTEGER_TYPE + SIZE_DEFAULT + COMMA_SEP +
                SegmentColumns.COLUMN_NAME_END + INTEGER_TYPE + SIZE_DEFAULT +
                " )"
    }
}