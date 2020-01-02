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
