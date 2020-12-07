package com.coopsrc.xandroid.demos

import android.content.Context
import androidx.startup.Initializer
import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.downloader.core.DownloaderConfig
import com.coopsrc.xandroid.downloader.helper.RangeMode.Companion.fixed


/**
 * @author Tingkuo
 * <p>
 * Datetime: 2020-09-14 17:05
 */
class DemoAppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ExDownloader.init(
            DownloaderConfig.Builder()
                .rangeMode(fixed(4))
                .maxRange(4)
                .maxTask(3)
                .withDebug(true)
                .build()
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>?>> {
        return emptyList()
    }
}