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

package com.coopsrc.xandroid.http.monitor.arch;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.model.KeyValue;
import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils;
import com.coopsrc.xandroid.utils.AppTaskExecutors;
import com.coopsrc.xandroid.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 20:25
 */
public class MonitorViewModel extends ViewModel {
    private static final String TAG = "MonitorListViewModel";
    private final HttpMonitorDatabase database;

    private MutableLiveData<Integer> fetchCount = new MutableLiveData<>();
    private LiveData<List<HttpInfo>> listLiveData;

    private MutableLiveData<Long> fetchId = new MutableLiveData<>();
    private LiveData<HttpInfo> httpInfoLiveData;


    private LiveData<List<KeyValue>> keyValueData;

    public MonitorViewModel(final HttpMonitorDatabase database) {
        this.database = database;
        LiveData<LiveData<List<HttpInfo>>> listSource = Transformations.map(fetchCount, new Function<Integer, LiveData<List<HttpInfo>>>() {
            @Override
            public LiveData<List<HttpInfo>> apply(Integer input) {
                return database.getHttpMonitorDao().queryData(input);
            }
        });

        listLiveData = Transformations.switchMap(listSource, new Function<LiveData<List<HttpInfo>>, LiveData<List<HttpInfo>>>() {
            @Override
            public LiveData<List<HttpInfo>> apply(LiveData<List<HttpInfo>> input) {
                Log.i(TAG, "apply: ");
                return input;
            }
        });

        LiveData<LiveData<HttpInfo>> source = Transformations.map(fetchId, new Function<Long, LiveData<HttpInfo>>() {
            @Override
            public LiveData<HttpInfo> apply(Long input) {
                return database.getHttpMonitorDao().queryDataById(input);
            }
        });
        httpInfoLiveData = Transformations.switchMap(source, new Function<LiveData<HttpInfo>, LiveData<HttpInfo>>() {
            @Override
            public LiveData<HttpInfo> apply(LiveData<HttpInfo> input) {
                return input;
            }
        });

        keyValueData = Transformations.map(httpInfoLiveData, new Function<HttpInfo, List<KeyValue>>() {
            @Override
            public List<KeyValue> apply(HttpInfo input) {
                LogUtils.w("1. onChanged: %s", input);

                List<KeyValue> list = new LinkedList<>();

                list.add(new KeyValue("Url", input.requestInfo.getUrl().toString()));
                list.add(new KeyValue("Method", input.requestInfo.getMethod()));
                list.add(new KeyValue("Protocol", input.responseInfo.getProtocol().toString()));
                list.add(new KeyValue("Response Code", String.valueOf(input.responseInfo.getCode())));
                list.add(new KeyValue("Request Date", MonitorUtils.getDateFormatLong(input.requestInfo.getDate())));
                list.add(new KeyValue("Response Date", MonitorUtils.getDateFormatLong(input.responseInfo.getDate())));
                list.add(new KeyValue("Duration", String.format("%s ms", input.responseInfo.getDuration())));
                list.add(new KeyValue("Request Size", MonitorUtils.formatBytes(input.requestInfo.getContentLength())));
                list.add(new KeyValue("Response Size", MonitorUtils.formatBytes(input.responseInfo.getContentLength())));

                return list;
            }
        });

    }

    public LiveData<List<HttpInfo>> getListLiveData() {
        return listLiveData;
    }

    public void fetch(int limit) {
        fetchCount.postValue(limit);
    }

    public LiveData<HttpInfo> getHttpInfoLiveData() {
        return httpInfoLiveData;
    }

    public void getById(long id) {
        fetchId.postValue(id);
    }

    public LiveData<List<KeyValue>> getKeyValueData() {
        return keyValueData;
    }

    public void clearRecord() {
        AppTaskExecutors.databaseIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getHttpMonitorDao().clear();
            }
        });
    }

    public void delete(final HttpInfo httpInfo) {
        AppTaskExecutors.databaseIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getHttpMonitorDao().delete(httpInfo);
            }
        });
    }

    public void deleteById(final long id) {
        AppTaskExecutors.databaseIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getHttpMonitorDao().deleteById(id);
            }
        });
    }
}
