package com.coopsrc.xandroid.http

import androidx.annotation.NonNull
import com.coopsrc.xandroid.http.config.BasicParamsConfig
import com.coopsrc.xandroid.http.config.HttpClientConfig
import com.coopsrc.xandroid.http.config.ServerHostConfig
import com.coopsrc.xandroid.http.interceptor.BasicParamsInterceptor
import com.coopsrc.xandroid.http.logging.HttpLogger
import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:53
 */
object RetrofitManager {

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return createRetrofit(serverConfig, BaseHttpClientConfig(), BaseParamsConfig())
    }

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig, @NonNull paramsConfig: BasicParamsConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return createRetrofit(serverConfig, paramsConfig = paramsConfig)
    }

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig, @NonNull clientConfig: HttpClientConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return createRetrofit(serverConfig, clientConfig)
    }

    @JvmStatic
    fun newRetrofit(@NonNull serverConfig: ServerHostConfig, @NonNull clientConfig: HttpClientConfig, @NonNull paramsConfig: BasicParamsConfig): Retrofit {
        LogUtils.i("newRetrofit: %s", serverConfig.getPrimaryHost())

        return createRetrofit(serverConfig, clientConfig, paramsConfig)
    }

    private fun createRetrofit(
        @NonNull serverConfig: ServerHostConfig,
        @NonNull clientConfig: HttpClientConfig = BaseHttpClientConfig(),
        @NonNull paramsConfig: BasicParamsConfig = BaseParamsConfig()
    ): Retrofit {
        LogUtils.i("createRetrofit: ", serverConfig)

        val httpClientBuilder = OkHttpClient.Builder()

        // add basic params interceptor.
        val basicParamsBuilder = BasicParamsInterceptor.Builder()
        basicParamsBuilder.addQueryParams(paramsConfig.getBasicQueryParams())
        basicParamsBuilder.addBodyParams(paramsConfig.getBodyMapParams())
        basicParamsBuilder.addHeaderParams(paramsConfig.getHeaderMapParams())
        httpClientBuilder.addInterceptor(basicParamsBuilder.build())

        // add custom interceptor
        for (interceptor in paramsConfig.getInterceptors()) {
            httpClientBuilder.addInterceptor(interceptor)
        }

        // add custom network interceptor
        for (interceptor in paramsConfig.getNetworkInterceptors()) {
            httpClientBuilder.addNetworkInterceptor(interceptor)
        }

        // set logger interceptor.
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLoggingInterceptor.level = clientConfig.httpLogLevel()
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // set cache dir.
        if (clientConfig.httpClientCache() != null) {
            httpClientBuilder.cache(clientConfig.httpClientCache())
        }

        // set time out.
        httpClientBuilder.connectTimeout(clientConfig.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(clientConfig.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(clientConfig.readTimeoutMillis(), TimeUnit.MILLISECONDS)
        httpClientBuilder.retryOnConnectionFailure(clientConfig.retryOnConnectionFailure())

        // create retrofit.
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(httpClientBuilder.build())
        retrofitBuilder.baseUrl(serverConfig.getPrimaryHost())
        for (callAdapterFactory in clientConfig.callAdapterFactories()) {
            retrofitBuilder.addCallAdapterFactory(callAdapterFactory)
        }
        for (converterFactory in clientConfig.converterFactories()) {
            retrofitBuilder.addConverterFactory(converterFactory)
        }

        return retrofitBuilder.build()
    }

    private class BaseHttpClientConfig : HttpClientConfig()

    private class BaseParamsConfig : BasicParamsConfig()
}
