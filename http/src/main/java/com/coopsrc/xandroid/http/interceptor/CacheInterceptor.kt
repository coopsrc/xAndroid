package com.coopsrc.xandroid.http.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.text.TextUtils
import com.coopsrc.xandroid.http.config.HttpConstants
import com.coopsrc.xandroid.utils.ContextProvider
import okhttp3.CacheControl

/**
 * @author tingkuo
 *
 * Datetime: 2019-11-08 12:13
 */
open class CacheInterceptor : Interceptor {

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