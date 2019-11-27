package com.coopsrc.xandroid.demos.downloader

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.xandroid.demos.R
import com.coopsrc.xandroid.demos.downloader.simple.SimpleDownloadActivity
import com.coopsrc.xandroid.utils.MemoryUnit

import kotlinx.android.synthetic.main.activity_downloader.*
import kotlinx.android.synthetic.main.content_downloader.*

class DownloaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun simpleDownload(view: View) {
        val intent = Intent(this, SimpleDownloadActivity::class.java)
        startActivity(intent)
    }

}
