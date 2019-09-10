package com.coopsrc.xandroid.http.config

import com.coopsrc.xandroid.http.adapter.LiveDataCallAdapterFactory
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

abstract class HttpClientConfig : IHttpClientConfig {
    override fun httpClientCache(): Cache? {
        return null
    }

    override fun httpLogLevel(): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.HEADERS
    }

    override fun connectTimeoutMillis(): Long {
        return TimeUnit.SECONDS.toMillis(10)
    }

    override fun writeTimeoutMillis(): Long {
        return TimeUnit.SECONDS.toMillis(10)
    }

    override fun readTimeoutMillis(): Long {
        return TimeUnit.SECONDS.toMillis(10)
    }

    override fun retryOnConnectionFailure(): Boolean {
        return true
    }

    override fun callAdapterFactories(): Set<CallAdapter.Factory> {
        return linkedSetOf(
            RxJava2CallAdapterFactory.create(),
            LiveDataCallAdapterFactory.create()
        )
    }

    override fun converterFactories(): Set<Converter.Factory> {
        return linkedSetOf(
            GsonConverterFactory.create()
        )
    }
}
