package com.coopsrc.xandroid.http

import com.coopsrc.xandroid.http.config.IServerHostConfig
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:53
 */
abstract class ServerHostConfig : IServerHostConfig {

    override fun getSecondaryHosts(): LinkedHashSet<String> {
        return LinkedHashSet()
    }

    override fun isHostLoopEnabled(): Boolean {
        return false
    }
}
