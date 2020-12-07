package com.coopsrc.xandroid.downloader.model

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 18:08
 */
enum class Status {
    Idle, Waiting, Starting, Prepared, Downloading, Suspend, Complete, Failed, Removed, Deleted;

    object Parser {
        fun parse(ordinal: Int): Status {
            return when (ordinal) {
                Idle.ordinal -> Idle
                Waiting.ordinal -> Waiting
                Starting.ordinal -> Starting
                Prepared.ordinal -> Prepared
                Downloading.ordinal -> Downloading
                Suspend.ordinal -> Suspend
                Complete.ordinal -> Complete
                Failed.ordinal -> Failed
                Removed.ordinal -> Removed
                Deleted.ordinal -> Deleted
                else -> Idle
            }
        }

        fun parse(name: String): Status {

            return when (name) {
                Idle.name -> Idle
                Waiting.name -> Waiting
                Starting.name -> Starting
                Prepared.name -> Prepared
                Downloading.name -> Downloading
                Suspend.name -> Suspend
                Complete.name -> Complete
                Failed.name -> Failed
                Removed.name -> Removed
                Deleted.name -> Deleted
                else -> Idle
            }
        }
    }

    fun isWorking(): Boolean {
        return this == Waiting || this == Starting || this == Downloading
    }

    fun canStart(): Boolean {
        return !isWorking() && this != Complete
    }

    fun canStop(): Boolean {
        return isWorking()
    }
}