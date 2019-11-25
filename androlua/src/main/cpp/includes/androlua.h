//
// Created by tingkuo on 19-11-23.
//

#ifndef XANDROID_ANDROLUA_H
#define XANDROID_ANDROLUA_H

#include <jni.h>
#include "logger.h"

extern "C" {

#include "lua.hpp"
#include "luaconf.h"


JNIEXPORT jstring JNICALL
Java_com_coopsrc_xandroid_androlua_Androlua_getLuaVersion(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL
Java_com_coopsrc_xandroid_androlua_Androlua_getLuaRelease(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL
Java_com_coopsrc_xandroid_androlua_Androlua_getLuaCopyright(JNIEnv *env, jclass clazz);
JNIEXPORT jstring JNICALL
Java_com_coopsrc_xandroid_androlua_Androlua_getLuaAuthors(JNIEnv *env, jclass clazz);

};

#endif //XANDROID_ANDROLUA_H
