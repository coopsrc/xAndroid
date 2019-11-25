package com.coopsrc.xandroid.dewdrops.blur;

import androidx.core.math.MathUtils;

import com.coopsrc.xandroid.dewdrops.annotation.Direction;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 20:07
 */
final class OriginalGaussianBlur {
    static void doBlur(int[] pixels, int width, int height, int radius, @Direction int direction) {
        int[] result = new int[width * height];
        float[] kernel = makeKernel(radius);

        if (direction == BlurConfig.DIRECTION_HORIZONTAL) {
            gaussianBlurHorizontal(kernel, pixels, result, width, height);
            System.arraycopy(result, 0, pixels, 0, result.length);
        } else if (direction == BlurConfig.DIRECTION_VERTICAL) {
            gaussianBlurVertical(kernel, pixels, result, width, height);
            System.arraycopy(result, 0, pixels, 0, result.length);

        } else {
            gaussianBlurHorizontal(kernel, pixels, result, width, height);
            gaussianBlurVertical(kernel, result, pixels, width, height);
        }
    }

    private static void gaussianBlurHorizontal(float[] kernel, int[] inPixels, int[] outPixels, int width, int height) {
        int cols = kernel.length;
        int cols2 = cols / 2;

        for (int y = 0; y < height; y++) {
            int ioffset = y * width;
            for (int x = 0; x < width; x++) {
                float r = 0, g = 0, b = 0;
                int moffset = cols2;
                for (int col = -cols2; col <= cols2; col++) {
                    float f = kernel[moffset + col];

                    if (f != 0) {
                        int ix = x + col;
                        if (ix < 0) {
                            ix = 0;
                        } else if (ix >= width) {
                            ix = width - 1;
                        }
                        int rgb = inPixels[ioffset + ix];
                        r += f * ((rgb >> 16) & 0xff);
                        g += f * ((rgb >> 8) & 0xff);
                        b += f * (rgb & 0xff);
                    }
                }
                int outIndex = ioffset + x;
                int ia = (inPixels[outIndex] >> 24) & 0xff;
                int ir = MathUtils.clamp((int) (r + 0.5), 0, 255);
                int ig = MathUtils.clamp((int) (g + 0.5), 0, 255);
                int ib = MathUtils.clamp((int) (b + 0.5), 0, 255);
                outPixels[outIndex] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
            }
        }
    }

    private static void gaussianBlurVertical(float[] kernel, int[] inPixels, int[] outPixels, int width, int height) {
        int cols = kernel.length;
        int cols2 = cols / 2;
        for (int x = 0; x < width; x++) {
            int ioffset = x;
            for (int y = 0; y < height; y++) {
                float r = 0, g = 0, b = 0;
                int moffset = cols2;
                for (int col = -cols2; col <= cols2; col++) {
                    float f = kernel[moffset + col];

                    if (f != 0) {
                        int iy = y + col;
                        if (iy < 0) {
                            iy = 0;
                        } else if (iy >= height) {
                            iy = height - 1;
                        }
                        int rgb = inPixels[ioffset + iy * width];
                        r += f * ((rgb >> 16) & 0xff);
                        g += f * ((rgb >> 8) & 0xff);
                        b += f * (rgb & 0xff);
                    }
                }
                int outIndex = ioffset + y * width;
                int ia = (inPixels[outIndex] >> 24) & 0xff;
                int ir = MathUtils.clamp((int) (r + 0.5), 0, 255);
                int ig = MathUtils.clamp((int) (g + 0.5), 0, 255);
                int ib = MathUtils.clamp((int) (b + 0.5), 0, 255);
                outPixels[outIndex] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
            }
        }
//
    }

    /**
     * Make a Gaussian blur kernel.
     */
    private static float[] makeKernel(int r) {
        int rows = r * 2 + 1;
        float[] matrix = new float[rows];
        float sigma = (r + 1) / 2.0f;
        float sigma22 = 2 * sigma * sigma;
        float total = 0;
        int index = 0;
        for (int row = -r; row <= r; row++) {
            matrix[index] = (float) (Math.exp(-1 * (row * row) / sigma22) / sigma);

            total += matrix[index];
            index++;
        }
        for (int i = 0; i < rows; i++)
            matrix[i] /= total;

        return matrix;
    }

}
