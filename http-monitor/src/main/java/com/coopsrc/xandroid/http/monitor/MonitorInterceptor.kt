package com.coopsrc.xandroid.http.monitor

import android.content.Context
import android.text.TextUtils
import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
import com.coopsrc.xandroid.http.logging.HttpLogger
import com.coopsrc.xandroid.http.monitor.common.IMonitor
import com.coopsrc.xandroid.http.monitor.common.Monitor
import com.coopsrc.xandroid.http.monitor.model.HttpInfo
import com.coopsrc.xandroid.http.monitor.model.RequestInfo
import com.coopsrc.xandroid.http.monitor.model.ResponseInfo
import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils
import com.coopsrc.xandroid.utils.ContextProvider
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author tingkuo
 *
 *
 * Datetime: 2019-09-23 10:32
 */
open class MonitorInterceptor : BaseMonitorInterceptor {

    private val monitor: IMonitor

    constructor() : super() {
        monitor = Monitor(context)
    }

    constructor(level: Level) : super(level) {
        monitor = Monitor(context)
    }

    constructor(monitor: IMonitor) : super() {
        this.monitor = monitor
    }

    constructor(level: Level, monitor: IMonitor) : super(level) {
        this.monitor = monitor
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val httpInfo = HttpInfo(RequestInfo(), ResponseInfo())
        val request = chain.request()

        if (level == Level.NONE) {
            return super.intercept(chain)
        }

        val monitorBody = level === Level.BODY
        val monitorHeaders = monitorBody || level === Level.HEADERS

        val requestBody = request.body
        httpInfo.requestInfo.date = Date()
        httpInfo.requestInfo.method = request.method
        httpInfo.requestInfo.url = request.url
        val connection = chain.connection()
        if (connection != null) {
            httpInfo.requestInfo.protocol = connection.protocol()
        }
        if (monitorHeaders) {
            httpInfo.requestInfo.headers = request.headers
            if (requestBody != null) {
                httpInfo.requestInfo.contentType = requestBody.contentType()
                httpInfo.requestInfo.contentLength = requestBody.contentLength()
            }
            if (!monitorBody || requestBody == null) {
                httpInfo.requestInfo.extra = String.format("END %s", request.method)
            } else if (bodyHasUnknownEncoding(request.headers)) {
                httpInfo.requestInfo.extra = String.format(
                    "END %s (encoded body omitted)",
                    request.method
                )
            } else if (requestBody.isDuplex()) {
                httpInfo.requestInfo.extra = String.format(
                    "END %s (duplex request body omitted)",
                    request.method
                )
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                val contentType = requestBody.contentType()
                var charset: Charset? = null
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8)
                }
                if (charset == null) {
                    charset = StandardCharsets.UTF_8
                }
                if (MonitorUtils.isProbablyUtf8(buffer)) {
                    httpInfo.requestInfo.body = buffer.readString(charset!!)
                    httpInfo.requestInfo.extra = String.format(
                        "END %s (%s-byte body)",
                        request.method,
                        requestBody.contentLength()
                    )
                } else {
                    httpInfo.requestInfo.extra = String.format(
                        "END %s (binary %s-byte body omitted)",
                        request.method,
                        requestBody.contentLength()
                    )
                }
            }
        }
        // insert to db
        httpInfo.id = insert(httpInfo)
        val startTime = System.nanoTime()

        try {
            val response = chain.proceed(request)

            val responseBody = response.body
            httpInfo.responseInfo.date = Date()
            httpInfo.responseInfo.duration =
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
            httpInfo.responseInfo.protocol = response.protocol
            httpInfo.responseInfo.code = response.code
            httpInfo.responseInfo.message = response.message
            if (responseBody != null) {
                httpInfo.responseInfo.contentLength = responseBody.contentLength()
                httpInfo.responseInfo.contentType = responseBody.contentType()
            }
            if (monitorHeaders) {
                httpInfo.responseInfo.headers = response.headers
                if (!monitorBody || !response.promisesBody()) {
                    httpInfo.responseInfo.extra = "END HTTP"
                } else if (bodyHasUnknownEncoding(response.headers)) {
                    httpInfo.responseInfo.extra = "END HTTP (encoded body omitted)"
                } else {
                    if (responseBody != null) {
                        val source = responseBody.source()
                        source.request(Long.MAX_VALUE)
                        var buffer = source.buffer
                        var gzippedLength: Long? = null
                        if ("gzip".equals(
                                response.headers["Content-Encoding"],
                                ignoreCase = true
                            )
                        ) {
                            gzippedLength = buffer.size
                            val gzipSource = GzipSource(buffer.clone())
                            buffer = Buffer()
                            buffer.writeAll(gzipSource)
                        }
                        val contentType = responseBody.contentType()
                        var charset: Charset? = null
                        if (contentType != null) {
                            charset = contentType.charset(StandardCharsets.UTF_8)
                        }
                        if (charset == null) {
                            charset = StandardCharsets.UTF_8
                        }
                        if (!MonitorUtils.isProbablyUtf8(buffer)) {
                            httpInfo.responseInfo.extra = String.format(
                                "END HTTP (binary %s-byte body omitted)",
                                buffer.size
                            )
                            update(httpInfo)
                            return response
                        }
                        if (responseBody.contentLength() != 0L) {
                            httpInfo.responseInfo.body = buffer.clone().readString(charset!!)
                        }
                        if (gzippedLength != null) {
                            httpInfo.responseInfo.contentLength = gzippedLength
                            httpInfo.responseInfo.extra = String.format(
                                "END HTTP (binary %s-byte, %s-gzipped-byte body)",
                                buffer.size,
                                gzippedLength
                            )
                        } else {
                            httpInfo.responseInfo.contentLength = buffer.size
                            httpInfo.responseInfo.extra = String.format(
                                "END HTTP (binary %s-byte body)",
                                buffer.size
                            )
                        }
                    }
                }
            }
            update(httpInfo)

            return response
        } catch (exception: Exception) {
            httpInfo.responseInfo.error = exception.toString()
            update(httpInfo)
            LogUtils.e(exception)
            return super.intercept(chain)
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return if (TextUtils.isEmpty(contentEncoding)) {
            false
        } else {
            !contentEncoding.equals("identity", ignoreCase = true) &&
                    !contentEncoding.equals("gzip", ignoreCase = true)
        }
    }

    private fun insert(httpInfo: HttpInfo): Long {
        LogUtils.d("insert: %s", httpInfo)
        return monitor.insert(httpInfo)
    }

    private fun update(httpInfo: HttpInfo) {
        LogUtils.i("update: %s", httpInfo)
        monitor.update(httpInfo)
    }

    private val context: Context
        get() = ContextProvider.getAppContext()
}