package com.coopsrc.xandroid.http.config

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:37
 */
interface IServerHostConfig {

    fun getPrimaryHost(): String

    fun getSecondaryHosts(): LinkedHashSet<String>

    fun isHostLoopEnabled(): Boolean
}