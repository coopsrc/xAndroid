package com.coopsrc.xandroid.http.monitor.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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
}
