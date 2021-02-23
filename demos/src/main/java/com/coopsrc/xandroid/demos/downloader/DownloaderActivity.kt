package com.coopsrc.xandroid.demos.downloader

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.xandroid.demos.databinding.ActivityDownloaderBinding
import com.coopsrc.xandroid.demos.downloader.simple.SimpleDownloadActivity
import com.google.android.material.snackbar.Snackbar

class DownloaderActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDownloaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDownloaderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)

        mBinding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun simpleDownload(view: View) {
        val intent = Intent(this, SimpleDownloadActivity::class.java)
        startActivity(intent)
    }

}
