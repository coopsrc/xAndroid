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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.coopsrc.xandroid.http.monitor.model.HttpInfo;

import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 17:01
 */
@Dao
public interface HttpMonitorDao {
    @Insert
    long insert(HttpInfo httpInfo);

    @Update
    void update(HttpInfo httpInfo);

    @Query("SELECT * FROM http_info ORDER BY id DESC")
    List<HttpInfo> query();

    @Query("SELECT * FROM http_info ORDER BY id DESC LIMIT :limit")
    List<HttpInfo> query(int limit);

    @Query("SELECT * FROM http_info WHERE id =:id ORDER BY id DESC")
    HttpInfo queryById(long id);

    @Query("SELECT * FROM http_info ORDER BY id DESC")
    LiveData<List<HttpInfo>> queryData();

    @Query("SELECT * FROM http_info ORDER BY id DESC LIMIT :limit")
    LiveData<List<HttpInfo>> queryData(int limit);

    @Query("SELECT * FROM http_info WHERE id =:id ORDER BY id DESC")
    LiveData<HttpInfo> queryDataById(long id);

    @Query("DELETE FROM http_info")
    void clear();

    @Delete
    void delete(HttpInfo httpInfo);

    @Query("DELETE FROM http_info WHERE id =:id")
    void deleteById(long id);


}
