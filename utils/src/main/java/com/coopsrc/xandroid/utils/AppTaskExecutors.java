package com.coopsrc.xandroid.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coopsrc.xandroid.utils.executor.DefaultTaskExecutor;
import com.coopsrc.xandroid.utils.executor.TaskExecutor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-01-02 15:23
 */
public class AppTaskExecutors extends TaskExecutor {

    @NonNull
    private TaskExecutor mDelegate;

    @NonNull
    private TaskExecutor mDefaultTaskExecutor;

    @NonNull
    private static final Executor sMainThreadExecutor = new Executor() {
        @Override
        public void execute(@NotNull Runnable command) {
            getInstance().postToMainThread(command);
        }
    };

    @NonNull
    private static final Executor sDiskIOThreadExecutor = new Executor() {
        @Override
        public void execute(@NotNull Runnable command) {
            getInstance().executeOnDiskIO(command);
        }
    };

    @NonNull
    private static final Executor sNetworkIOThreadExecutor = new Executor() {
        @Override
        public void execute(@NotNull Runnable command) {
            getInstance().executeOnNetworkIO(command);
        }
    };

    @NonNull
    private static final Executor sDatabaseIOThreadExecutor = new Executor() {
        @Override
        public void execute(@NotNull Runnable command) {
            getInstance().executeOnDatabaseIO(command);
        }
    };

    private static final class Holder {
        private static final AppTaskExecutors INSTANCE = new AppTaskExecutors();
    }

    public static AppTaskExecutors getInstance() {
        return Holder.INSTANCE;
    }

    private AppTaskExecutors() {
        mDefaultTaskExecutor = new DefaultTaskExecutor();
        mDelegate = mDefaultTaskExecutor;
    }

    /**
     * Sets a delegate to handle task execution requests.
     * <p>
     * If you have a common executor, you can set it as the delegate and App Toolkit components will
     * use your executors. You may also want to use this for your tests.
     * <p>
     * Calling this method with {@code null} sets it to the default TaskExecutor.
     *
     * @param taskExecutor The task executor to handle task requests.
     */
    public void setDelegate(@Nullable TaskExecutor taskExecutor) {
        mDelegate = taskExecutor == null ? mDefaultTaskExecutor : taskExecutor;
    }

    @Override
    public void executeOnDiskIO(@NonNull Runnable runnable) {
        mDelegate.executeOnDiskIO(runnable);
    }

    @Override
    public void executeOnNetworkIO(@NonNull Runnable runnable) {
        mDelegate.executeOnNetworkIO(runnable);
    }

    @Override
    public void executeOnDatabaseIO(@NonNull Runnable runnable) {
        mDelegate.executeOnDatabaseIO(runnable);
    }

    @Override
    public void postToMainThread(@NonNull Runnable runnable) {
        mDelegate.postToMainThread(runnable);
    }

    @Override
    public boolean isMainThread() {
        return mDelegate.isMainThread();
    }

    @NonNull
    public static Executor diskIO() {
        return sDiskIOThreadExecutor;
    }

    @NonNull
    public static Executor networkIO() {
        return sNetworkIOThreadExecutor;
    }

    @NonNull
    public static Executor databaseIO() {
        return sDatabaseIOThreadExecutor;
    }

    @NonNull
    public static Executor mainThread() {
        return sMainThreadExecutor;
    }

}
