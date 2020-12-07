package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.api.DownloadApiProxy
import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.Downloader
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-26
 * Time: 15:22
 */
internal class SingleDownloader(downloadTask: DownloadTask) : Downloader(downloadTask) {

    private val tag = "SingleDownloader"

    init {
        Logger.i(tag, "init: ${downloadTask.taskInfo}")

        downloaderProxy = SingleDownloaderProxy(downloadTask)
    }

    override fun targetFile(): File? {
        Logger.i(tag, "targetFile:")

        return if (downloaderProxy.isComplete()) {
            downloaderProxy.targetFile
        } else null
    }

    override fun download(): Flowable<out Progress> {
        Logger.i(tag, "download:")

        checkLastModified()

        if (downloaderProxy.isComplete()) {
            return Flowable.just(downloadTask.taskInfo.progress)
        }

        downloaderProxy.initWorkspace()

        return Maybe.just(Constants.any)
            .flatMap {
                DownloadApiProxy.download(downloadTask.taskInfo.url, provideRange())
            }.flatMapPublisher {
                downloaderProxy.saveTargetFile(it)
            }.map {
                it as Progress
            }
    }

    override fun checkLastModified() {
        if (downloadTask.taskInfo.progress.hasUpdate()) {
            downloaderProxy.cleanWorkspace()
        }
    }

    override fun remove() {
        Logger.i(tag, "remove:")

        downloaderProxy.cleanWorkspace()
    }

    private fun provideRange(): Map<String, String> {
        return Constants.RangeHeader.single(downloadTask.taskInfo.progress.downloadSize)
    }
}