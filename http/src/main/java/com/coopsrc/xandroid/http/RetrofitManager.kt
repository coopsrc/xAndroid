package com.coopsrc.xandroid.http

import android.content.Context
import com.coopsrc.xandroid.http.interceptor.BasicParamsInterceptor
import com.coopsrc.xandroid.http.logging.HttpLogger
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:53
 */
object RetrofitManager {

    private val DEFAULT_CONN_TIME_OUT = TimeUnit.SECONDS.toMillis(5)
    private val DEFAULT_READ_TIME_OUT = TimeUnit.SECONDS.toMillis(5)
    private val DEFAULT_WRITE_TIME_OUT = TimeUnit.SECONDS.toMillis(5)

    private const val CACHE_DIR = "http_cache"
    private const val CACHE_SIZE: Long = 1024 * 1024 * 100

    @JvmStatic
    fun newRetrofit(context: Context, serverHostConfig: ServerHostConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverHostConfig.getPrimaryHost())

        return newRetrofit(context, serverHostConfig, BaseParamsConfig())
    }

    @JvmStatic
    fun newRetrofit(
        context: Context,
        serverHostConfig: ServerHostConfig,
        basicParamsConfig: BasicParamsConfig
    ): Retrofit {
        LogUtils.i("newRetrofit: %s", serverHostConfig.getPrimaryHost())

        return createRetrofit(context, serverHostConfig, basicParamsConfig)
    }

    private fun createRetrofit(
        context: Context,
        serverHostConfig: ServerHostConfig,
        basicParamsConfig: BasicParamsConfig
    ): Retrofit {
        LogUtils.i("createRetrofit: ", serverHostConfig)

        val httpClientBuilder = OkHttpClient.Builder()

        // add basic params interceptor.
        val basicParamsBuilder = BasicParamsInterceptor.Builder()
        basicParamsBuilder.addQueryParams(basicParamsConfig.getBasicQueryParams())
        basicParamsBuilder.addBodyParams(basicParamsConfig.getBodyMapParams())
        basicParamsBuilder.addHeaderParams(basicParamsConfig.getHeaderMapParams())
        httpClientBuilder.addInterceptor(basicParamsBuilder.build())

        // add custom interceptor
        for (interceptor in basicParamsConfig.getCustomInterceptors()) {
            httpClientBuilder.addInterceptor(interceptor)
        }

        // set logger interceptor.
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // set cache dir.
        if (basicParamsConfig.getCache() != null) {
            httpClientBuilder.cache(basicParamsConfig.getCache())
        } else {
            val cacheDir = File(context.cacheDir, CACHE_DIR)
            val cache = Cache(cacheDir, CACHE_SIZE) //100Mb
            httpClientBuilder.cache(cache)
        }

        // set time out.
        httpClientBuilder.connectTimeout(DEFAULT_CONN_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.retryOnConnectionFailure(true)

        // create retrofit.
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(httpClientBuilder.build())
        retrofitBuilder.baseUrl(serverHostConfig.getPrimaryHost())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())

        return retrofitBuilder.build()
    }

    private class BaseParamsConfig : BasicParamsConfig()
}
