package com.coopsrc.xandroid.http.api

import com.coopsrc.xandroid.http.RetrofitManager
import com.coopsrc.xandroid.http.config.HttpClientConfig

import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType


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

        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val service: Class<T> = parameterizedType.actualTypeArguments[0] as Class<T>
        apiService = createApiService(service)
    }

    protected open fun clientConfig(): HttpClientConfig {
        return BaseApiClientConfig()
    }

    private fun createApiService(service: Class<T>): T {
        return retrofit.create(service)
    }

    protected fun createApiService(baseUrl: String, service: Class<T>): T {
        return retrofit.newBuilder().baseUrl(baseUrl).build().create(service)
    }

    fun updateBaseUrl(baseUrl: String) {
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build()
    }

    protected open class BaseApiClientConfig : HttpClientConfig()

}
