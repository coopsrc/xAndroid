package com.coopsrc.xandroid.downloader.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.utils.Logger
import com.squareup.sqlbrite3.BriteDatabase
import com.squareup.sqlbrite3.SqlBrite
import io.reactivex.schedulers.Schedulers

/**
 * Created by tingkuo.
 * Date: 2018-08-01
 * Time: 09:48
 */
internal class DatabaseModule private constructor() {

    private val tag = "DatabaseModule"

    lateinit var briteDatabase: BriteDatabase
    lateinit var taskInfoAction: TaskInfoAction
    lateinit var segmentsAction: SegmentsAction

    private object Holder {
        val INSTANCE = DatabaseModule()
    }

    companion object {
        val instance: DatabaseModule by lazy { Holder.INSTANCE }
    }

    fun init(context: Context) {
        Logger.i(tag, "init: $context")

        briteDatabase = createDatabase(context)

        taskInfoAction = TaskInfoAction(briteDatabase)
        segmentsAction = SegmentsAction(briteDatabase)

    }

    private fun createDatabase(context: Context): BriteDatabase {
        val sqlBrite = SqlBrite.Builder()
            .logger(SqlLogger())
            .build()

        val configuration = SupportSQLiteOpenHelper.Configuration
            .builder(context.applicationContext)
            .name(DatabaseCallback.DB_NAME)
            .callback(DatabaseCallback())
            .build()

        val databaseHelper = FrameworkSQLiteOpenHelperFactory().create(configuration)
        val briteDatabase = sqlBrite.wrapDatabaseHelper(databaseHelper, Schedulers.io())
        briteDatabase.setLoggingEnabled(ExDownloader.withDebug())
        return briteDatabase
    }

}