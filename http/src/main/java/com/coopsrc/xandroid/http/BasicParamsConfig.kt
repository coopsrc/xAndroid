package com.coopsrc.xandroid.http

import com.coopsrc.xandroid.http.config.IBasicParamsConfig
import okhttp3.Cache
import okhttp3.Interceptor
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:52
 */
abstract class BasicParamsConfig : IBasicParamsConfig {

    override fun getBasicQueryParams(): Map<String, String> {
        return HashMap()
    }

    override fun getBodyMapParams(): Map<String, String> {
        return HashMap()
    }

    override fun getHeaderMapParams(): Map<String, String> {
        return HashMap()
    }

    override fun getCustomInterceptors(): Set<Interceptor> {
        return HashSet()
    }

    override fun getCache(): Cache? {
        return null
    }
}