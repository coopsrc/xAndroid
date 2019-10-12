package com.coopsrc.xandroid.utils;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-01-02 15:23
 */
public class AppExecutors {

    private Executor mDatabaseIO;
    private Executor mDiskIO;
    private Executor mNetworkIO;
    private Executor mMainThread;

    private static final class Holder {
        private static final AppExecutors INSTANCE = new AppExecutors();
    }

    public static AppExecutors getInstance() {
        return Holder.INSTANCE;
    }

    private AppExecutors() {
        this.mDatabaseIO = Executors.newSingleThreadExecutor();
        this.mDiskIO = Executors.newSingleThreadExecutor();
        this.mNetworkIO = Executors.newFixedThreadPool(4);
        this.mMainThread = new MainThreadExecutor();
    }

    public Executor databaseIO() {
        return mDatabaseIO;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NotNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
