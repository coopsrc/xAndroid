package com.coopsrc.xandroid.http.monitor.model;

import android.text.TextUtils;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 11:26
 */
@Entity(tableName = "http_info")
public class HttpInfo {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @Embedded
    public RequestInfo requestInfo;
    @Embedded
    public ResponseInfo responseInfo;

    public HttpInfo() {

    }

    @Ignore
    public HttpInfo(RequestInfo requestInfo, ResponseInfo responseInfo) {
        this.requestInfo = requestInfo;
        this.responseInfo = responseInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getNotificationText() {
        if (!TextUtils.isEmpty(responseInfo.getError())) {
            return String.format("Failed request %s", requestInfo.getUrl().encodedPath());
        } else if (responseInfo.isSuccessful()) {
            return String.format("%s %s", responseInfo.getCode(), requestInfo.getUrl().encodedPath());
        } else {
            return String.format("Running request %s", requestInfo.getUrl().encodedPath());
        }
    }

    @Override
    public String toString() {
        return "HttpInfo{" +
                "id=" + id +
                ", requestInfo=" + requestInfo +
                ", responseInfo=" + responseInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpInfo httpInfo = (HttpInfo) o;

        if (id != httpInfo.id) return false;
        if (requestInfo != null ? !requestInfo.equals(httpInfo.requestInfo) : httpInfo.requestInfo != null)
            return false;
        return responseInfo != null ? responseInfo.equals(httpInfo.responseInfo) : httpInfo.responseInfo == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (requestInfo != null ? requestInfo.hashCode() : 0);
        result = 31 * result + (responseInfo != null ? responseInfo.hashCode() : 0);
        return result;
    }

    public String getRequestHeadersString(boolean withMarkup) {
        return MonitorUtils.formatHeaders(requestInfo.getHeaders(), withMarkup);
    }

    public String getResponseHeadersString(boolean withMarkup) {
        return MonitorUtils.formatHeaders(responseInfo.getHeaders(), withMarkup);
    }

    public String getFormattedRequestBody() {
        String contentType = requestInfo.getContentType() != null ? requestInfo.getContentType().type() : "";
        return MonitorUtils.formatBody(requestInfo.getBody(), contentType);
    }

    public String getFormattedResponseBody() {
        String contentType = responseInfo.getContentType() != null ? responseInfo.getContentType().type() : "";
        return MonitorUtils.formatBody(responseInfo.getBody(), contentType);
    }
}
