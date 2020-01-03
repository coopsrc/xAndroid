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

package com.coopsrc.xandroid.http.logging

import com.coopsrc.xandroid.utils.LogUtils
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by tingkuo.
 * Date: 2017-12-23
 * Time: 10:45
 */
class HttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        LogUtils.tag(buildTag()).d(message)
    }

    companion object {
        private fun buildTag(): String {
            return String.format("|HttpLogger|%s|", Thread.currentThread().name)
        }
    }
}
