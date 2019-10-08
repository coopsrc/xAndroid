#include <jni.h>
#include <string>

#include "logger.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_coopsrc_xandroid_dewdrops_DewdropsBlur_libVersion(JNIEnv *env, jclass clazz) {
    std::string version = "DewdropsBlur 1.0.0";
    return env->NewStringUTF(version.c_str());
}