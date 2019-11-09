package com.coopsrc.xandroid.http.config;

import com.coopsrc.xandroid.utils.MemoryUnit;

import java.util.concurrent.TimeUnit;

public class HttpConstants {

    public static final String BASE_URL = "http://127.0.0.1/";

    public static final String ACCESS_TOKEN = "access_token";

    public static final long TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(15);

    public static final boolean RETRY_ON_CONNECTION_FAILURE = false;

    public static final boolean HOST_LOOP_ENABLE = false;

    public static final String HTTP_CACHE_DIR_NAME = "http_cache";
    public static final long HTTP_CACHE_SIZE = MemoryUnit.MEGA_BYTE.toBytes(100);


    public static final long CACHE_MAX_AGE = TimeUnit.MINUTES.toSeconds(15);
    public static final long CACHE_MAX_STALE = TimeUnit.DAYS.toSeconds(7);


}
