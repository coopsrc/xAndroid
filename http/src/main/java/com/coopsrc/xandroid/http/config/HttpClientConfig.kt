package com.coopsrc.xandroid.http.config

import android.content.Context
import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
import com.coopsrc.xandroid.utils.ContextProvider
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import java.io.File

abstract class HttpClientConfig : IHttpClientConfig {
    override fun getAppContext(): Context {
        return ContextProvider.getAppContext()
    }

    override fun getPrimaryHost(): String {
        return HttpConstants.BASE_URL
    }

    override fun httpClientCache(): Cache {
        val cacheDir = File(getAppContext().externalCacheDir, HttpConstants.HTTP_CACHE_DIR_NAME)
        return Cache(cacheDir, HttpConstants.HTTP_CACHE_SIZE)
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

    override fun getDebugMonitorInterceptor(): BaseMonitorInterceptor? {
        return null
    }
}
