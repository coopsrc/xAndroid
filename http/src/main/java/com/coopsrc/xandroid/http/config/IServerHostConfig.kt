package com.coopsrc.xandroid.http.config

import java.util.*

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:37
 */
interface IServerHostConfig {

    fun getPrimaryHost(): String

    fun getSecondaryHosts(): LinkedList<String>

    fun isEnableHostLoop(): Boolean
}