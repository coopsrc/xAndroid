package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.model.Progress
import io.reactivex.Flowable
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:40
 */
internal abstract class Downloader(val downloadTask: DownloadTask) {

    protected lateinit var downloaderProxy: DownloaderProxy

    internal abstract fun targetFile(): File?

    internal abstract fun download(): Flowable<out Progress>

    internal abstract fun checkLastModified()

    internal abstract fun remove()
}