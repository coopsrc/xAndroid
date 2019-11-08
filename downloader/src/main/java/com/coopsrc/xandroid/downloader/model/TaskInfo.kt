package com.coopsrc.xandroid.downloader.model

import com.coopsrc.xandroid.downloader.utils.DownloaderUtils

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:44
 */
class TaskInfo(var url: String) {

    var saveName: String = ""
    var savePath: String = ""
    var type: Type? = null
    var tag: String = DownloaderUtils.encodeMd5(url)

    var progress: Progress = Progress(tag)

    constructor(url: String, saveName: String) : this(url) {
        this.saveName = saveName
    }

    constructor(url: String, saveName: String, savePath: String) : this(url) {
        this.saveName = saveName
        this.savePath = savePath

    }

    constructor(url: String, saveName: String, savePath: String, type: Type) : this(url) {
        this.saveName = saveName
        this.savePath = savePath
        this.type = type
    }

    constructor(url: String, saveName: String, savePath: String, type: Type?, tag: String) : this(url) {
        this.saveName = saveName
        this.savePath = savePath
        this.type = type
        this.tag = tag
    }

    fun update(saveName: String = this.saveName,
               savePath: String = this.savePath,
               type: Type? = this.type) {
        this.saveName = saveName
        this.savePath = savePath
        this.type = type
    }

    fun update(taskInfo: TaskInfo) {
        this.saveName = taskInfo.saveName
        this.savePath = taskInfo.savePath
        this.type = taskInfo.type
        this.progress.update(taskInfo.progress)
        this.progress.update(lastModified = taskInfo.progress.lastModified)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskInfo

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }

    override fun toString(): String {
        return "TaskInfo(url='$url', saveName='$saveName', workPath='$savePath', type=$type, tag='$tag', progress=$progress)"
    }

}