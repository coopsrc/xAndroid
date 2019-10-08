package com.coopsrc.xandroid.dewdrops.blur;

import android.graphics.Bitmap;

import com.coopsrc.xandroid.dewdrops.annotation.Direction;
import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 20:08
 */
public final class NativeBlur {
    static {
        System.loadLibrary("dewdrops-blur");
    }

    public static void doFullBlur(@Mode int mode, Bitmap bitmap, int radius) {
        doBlur(mode, bitmap, radius, 1, 0, BlurConfig.DIRECTION_HORIZONTAL);
        doBlur(mode, bitmap, radius, 1, 0, BlurConfig.DIRECTION_VERTICAL);
    }

    public static void doBlur(@Mode int mode, Bitmap bitmap, int radius, int cores, int index, @Direction int direction) {
        switch (mode) {
            case BlurConfig.MODE_GAUSSIAN:
                gaussianBlur(bitmap, radius, cores, index, direction);
                break;
            case BlurConfig.MODE_BOX:
                boxBlur(bitmap, radius, cores, index, direction);
                break;
            case BlurConfig.MODE_STACK:
                stackBlur(bitmap, radius, cores, index, direction);
                break;

        }
    }

    private static native void gaussianBlur(Bitmap bitmap, int radius, int workers, int index, int direction);

    private static native void boxBlur(Bitmap bitmap, int radius, int workers, int index, int direction);

    private static native void stackBlur(Bitmap bitmap, int radius, int workers, int index, int direction);
}
