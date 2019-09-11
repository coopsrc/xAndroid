package com.coopsrc.xandroid.http.interceptor

import com.coopsrc.xandroid.http.config.HttpConstants
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

abstract class OAuthTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        LogUtils.d("intercept: chain=%s", chain)

        val originalRequest = chain.request()

        val accessToken = getAccessToken()
        LogUtils.d("intercept: add access token => %s", accessToken)

        val url = originalRequest.url.newBuilder()
            .setQueryParameter(HttpConstants.ACCESS_TOKEN, accessToken)
            .build()

        return chain.proceed(originalRequest.newBuilder().url(url).build())
    }

    protected abstract fun getAccessToken(): String

}
