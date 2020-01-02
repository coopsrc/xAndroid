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

package com.coopsrc.xandroid.http.config

import android.content.Context
import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
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
    fun getDebugMonitorInterceptor(): BaseMonitorInterceptor?
}
