package com.coopsrc.xandroid.downloader.utils

import android.content.Context
import com.coopsrc.xandroid.downloader.BuildConfig
import com.coopsrc.xandroid.utils.MemoryUnit
import java.util.concurrent.TimeUnit

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 20:02
 */
object Constants {
    var any = Any()

    object Config {

        internal val rangeSize: Long = MemoryUnit.MEGA_BYTE.toBytes(8)
        internal val bufferSize = MemoryUnit.KILO_BYTE.toBytes(4).toInt()
        internal val period: Long = TimeUnit.MILLISECONDS.toMillis(200)

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