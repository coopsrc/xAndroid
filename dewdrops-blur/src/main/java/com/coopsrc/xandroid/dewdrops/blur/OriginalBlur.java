package com.coopsrc.xandroid.dewdrops.blur;

import android.graphics.Bitmap;

import com.coopsrc.xandroid.dewdrops.annotation.Direction;
import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.utils.BitmapUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 20:07
 */
public final class OriginalBlur {

    public static void doBlur(@Mode int mode, Bitmap bitmap, int radius, int cores, int index, @Direction int direction) {

    }

    public static void doFullBlur(@Mode int mode, Bitmap bitmap, int radius) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        final int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        switch (mode) {
            case BlurConfig.MODE_GAUSSIAN:
                gaussianBlur(pixels, width, height, radius, BlurConfig.DIRECTION_FULL);
                break;
            case BlurConfig.MODE_STACK:
                stackBlur(pixels, width, height, radius, BlurConfig.DIRECTION_FULL);
                break;
            case BlurConfig.MODE_BOX:
                boxBlur(pixels, width, height, radius, BlurConfig.DIRECTION_FULL);
                break;
        }

        if (bitmap.isMutable()) {
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } else {
            BitmapUtils.replaceBitmap(bitmap, pixels, 0, 0, width, height);
        }
    }

    private static void gaussianBlur(int[] pixels, int width, int height, int radius, @Direction int direction) {
        OriginalGaussianBlur.doBlur(pixels, width, height, radius, direction);
    }

    private static void stackBlur(int[] pixels, int width, int height, int radius, @Direction int direction) {
        OriginalStackBlur.doBlur(pixels, width, height, radius, direction);
    }

    private static void boxBlur(int[] pixels, int width, int height, int radius, @Direction int direction) {
        OriginalBoxBlur.doBlur(pixels, width, height, radius, direction);
    }
}
