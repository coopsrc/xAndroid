package com.coopsrc.xandroid.downloader.core.impl

import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.api.DownloadApiProxy
import com.coopsrc.xandroid.downloader.core.DownloadTask
import com.coopsrc.xandroid.downloader.core.Downloader
import com.coopsrc.xandroid.downloader.db.DatabaseModule
import com.coopsrc.xandroid.downloader.model.Progress
import com.coopsrc.xandroid.downloader.model.Segment
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-26
 * Time: 17:17
 */
internal class RangeDownloader(downloadTask: DownloadTask) : Downloader(downloadTask) {

    private val tag = "RangeDownloader"

    private val rangeSize = Constants.Config.rangeSize

    private val maxRange = ExDownloader.downloadCore.config.rangeMode.count
    private val segments = mutableListOf<Segment>()

    private val segmentsAction = DatabaseModule.instance.segmentsAction

    init {
        Logger.i(tag, "init: ${downloadTask.taskInfo}")

        downloaderProxy = RangeDownloaderProxy(downloadTask)

        if (segmentsAction.exist(downloadTask.taskInfo.tag)
            && !downloaderProxy.isTargetDelete()
            && !downloaderProxy.isCacheDelete()
        ) {
            segments.clear()
            segments.addAll(segmentsAction.list(downloadTask.taskInfo.tag))
        } else {
            initSegments(downloadTask.taskInfo)
        }
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

        val segmentFlowList = mutableListOf<Flowable<Segment>>()

        segments.filter {
            !it.isComplete()
        }.forEach {
            segmentFlowList.add(downloadSegment(it))
        }

        return Flowable.mergeDelayError(segmentFlowList, maxRange)
            .doOnNext {
                segmentsAction.update(it)
                updateProgress(it)
            }.map {
                downloadTask.taskInfo.progress
            }.doOnComplete {
                downloaderProxy.doOnComplete()
            }
    }

    override fun checkLastModified() {

        if (downloadTask.taskInfo.progress.hasUpdate()) {
            downloaderProxy.cleanWorkspace()
            initSegments(downloadTask.taskInfo)
        }
    }

    override fun remove() {
        Logger.i(tag, "remove:")

        downloaderProxy.cleanWorkspace()
    }

    @Synchronized
    private fun updateProgress(segment: Segment) {
        segments[segment.index.toInt()] = segment

        val downloadSize = segments.map {
            it.downloadSize()
        }.sum()

        downloadTask.taskInfo.progress.update(downloadSize = downloadSize)
    }

    private fun initSegments(taskInfo: TaskInfo) {
        Logger.w(tag, "initSegments: $taskInfo")
        segments.clear()
        segmentsAction.delete(taskInfo.tag)

        var start: Long
        var end: Long
        val segmentsCount: Long = calculateSegmentCount(taskInfo)

        for (index in 0 until segmentsCount) {
            start = rangeSize * index
            end = rangeSize * (index + 1) - 1

            if (end >= taskInfo.progress.totalSize) {
                end = taskInfo.progress.totalSize - 1
            }

            Logger.d(tag, "$index, $start, $start, $end")
            segments.add(Segment(taskInfo.tag, index, start, start, end))
        }

        segments.forEach {
            segmentsAction.create(it)
        }
    }

    private fun calculateSegmentCount(taskInfo: TaskInfo): Long {
        val remainder = taskInfo.progress.totalSize % rangeSize

        return if (remainder == 0L) {
            taskInfo.progress.totalSize / rangeSize
        } else {
            taskInfo.progress.totalSize / rangeSize + 1
        }
    }

    private fun downloadSegment(segment: Segment): Flowable<Segment> {
        return Maybe.just(segment).subscribeOn(Schedulers.io()).map {
            "bytes=${it.position}-${it.end}"
        }.doOnSuccess {
            Logger.w(tag, "Start download: $segment, Range: $it")
        }.flatMap {
            DownloadApiProxy.download(downloadTask.taskInfo.url, it)
        }.flatMapPublisher {
            downloaderProxy.saveTargetFile(it, segment)
        }.map {
            it as Segment
        }
    }
}