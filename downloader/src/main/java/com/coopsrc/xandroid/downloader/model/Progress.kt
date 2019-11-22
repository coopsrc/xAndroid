package com.coopsrc.xandroid.downloader.model

import com.coopsrc.xandroid.utils.MemoryUnit
import java.text.NumberFormat

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 19:20
 */
class Progress(var tag: String) {
    var segmentIndex: Int = 0
    var segmentProgress: Long = 0L
    var segmentSize: Long = 0L
    var downloadSize: Long = 0L
    var totalSize: Long = 0L
    var status: Status = Status.Idle
    var lastModified: Long = 0L
    var updated: Long = 0L
    private var speed: Long = 0L

    constructor(progress: Progress) : this(progress.tag) {
        this.downloadSize = progress.downloadSize
        this.totalSize = progress.totalSize
        this.status = progress.status
        this.lastModified = progress.lastModified
        this.updated = progress.updated
        this.speed = progress.speed
    }

    fun getPercent(): String {
        var percent = 0.0
        if (totalSize > 0) {
            percent = downloadSize * 1.0 / totalSize
        }
        val numberFormat = NumberFormat.getNumberInstance()
        numberFormat.maximumFractionDigits = 2
        return numberFormat.format(percent)
    }

    fun getProgress(): String {
        return String.format("%s/%s", getFormatDownloadSize(), getFormatTotalSize())
    }

    fun isComplete(): Boolean {
        return downloadSize == totalSize && totalSize > 0
    }

    fun hasUpdate(): Boolean {
        return lastModified != updated
    }

    fun setTargetDeleted() {
        this.downloadSize = 0L
        this.status = Status.Deleted
    }

    fun update(
        downloadSize: Long = this.downloadSize,
        totalSize: Long = this.totalSize,
        status: Status? = this.status,
        lastModified: Long = this.lastModified,
        updated: Long = this.updated
    ) {

        this.downloadSize = downloadSize
        this.totalSize = totalSize
        this.status = status ?: Status.Idle
        this.lastModified = lastModified
        this.updated = updated
    }

    fun update(progress: Progress) {
        this.downloadSize = progress.downloadSize
        this.totalSize = progress.totalSize
        this.status = progress.status
    }

    private fun getFormatDownloadSize(): String {
        return MemoryUnit.format(downloadSize)
    }

    private fun getFormatTotalSize(): String {
        return MemoryUnit.format(totalSize)
    }

    private fun getFormatSpeed(): String {
        return MemoryUnit.formatSpeed(speed)
    }

    override fun toString(): String {
        return "Progress(tag='$tag', downloadSize=$downloadSize, totalSize=$totalSize, status=$status, lastModified=$lastModified, updated=$updated, speed=$speed)"
    }

}