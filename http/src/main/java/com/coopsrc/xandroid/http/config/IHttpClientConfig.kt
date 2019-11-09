package com.coopsrc.xandroid.http.config

import android.content.Context
import com.coopsrc.xandroid.http.monitor.MonitorInterceptor
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

internal interface IHttpClientConfig {

    // primary
    fun getAppContext(): Context

    fun httpClientCache(): Cache

    fun httpLogLevel(): HttpLoggingInterceptor.Level

    fun connectTimeoutMillis(): Long

    fun writeTimeoutMillis(): Long

    fun readTimeoutMillis(): Long

    fun retryOnConnectionFailure(): Boolean

    fun callAdapterFactories(): Set<CallAdapter.Factory>

    fun converterFactories(): Set<Converter.Factory>

    fun getPrimaryHost(): String

    // optional
    fun getSecondaryHosts(): Set<String>

    fun isHostLoopEnable(): Boolean

    fun getBasicQueryParams(): Map<String, String>

    fun getBodyMapParams(): Map<String, String>

    fun getHeaderMapParams(): Map<String, String>

    fun getInterceptors(): Set<Interceptor>

    fun getNetworkInterceptors(): Set<Interceptor>

    fun getTokenInterceptor(): Interceptor?

    fun getTokenAuthenticator(): Authenticator?

    // helper
    fun useDebugMonitor(): Boolean

    fun debugMonitorLevel(): MonitorInterceptor.Level
}
