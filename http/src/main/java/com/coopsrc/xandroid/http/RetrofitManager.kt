package com.coopsrc.xandroid.http

import androidx.annotation.NonNull
import com.coopsrc.xandroid.http.interceptor.BasicParamsInterceptor
import com.coopsrc.xandroid.http.logging.HttpLogger
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return newRetrofit(serverConfig, BaseParamsConfig())
    }

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig, @NonNull paramsConfig: BasicParamsConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return createRetrofit(serverConfig, paramsConfig)
    }

    private fun createRetrofit(@NonNull serverConfig: ServerHostConfig, @NonNull paramsConfig: BasicParamsConfig): Retrofit {
        LogUtils.i("createRetrofit: ", serverConfig)

        val httpClientBuilder = OkHttpClient.Builder()

        // add basic params interceptor.
        val basicParamsBuilder = BasicParamsInterceptor.Builder()
        basicParamsBuilder.addQueryParams(paramsConfig.getBasicQueryParams())
        basicParamsBuilder.addBodyParams(paramsConfig.getBodyMapParams())
        basicParamsBuilder.addHeaderParams(paramsConfig.getHeaderMapParams())
        httpClientBuilder.addInterceptor(basicParamsBuilder.build())

        // add custom interceptor
        for (interceptor in paramsConfig.getCustomInterceptors()) {
            httpClientBuilder.addInterceptor(interceptor)
        }

        // set logger interceptor.
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // set cache dir.
        if (paramsConfig.getCache() != null) {
            httpClientBuilder.cache(paramsConfig.getCache())
        }

        // set time out.
        httpClientBuilder.connectTimeout(DEFAULT_CONN_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClientBuilder.retryOnConnectionFailure(true)

        // create retrofit.
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(httpClientBuilder.build())
        retrofitBuilder.baseUrl(serverConfig.getPrimaryHost())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())

        return retrofitBuilder.build()
    }

    private class BaseParamsConfig : BasicParamsConfig()
}
