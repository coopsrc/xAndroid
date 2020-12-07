package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.DownloaderProxy
import com.coopsrc.xandroid.downloader.model.SegmentInfo
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
        val segmentInfo: SegmentInfo = args[0] as SegmentInfo

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
                            segmentInfo.position,
                            readLength.toLong()
                        )
                        cacheBuffer.put(buffer, 0, readLength)

                        segmentInfo.position += readLength

                        emitter.onNext(segmentInfo)

                        readLength = source.read(buffer)
                    }

                    emitter.onComplete()
                }
            }
        }, BackpressureStrategy.BUFFER).sample(period, TimeUnit.MILLISECONDS, true)

    }
}