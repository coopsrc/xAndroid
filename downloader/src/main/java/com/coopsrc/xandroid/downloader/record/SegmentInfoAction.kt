package com.coopsrc.xandroid.downloader.record

import com.coopsrc.xandroid.downloader.db.DatabaseAction
import com.coopsrc.xandroid.downloader.db.SegmentInfoDao
import com.coopsrc.xandroid.downloader.db.SegmentInfoEntry
import com.coopsrc.xandroid.downloader.model.SegmentInfo
import com.coopsrc.xandroid.downloader.utils.Logger

/**
 * Created by tingkuo.
 * Date: 2018-08-02
 * Time: 16:53
 */
internal class SegmentInfoAction(private val segmentInfoDao: SegmentInfoDao) :
    DatabaseAction<SegmentInfo> {
    private val tag = "SegmentInfoAction"

    fun exist(tag: String): Boolean {
        Logger.i(tag, "exist: $tag")

        return segmentInfoDao.count(tag) > 0
    }

    fun list(tag: String): MutableList<SegmentInfo> {
        Logger.i(tag, "list: $tag")

        val segments = mutableListOf<SegmentInfo>()

        segmentInfoDao.load(tag).forEach {
            segments.add(it as SegmentInfo)
        }
        return segments
    }

    override fun exist(data: SegmentInfo): Boolean {
        Logger.i(tag, "exist: $data")

        val segmentInfo = segmentInfoDao.loadWithOrder(data.tag)
        return segmentInfo.isNotEmpty()
    }

    override fun create(data: SegmentInfo): Long {
        Logger.i(tag, "create: $data")

        return segmentInfoDao.insert(SegmentInfoEntry(data))
    }

    override fun read(data: SegmentInfo): Boolean {
        Logger.i(tag, "read: $data")

        val segmentInfo = segmentInfoDao.read(data.tag, data.index)

        if (segmentInfo == null) {
            return false
        } else {
            data.update(segmentInfo)
            return true
        }
    }

    override fun update(data: Any): Int {
        Logger.i(tag, "update: $data")
        return when (data) {
            is SegmentInfo -> updateSegment(data)
            else -> -1
        }
    }

    private fun updateSegment(segmentInfo: SegmentInfo): Int {
        Logger.i(tag, "updateSegment: $segmentInfo")

        return segmentInfoDao.update(SegmentInfoEntry(segmentInfo))
    }

    fun delete(tag: String): Int {
        Logger.i(tag, "delete: $tag")

        return segmentInfoDao.delete(tag)
    }

    override fun delete(data: SegmentInfo): Int {
        Logger.i(tag, "delete: $data")

        return segmentInfoDao.delete(data.tag, data.index)
    }

    override fun clear(): Int {
        Logger.i(tag, "clear:")

        return segmentInfoDao.clear()
    }

}