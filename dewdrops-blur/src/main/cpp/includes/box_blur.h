//
// Created by tingkuo on 19-10-8.
//

#ifndef XANDROID_BOX_BLUR_H
#define XANDROID_BOX_BLUR_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

void boxBlurHorizontal(jint *in, jint *out, jint width, jint height, jint radius, jint startX,
                       jint startY, jint deltaX, jint deltaY);

void boxBlurVertical(jint *in, jint *out, jint width, jint height, jint radius, jint startX,
                     jint startY, jint deltaX, jint deltaY);

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_boxBlur(JNIEnv *env, jclass clazz,
                                                           jobject bitmap, jint radius, jint workers,
                                                           jint index, jint direction);

#ifdef __cplusplus
}
#endif
#endif //XANDROID_BOX_BLUR_H
