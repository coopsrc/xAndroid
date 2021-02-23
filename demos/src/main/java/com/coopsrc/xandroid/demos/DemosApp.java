package com.coopsrc.xandroid.demos;

import android.app.Application;

import com.coopsrc.xandroid.downloader.ExDownloader;
import com.coopsrc.xandroid.downloader.core.DownloaderConfig;
import com.coopsrc.xandroid.downloader.helper.RangeMode;
import com.coopsrc.xandroid.utils.LogUtils;
import com.coopsrc.xandroid.utils.logger.DebugLogger;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-23 17:24
 */
public class DemosApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.register(new DebugLogger());
    }
}
