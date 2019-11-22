package com.coopsrc.xandroid.downloader.utils

import io.reactivex.disposables.Disposable
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat

/**
 * Created by tingkuo.
 * Date: 2018-07-23
 * Time: 17:55
 */
object DownloaderUtils {

    fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    fun formatSize(size: Long): String {
        val b = size.toDouble()
        val k = b / 1024.0
        val m = k / 1024.0
        val g = m / 1024.0
        val t = g / 1024.0
        val decimalFormat = DecimalFormat("0.00")

        return when {
            t > 1 -> decimalFormat.format(t) + " TB"
            g > 1 -> decimalFormat.format(g) + " GB"
            m > 1 -> decimalFormat.format(m) + " MB"
            k > 1 -> decimalFormat.format(k) + " KB"
            else -> decimalFormat.format(b) + " B"
        }
    }

    fun encodeMd5(text: String): String {
        try {
            val digest: MessageDigest = MessageDigest.getInstance("MD5")
            val data: ByteArray = digest.digest(text.toByteArray())
            val sb = StringBuffer()
            for (b in data) {
                val i: Int = b.toInt() and 0xff
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return text
        }
    }
}