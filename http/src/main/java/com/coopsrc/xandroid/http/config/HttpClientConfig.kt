package com.coopsrc.xandroid.http.config

import android.content.Context
import com.coopsrc.xandroid.http.monitor.MonitorInterceptor
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

abstract class HttpClientConfig : IHttpClientConfig {
    override fun getAppContext(): Context? {
        return null
    }

    override fun getPrimaryHost(): String {
        return HttpConstants.BASE_URL
    }

    override fun httpClientCache(): Cache? {
        return null
    }

    override fun httpLogLevel(): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.HEADERS
    }

    override fun connectTimeoutMillis(): Long {
        return HttpConstants.TIMEOUT_MILLIS
    }

    override fun writeTimeoutMillis(): Long {
        return HttpConstants.TIMEOUT_MILLIS
    }

    override fun readTimeoutMillis(): Long {
        return HttpConstants.TIMEOUT_MILLIS
    }

    override fun retryOnConnectionFailure(): Boolean {
        return HttpConstants.RETRY_ON_CONNECTION_FAILURE
    }

    override fun callAdapterFactories(): Set<CallAdapter.Factory> {
        return linkedSetOf()
    }

    override fun converterFactories(): Set<Converter.Factory> {
        return linkedSetOf()
    }

    override fun getSecondaryHosts(): Set<String> {
        return linkedSetOf()
    }

    override fun isHostLoopEnable(): Boolean {
        return HttpConstants.HOST_LOOP_ENABLE
    }

    override fun getBasicQueryParams(): Map<String, String> {
        return linkedMapOf()
    }

    override fun getBodyMapParams(): Map<String, String> {
        return linkedMapOf()
    }

    override fun getHeaderMapParams(): Map<String, String> {
        return linkedMapOf()
    }

    override fun getInterceptors(): Set<Interceptor> {
        return linkedSetOf()
    }

    override fun getNetworkInterceptors(): Set<Interceptor> {
        return linkedSetOf()
    }

    override fun getTokenInterceptor(): Interceptor? {
        return null
    }

    override fun getTokenAuthenticator(): Authenticator? {
        return null
    }

    override fun useDebugMonitor(): Boolean {
        return false
    }

    override fun debugMonitorLevel(): MonitorInterceptor.Level {
        return MonitorInterceptor.Level.HEADERS
    }

    internal fun getDebugMonitorInterceptor(): MonitorInterceptor? {
        if (useDebugMonitor() && getAppContext() != null) {
            val monitorInterceptor = MonitorInterceptor(getAppContext())
            monitorInterceptor.level = debugMonitorLevel()
            return monitorInterceptor
        }
        return null
    }
}
