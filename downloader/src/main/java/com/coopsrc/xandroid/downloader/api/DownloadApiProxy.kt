package com.coopsrc.xandroid.downloader.api

import android.content.Context
import com.coopsrc.xandroid.downloader.BuildConfig
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.http.api.BaseApiProxy
import com.coopsrc.xandroid.http.config.HttpClientConfig
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

internal object DownloadApiProxy : BaseApiProxy<DownloadApiService>() {

    private const val tag = "DownloadApiImpl"

    override fun initApiService(): DownloadApiService {
        return createApiService(DownloadApiService::class.java)
    }

    override fun clientConfig(): HttpClientConfig {
        return DownloadClientConfig()
    }

    private class DownloadClientConfig : ClientConfig() {
        override fun getAppContext(): Context? {
            return ExDownloader.downloadCore.config.context
        }

        override fun callAdapterFactories(): LinkedHashSet<CallAdapter.Factory> {
            return linkedSetOf(RxJava2CallAdapterFactory.create())
        }

        override fun useDebugMonitor(): Boolean {
            return ExDownloader.withDebug()
        }
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
