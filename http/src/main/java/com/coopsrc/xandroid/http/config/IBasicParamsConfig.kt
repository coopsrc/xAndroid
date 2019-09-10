package com.coopsrc.xandroid.http.config

import okhttp3.Interceptor

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:38
 */
internal interface IBasicParamsConfig {
    fun getBasicQueryParams(): Map<String, String>

    fun getBodyMapParams(): Map<String, String>

    fun getHeaderMapParams(): Map<String, String>

    fun getInterceptors(): Set<Interceptor>

    fun getNetworkInterceptors(): Set<Interceptor>
}