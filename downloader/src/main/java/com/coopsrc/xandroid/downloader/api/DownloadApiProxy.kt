package com.coopsrc.xandroid.downloader.api

import com.coopsrc.xandroid.downloader.utils.Logger
import com.coopsrc.xandroid.http.api.BaseApiProxy
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response

internal object DownloadApiProxy : BaseApiProxy<DownloadApiService,  DownloadClientConfig>() {

    private const val tag = "DownloadApiImpl"

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
