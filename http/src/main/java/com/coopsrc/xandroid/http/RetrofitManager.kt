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

package com.coopsrc.xandroid.http

import androidx.annotation.NonNull
import com.coopsrc.xandroid.http.config.HttpClientConfig
import com.coopsrc.xandroid.http.interceptor.BasicParamsInterceptor
import com.coopsrc.xandroid.http.logging.HttpLogger
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:53
 */
object RetrofitManager {

    @JvmStatic
    fun newRetrofit(): Retrofit {
        LogUtils.i("newRetrofit: ")

        return createRetrofit()
    }

    @JvmStatic
    fun newRetrofit(@NonNull clientConfig: HttpClientConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", clientConfig.getPrimaryHost())

        return createRetrofit(clientConfig)
    }

    private fun createRetrofit(@NonNull clientConfig: HttpClientConfig = BaseHttpClientConfig()): Retrofit {
        LogUtils.i("createRetrofit: ", clientConfig)

        val httpClientBuilder = OkHttpClient.Builder()

        // add basic params interceptor.
        val basicParamsBuilder = BasicParamsInterceptor.Builder()
        basicParamsBuilder.addQueryParams(clientConfig.getBasicQueryParams())
        basicParamsBuilder.addBodyParams(clientConfig.getBodyMapParams())
        basicParamsBuilder.addHeaderParams(clientConfig.getHeaderMapParams())
        httpClientBuilder.addInterceptor(basicParamsBuilder.build())

        // add custom interceptor
        for (interceptor in clientConfig.getInterceptors()) {
            httpClientBuilder.addInterceptor(interceptor)
        }

        // add custom network interceptor
        for (interceptor in clientConfig.getNetworkInterceptors()) {
            httpClientBuilder.addNetworkInterceptor(interceptor)
        }

        //  add token interceptor
        if (clientConfig.getTokenInterceptor() != null) {
            httpClientBuilder.addInterceptor(clientConfig.getTokenInterceptor()!!)
        }

        // add token authenticator
        if (clientConfig.getTokenAuthenticator() != null) {
            httpClientBuilder.authenticator(clientConfig.getTokenAuthenticator()!!)
        }

        // set debug monitor interceptor
        if (clientConfig.getDebugMonitorInterceptor() != null) {
            httpClientBuilder.addInterceptor(clientConfig.getDebugMonitorInterceptor()!!)
        }

        // set logger interceptor.
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLoggingInterceptor.level = clientConfig.httpLogLevel()
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // set cache dir.
        httpClientBuilder.cache(clientConfig.httpClientCache())

        // set time out.
        httpClientBuilder.connectTimeout(clientConfig.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(clientConfig.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(clientConfig.readTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.retryOnConnectionFailure(clientConfig.retryOnConnectionFailure())

        // create retrofit.
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(httpClientBuilder.build())
        retrofitBuilder.baseUrl(clientConfig.getPrimaryHost())
        for (callAdapterFactory in clientConfig.callAdapterFactories()) {
            retrofitBuilder.addCallAdapterFactory(callAdapterFactory)
        }
        for (converterFactory in clientConfig.converterFactories()) {
            retrofitBuilder.addConverterFactory(converterFactory)
        }

        return retrofitBuilder.build()
    }

    private class BaseHttpClientConfig : HttpClientConfig()

}
