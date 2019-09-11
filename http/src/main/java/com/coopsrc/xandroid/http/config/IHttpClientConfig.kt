package com.coopsrc.xandroid.http.config

import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

internal interface IHttpClientConfig {

    // primary
    fun httpClientCache(): Cache?

    fun httpLogLevel(): HttpLoggingInterceptor.Level

    fun connectTimeoutMillis(): Long

    fun writeTimeoutMillis(): Long

    fun readTimeoutMillis(): Long

    fun retryOnConnectionFailure(): Boolean

    fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory>

    fun converterFactories(): LinkedHashSet<Converter.Factory>

    fun getPrimaryHost(): String

    // optional
    fun getSecondaryHosts(): LinkedHashSet<String>

    fun isHostLoopEnable(): Boolean

    fun getBasicQueryParams(): LinkedHashMap<String, String>

    fun getBodyMapParams(): LinkedHashMap<String, String>

    fun getHeaderMapParams(): LinkedHashMap<String, String>

    fun getInterceptors(): Set<Interceptor>

    fun getNetworkInterceptors(): LinkedHashSet<Interceptor>

    fun getTokenInterceptor(): Interceptor?

    fun getTokenAuthenticator(): Authenticator?
}
