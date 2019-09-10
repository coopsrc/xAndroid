package com.coopsrc.xandroid.http.config

import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

internal interface IHttpClientConfig {

    fun httpClientCache(): Cache?

    fun httpLogLevel(): HttpLoggingInterceptor.Level

    fun connectTimeoutMillis(): Long

    fun writeTimeoutMillis(): Long

    fun readTimeoutMillis(): Long

    fun retryOnConnectionFailure(): Boolean

    fun callAdapterFactories(): Set<CallAdapter.Factory>

    fun converterFactories(): Set<Converter.Factory>
}
