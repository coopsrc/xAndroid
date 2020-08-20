package com.coopsrc.xandroid.dewdrops.blur;

import com.coopsrc.xandroid.dewdrops.annotation.Direction;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.utils.MathUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 20:08
 */
final class OriginalBoxBlur {
    static void doBlur(int[] pixels, int width, int height, int radius, @Direction int direction) {
        int[] result = new int[width * height];

        if (direction == BlurConfig.DIRECTION_HORIZONTAL) {
            boxBlurHorizontal(pixels, result, width, height, radius);
            System.arraycopy(result, 0, pixels, 0, result.length);
        } else if (direction == BlurConfig.DIRECTION_VERTICAL) {
            boxBlurVertical(pixels, result, width, height, radius);
            System.arraycopy(result, 0, pixels, 0, result.length);
        } else {
            boxBlurHorizontal(pixels, result, width, height, radius);
            boxBlurVertical(result, pixels, width, height, radius);

        }
    }


    private static void boxBlurHorizontal(int[] in, int[] out, int width, int height, int radius) {
        int widthMinus1 = width - 1;
        int tableSize = 2 * radius + 1;
        int[] divide = new int[256 * tableSize];

        // construct a query table from 0 to 255
        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int inIndex = 0;

        //
        for (int y = 0; y < height; y++) {
            int ta = 0, tr = 0, tg = 0, tb = 0; // ARGB

            for (int i = -radius; i <= radius; i++) {
                int rgb = in[inIndex + MathUtils.clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            int baseIndex = y * width;
            for (int x = 0; x < width; x++) {
                out[baseIndex + x] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = x + radius + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - radius;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
//                outIndex += height;
            }
            inIndex += width;
        }
    }

    private static void boxBlurVertical(int[] in, int[] out, int width, int height, int radius) {
        int heightMinus1 = height - 1;
        int tableSize = 2 * radius + 1;
        int[] divide = new int[256 * tableSize];

        // construct a query table from 0 to 255
        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        for (int x = 0; x < width; x++) {
            int ta = 0, tr = 0, tg = 0, tb = 0; // ARGB

            for (int i = -radius; i <= radius; i++) {
                int rgb = in[x + MathUtils.clamp(i, 0, height - 1) * width];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int y = 0; y < height; y++) { // Sliding window computation
                out[y * width + x] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = y + radius + 1;
                if (i1 > heightMinus1)
                    i1 = heightMinus1;
                int i2 = y - radius;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[x + i1 * width];
                int rgb2 = in[x + i2 * width];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
            }
        }
    }
}
