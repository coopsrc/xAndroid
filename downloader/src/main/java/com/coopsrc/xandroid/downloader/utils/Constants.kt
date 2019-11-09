package com.coopsrc.xandroid.downloader.utils

import android.content.Context
import android.os.Environment
import com.coopsrc.xandroid.downloader.BuildConfig
import java.io.File

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:02
 */
object Constants {
    var any = Any()

    object Config {

        internal const val rangeSize: Long = 8 * 1024 * 1024
        internal const val bufferSize = 1024 * 4
        internal const val period: Long = (1000 / 5).toLong()

        internal var maxTask = 3
        internal var maxRange = Runtime.getRuntime().availableProcessors()
        internal fun workPath(context: Context): String {
            val externalCacheDir = context.externalCacheDir

            return if (externalCacheDir != null && externalCacheDir.exists()) {
                externalCacheDir.path
            } else {
                context.cacheDir.path
            }
        }

        internal var autoStart = false
        internal var enableBackground = false
        internal var limitSpeed = 0L

        internal var withDebug = BuildConfig.DEBUG
    }
}