package com.coopsrc.xandroid.downloader.api

import android.content.Context
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.http.api.BaseApiProxy
import com.coopsrc.xandroid.http.config.HttpClientConfig
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.HeaderMap

internal object DownloadApiProxy : BaseApiProxy<DownloadApiService>() {

    private const val tag = "DownloadApiImpl"

    private class DownloadClientConfig : HttpClientConfig() {
        override fun getAppContext(): Context {
            return ExDownloader.downloadCore.config.context
        }

        override fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory> {
            return linkedSetOf(RxJava2CallAdapterFactory.create())
        }

    }

    override fun clientConfig(): HttpClientConfig {
        return DownloadClientConfig()
    }

    fun detect(url: String, @HeaderMap headers: Map<String, String>): Maybe<Response<Void>> {
        Logger.i(tag, "detect: url=>$url")

        return apiService.detect(url, headers).doOnSuccess {

            Logger.i(tag, "detect doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }

    fun download(url: String, headers: Map<String, String> = Constants.RangeHeader.NORMAL): Maybe<Response<ResponseBody>> {
        Logger.i(tag, "download: range=>$headers, url=>$url")

        return apiService.download(url, headers).doOnSuccess {
            Logger.i(tag, "download doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }
}
