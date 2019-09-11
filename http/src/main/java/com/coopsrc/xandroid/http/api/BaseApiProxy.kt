package com.coopsrc.xandroid.http.api

import com.coopsrc.xandroid.http.RetrofitManager
import com.coopsrc.xandroid.http.adapter.LiveDataCallAdapterFactory
import com.coopsrc.xandroid.http.config.HttpClientConfig

import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author tingkuo
 *
 * Date: 2019-09-02 14:08
 */
abstract class BaseApiProxy<T> protected constructor() {

    private var retrofit: Retrofit
    protected var apiService: T

    init {
        retrofit = RetrofitManager.newRetrofit(clientConfig())
        apiService = initApiService()
    }

    protected open fun clientConfig(): HttpClientConfig {
        return ClientConfig()
    }

    protected abstract fun initApiService(): T

    protected fun createApiService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <S> create(service: Class<S>): S {
        return retrofit.create(service)
    }

    protected fun createApiService(baseUrl: String, service: Class<T>): T {
        return retrofit.newBuilder().baseUrl(baseUrl).build().create(service)
    }

    fun <S> create(baseUrl: String, service: Class<S>): S {
        return retrofit.newBuilder().baseUrl(baseUrl).build().create(service)
    }

    fun updateBaseUrl(baseUrl: String) {
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build()
    }

    protected open class ClientConfig : HttpClientConfig() {
        override fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory> {
            val factorySet = super.callAdapterFactories()

            factorySet.plus(RxJava2CallAdapterFactory.create())
            factorySet.plus(LiveDataCallAdapterFactory.create())

            return factorySet
        }

        override fun converterFactories(): LinkedHashSet<Converter.Factory> {
            val factorySet = super.converterFactories()

            factorySet.plus(GsonConverterFactory.create())

            return factorySet
        }
    }

}
