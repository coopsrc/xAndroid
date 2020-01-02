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

package com.coopsrc.xandroid.http.interceptor

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.text.TextUtils
import com.coopsrc.xandroid.http.config.HttpConstants
import com.coopsrc.xandroid.utils.ContextProvider
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author tingkuo
 *
 * Datetime: 2019-11-08 12:13
 */
open class CacheControlInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()

        val originalCacheControl = originalRequest.cacheControl
        if (!TextUtils.isEmpty(originalCacheControl.toString())) {
            return chain.proceed(originalRequest)
        }

        if (!isNetworkReachable(getContext())) {
            originalRequest = originalRequest.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }

        var response = chain.proceed(originalRequest)

        val cacheControlValue: String = if (isNetworkReachable(getContext())) {
            "public, max-age=${getMaxAge()}"
        } else {
            "public, only-if-cached, max-stale=${getMaxStale()}"
        }

        response = response.newBuilder()
            .header("Cache-Control", cacheControlValue)
            .removeHeader("pragma")
            .build()

        return response
    }

    protected open fun getContext(): Context {
        return ContextProvider.getAppContext();
    }

    protected open fun getMaxAge(): Long {
        return HttpConstants.CACHE_MAX_AGE
    }

    protected open fun getMaxStale(): Long {
        return HttpConstants.CACHE_MAX_STALE
    }

    @Synchronized
    protected open fun isNetworkReachable(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val current = cm.activeNetworkInfo
        return current != null && current.isAvailable
    }
}