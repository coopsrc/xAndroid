/*
 * Copyright (C) 2019 Zhang Tingkuo(zhangtingkuo@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coopsrc.xandroid.http.monitor.db;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.utils.LogUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 17:00
 */
@Database(entities = {HttpInfo.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HttpMonitorDatabase extends RoomDatabase {

    private static final String DB_NAME = "HttpMonitor.db";

    private static HttpMonitorDatabase sInstance;

    public static HttpMonitorDatabase getInstance(Context context) {
        LogUtils.i("getInstance: %s", context);
        if (sInstance == null) {
            sInstance = createDatabase(context, false);
        }

        return sInstance;
    }

    private static HttpMonitorDatabase createDatabase(Context context, boolean useInMemory) {
        LogUtils.i("createDatabase: %s,%s", context, useInMemory);
        RoomDatabase.Builder<HttpMonitorDatabase> databaseBuilder;
        if (useInMemory) {
            databaseBuilder = Room.inMemoryDatabaseBuilder(context, HttpMonitorDatabase.class);
        } else {
            databaseBuilder = Room.databaseBuilder(context, HttpMonitorDatabase.class, DB_NAME);
        }

        return databaseBuilder.fallbackToDestructiveMigration().build();
    }

    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        LogUtils.i("switchToInMemory: %s", context);
        sInstance = createDatabase(context, true);
    }

    public abstract HttpMonitorDao getHttpMonitorDao();
}
