/*
 * Copyright (C) 2021 Zhang Tingkuo(zhangtingkuo@gmail.com)
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
 *
 */

package com.coopsrc.xandroid.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.coopsrc.xandroid.utils.AppTaskExecutors;
import com.coopsrc.xandroid.utils.HandlerUtils;
import com.coopsrc.xandroid.utils.LogUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2021-02-04 14:25
 */
public abstract class ContentLiveData<T> extends MutableLiveData<T> {

    private static final String TAG = "ContentLiveData";

    public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
    public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
    public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";

    private final Uri mUri;

    private final ContentResolver mContentResolver;
    private final ContentObserver mContentObserver;


    private long mLastUpdate = -1;
    private static final long DEFAULT_INTERVAL = 2_000L;

    private static final int MSG_INIT_DATA = 0x00;
    private static final int MSG_UPDATE_DATA = 0x01;
    private static final int MSG_FETCH_DATA = 0x02;
    private final Handler mHandler = HandlerUtils.createAsync(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_INIT_DATA:
                    doInitData();
                    break;
                case MSG_UPDATE_DATA:
                    doUpdateData();
                    break;
                case MSG_FETCH_DATA:
                    doFetchData((String[]) msg.obj, msg.getData());
                    break;
            }
            return false;
        }
    });

    public ContentLiveData(Context context, Uri uri) {
        mUri = uri;

        mContentResolver = context.getContentResolver();
        mContentObserver = new ContentObserver(HandlerUtils.createAsync()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                onContentUpdate(selfChange);
            }
        };
    }

    @Override
    protected void onActive() {
        super.onActive();

        fetchDataOnActive();

        try {
            mContentResolver.registerContentObserver(mUri, true, mContentObserver);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.tag(TAG).w("onActive: " + e);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        LogUtils.tag(TAG).w("onInactive: ");

        mContentResolver.unregisterContentObserver(mContentObserver);
    }

    @Override
    public void postValue(T value) {
        LogUtils.tag(TAG).i("postValue: " + value);
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        LogUtils.tag(TAG).w("setValue: %s", value);
        super.setValue(value);
    }

    protected void onContentUpdate(boolean selfChange) {
        LogUtils.tag(TAG).d("onContentUpdate: " + selfChange);

        long updateInterval = System.currentTimeMillis() - mLastUpdate;
        LogUtils.tag(TAG).d("onContentUpdate: interval=%s, LastUpdate=%s", updateInterval, mLastUpdate);

        if (mLastUpdate > 0) {
            if (updateInterval > Math.max(DEFAULT_INTERVAL, updateInterval())) {
                fetchDataOnUpdate();
            } else {
                LogUtils.tag(TAG).e("onContentUpdate: ignore update");
            }
        } else {
            fetchDataOnUpdate();
        }

        mLastUpdate = System.currentTimeMillis();
    }

    private void fetchDataOnActive() {
        mHandler.removeMessages(MSG_INIT_DATA);
        mHandler.sendEmptyMessageDelayed(MSG_INIT_DATA, 100);
    }

    private void fetchDataOnUpdate() {
        mHandler.removeMessages(MSG_UPDATE_DATA);
        mHandler.sendEmptyMessage(MSG_UPDATE_DATA);
    }

    public final void fetchData() {
        mHandler.removeMessages(MSG_FETCH_DATA);
        mHandler.sendEmptyMessage(MSG_FETCH_DATA);
    }

    public final void fetchData(@Nullable String[] projection, @Nullable Bundle queryArgs) {
        Message message = mHandler.obtainMessage(MSG_FETCH_DATA, projection);
        message.setData(queryArgs);
        mHandler.removeMessages(MSG_FETCH_DATA);
        mHandler.sendMessage(message);
    }

    protected void doUpdateData() {
        LogUtils.tag(TAG).i("doUpdateData: ");
        try {
            queryData(new Function1<T>() {
                @Override
                public void invoke(T input) {
                    postValue(input);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doInitData() {
        LogUtils.tag(TAG).i("doInitData: ");
        try {
            queryData(new Function1<T>() {
                @Override
                public void invoke(T input) {
                    runOnUIExecutor(new Runnable() {
                        @Override
                        public void run() {
                            setValue(input);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doFetchData(@Nullable String[] projection, @Nullable Bundle queryArgs) {
        LogUtils.tag(TAG).i("doFetchData: %s, %s", projection, queryArgs);
        try {
            queryData(new Function1<T>() {
                @Override
                public void invoke(T input) {
                    runOnUIExecutor(new Runnable() {
                        @Override
                        public void run() {
                            setValue(input);
                        }
                    });
                }
            }, projection, queryArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void queryData(@NonNull Function1<T> callback) {
        LogUtils.tag(TAG).i("queryData: " + mUri);

        if (mUri == null) {
            callback.invoke(null);
        }

        runOnIOExecutor(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = queryData();
                callback.invoke(checkoutData(cursor));
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        });
    }

    private void queryData(@NonNull Function1<T> callback, @Nullable String[] projection, @Nullable Bundle queryArgs) {
        LogUtils.tag(TAG).i("queryData: %s, \r\n projection=%s, \r\nqueryArgs=%s", mUri, projection, queryArgs);

        if (mUri == null) {
            callback.invoke(null);
        }

        runOnIOExecutor(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    cursor = queryData(projection, queryArgs);
                } else {
                    String selection = queryArgs.getString(QUERY_ARG_SQL_SELECTION);
                    String[] selectionArgs = queryArgs.getStringArray(QUERY_ARG_SQL_SELECTION_ARGS);
                    String sortOrder = queryArgs.getString(QUERY_ARG_SQL_SORT_ORDER);
                    cursor = queryData(projection, selection, selectionArgs, sortOrder);
                }
                callback.invoke(checkoutData(cursor));
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        });

    }

    protected final Cursor queryData() {
        return mContentResolver.query(mUri, null, null, null, null);
    }

    protected final Cursor queryData(@Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mContentResolver.query(mUri, projection, selection, selectionArgs, sortOrder);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected final Cursor queryData(@Nullable String[] projection, @Nullable Bundle queryArgs) {
        return mContentResolver.query(mUri, projection, queryArgs, null);
    }

    protected abstract T checkoutData(Cursor cursor);

    protected long updateInterval() {
        return DEFAULT_INTERVAL;
    }

    protected void runOnIOExecutor(Runnable body) {
        AppTaskExecutors.diskIO().execute(body);
    }

    protected void runOnUIExecutor(Runnable body) {
        AppTaskExecutors.getInstance().executeOnMainThread(body);
    }
}
