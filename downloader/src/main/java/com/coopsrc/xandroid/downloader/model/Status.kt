package com.coopsrc.xandroid.downloader.model

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 18:08
 */
enum class Status {
    Idle, Waiting, Starting, Downloading, Suspend, Complete, Failed, Removed, Deleted;

    object Parser {
        fun parse(ordinal: Int): Status {
            return when (ordinal) {
                Idle.ordinal -> Idle
                Waiting.ordinal -> Waiting
                Starting.ordinal -> Starting
                Downloading.ordinal -> Downloading
                Suspend.ordinal -> Suspend
                Complete.ordinal -> Complete
                Failed.ordinal -> Failed
                Removed.ordinal -> Removed
                Deleted.ordinal -> Deleted
                else -> Idle
            }
        }

        fun parse(type: String): Status {
            return valueOf(type)
        }
    }

    fun isWorking(): Boolean {
        return this == Waiting || this == Starting || this == Downloading
    }

    fun canStart(): Boolean {
        return !isWorking() && this != Complete
    }

    fun canStop():Boolean{
        return isWorking()
    }
}