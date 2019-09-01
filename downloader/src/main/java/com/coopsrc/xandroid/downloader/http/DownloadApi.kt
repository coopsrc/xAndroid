package com.coopsrc.xandroid.downloader.http

import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 19:58
 */
interface DownloadApi {
    @GET
    fun detect(@Url url: String, @Header("Range") range: String): Maybe<Response<Void>>

    @GET
    @Streaming
    fun download(@Url url: String, @Header("Range") range: String = ""): Maybe<Response<ResponseBody>>
}