package com.coopsrc.xandroid.dewdrops.task;

import androidx.core.util.Preconditions;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-09 10:33
 */
public class BlurTaskManager {

    private static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors() <= 3 ?
            1 : Runtime.getRuntime().availableProcessors() / 2;

    private static final ExecutorService CONCURRENT_BLUR_EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);

    private static final class Holder {
        private static final BlurTaskManager INSTANCE = new BlurTaskManager();
    }

    public static BlurTaskManager getInstance() {
        return Holder.INSTANCE;
    }

    private BlurTaskManager() {
    }

    public void invokeAll(Collection<BlurTask> blurTasks) {
        Preconditions.checkNotNull(blurTasks, "tasks == null");
        try {
            CONCURRENT_BLUR_EXECUTOR.invokeAll(blurTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getWorkersThreads() {
        return EXECUTOR_THREADS;
    }
}
