//
// Created by tingkuo on 19-11-23.
//

#include "androlua.h"
#include <string>

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_coopsrc_cicada_androlua_Androlua_getLuaVersion(JNIEnv *env, jclass clazz) {
    std::string lua_version = LUA_VERSION;

    ALOGE("lua version: %s", lua_version.c_str());

    return env->NewStringUTF(lua_version.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coopsrc_cicada_androlua_Androlua_getLuaRelease(JNIEnv *env, jclass clazz) {
    std::string lua_release = LUA_RELEASE;

    ALOGE("lua release: %s", lua_release.c_str());

    return env->NewStringUTF(lua_release.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coopsrc_cicada_androlua_Androlua_getLuaCopyright(JNIEnv *env, jclass clazz) {
    std::string lua_copyright = LUA_COPYRIGHT;

    ALOGE("lua copyright: %s", lua_copyright.c_str());

    return env->NewStringUTF(lua_copyright.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coopsrc_cicada_androlua_Androlua_getLuaAuthors(JNIEnv *env, jclass clazz) {
    std::string lua_authors = LUA_AUTHORS;

    ALOGE("lua authors: %s", lua_authors.c_str());

    return env->NewStringUTF(lua_authors.c_str());
}

}