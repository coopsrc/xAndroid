package com.coopsrc.xandroid.http.monitor.common;

import android.app.PendingIntent;
import android.content.Context;

import com.coopsrc.xandroid.http.monitor.HttpMonitor;
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
        notification(httpInfo);
        return httpMonitorDao.insert(httpInfo);
//        return -1;
    }

    @Override
    public void update(HttpInfo httpInfo) {
        LogUtils.d("update: %s", httpInfo);
        notification(httpInfo);
        httpMonitorDao.update(httpInfo);
    }

    protected void notification(HttpInfo httpInfo) {
        LogUtils.w("notification: %s", httpInfo);
        MonitorNotification.getInstance(context).show(httpInfo);
    }
}
