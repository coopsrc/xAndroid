//
// Created by tingkuo on 19-11-23.
//

#include "androlua.h"
#include <string>

#include "logger.h"

#include "lua.hpp"
#include "luaconf.h"

JNINativeMethod methods[] = {
        {"_getLuaVersion",   "()Ljava/lang/String;", (void *) getLuaVersion},
        {"_getLuaRelease",   "()Ljava/lang/String;", (void *) getLuaRelease},
        {"_getLuaCopyright", "()Ljava/lang/String;", (void *) getLuaCopyright},
        {"_getLuaAuthors",   "()Ljava/lang/String;", (void *) getLuaAuthors}
};

jint registerNativeMethod(JNIEnv *env) {
    jclass clazz = env->FindClass("com/coopsrc/xandroid/androlua/Androlua");
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) < 0) {
        return JNI_ERR;
    }

    return JNI_OK;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    if (registerNativeMethod(env) != JNI_OK) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}

JNIEXPORT jstring JNICALL getLuaVersion(JNIEnv *env, jclass clazz) {
    std::string lua_version = LUA_VERSION;

    ALOGE("lua version: %s", lua_version.c_str());

    return env->NewStringUTF(lua_version.c_str());
}

JNIEXPORT jstring JNICALL getLuaRelease(JNIEnv *env, jclass clazz) {
    std::string lua_release = LUA_RELEASE;

    ALOGE("lua release: %s", lua_release.c_str());

    return env->NewStringUTF(lua_release.c_str());
}

JNIEXPORT jstring JNICALL getLuaCopyright(JNIEnv *env, jclass clazz) {
    std::string lua_copyright = LUA_COPYRIGHT;

    ALOGE("lua copyright: %s", lua_copyright.c_str());

    return env->NewStringUTF(lua_copyright.c_str());
}

JNIEXPORT jstring JNICALL getLuaAuthors(JNIEnv *env, jclass clazz) {
    std::string lua_authors = LUA_AUTHORS;

    ALOGE("lua authors: %s", lua_authors.c_str());

    return env->NewStringUTF(lua_authors.c_str());
}
