//
// Created by tingkuo on 19-10-8.
//

#ifndef XANDROID_GAUSSIAN_BLUR_H
#define XANDROID_GAUSSIAN_BLUR_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

void gaussianBlurHorizontal(float *kernel, jint *inPixels, jint *outPixels, jint width, jint height,
                            jint radius,
                            jint startX, jint startY, jint deltaX, jint deltaY);

void gaussianBlurVertical(float *kernel, jint *inPixels, jint *outPixels, jint width, jint height,
                          jint radius,
                          jint startX, jint startY, jint deltaX, jint deltaY);

float *makeKernel(jint r);

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_gaussianBlur(JNIEnv *env, jclass clazz,
                                                                jobject bitmap, jint radius,
                                                                jint workers, jint index,
                                                                jint direction);

#ifdef __cplusplus
}
#endif
#endif //XANDROID_GAUSSIAN_BLUR_H
