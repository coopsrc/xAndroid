package com.coopsrc.xandroid.downloader.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coopsrc.xandroid.downloader.model.TaskInfo
import com.coopsrc.xandroid.downloader.model.Type

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 20:26
 */
@Entity(tableName = "tasks", indices = [Index(value = ["tag"], unique = true)])
class TaskInfoEntry(
    url: String,
    saveName: String,
    savePath: String,
    type: Type?,
    tag: String
) : TaskInfo(
    url,
    saveName,
    savePath,
    type,
    tag
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(taskInfo: TaskInfo) : this(
        taskInfo.url,
        taskInfo.saveName,
        taskInfo.savePath,
        taskInfo.type,
        taskInfo.tag
    ) {
        progress.update(taskInfo.progress)
    }
}