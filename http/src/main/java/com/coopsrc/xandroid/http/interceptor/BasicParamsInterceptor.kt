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

    private val queryParams = HashMap<String, String>()
    private val bodyParams = HashMap<String, String>()
    private val headerParams = HashMap<String, String>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()


        val headersBuilder = request.headers.newBuilder()
        for ((key, value) in headerParams) {
            headersBuilder.add(key, value)
        }

        val requestBuilder = request.newBuilder()
        val httpUrlBuilder = request.url.newBuilder()
        for ((key, value) in queryParams) {
            httpUrlBuilder.addQueryParameter(key, value)
        }
        requestBuilder.url(httpUrlBuilder.build())

        if (canInjectRequestBody(request)) {
            if (request.body is FormBody) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in bodyParams) {
                    formBodyBuilder.add(key, value)
                }

                val originalBody = request.body as FormBody?
                if (originalBody != null && originalBody.size > 0) {
                    for (i in 0 until originalBody.size) {
                        formBodyBuilder.add(originalBody.name(i), originalBody.value(i))
                    }
                }

                requestBuilder.method(request.method, formBodyBuilder.build());
            } else if (request.body is MultipartBody) {
                val multipartBuilder = MultipartBody.Builder()
                for ((key, value) in bodyParams) {
                    multipartBuilder.addFormDataPart(key, value)
                }

                val originalBody = request.body as MultipartBody?
                if (originalBody != null && originalBody.size > 0) {
                    for (part in originalBody.parts) {
                        multipartBuilder.addPart(part)
                    }
                }

                requestBuilder.method(request.method, multipartBuilder.build())
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

        fun addQueryParam(key: String, value: String): Builder {
            mInterceptor.queryParams[key] = value
            return this
        }

        fun addQueryParams(params: Map<String, String>): Builder {
            mInterceptor.queryParams.putAll(params)
            return this
        }

        fun addBodyParam(key: String, value: String): Builder {
            mInterceptor.bodyParams[key] = value
            return this
        }

        fun addBodyParams(body: Map<String, String>): Builder {
            mInterceptor.bodyParams.putAll(body)
            return this
        }

        fun addHeaderParam(key: String, value: String): Builder {
            mInterceptor.headerParams[key] = value
            return this
        }

        fun addHeaderParams(headers: Map<String, String>): Builder {
            mInterceptor.headerParams.putAll(headers)
            return this
        }

        fun build(): BasicParamsInterceptor {
            return mInterceptor
        }
    }
}