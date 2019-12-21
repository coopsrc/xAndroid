package com.coopsrc.xandroid.http.monitor

import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author tingkuo
 *
 *
 * Datetime: 2019-09-23 10:32
 */
open class MonitorInterceptor : BaseMonitorInterceptor {

    constructor() : super() {
    }

    constructor(level: Level) : super(level) {
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        return super.intercept(chain)
    }
}