package com.coopsrc.xandroid.http.interceptor

import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author tingkuo
 *
 *
 * Datetime: 2019-11-22 16:14
 */
abstract class BaseMonitorInterceptor : Interceptor {

    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    protected val level: Level

    constructor() {
        level = Level.BASIC
    }

    constructor(level: Level) {
        this.level = level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}