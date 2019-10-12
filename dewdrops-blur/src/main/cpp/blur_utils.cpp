//
// Created by tingkuo on 19-10-8.
//
#include "blur_utils.h"

#ifdef __cplusplus
extern "C" {
#endif

jint clamp(jint i, jint minValue, jint maxValue) {
    if (i < minValue) {
        return minValue;
    } else if (i > maxValue) {
        return maxValue;
    } else {
        return i;
    }
}

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_utils_BitmapUtils_replaceBitmap(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap, jintArray pixels,
                                                                   jint x, jint y, jint delta_x,
                                                                   jint delta_y) {
    ALOGD("BitmapUtils#replaceBitmap");
    if (bitmap == nullptr) {
        return;
    }

    AndroidBitmapInfo bitmapInfo = {0};

    if (AndroidBitmap_getInfo(env, bitmap, &bitmapInfo) < 0) {
        return;
    }

    int *_pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&_pixels)) < 0) {
        return;
    }

    jint *elements;
    elements = env->GetIntArrayElements(pixels, nullptr);

    int width = bitmapInfo.width;
    int height = bitmapInfo.height;
    for (int i = x; i < x + delta_x; i++) {
        for (int j = y; j < y + delta_y; j++) {
            jint argb = elements[i - x + (j - y) * delta_x];
            jint a = ((argb >> 24) & 0xff) << 24;
            jint r = (argb >> 16) & 0xff;
            jint g = ((argb >> 8) & 0xff) << 8;
            jint b = (argb & 0xff) << 16;
            _pixels[i + j * width] = a + r + g + b;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    env->ReleaseIntArrayElements(pixels, elements, 0);
}

#ifdef __cplusplus
}
#endif