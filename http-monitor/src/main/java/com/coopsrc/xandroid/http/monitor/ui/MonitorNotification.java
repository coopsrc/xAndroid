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

package com.coopsrc.xandroid.http.monitor.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.LongSparseArray;

import androidx.core.app.NotificationCompat;

import com.coopsrc.xandroid.http.monitor.BuildConfig;
import com.coopsrc.xandroid.http.monitor.HttpMonitor;
import com.coopsrc.xandroid.http.monitor.R;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 17:39
 */
public class MonitorNotification {

    private static final String CHANNEL_ID = "http-monitor";

    private static final String CHANNEL_NAME = "Http Monitor Notifications";

    private static final String NOTIFICATION_TITLE = "Http Monitor";

    private static final int NOTIFICATION_ID = 10800;

    private static volatile MonitorNotification instance;

    private Context context;

    private int transactionCount;
    private LongSparseArray<HttpInfo> transactionBuffer = new LongSparseArray<>();
    private static final int BUFFER_SIZE = 5;

    private NotificationManager notificationManager;

    private volatile boolean showNotification = true;

    private MonitorNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    public static MonitorNotification getInstance(Context context) {
        if (instance == null) {
            synchronized ((MonitorNotification.class)) {
                if (instance == null) {
                    instance = new MonitorNotification(context);
                }
            }
        }
        return instance;
    }

    public synchronized void show(HttpInfo httpInfo) {
        if (showNotification && notificationManager != null) {
            addToBuffer(httpInfo);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setContentIntent(getContentIntent(context));
            builder.setLocalOnly(true);
            builder.setSmallIcon(R.drawable.ic_timeline);
            builder.setContentTitle(NOTIFICATION_TITLE);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            int size = transactionBuffer.size();
            if (size > 0) {
                builder.setContentText(transactionBuffer.valueAt(size - 1).getNotificationText());
                for (int i = size - 1; i >= 0; i--) {
                    inboxStyle.addLine(transactionBuffer.valueAt(i).getNotificationText());
                }
            }
            builder.setAutoCancel(false);
            builder.setStyle(inboxStyle);
            builder.setSound(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setSubText(String.valueOf(transactionCount));
            } else {
                builder.setNumber(transactionCount);
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }

    private synchronized void addToBuffer(HttpInfo httpInfo) {
        transactionCount++;
        transactionBuffer.put(httpInfo.id, httpInfo);

        if (transactionBuffer.size() > BUFFER_SIZE) {
            transactionBuffer.removeAt(0);
        }
    }

    public synchronized void clearBuffer() {
        transactionBuffer.clear();
        transactionCount = 0;
    }

    public synchronized void dismiss() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    private PendingIntent getContentIntent(Context context) {
        return PendingIntent.getActivity(context, 100, HttpMonitor.getMonitorIntent(context), 0);
    }
}
