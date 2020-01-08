/*
 * Copyright (C) 2019 Zhang Tingkuo(zhangtingkuo@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coopsrc.xandroid.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coopsrc.xandroid.utils.logger.DebugLogger
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author tingkuo
 *
 *
 * Datetime: 2020-01-08 17:22
 */
@RunWith(AndroidJUnit4::class)
class LogUtilsTest {

    @Test
    fun testLogUtils() {
        LogUtils.register(DebugLogger())
        val exception = Exception("Test Exception")

        LogUtils.v(exception)
        LogUtils.v("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.v(exception, "testLogUtils: [%d, %d]", 1920, 1080)

        LogUtils.d(exception)
        LogUtils.d("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.d(exception, "testLogUtils: [%d, %d]", 1920, 1080)

        LogUtils.i(exception)
        LogUtils.i("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.i(exception, "testLogUtils: [%d, %d]", 1920, 1080)

        LogUtils.w(exception)
        LogUtils.w("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.w(exception, "testLogUtils: [%d, %d]", 1920, 1080)

        LogUtils.e(exception)
        LogUtils.e("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.e(exception, "testLogUtils: [%d, %d]", 1920, 1080)

        LogUtils.wtf(exception)
        LogUtils.wtf("testLogUtils: [%d, %d]", 1920, 1080)
        LogUtils.wtf(exception, "testLogUtils: [%d, %d]", 1920, 1080)
    }
}