package com.coopsrc.xandroid.downloader.db

import com.coopsrc.xandroid.utils.LogUtils
import com.squareup.sqlbrite3.SqlBrite

/**
 * Created by tingkuo.
 * Date: 2018-07-25
 * Time: 11:25
 */
internal class SqlLogger : SqlBrite.Logger {
    override fun log(message: String?) {

        LogUtils.tag(buildTag()).d(message);
    }

    companion object {
        private fun buildTag(): String {
            return String.format("|SqlLogger|%s|", Thread.currentThread().name)
        }
    }
}