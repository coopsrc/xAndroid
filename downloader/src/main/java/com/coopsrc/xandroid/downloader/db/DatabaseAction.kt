package com.coopsrc.xandroid.downloader.db

import com.coopsrc.xandroid.downloader.core.Action

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 08:17
 */
internal interface DatabaseAction<in T> : Action {

    fun exist(data: T): Boolean

    fun create(data: T): Long

    fun read(data: T): Boolean

    fun update(data: Any): Int

    fun delete(data: T): Int

    fun clear(): Int
}