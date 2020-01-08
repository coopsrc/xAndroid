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

package com.coopsrc.xandroid.http.monitor

import com.coopsrc.xandroid.http.interceptor.BaseMonitorInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author tingkuo
 *
 *
 * Datetime: 2019-09-23 10:32
 */
open class MonitorInterceptor : BaseMonitorInterceptor {

    constructor() : super() {
    }

    constructor(level: Level) : super(level) {
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        return super.intercept(chain)
    }
}