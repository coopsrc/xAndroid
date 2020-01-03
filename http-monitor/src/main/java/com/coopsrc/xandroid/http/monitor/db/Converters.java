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

package com.coopsrc.xandroid.http.monitor.db;

import androidx.room.TypeConverter;

import com.coopsrc.xandroid.utils.GsonUtils;

import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 17:06
 */
public class Converters {

    @TypeConverter
    public static Long convertDate(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date revertDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static String convertHeaders(Headers headers) {
        return GsonUtils.toJson(headers);
    }

    @TypeConverter
    public static Headers revertHeaders(String headers) {
        return GsonUtils.fromJson(headers, Headers.class);
    }

    @TypeConverter
    public static String convertHttpUrl(HttpUrl httpUrl) {
        return httpUrl.toString();
    }

    @TypeConverter
    public static HttpUrl revertHttpUrl(String url) {
        return HttpUrl.parse(url);
    }

    @TypeConverter
    public static String convertProtocol(Protocol protocol) {
        if (protocol != null) {
            return protocol.toString();
        }
        return Protocol.HTTP_1_1.toString();
    }

    @TypeConverter
    public static Protocol revertProtocol(String protocol) {
        try {
            return Protocol.get(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Protocol.HTTP_1_1;
    }

    @TypeConverter
    public static String convertMediaType(MediaType mediaType) {
        return GsonUtils.toJson(mediaType);
    }

    @TypeConverter
    public static MediaType revertMediaType(String mediaType) {
        return GsonUtils.fromJson(mediaType, MediaType.class);
    }
}
