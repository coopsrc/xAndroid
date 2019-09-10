package com.coopsrc.xandroid.downloader.api

import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.http.RetrofitManager
import com.coopsrc.xandroid.http.config.HttpClientConfig
import com.coopsrc.xandroid.http.config.ServerHostConfig
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:07
 */
object DownloadApiImpl {
    private const val BASE_URL = "http://127.0.0.1"

    private var sRetrofit: Retrofit =
        RetrofitManager.newRetrofit(object : ServerHostConfig() {
            override fun getPrimaryHost(): String {
                return BASE_URL
            }
        }, object : HttpClientConfig() {
            override fun callAdapterFactories(): Set<CallAdapter.Factory> {
                return linkedSetOf(RxJava2CallAdapterFactory.create())
            }

            override fun converterFactories(): Set<Converter.Factory> {
                return linkedSetOf()
            }
        })
    private var downloadApi: DownloadApi = sRetrofit.create(DownloadApi::class.java)

    private const val tag = "DownloadApiImpl"

    fun detect(url: String, range: String = "bytes=0-0"): Maybe<Response<Void>> {
        Logger.i(tag, "detect: url=>$url")

        return downloadApi.detect(url, range).doOnSuccess {

            Logger.i(tag, "detect doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }

    fun download(url: String, range: String = ""): Maybe<Response<ResponseBody>> {
        Logger.i(tag, "download: range=>$range, url=>$url")

        return downloadApi.download(url, range).doOnSuccess {
            Logger.i(tag, "download doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }
}