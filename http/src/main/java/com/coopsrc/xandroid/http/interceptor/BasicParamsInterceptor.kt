package com.coopsrc.xandroid.http.interceptor

import android.text.TextUtils
import okhttp3.*
import java.io.IOException
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:50
 */
class BasicParamsInterceptor : Interceptor {

    private val headerParams = HashMap<String, String>()
    private val queryParams = HashMap<String, String>()
    private val bodyParams = HashMap<String, String>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        val headersBuilder = originalRequest.headers.newBuilder()
        for ((key, value) in headerParams) {
            headersBuilder.add(key, value)
        }
        requestBuilder.headers(headersBuilder.build())

        val httpUrlBuilder = originalRequest.url.newBuilder()
        for ((key, value) in queryParams) {
            httpUrlBuilder.addQueryParameter(key, value)
        }
        requestBuilder.url(httpUrlBuilder.build())

        if (canInjectRequestBody(originalRequest)) {
            if (originalRequest.body is FormBody) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in bodyParams) {
                    formBodyBuilder.add(key, value)
                }

                val originalBody = originalRequest.body as FormBody?
                if (originalBody != null && originalBody.size > 0) {
                    for (i in 0 until originalBody.size) {
                        formBodyBuilder.add(originalBody.name(i), originalBody.value(i))
                    }
                }

                requestBuilder.method(originalRequest.method, formBodyBuilder.build());
            } else if (originalRequest.body is MultipartBody) {
                val multipartBuilder = MultipartBody.Builder()
                for ((key, value) in bodyParams) {
                    multipartBuilder.addFormDataPart(key, value)
                }

                val originalBody = originalRequest.body as MultipartBody?
                if (originalBody != null && originalBody.size > 0) {
                    for (part in originalBody.parts) {
                        multipartBuilder.addPart(part)
                    }
                }

                requestBuilder.method(originalRequest.method, multipartBuilder.build())
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun canInjectRequestBody(request: Request): Boolean {

        if (TextUtils.equals(request.method, "GET") || TextUtils.equals(request.method, "HEAD")) {
            return false
        }

        return true
    }

    class Builder {

        private val mInterceptor: BasicParamsInterceptor = BasicParamsInterceptor()

        fun addHeaderParams(headers: Map<String, String>): Builder {
            mInterceptor.headerParams.putAll(headers)
            return this
        }

        fun addQueryParams(params: Map<String, String>): Builder {
            mInterceptor.queryParams.putAll(params)
            return this
        }

        fun addBodyParams(body: Map<String, String>): Builder {
            mInterceptor.bodyParams.putAll(body)
            return this
        }

        fun build(): BasicParamsInterceptor {
            return mInterceptor
        }
    }
}