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

package com.coopsrc.xandroid.http.config;

import com.coopsrc.xandroid.utils.MemoryUnit;

import java.util.concurrent.TimeUnit;

public class HttpConstants {

    public static final String TEMP_BASE_URL = "http://127.0.0.1/";

    public static final String ACCESS_TOKEN = "access_token";

    public static final long TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(15);

    public static final boolean RETRY_ON_CONNECTION_FAILURE = false;

    public static final boolean HOST_LOOP_ENABLE = false;

    public static final String HTTP_CACHE_DIR_NAME = "http_cache";
    public static final long HTTP_CACHE_SIZE = MemoryUnit.MEGA_BYTE.toBytes(100);


    public static final long CACHE_MAX_AGE = TimeUnit.MINUTES.toSeconds(15);
    public static final long CACHE_MAX_STALE = TimeUnit.DAYS.toSeconds(7);


}
