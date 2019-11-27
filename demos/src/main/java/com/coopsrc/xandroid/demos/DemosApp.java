package com.coopsrc.xandroid.demos;

import android.app.Application;

import com.coopsrc.xandroid.downloader.ExDownloader;
import com.coopsrc.xandroid.downloader.core.DownloaderConfig;
import com.coopsrc.xandroid.downloader.helper.RangeMode;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-23 17:24
 */
public class DemosApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ExDownloader.INSTANCE.init(new DownloaderConfig.Builder()
                .rangeMode(RangeMode.Companion.fixed(4))
                .maxRange(4)
                .maxTask(3)
                .build());


    }
}
