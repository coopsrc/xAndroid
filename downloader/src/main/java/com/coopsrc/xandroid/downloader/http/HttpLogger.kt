package com.coopsrc.xandroid.downloader.http

import android.util.Log
import com.coopsrc.xandroid.downloader.utils.Logger
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 19:59
 */
class HttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        Log.d(buildTag(tag), message)
    }

    companion object {
        private const val tag = "HttpLogger"

        private fun buildTag(tag: String): String {
            return String.format(Locale.ROOT, "|%s|%s|%s|", tag, Thread.currentThread().name, Logger.randomKey())
        }
    }
}