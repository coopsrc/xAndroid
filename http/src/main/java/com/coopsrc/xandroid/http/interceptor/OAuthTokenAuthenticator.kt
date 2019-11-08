package com.coopsrc.xandroid.http.interceptor

import com.coopsrc.xandroid.http.config.HttpConstants
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

abstract class OAuthTokenAuthenticator : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        LogUtils.w("authenticate: rote=%s, response=%s", route, response)

        val accessToken = refreshAccessToken()
        LogUtils.i("intercept: refreshAccessToken => %s", accessToken)

        val original = response.request

        val url = original.url.newBuilder()
            .setQueryParameter(HttpConstants.ACCESS_TOKEN, accessToken)
            .build()
        LogUtils.i("intercept: %s", url.queryParameterNames)

        return original.newBuilder().url(url).build()
    }

    protected abstract fun refreshAccessToken(): String

}
