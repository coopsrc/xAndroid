package com.coopsrc.xandroid.downloader.api

import android.content.Context
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.http.api.BaseApiProxy
import com.coopsrc.xandroid.http.config.HttpClientConfig
import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
import com.coopsrc.xandroid.http.monitor.MonitorInterceptor
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

internal object DownloadApiProxy : BaseApiProxy<DownloadApiService >() {

    private const val tag = "DownloadApiImpl"

    private class DownloadClientConfig : HttpClientConfig() {
        override fun getAppContext(): Context {
            return ExDownloader.downloadCore.config.context
        }

        override fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory> {
            return linkedSetOf(RxJava2CallAdapterFactory.create())
        }

        override fun getDebugMonitorInterceptor(): BaseMonitorInterceptor? {
            return if (ExDownloader.withDebug()){
                MonitorInterceptor(getAppContext())
            } else{
                null
            }
        }
    }

    override fun clientConfig(): HttpClientConfig {
        return DownloadClientConfig()
    }

    fun detect(url: String, range: String = "bytes=0-0"): Maybe<Response<Void>> {
        Logger.i(tag, "detect: url=>$url")

        return apiService.detect(url, range).doOnSuccess {

            Logger.i(tag, "detect doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }

    fun download(url: String, range: String = ""): Maybe<Response<ResponseBody>> {
        Logger.i(tag, "download: range=>$range, url=>$url")

        return apiService.download(url, range).doOnSuccess {
            Logger.i(tag, "download doOnSuccess")

            if (!it.isSuccessful) {
                throw RuntimeException(it.message())
            }
        }
    }
}
