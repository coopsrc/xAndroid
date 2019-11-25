package com.coopsrc.xandroid.downloader.helper

import com.coopsrc.xandroid.downloader.utils.Constants

/**
 * @author tingkuo
 *
 *
 * Datetime: 2019-11-22 15:00
 */
class RangeMode {
    enum class Mode {
        FIXED, FLEXED
    }

    var mode: Mode
    var count: Int

    constructor(mode: Mode, count: Int) {
        this.mode = mode
        this.count = count
    }

    constructor(mode: Mode) {
        this.mode = mode
        count = Constants.Config.maxRange
    }

    companion object {
        val FIXED = RangeMode(Mode.FIXED)
        val FLEXED = RangeMode(Mode.FLEXED)

        fun fixed(count: Int): RangeMode {
            return RangeMode(Mode.FIXED, fixedCount(count))
        }

        fun flexed(count: Int): RangeMode {
            return RangeMode(Mode.FLEXED, fixedCount(count))
        }

        private fun fixedCount(count: Int): Int {
            return when {
                count <= 0 -> 1
                count > Constants.Config.maxRange -> Constants.Config.maxRange
                else -> count
            }
        }
    }
}