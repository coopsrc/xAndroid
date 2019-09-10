package com.coopsrc.xandroid.http

import com.coopsrc.xandroid.http.config.IBasicParamsConfig
import okhttp3.Cache
import okhttp3.Interceptor

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:52
 */
abstract class BasicParamsConfig : IBasicParamsConfig {

    override fun getBasicQueryParams(): Map<String, String> {
        return LinkedHashMap()
    }

    override fun getBodyMapParams(): Map<String, String> {
        return LinkedHashMap()
    }

    override fun getHeaderMapParams(): Map<String, String> {
        return LinkedHashMap()
    }

    override fun getInterceptors(): Set<Interceptor> {
        return LinkedHashSet()
    }

    override fun getNetworkInterceptors(): Set<Interceptor> {
        return LinkedHashSet()
    }

    override fun getCache(): Cache? {
        return null
    }
}