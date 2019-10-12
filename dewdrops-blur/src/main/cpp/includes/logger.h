//
// Created by tingkuo on 19-10-8.
//

#include <android/log.h>

#ifndef XANDROID_LOGGER_H
#define XANDROID_LOGGER_H

#define LOG_TAG "DewdropsBlur"
#define ALOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__))
#define ALOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#define ALOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define ALOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__))
#define ALOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))

#endif //XANDROID_LOGGER_H
