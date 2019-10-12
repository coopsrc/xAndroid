//
// Created by tingkuo on 19-10-8.
//

#ifndef XANDROID_STACK_BLUR_H
#define XANDROID_STACK_BLUR_H

#define max(a, b) ((a)>(b)?(a):(b))
#define min(a, b) ((a)<(b)?(a):(b))

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

void doHorizontalBlur(jint *pix, jint w, jint h, jint radius, jint startX, jint startY, jint deltaX,
                      jint deltaY);

void doVerticalBlur(jint *pix, jint w, jint h, jint radius, jint startX, jint startY, jint deltaX,
                    jint deltaY);

JNIEXPORT void JNICALL
Java_com_coopsrc_xandroid_dewdrops_blur_NativeBlur_stackBlur(JNIEnv *env, jclass clazz,
                                                             jobject bitmap, jint radius,
                                                             jint workers, jint index,
                                                             jint direction);

#ifdef __cplusplus
}
#endif
#endif //XANDROID_STACK_BLUR_H
