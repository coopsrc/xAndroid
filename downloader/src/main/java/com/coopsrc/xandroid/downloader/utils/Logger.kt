package com.coopsrc.xandroid.downloader.utils

import com.coopsrc.xandroid.downloader.ExDownloader
import com.coopsrc.xandroid.utils.LogUtils
import java.util.*

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:14
 */
object Logger {
    private const val TAG = "ExDownloader"

    fun i(message: String) {
        if (debug()) {
            LogUtils.tag(TAG).i(message)
        }
    }

    fun d(message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(TAG)).d(buildMessage(message))
        }
    }

    fun w(message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(TAG)).w(buildMessage(message))
        }
    }

    fun e(message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(TAG)).e(buildMessage(message))
        }
    }

    fun v(message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(TAG)).v(buildMessage(message))
        }
    }

    fun i(tag: String, message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(tag)).i(buildMessage(message))
        }
    }

    fun d(tag: String, message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(tag)).d(buildMessage(message))
        }
    }

    fun w(tag: String, message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(tag)).w(buildMessage(message))
        }
    }

    fun e(tag: String, message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(tag)).e(buildMessage(message))
        }
    }

    fun v(tag: String, message: String) {
        if (debug()) {
            LogUtils.tag(buildTag(tag)).v(buildMessage(message))
        }
    }

    private fun buildTag(tag: String): String {

        return String.format("|%s|%s|", tag, Thread.currentThread().name)
    }

    private fun buildMessage(message: String): String {

        val traceElements = Thread.currentThread().stackTrace

        if (traceElements.size < 4) {
            return message
        }
        val traceElement = traceElements[4]

        return String.format(
            Locale.ROOT, "%s.%s[%s:%d] %s",
            traceElement.className.substring(traceElement.className.lastIndexOf(".") + 1),
            traceElement.methodName,
            traceElement.fileName,
            traceElement.lineNumber,
            message
        )
    }

    private fun debug(): Boolean {
        return ExDownloader.withDebug()
    }
}