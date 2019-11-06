package com.coopsrc.xandroid.downloader.api

import android.content.Context
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.http.config.HttpClientConfig
import retrofit2.CallAdapter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * @author tingkuo
 *
 * Datetime: 2019-11-06 16:18
 */
class DownloadClientConfig : HttpClientConfig() {
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