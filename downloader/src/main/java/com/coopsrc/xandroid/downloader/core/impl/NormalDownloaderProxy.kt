package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.DownloaderProxy
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.Response
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 11:50
 */
internal class NormalDownloaderProxy(downloadTask: DownloadTask) : DownloaderProxy(downloadTask) {

    private val tag = "NormalDownloaderProxy"

    init {
        Logger.i(tag, "init: ")
    }

    override fun initWorkspace() {
        Logger.i(tag, "initWorkspace: ")
        cacheFile.createNewFile()

        updateProgress(0L)
    }

    override fun saveTargetFile(response: Response<ResponseBody>, vararg args: Any): Flowable<Any> {
        Logger.i(tag, "saveTargetFile: $response, $args")

        val responseBody = response.body() ?: throw RuntimeException("Response Body is NULL")

        val period = Constants.Config.period

        var downloadSize = 0L
        val bufferSize = Constants.Config.bufferSize.toLong()

        updateProgress(downloadSize, Status.Downloading)

        return Flowable.create<Any>({ emitter ->
            Logger.w(tag, "start save")

            responseBody.source().use { source ->
                cacheFile.sink().buffer().use { sink ->
                    var readLength = source.read(sink.buffer, bufferSize)
                    while (readLength != -1L && !emitter.isCancelled) {
                        sink.emit()

                        downloadSize += readLength
                        updateProgress(downloadSize)

                        emitter.onNext(progress)

                        readLength = source.read(sink.buffer, bufferSize)
                    }

                    source.close()
                    sink.close()
                    Logger.i(tag, "finish save: cache file size:${cacheFile.length()}")

                    if (!emitter.isCancelled) {
                        if (doOnComplete()) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(Throwable("Save target file failed!"))
                        }
                    } else {
                        emitter.onError(Throwable("Save target file failed!"))
                    }
                }
            }

        }, BackpressureStrategy.BUFFER).sample(period, TimeUnit.MILLISECONDS, true)
    }

}