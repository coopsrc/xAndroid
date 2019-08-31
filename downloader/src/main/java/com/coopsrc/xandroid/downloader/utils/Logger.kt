package com.coopsrc.xandroid.downloader.utils

import android.util.Log
import com.coopsrc.xandroid.downloader.ExDownloader
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:14
 */
object Logger {
    private const val TAG = "ExDownloader"

    private val debug = ExDownloader.withDebug()

    fun i(message: String) {
        if (debug) {
            Log.d(buildTag(TAG), buildMessage(message))
        }
    }

    fun d(message: String) {
        if (debug) {
            Log.d(buildTag(TAG), buildMessage(message))
        }
    }

    fun w(message: String) {
        if (debug) {
            Log.w(buildTag(TAG), buildMessage(message))
        }
    }

    fun e(message: String) {
        if (debug) {
            Log.e(buildTag(TAG), buildMessage(message))
        }
    }

    fun v(message: String) {
        if (debug) {
            Log.v(buildTag(TAG), buildMessage(message))
        }
    }

    fun i(tag: String, message: String) {
        if (debug) {
            Log.i(buildTag(tag), buildMessage(message))
        }
    }

    fun d(tag: String, message: String) {
        if (debug) {
            Log.d(buildTag(tag), buildMessage(message))
        }
    }

    fun w(tag: String, message: String) {
        if (debug) {
            Log.w(buildTag(tag), buildMessage(message))
        }
    }

    fun e(tag: String, message: String) {
        if (debug) {
            Log.e(buildTag(tag), buildMessage(message))
        }
    }

    fun v(tag: String, message: String) {
        if (debug) {
            Log.v(buildTag(tag), buildMessage(message))
        }
    }

    private fun buildTag(tag: String): String {

        return String.format(Locale.ROOT, "|%s|%s|%s|", tag, Thread.currentThread().name, randomKey())
    }

    private fun buildMessage(message: String): String {

        val traceElements = Thread.currentThread().stackTrace

        if (traceElements.size < 4) {
            return message
        }
        val traceElement = traceElements[4]

        return String.format(Locale.ROOT, "%s.%s[%s:%d] %s",
                traceElement.className.substring(traceElement.className.lastIndexOf(".") + 1),
                traceElement.methodName,
                traceElement.fileName,
                traceElement.lineNumber,
                message
        )
    }


    private var last: Int = 0
    fun randomKey(): String {
        var random = (10 * Math.random()).toInt()
        if (random == last) {
            random = (random + 1) % 10
        }
        last = random
        return random.toString()
    }
}