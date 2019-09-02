package com.coopsrc.demo.exdownloader.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.demo.exdownloader.R

import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
    }
}
