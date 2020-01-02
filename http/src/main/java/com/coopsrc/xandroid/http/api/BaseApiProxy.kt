/*
 * Copyright (C) 2019 Zhang Tingkuo(zhangtingkuo@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coopsrc.xandroid.http.api

import com.coopsrc.xandroid.http.RetrofitManager
import com.coopsrc.xandroid.http.config.HttpClientConfig
import okhttp3.OkHttpClient

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

    fun getHttpClient(): OkHttpClient {
        return retrofit.callFactory() as OkHttpClient
    }

    fun updateHttpClient(httpClient: OkHttpClient) {
        retrofit = retrofit.newBuilder().client(httpClient).build()
    }

    protected open class BaseApiClientConfig : HttpClientConfig()

}
