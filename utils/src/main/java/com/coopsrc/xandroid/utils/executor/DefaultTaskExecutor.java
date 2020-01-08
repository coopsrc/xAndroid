package com.coopsrc.xandroid.utils.executor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-25 10:18
 */
public class DefaultTaskExecutor extends TaskExecutor {

    private final Object mLock = new Object();

    private final ExecutorService mDiskIO = Executors.newFixedThreadPool(4, new ThreadFactory() {
        private static final String THREAD_NAME_STEM = "x_disk_io_%d";

        private final AtomicInteger mThreadId = new AtomicInteger(0);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(Locale.getDefault(), THREAD_NAME_STEM, mThreadId.getAndIncrement()));
            return thread;
        }
    });

    private final ExecutorService mNetworkIO = Executors.newFixedThreadPool(4, new ThreadFactory() {
        private static final String THREAD_NAME_STEM = "x_network_io_%d";

        private final AtomicInteger mThreadId = new AtomicInteger(0);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(Locale.getDefault(), THREAD_NAME_STEM, mThreadId.getAndIncrement()));
            return thread;
        }
    });

    private final ExecutorService mDatabaseIO = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private static final String THREAD_NAME_STEM = "x_database_io";

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(THREAD_NAME_STEM);
            return thread;
        }
    });

    @Nullable
    private volatile Handler mMainHandler;

    @Override
    public void executeOnDiskIO(@NonNull Runnable runnable) {
        mDiskIO.execute(runnable);
    }

    @Override
    public void executeOnNetworkIO(@NonNull Runnable runnable) {
        mNetworkIO.execute(runnable);
    }

    @Override
    public void executeOnDatabaseIO(@NonNull Runnable runnable) {
        mDatabaseIO.execute(runnable);
    }

    @Override
    public void postToMainThread(@NonNull Runnable runnable) {
        if (mMainHandler == null) {
            synchronized (mLock) {
                if (mMainHandler == null) {
                    mMainHandler = createAsync(Looper.getMainLooper());
                }
            }
        }
        //noinspection ConstantConditions
        mMainHandler.post(runnable);
    }

    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private static Handler createAsync(@NonNull Looper looper) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper);
        }
        try {
            return Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, boolean.class)
                    .newInstance(looper, null, true);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException ignored) {
        } catch (InvocationTargetException e) {
            return new Handler(looper);
        }
        return new Handler(looper);
    }
}

