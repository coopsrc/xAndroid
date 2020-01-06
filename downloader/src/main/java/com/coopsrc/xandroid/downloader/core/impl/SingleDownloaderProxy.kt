package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.DownloaderProxy
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.HttpUtils
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-07-26
 * Time: 15:21
 */
internal class SingleDownloaderProxy(downloadTask: DownloadTask) : DownloaderProxy(downloadTask) {

    private val tag = "SingleDownloaderProxy"

    init {
        Logger.i(tag, "init: ")
    }

    override fun initWorkspace() {
        Logger.i(tag, "initWorkspace: ")
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }
    }

    override fun saveTargetFile(response: Response<ResponseBody>, vararg args: Any): Flowable<Any> {
        Logger.i(tag, "saveTargetFile: $response, $args")
        val responseBody = response.body() ?: throw RuntimeException("Response Body is NULL")
        val period = Constants.Config.period

        var downloadSize = HttpUtils.contentRangeStart(response)
        val bufferSize = Constants.Config.bufferSize

        updateProgress(Status.Downloading)

        return Flowable.create<Any>({ emitter ->
            Logger.w(tag, "start save: from $downloadSize")

            responseBody.byteStream().use { source ->
                RandomAccessFile(cacheFile, "rw").use { cache ->
                    Logger.w(tag, cache.filePointer.toString())

                    cache.seek(downloadSize)
                    cache.channel.use { cacheChannel ->
                        val buffer = ByteArray(bufferSize)
                        var readLength = source.read(buffer)
                        var byteBuffer: MappedByteBuffer

                        while (readLength != -1 && !emitter.isCancelled) {
                            byteBuffer = cacheChannel.map(
                                FileChannel.MapMode.READ_WRITE,
                                downloadSize,
                                readLength.toLong()
                            )
                            byteBuffer.put(buffer, 0, readLength)

                            downloadSize += readLength
                            updateProgress(downloadSize)

                            emitter.onNext(progress)

                            readLength = source.read(buffer)
                        }

                        source.close()
                        cacheChannel.close()
                        cache.close()
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
            }

        }, BackpressureStrategy.BUFFER).sample(period, TimeUnit.MILLISECONDS, true)

    }
}