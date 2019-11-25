package com.coopsrc.xandroid.downloader.core

import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 11:33
 */
internal abstract class DownloaderProxy(protected val downloadTask: DownloadTask) {

    private val tag = "DownloaderProxy"

    private var workspace: String

    var targetFile: File
    protected var cacheFile: File

    val progress: Progress

    init {
        Logger.i(tag, "init")
        workspace = if (downloadTask.taskInfo.savePath.isEmpty()) {
            ExDownloader.downloadCore.config.workPath
        } else {
            downloadTask.taskInfo.savePath
        }

        val workPath = File(workspace)
        if (!workPath.exists() || !workPath.isDirectory) {
            workPath.mkdirs()
        }

        targetFile = File(workspace, downloadTask.taskInfo.saveName)
        cacheFile = File(workspace, "${downloadTask.taskInfo.saveName}.dl")

        if (isTargetDelete() || isCacheDelete()) {
            downloadTask.taskInfo.progress.setTargetDeleted()
        }

        progress = Progress(downloadTask.taskInfo.progress)
    }

    internal abstract fun initWorkspace()

    internal fun isTargetDelete(): Boolean {
        return downloadTask.taskInfo.progress.status == Status.Deleted ||
                (downloadTask.taskInfo.progress.status == Status.Complete && !isComplete())
    }

    internal fun isCacheDelete(): Boolean {
        return downloadTask.taskInfo.progress.downloadSize > 0 && !isCacheExists()
    }

    internal fun isCacheExists(): Boolean {
        return cacheFile.exists() && cacheFile.isFile
    }

    internal fun isComplete(): Boolean {
        Logger.i(tag, "isComplete: ")

        return targetFile.exists() && targetFile.isFile
    }

    internal abstract fun saveTargetFile(response: Response<ResponseBody>, vararg args: Any): Flowable<Any>

    internal fun cleanWorkspace() {
        Logger.i(tag, "cleanWorkspace: ")

        if (targetFile.exists()) {
            targetFile.delete()
        }

        if (cacheFile.exists()) {
            cacheFile.delete()
        }

        updateProgress(0L)
    }

    internal fun doOnComplete(): Boolean {
        Logger.i(tag, "doOnComplete: $cacheFile, $targetFile")
        return cacheFile.renameTo(targetFile)
    }

    protected fun updateProgress(downloadSize: Long) {
        progress.update(downloadSize = downloadSize)
    }

    protected fun updateProgress(status: Status) {
        progress.update(status = status)
    }

    protected fun updateProgress(downloadSize: Long, status: Status) {
        progress.update(downloadSize = downloadSize, status = status)
    }
}