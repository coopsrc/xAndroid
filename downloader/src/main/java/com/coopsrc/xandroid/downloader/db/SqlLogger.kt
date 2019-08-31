package com.coopsrc.xandroid.downloader.db

import android.util.Log
import com.coopsrc.xandroid.downloader.utils.Logger
import com.squareup.sqlbrite3.SqlBrite
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2018-07-25
 * Time: 11:25
 */
class SqlLogger : SqlBrite.Logger {
    override fun log(message: String?) {
        Log.d(buildTag(tag), message)
    }

    companion object {
        private const val tag = "SqlLogger"

        private fun buildTag(tag: String): String {
            return String.format(Locale.ROOT, "|%s|%s|%s|", tag, Thread.currentThread().name, Logger.randomKey())
        }
    }
}