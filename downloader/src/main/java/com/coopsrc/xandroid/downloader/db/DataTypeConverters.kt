package com.coopsrc.xandroid.downloader.db

import androidx.room.TypeConverter
import com.coopsrc.xandroid.downloader.model.Status
import com.coopsrc.xandroid.downloader.model.Type

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 20:15
 */
class DataTypeConverters {
    @TypeConverter
    fun convertDownloaderType(type: Type?): String {
        return type?.name ?: ""
    }

    @TypeConverter
    fun revertDownloaderType(name: String): Type? {
        return Type.Parser.parse(name)
    }

    @TypeConverter
    fun convertDownloadStatus(status: Status): String {
        return status.name
    }

    @TypeConverter
    fun revertDownloadStatus(name: String): Status {
        return Status.Parser.parse(name)
    }
}