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
        internal fun savePath(context: Context?): String {

            return try {
                if (context == null) {
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                } else {
                    if (File(context.externalCacheDir!!.path).exists()) {
                        context.externalCacheDir!!.path
                    } else {
                        context.cacheDir.path
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Environment.getExternalStorageDirectory().path
            }

        }

        internal var autoStart = false
        internal var enableBackground = false
        internal var limitSpeed = 0L

        internal var withDebug = BuildConfig.DEBUG
    }
}