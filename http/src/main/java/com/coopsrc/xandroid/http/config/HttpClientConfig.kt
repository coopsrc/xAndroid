package com.coopsrc.xandroid.http.config

import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

abstract class HttpClientConfig : IHttpClientConfig {
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

    override fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory> {
        return linkedSetOf()
    }

    override fun converterFactories(): LinkedHashSet<Converter.Factory> {
        return linkedSetOf()
    }

    override fun getSecondaryHosts(): LinkedHashSet<String> {
        return linkedSetOf()
    }

    override fun isHostLoopEnable(): Boolean {
        return HttpConstants.HOST_LOOP_ENABLE
    }

    override fun getBasicQueryParams(): LinkedHashMap<String, String> {
        return linkedMapOf()
    }

    override fun getBodyMapParams(): LinkedHashMap<String, String> {
        return linkedMapOf()
    }

    override fun getHeaderMapParams(): LinkedHashMap<String, String> {
        return linkedMapOf()
    }

    override fun getInterceptors(): LinkedHashSet<Interceptor> {
        return linkedSetOf()
    }

    override fun getNetworkInterceptors(): LinkedHashSet<Interceptor> {
        return linkedSetOf()
    }

    override fun getTokenInterceptor(): Interceptor? {
        return null
    }

    override fun getTokenAuthenticator(): Authenticator? {
        return null
    }
}
