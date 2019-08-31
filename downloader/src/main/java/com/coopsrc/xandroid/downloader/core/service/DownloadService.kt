package com.coopsrc.xandroid.downloader.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.coopsrc.xandroid.downloader.utils.Constants
import com.coopsrc.xandroid.downloader.utils.Logger

class DownloadService : Service() {
    private val tag = "DownloadService"

    private var binder: DownloadBinder = DownloadBinder()

    override fun onCreate() {
        super.onCreate()
        Logger.i(tag, "onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.i(tag, "onStartCommand: $intent, $flags, $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        Logger.i(tag, "onBind: $intent")
        val maxTask = intent.getIntExtra("maxTask", Constants.Config.maxTask)
        Logger.i(tag, "createLocalTaskRepo: $maxTask")
        binder = DownloadBinder(maxTask)
        return binder
    }

    override fun onDestroy() {
        Logger.v(tag, "onDestroy: ")

        binder.stopAll().subscribe()

        super.onDestroy()
    }
}
