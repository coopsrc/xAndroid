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

package com.coopsrc.xandroid.http.monitor.common;

import android.content.Context;

import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDao;
import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.ui.MonitorNotification;
import com.coopsrc.xandroid.utils.LogUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 15:42
 */
public class Monitor implements IMonitor {


    private Context context;
    private HttpMonitorDao httpMonitorDao;

    public Monitor(Context context) {
        this.context = context;
        httpMonitorDao = HttpMonitorDatabase.getInstance(context).getHttpMonitorDao();
    }

    @Override
    public long insert(HttpInfo httpInfo) {
        LogUtils.d("insert: %s", httpInfo);
//        notification(httpInfo);
        return httpMonitorDao.insert(httpInfo);
    }

    @Override
    public void update(HttpInfo httpInfo) {
        LogUtils.d("update: %s", httpInfo);
        notification(httpInfo);
        httpMonitorDao.update(httpInfo);
    }

    private void notification(HttpInfo httpInfo) {
        LogUtils.w("notification: %s", httpInfo);
        MonitorNotification.getInstance(context).show(httpInfo);
    }
}
