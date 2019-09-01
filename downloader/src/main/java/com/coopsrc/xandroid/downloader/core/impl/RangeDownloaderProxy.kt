package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.DownloaderProxy
import com.coopsrc.xandroid.downloader.model.Segment
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-07-26
 * Time: 17:16
 */
internal class RangeDownloaderProxy(downloadTask: DownloadTask) : DownloaderProxy(downloadTask) {
    private val tag = "RangeDownloaderProxy"

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
        val segment: Segment = args[0] as Segment

        val responseBody = response.body() ?: throw RuntimeException("Response Body is NULL")
        val period = Constants.Config.period
        val bufferSize = Constants.Config.bufferSize

        return Flowable.create<Any>({ emitter ->

            Logger.w(tag, "start save")

            val buffer = ByteArray(bufferSize)

            responseBody.byteStream().use { source ->
                RandomAccessFile(cacheFile, "rw").channel.use { cacheChannel ->

                    var readLength = source.read(buffer)
                    while (readLength != -1 && !emitter.isCancelled) {
                        val cacheBuffer = cacheChannel.map(
                                FileChannel.MapMode.READ_WRITE,
                                segment.position,
                                readLength.toLong()
                        )
                        cacheBuffer.put(buffer, 0, readLength)

                        segment.position += readLength

                        emitter.onNext(segment)

                        readLength = source.read(buffer)
                    }

                    emitter.onComplete()
                }
            }
        }, BackpressureStrategy.BUFFER).sample(period, TimeUnit.MILLISECONDS, true)

    }
}