//
// Created by tingkuo on 19-10-8.
//

#include <cstdlib>
#include "box_blur.h"
#include "blur_utils.h"
#include "logger.h"

#ifdef __cplusplus
extern "C" {
#endif

void boxBlurHorizontal(jint *in, jint *out, jint width, jint height, jint radius, jint startX,
                       jint startY, jint deltaX, jint deltaY) {
    ALOGD("boxBlurHorizontal");
    jint widthMinus1 = width - 1;
    jint tableSize = 2 * radius + 1;
    jint divide[256 * tableSize];

    for (jint i = 0; i < 256 * tableSize; i++)
        divide[i] = i / tableSize;

    for (jint y = startY; y < startY + deltaY; y++) {
        jint ta = 0, tr = 0, tg = 0, tb = 0;

        for (jint i = -radius; i <= radius; i++) {
            jint rgb = in[y * width + clamp(i, startX, startX + deltaX - 1)];
            ta += (rgb >> 24) & 0xff;
            tr += (rgb >> 16) & 0xff;
            tg += (rgb >> 8) & 0xff;
            tb += rgb & 0xff;
        }

        jint baseIndex = y * width;

        for (jint x = startX; x < startX + deltaX; x++) {

            jint i1 = x + radius + 1;
            if (i1 > startX + deltaX - 1)
                i1 = startX + deltaX - 1;
            jint i2 = x - radius;
            if (i2 < startX)
                i2 = startX;
            jint rgb1 = in[baseIndex + i1];
            jint rgb2 = in[baseIndex + i2];

            ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
            tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
            tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
            tb += (rgb1 & 0xff) - (rgb2 & 0xff);

            out[baseIndex + x] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) |
                                 divide[tb];
        }
    }
}

void boxBlurVertical(jint *in, jint *out, jint width, jint height, jint radius, jint startX,
                     jint startY, jint deltaX, jint deltaY) {
    ALOGD("boxBlurVertical");
    jint heightMinus1 = height - 1;
    jint tableSize = 2 * radius + 1;
    jint divide[256 * tableSize];

    for (jint i = 0; i < 256 * tableSize; i++)
        divide[i] = i / tableSize;

    for (jint x = startX; x < startX + deltaX; x++) {
        jint ta = 0, tr = 0, tg = 0, tb = 0;

        for (jint i = -radius; i <= radius; i++) {
            jint rgb = in[x + clamp(i, startY, startY + deltaY - 1) * width];
            ta += (rgb >> 24) & 0xff;
            tr += (rgb >> 16) & 0xff;
            tg += (rgb >> 8) & 0xff;
            tb += rgb & 0xff;
        }

        for (jint y = startY; y < startY + deltaY; y++) {
            out[y * width + x] =
                    (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

            jint i1 = y + radius + 1;
            if (i1 > startY + deltaY - 1)
                i1 = startY + deltaY - 1;
            jint i2 = y - radius;
            if (i2 < startY)
                i2 = startY;
            jint rgb1 = in[x + i1 * width];
            jint rgb2 = in[x + i2 * width];

            ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
            tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
            tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
            tb += (rgb1 & 0xff) - (rgb2 & 0xff);
        }
    }
}

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_boxBlur(JNIEnv *env, jclass clazz,
                                                           jobject bitmap, jint radius,
                                                           jint workers,
                                                           jint index, jint direction) {
    ALOGD("NativeBlur#boxBlur: %d, %d, %d, %d", radius, workers, index, direction);
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

        boxBlurHorizontal(copy, pixels, w, h, radius, 0, startY, w, deltaY);

    } else if (direction == VERTICAL) {
        int deltaX = w / workers;
        int startX = index * deltaX;

        if (index == workers - 1) {
            deltaX = w - (workers - 1) * (w / workers);
        }

        boxBlurVertical(copy, pixels, w, h, radius, startX, 0, deltaX, h);
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    free(copy);
}

#ifdef __cplusplus
}
#endif