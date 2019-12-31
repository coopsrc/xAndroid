//
// Created by tingkuo on 19-11-23.
//

#include <jni.h>

#ifndef XANDROID_ANDROLUA_H
#define XANDROID_ANDROLUA_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL getLuaVersion(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL getLuaRelease(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL getLuaCopyright(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL getLuaAuthors(JNIEnv *env, jclass clazz);

#ifdef __cplusplus
}
#endif

#endif //XANDROID_ANDROLUA_H
