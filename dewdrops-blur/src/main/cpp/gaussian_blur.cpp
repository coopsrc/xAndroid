//
// Created by tingkuo on 19-10-8.
//

#include <cstdlib>
#include <cmath>
#include "gaussian_blur.h"
#include "blur_utils.h"
#include "logger.h"

#ifdef __cplusplus
extern "C" {
#endif

void gaussianBlurHorizontal(float *kernel, jint *inPixels, jint *outPixels, jint width, jint height,
                            jint radius,
                            jint startX, jint startY, jint deltaX, jint deltaY) {
    ALOGD("gaussianBlurHorizontal");
    jint cols = 2 * radius + 1;
    jint cols2 = cols / 2;
    jint x, y, col;

    jint endY = startY + deltaY;
    jint endX = startX + deltaX;

    for (y = startY; y < endY; y++) {
        jint ioffset = y * width;
        for (x = startX; x < endX; x++) {
            float r = 0, g = 0, b = 0;
            int moffset = cols2;
            for (col = -cols2; col <= cols2; col++) {
                float f = kernel[moffset + col];

                if (f != 0) {
                    jint ix = x + col;
                    if (ix < startX) {
                        ix = startX;
                    } else if (ix >= endX) {
                        ix = endX - 1;
                    }
                    jint rgb = inPixels[ioffset + ix];
                    r += f * ((rgb >> 16) & 0xff);
                    g += f * ((rgb >> 8) & 0xff);
                    b += f * (rgb & 0xff);
                }
            }

            jint outIndex = ioffset + x;
            jint ia = (inPixels[ioffset + x] >> 24) & 0xff;
            jint ir = clamp((jint) (r + 0.5), 0, 255);
            jint ig = clamp((jint) (g + 0.5), 0, 255);
            jint ib = clamp((jint) (b + 0.5), 0, 255);
            outPixels[outIndex] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
        }
    }
}

void gaussianBlurVertical(float *kernel, jint *inPixels, jint *outPixels, jint width, jint height,
                          jint radius,
                          jint startX, jint startY, jint deltaX, jint deltaY) {
    ALOGD("gaussianBlurVertical");
    jint cols = 2 * radius + 1;
    jint cols2 = cols / 2;
    jint x, y, col;

    jint endY = startY + deltaY;
    jint endX = startX + deltaX;

    for (x = startX; x < endX; x++) {
        jint ioffset = x;
        for (y = startY; y < endY; y++) {
            float r = 0, g = 0, b = 0;
            int moffset = cols2;
            for (col = -cols2; col <= cols2; col++) {
                float f = kernel[moffset + col];

                if (f != 0) {
                    jint iy = y + col;
                    if (iy < startY) {
                        iy = startY;
                    } else if (iy >= endY) {
                        iy = endY - 1;
                    }
                    jint rgb = inPixels[ioffset + iy * width];
                    r += f * ((rgb >> 16) & 0xff);
                    g += f * ((rgb >> 8) & 0xff);
                    b += f * (rgb & 0xff);
                }
            }
            jint outIndex = ioffset + y * width;
            jint ia = (inPixels[ioffset + x] >> 24) & 0xff;
            jint ir = clamp((jint) (r + 0.5), 0, 255);
            jint ig = clamp((jint) (g + 0.5), 0, 255);
            jint ib = clamp((jint) (b + 0.5), 0, 255);
            outPixels[outIndex] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
        }
    }
}

float *makeKernel(jint r) {
    ALOGD("makeKernel");

    jint i, row;
    jint rows = r * 2 + 1;
    float *matrix = (float *) malloc(sizeof(float) * rows);
    float sigma = (r + 1) / 2.0f;
    float sigma22 = 2 * sigma * sigma;
    float total = 0;
    jint index = 0;
    for (row = -r; row <= r; row++) {
        matrix[index] = exp(-1 * (row * row) / sigma22) / sigma;
        total += matrix[index];
        index++;
    }
    for (i = 0; i < rows; i++) {
        matrix[i] /= total;
    }

    return matrix;
}

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_gaussianBlur(JNIEnv *env, jclass clazz,
                                                                jobject bitmap, jint radius,
                                                                jint workers, jint index,
                                                                jint direction) {
    ALOGD("NativeBlur#gaussianBlur: %d, %d, %d, %d", radius, workers, index, direction);
    if (bitmap == nullptr) {
        return;
    }

    AndroidBitmapInfo bmpInfo = {0};
    if (AndroidBitmap_getInfo(env, bitmap, &bmpInfo) < 0) {
        return;
    }

    int *pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels) < 0) {
        return;
    }

    int w = bmpInfo.width;
    int h = bmpInfo.height;

    float *kernel = nullptr;
    kernel = makeKernel(radius);

    jint *copy = nullptr;
    copy = (jint *) malloc(sizeof(jint) * w * h);

    for (int i = 0; i < w * h; i++) {
        copy[i] = pixels[i];
    }

    if (direction == HORIZONTAL) {
        int deltaY = h / workers;
        int startY = index * deltaY;

        if (index == workers - 1) {
            deltaY = h - (workers - 1) * deltaY;
        }

        gaussianBlurHorizontal(kernel, copy, pixels, w, h, radius, 0, startY, w, deltaY);

    } else if (direction == VERTICAL) {
        int deltaX = w / workers;
        int startX = index * deltaX;

        if (index == workers - 1) {
            deltaX = w - (workers - 1) * (w / workers);
        }

        gaussianBlurVertical(kernel, copy, pixels, w, h, radius, startX, 0, deltaX, h);
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    free(copy);
    free(kernel);
}

#ifdef __cplusplus
}
#endif