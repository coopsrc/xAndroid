package com.coopsrc.xandroid.downloader.model

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:47
 */
enum class Type {
     Normal, Single, Range;

    object Parser {
        fun parse(ordinal: Int): Type? {
            return when (ordinal) {
                Normal.ordinal -> Normal
                Single.ordinal -> Single
                Range.ordinal -> Range
                else -> null
            }
        }

        fun parse(type: String): Type? {
            return when (type) {
                Normal.name -> Normal
                Single.name -> Single
                Range.name -> Range
                else -> null
            }
        }
    }
}