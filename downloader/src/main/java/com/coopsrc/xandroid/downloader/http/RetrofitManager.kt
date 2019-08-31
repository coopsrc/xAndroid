package com.coopsrc.xandroid.downloader.http

import com.coopsrc.xandroid.downloader.utils.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 19:59
 */
object RetrofitManager {

    private const val TAG = "RetrofitManager"

    private const val DEFAULT_CONN_TIME_OUT = 30000
    private const val DEFAULT_READ_TIME_OUT = 30000
    private const val DEFAULT_WRITE_TIME_OUT = 30000

    fun newRetrofit(baseUrl: String): Retrofit {
        Logger.i(TAG, "newRetrofit: $baseUrl")

        return createRetrofit(baseUrl)
    }


    private fun createRetrofit(baseUrl: String): Retrofit {
        Logger.i(TAG, "createRetrofit: $baseUrl")

        val httpClientBuilder = OkHttpClient.Builder()

        // set time out.
        httpClientBuilder.connectTimeout(DEFAULT_CONN_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(DEFAULT_WRITE_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)

        // set logger interceptor.
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // set retrofit.
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(baseUrl)
        retrofitBuilder.client(httpClientBuilder.build())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        return retrofitBuilder.build()
    }

}