package com.coopsrc.xandroid.downloader.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coopsrc.xandroid.downloader.utils.Constants

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-12-07 17:57
 */
@Database(
    entities = [TaskInfoEntry::class, SegmentInfoEntry::class],
    version = Constants.DB.DB_VERSION,
    exportSchema = false
)
@TypeConverters(DataTypeConverters::class)
abstract class DownloadDatabase : RoomDatabase() {

    abstract fun taskInfoDao(): TaskInfoDao

    abstract fun segmentInfoDao(): SegmentInfoDao

    private fun populateInitialData() {

    }

    companion object {
        @Volatile
        private var sInstance: DownloadDatabase? = null

        fun getInstance(context: Context): DownloadDatabase =
            sInstance ?: synchronized(this) {
                sInstance ?: createDatabase(context, false).also { sInstance = it }
            }


        private fun createDatabase(context: Context, useInMemory: Boolean): DownloadDatabase {
            val databaseBuilder: Builder<DownloadDatabase> = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, DownloadDatabase::class.java)
            } else {
                Room.databaseBuilder(
                    context,
                    DownloadDatabase::class.java,
                    Constants.DB.DB_NAME
                )
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }

        @VisibleForTesting
        fun switchToInMemory(context: Context) {
            sInstance = createDatabase(context, true)
            sInstance!!.populateInitialData()
        }

    }


}