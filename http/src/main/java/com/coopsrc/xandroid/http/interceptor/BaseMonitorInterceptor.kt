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

    protected val level = Level.HEADERS

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        onRequest(request, chain.connection())
        try {
            val response = chain.proceed(request)
            onResponse(response)
            return response
        } catch (e: Exception) {
            onException(e)
            throw e
        }
    }

    @Throws(IOException::class)
    protected abstract fun onRequest(request: Request, connection: Connection?)

    @Throws(IOException::class)
    protected abstract fun onResponse(response: Response)

    protected abstract fun onException(exception: Exception)
}