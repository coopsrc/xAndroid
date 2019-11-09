package com.coopsrc.xandroid.example.downloader

import android.app.Application
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.core.Config
import com.coopsrc.xandroid.utils.LogUtils

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 19:43
 */
class DownloadApp : Application() {
    override fun onCreate() {
        super.onCreate()

        LogUtils.plant(LogUtils.DebugLogger())

        ExDownloader.init(
            Config.Builder()
                .autoStart(false)
                .maxTask(1)
                .maxRange(4)
                .enableBackground(false)
                .withDebug(true)
                .build()
        )
    }
}