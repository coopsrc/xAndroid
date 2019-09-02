package com.coopsrc.xandroid.http.logging

import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:45
 */
class HttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        LogUtils.tag(buildTag()).d(message)
    }

    companion object {
        private fun buildTag(): String {
            return String.format("|HttpLogger|%s|", Thread.currentThread().name)
        }
    }
}
