package com.coopsrc.xandroid.example.downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.coopsrc.xandroid.downloader.R
import com.coopsrc.xandroid.example.downloader.common.startActivity
import com.coopsrc.xandroid.example.downloader.compare.CompareActivity
import com.coopsrc.xandroid.example.downloader.list.ListActivity
import com.coopsrc.xandroid.example.downloader.simple.SimpleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSimple.setOnClickListener {
            startActivity(SimpleActivity::class.java)
        }

        buttonCompare.setOnClickListener {
            startActivity(CompareActivity::class.java)
        }

        buttonList.setOnClickListener {
            startActivity(ListActivity::class.java)
        }
    }
}
