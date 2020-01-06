package com.coopsrc.xandroid.downloader.model

/**
 * Created by tingkuo.
 * Date: 2018-08-01
 * Time: 09:38
 */
internal class Segment {

    var tag: String
    var index: Long
    var start: Long
    var position: Long
    var end: Long

    constructor(tag: String, index: Long, start: Long, end: Long) : this(
        tag,
        index,
        start,
        start,
        end
    )

    constructor(tag: String, index: Long, start: Long, position: Long, end: Long) {
        this.tag = tag
        this.index = index
        this.start = start
        this.position = position
        this.end = end
    }

    fun isComplete(): Boolean {
        return (position - end) == 1L
    }

    fun downloadSize(): Long {
        return position - start
    }

    fun size(): Long {
        return end - start
    }

    fun update(position: Long) {
        this.position = position
    }

    fun update(segment: Segment) {
        this.tag = segment.tag
        this.index = segment.index
        this.start = segment.start
        this.position = segment.position
        this.end = segment.end
    }

    override fun toString(): String {
        return "Segment(tag='$tag', index=$index, start=$start, position=$position, end=$end)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Segment

        if (tag != other.tag) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + index.hashCode()
        return result
    }
}