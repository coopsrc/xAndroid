package com.coopsrc.demo.exdownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.coopsrc.demo.exdownloader.common.startActivity
import com.coopsrc.demo.exdownloader.compare.CompareActivity
import com.coopsrc.demo.exdownloader.list.ListActivity
import com.coopsrc.demo.exdownloader.simple.SimpleActivity
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
