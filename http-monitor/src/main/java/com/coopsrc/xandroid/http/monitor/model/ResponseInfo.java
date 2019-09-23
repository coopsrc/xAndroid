package com.coopsrc.xandroid.http.monitor.model;

import androidx.room.ColumnInfo;

import java.util.Date;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 12:09
 */
public class ResponseInfo {
    @ColumnInfo(name = "response_date")
    private Date date;
    @ColumnInfo(name = "response_duration")
    private long duration;
    @ColumnInfo(name = "response_code")
    private int code = -1;
    @ColumnInfo(name = "response_headers")
    private Headers headers;
    @ColumnInfo(name = "response_body")
    private String body;
    @ColumnInfo(name = "response_message")
    private String message;
    @ColumnInfo(name = "response_protocol")
    private Protocol protocol;
    @ColumnInfo(name = "response_type")
    private MediaType contentType;
    @ColumnInfo(name = "response_length")
    private long contentLength;
    @ColumnInfo(name = "response_extra")
    private String extra;
    @ColumnInfo(name = "response_error")
    private String error;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccessful(){
        return code >= 200 && code < 300;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "date=" + date +
                ", duration=" + duration +
                ", code=" + code +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", message='" + message + '\'' +
                ", protocol=" + protocol +
                ", contentType=" + contentType +
                ", contentLength=" + contentLength +
                ", extra='" + extra + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseInfo that = (ResponseInfo) o;

        if (duration != that.duration) return false;
        if (code != that.code) return false;
        if (contentLength != that.contentLength) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (protocol != that.protocol) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (extra != null ? !extra.equals(that.extra) : that.extra != null) return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + code;
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (int) (contentLength ^ (contentLength >>> 32));
        result = 31 * result + (extra != null ? extra.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }
}
