package com.coopsrc.xandroid.downloader.utils

import okhttp3.Headers
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 17:26
 */
object HttpUtils {
    fun isChunked(response: Response<*>): Boolean {
        return "chunked".equals(transferEncoding(response), ignoreCase = true)
    }

    fun transferEncoding(response: Response<*>): String {
        var header = response.headers().get("Transfer-Encoding")

        if (header == null) {
            header = ""
        }

        return header
    }

    fun fileName(saveName: String, url: String, response: Response<*>): String {
        if (saveName.isNotEmpty()) {
            return saveName
        }

        var fileName = contentDisposition(response)
        if (fileName.isEmpty()) {
            fileName = substringUrl(url)
        }
        return fileName
    }

    private fun substringUrl(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1)
    }

    fun lastModified(response: Response<*>): Long {
        val lastModified = response.headers().get("Last-Modified")

        return Date.parse(lastModified)
    }


    private fun contentDisposition(response: Response<*>): String {
        val disposition = response.headers().get("Content-Disposition")

        if (disposition == null || disposition.isEmpty()) {
            return ""
        }

        val matcher =
            Pattern.compile(".*filename=(.*)").matcher(disposition.toLowerCase(Locale.ROOT))
        if (!matcher.find()) {
            return ""
        }

        var result = matcher.group(1)
        if (result.startsWith("\"")) {
            result = result.substring(1)
        }
        if (result.endsWith("\"")) {
            result = result.substring(0, result.length - 1)
        }

        result = result.replace("/", "_", false)

        return result
    }

    fun isSupportRange(resp: Response<*>): Boolean {
        if (!resp.isSuccessful) {
            return false
        }

        if (resp.code() == 206 || contentRange(resp).isNotEmpty() || acceptRanges(resp).isNotEmpty()) {
            return true
        }

        return false
    }

    private fun contentRange(response: Response<*>): String {
        var header = response.headers().get("Content-Range")
        if (header == null) {
            header = ""
        }
        return header
    }

    private fun acceptRanges(response: Response<*>): String {
        var header = response.headers().get("Accept-Ranges")
        if (header == null) {
            header = ""
        }
        return header
    }

    fun contentLength(response: Response<*>): Long {
        return contentLength(response.headers())
    }

    fun contentLength(response: okhttp3.Response): Long {
        return contentLength(response.headers)
    }

    private fun contentLength(headers: Headers): Long {
        return stringToLong(headers["Content-Length"])
    }

    private fun stringToLong(s: String?): Long {
        if (s == null) return -1
        try {
            return java.lang.Long.parseLong(s)
        } catch (e: NumberFormatException) {
            return -1
        }

    }

    fun contentRangeLength(response: Response<*>): Long {
        val contentRange = contentRange(response)

        return contentRange.split('/')[1].toLong()
    }

    fun contentRangeStart(response: Response<*>): Long {
        val contentRange = contentRange(response)

        return contentRange.replace("bytes ", "").split('-')[0].toLong()
    }
}