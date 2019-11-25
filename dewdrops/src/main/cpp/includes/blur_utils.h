//
// Created by tingkuo on 19-10-8.
//

#ifndef XANDROID_BLUR_UTILS_H
#define XANDROID_BLUR_UTILS_H

#include <jni.h>

#include "logger.h"
#include <android/bitmap.h>

#ifdef __cplusplus
extern "C" {
#endif

enum Direction {
    HORIZONTAL = 1,
    VERTICAL = 2,
};

jint clamp(jint i, jint minValue, jint maxValue);

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_utils_BitmapUtils_replaceBitmap(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap, jintArray pixels,
                                                                   jint x, jint y, jint delta_x,
                                                                   jint delta_y);

#ifdef __cplusplus
}
#endif

#endif //XANDROID_BLUR_UTILS_H
