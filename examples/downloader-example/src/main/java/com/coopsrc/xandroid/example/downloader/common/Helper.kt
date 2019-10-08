package com.coopsrc.xandroid.example.downloader.common

import android.app.Activity
import android.content.Intent
import android.view.View

/**
 * Created by tingkuo.
 * Date: 2018-07-30
 * Time: 13:52
 */

fun Activity.startActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun View.onAttach(){

}