package com.coopsrc.xandroid.demos.particle

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.xandroid.demos.R
import com.coopsrc.xandroid.demos.particle.drop.DropParticleActivity
import com.coopsrc.xandroid.utils.LogUtils
import com.coopsrc.xandroid.utils.logger.DebugLogger
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_particle.*

class ParticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_particle)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        testLogUtils()
    }

    fun dropParticle(view: View) {
        val intent = Intent(this, DropParticleActivity::class.java)
        startActivity(intent)
    }

    fun testLogUtils() {
        LogUtils.register(DebugLogger())
        val exception = Exception("Test Exception")

//        LogUtils.v(exception)
        LogUtils.v("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.v(exception, "testLogUtils: [%d, %d]", 1920, 1080)
//
//        LogUtils.d(exception)
        LogUtils.d("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.d(exception, "testLogUtils: [%d, %d]", 1920, 1080)
//
//        LogUtils.i(exception)
        LogUtils.i("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.tag("AAAA").i("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.i(exception, "testLogUtils: [%d, %d]", 1920, 1080)
//
//        LogUtils.w(exception)
        LogUtils.w("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.w(exception, "testLogUtils: [%d, %d]", 1920, 1080)
//
//        LogUtils.e(exception)
        LogUtils.e("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.e(exception, "testLogUtils: [%d, %d]", 1920, 1080)
//
//        LogUtils.wtf(exception)
        LogUtils.wtf("testLogUtils: [%d, %d]", 1920, 1080)
//        LogUtils.wtf(exception, "testLogUtils: [%d, %d]", 1920, 1080)
    }

}
