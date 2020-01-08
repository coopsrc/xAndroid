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

package com.coopsrc.xandroid.http.monitor.model;

import androidx.room.ColumnInfo;

import java.util.Date;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 12:01
 */
public class RequestInfo {
    @ColumnInfo(name = "request_date")
    private Date date;
    @ColumnInfo(name = "request_method")
    private String method;
    @ColumnInfo(name = "request_url")
    private HttpUrl url;
    @ColumnInfo(name = "request_protocol")
    private Protocol protocol;
    @ColumnInfo(name = "request_type")
    private MediaType contentType;
    @ColumnInfo(name = "request_length")
    private long contentLength;
    @ColumnInfo(name = "request_headers")
    private Headers headers;
    @ColumnInfo(name = "request_body")
    private String body;
    @ColumnInfo(name = "request_extra")
    private String extra;

    public RequestInfo() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public void setUrl(HttpUrl url) {
        this.url = url;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "date=" + date +
                ", method='" + method + '\'' +
                ", url=" + url +
                ", protocol=" + protocol +
                ", contentType=" + contentType +
                ", contentLength=" + contentLength +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestInfo that = (RequestInfo) o;

        if (contentLength != that.contentLength) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (protocol != that.protocol) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        return extra != null ? extra.equals(that.extra) : that.extra == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (int) (contentLength ^ (contentLength >>> 32));
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (extra != null ? extra.hashCode() : 0);
        return result;
    }
}
