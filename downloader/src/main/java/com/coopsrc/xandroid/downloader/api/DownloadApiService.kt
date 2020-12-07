package com.coopsrc.xandroid.downloader.api

import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 19:58
 */
internal interface DownloadApiService {
    @GET
    fun detect(@Url url: String, @HeaderMap headers: Map<String, String>): Maybe<Response<Void>>

    @GET
    @Streaming
    fun download(@Url url: String, @HeaderMap headers: Map<String, String>): Maybe<Response<ResponseBody>>
}