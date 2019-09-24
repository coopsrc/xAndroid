package com.coopsrc.xandroid.http.monitor;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.ui.MonitorNotification;
import com.coopsrc.xandroid.http.monitor.ui.list.MonitorActivity;

import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 19:46
 */
public class HttpMonitor {
    public static Intent getMonitorIntent(Context context) {
        Intent intent = new Intent(context, MonitorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void clearNotification(Context context) {
        MonitorNotification.getInstance(context).clearBuffer();
        MonitorNotification.getInstance(context).dismiss();
    }

    public static void showNotification(Context context, boolean showNotification) {
        MonitorNotification.getInstance(context).setShowNotification(showNotification);
    }

    public static void clearCache(Context context) {
        HttpMonitorDatabase.getInstance(context).getHttpMonitorDao().clear();
    }

    public static LiveData<List<HttpInfo>> queryData(Context context) {
        return HttpMonitorDatabase.getInstance(context).getHttpMonitorDao().queryData();
    }

    public static LiveData<List<HttpInfo>> queryData(Context context, int limit) {
        return HttpMonitorDatabase.getInstance(context).getHttpMonitorDao().queryData(limit);
    }

    public static LiveData<HttpInfo> queryDataById(Context context, long id) {
        return HttpMonitorDatabase.getInstance(context).getHttpMonitorDao().queryDataById(id);
    }

}
