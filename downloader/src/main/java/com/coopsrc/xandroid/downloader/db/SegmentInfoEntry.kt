package com.coopsrc.xandroid.downloader.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coopsrc.xandroid.downloader.model.SegmentInfo

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 20:27
 */
@Entity(tableName = "segments", indices = [Index(value = ["tag", "_index"], unique = true)])
class SegmentInfoEntry(tag: String, index: Long, start: Long, position: Long, end: Long) :
    SegmentInfo(
        tag,
        index,
        start,
        position,
        end
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(segmentInfo: SegmentInfo) : this(
        segmentInfo.tag,
        segmentInfo.index,
        segmentInfo.start,
        segmentInfo.position,
        segmentInfo.end
    )
}