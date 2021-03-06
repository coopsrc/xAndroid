//
// Created by tingkuo on 19-10-8.
//

#include <cstdlib>
#include <android/bitmap.h>
#include "blur_utils.h"
#include "stack_blur.h"
#include "logger.h"

#ifdef __cplusplus
extern "C" {
#endif

void doHorizontalBlur(jint *pix, jint w, jint h, jint radius, jint startX, jint startY, jint deltaX,
                      jint deltaY) {
    ALOGD("doVerticalBlur");
    jint wm = w - 1;
    jint hm = h - 1;
    jint wh = w * h;
    jint div = radius + radius + 1;

    jint rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    jint *vmin;

    vmin = (jint *) malloc(sizeof(jint) * max(w, h));

    jint divsum = (div + 1) >> 1;
    divsum *= divsum;

    short *dv;
    dv = (short *) malloc(sizeof(short) * 256 * divsum);

    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (short) (i / divsum);
    }

    yi = 0;

    //jint stack[div][3];

    jint (*stack)[3];
    stack = (jint(*)[3]) malloc(sizeof(jint) * div * 3);

    jint stackpointer;
    jint stackstart;
    jint *sir;
    jint rbs;
    jint r1 = radius + 1;
    jint routsum, goutsum, boutsum;
    jint rinsum, ginsum, binsum;
    jint baseIndex;
    jint endX = startX + deltaX;
    jint endY = startY + deltaY;

    for (y = startY; y < endY; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        baseIndex = y * w;

        for (i = -radius; i <= radius; i++) {
            p = pix[baseIndex + min(wm, max(startX, i + startX))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);
            rbs = r1 - abs(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            } else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        yi = baseIndex + startX;
        for (x = startX; x < endX; x++) {

            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

//            if (y == 0) {
            vmin[x] = min(x + radius + 1, wm);
//            }
            p = pix[baseIndex + vmin[x]];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
    }


    free(dv);
    free(stack);
}

void doVerticalBlur(jint *pix, jint w, jint h, jint radius, jint startX, jint startY, jint deltaX,
                    jint deltaY) {
    ALOGD("doVerticalBlur");
    jint wm = w - 1;
    jint hm = h - 1;
    jint wh = w * h;
    jint hmw = hm * w;
    jint div = radius + radius + 1;

    jint rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    jint *vmin;

    vmin = (jint *) malloc(sizeof(jint) * max(w, h));

    jint divsum = (div + 1) >> 1;
    divsum *= divsum;

    short *dv;
    dv = (short *) malloc(sizeof(short) * 256 * divsum);

    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (short) (i / divsum);
    }

    yi = 0;

    //jint stack[div][3];

    jint (*stack)[3];
    stack = (jint(*)[3]) malloc(sizeof(jint) * div * 3);

    jint stackpointer;
    jint stackstart;
    jint *sir;
    jint rbs;
    jint r1 = radius + 1;
    jint routsum, goutsum, boutsum;
    jint rinsum, ginsum, binsum;
    jint endX = startX + deltaX;
    jint endY = startY + deltaY;

    jint baseIndex = startY * w;

    for (x = startX; x < endX; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[min(hmw, max(baseIndex + i * w, baseIndex)) + x];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);
            rbs = r1 - abs(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            } else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        yi = baseIndex + x;
        for (y = startY; y < endY; y++) {

            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];


//            if (y == 0) {
            vmin[y] = min(y + radius + 1, hm);
//            }
            p = pix[vmin[y] * w + x];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }

    free(dv);
    free(stack);
}

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_stackBlur(JNIEnv *env, jclass clazz,
                                                             jobject bitmap, jint radius,
                                                             jint workers, jint index,
                                                             jint direction) {
    ALOGD("NativeBlur#stackBlur: %d, %d, %d, %d", radius, workers, index, direction);
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

    if (direction == HORIZONTAL) {
        int deltaY = h / workers;
        int startY = index * deltaY;

        if (index == workers - 1) {
            deltaY = h - (workers - 1) * deltaY;
        }

        doHorizontalBlur(pixels, w, h, radius, 0, startY, w, deltaY);

    } else if (direction == VERTICAL) {
        int deltaX = w / workers;
        int startX = index * deltaX;

        if (index == workers - 1) {
            deltaX = w - (workers - 1) * (w / workers);
        }

        doVerticalBlur(pixels, w, h, radius, startX, 0, deltaX, h);
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}

#ifdef __cplusplus
}
#endif
