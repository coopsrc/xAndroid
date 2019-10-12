package com.coopsrc.xandroid.dewdrops.task;

import android.graphics.Bitmap;

import androidx.core.util.Preconditions;

import com.coopsrc.xandroid.dewdrops.annotation.Direction;
import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;
import com.coopsrc.xandroid.dewdrops.blur.NativeBlur;
import com.coopsrc.xandroid.dewdrops.blur.OriginalBlur;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

import java.util.concurrent.Callable;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-09 10:27
 */
public class BlurTask implements Callable<Void> {

    @Scheme
    private final int scheme;
    @Mode
    private final int mode;
    @Direction
    private final int direction;

    private final Bitmap bitmap;

    private final int radius;
    private final int threads;
    private final int index;

    public BlurTask(int scheme, int mode, int direction, Bitmap bitmap, int radius, int threads, int index) {
        this.scheme = scheme;
        this.mode = mode;
        this.direction = direction;
        this.bitmap = bitmap;
        this.radius = radius;
        this.threads = threads;
        this.index = index;
    }

    @Override
    public Void call() throws Exception {
        Preconditions.checkNotNull(bitmap, "mBitmapOut == null");
        Preconditions.checkArgument(!bitmap.isRecycled(), "You must input an unrecycled bitmap !");
        Preconditions.checkArgument(threads > 0, "mCores < 0");

        applyPixelsBlur();

        return null;
    }

    private void applyPixelsBlur() {
        switch (scheme) {

            case BlurConfig.SCHEME_NATIVE:
                NativeBlur.doBlur(mode, bitmap, radius, threads, index, direction);
                break;
            case BlurConfig.SCHEME_OPEN_GL:
                break;
            case BlurConfig.SCHEME_JAVA:
                OriginalBlur.doBlur(mode, bitmap, radius, threads, index, direction);
                break;
            case BlurConfig.SCHEME_RENDER_SCRIPT:
                break;
        }
    }
}
