package com.coopsrc.xandroid.http.monitor;

import android.content.Context;
import android.text.TextUtils;

import com.coopsrc.xandroid.http.monitor.common.IMonitor;
import com.coopsrc.xandroid.http.monitor.common.Monitor;
import com.coopsrc.xandroid.http.monitor.model.HttpInfo;
import com.coopsrc.xandroid.http.monitor.model.RequestInfo;
import com.coopsrc.xandroid.http.monitor.model.ResponseInfo;
import com.coopsrc.xandroid.http.monitor.utils.MonitorUtils;
import com.coopsrc.xandroid.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 10:32
 */
public class MonitorInterceptor implements Interceptor {
    private static final String TAG = "MonitorInterceptor";

    public enum Level {
        NONE, BASIC, HEADERS, BODY
    }
    private Level level = Level.HEADERS;

    private final Context context;
    private final IMonitor monitor;

    public MonitorInterceptor(Context context) {
        this.context = context;
        this.monitor = new Monitor(context);
    }

    public MonitorInterceptor(Context context, Monitor monitor) {
        this.context = context;
        this.monitor = monitor;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();

        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        HttpInfo httpInfo = new HttpInfo(new RequestInfo(), new ResponseInfo());

        boolean monitorBody = level == Level.BODY;
        boolean monitorHeaders = monitorBody || level == Level.HEADERS;

        // request
        RequestBody requestBody = request.body();
        Connection connection = chain.connection();

        httpInfo.requestInfo.setDate(new Date());
        httpInfo.requestInfo.setMethod(request.method());
        httpInfo.requestInfo.setUrl(request.url());
        if (connection != null) {
            httpInfo.requestInfo.setProtocol(connection.protocol());
        }
        if (monitorHeaders) {
            httpInfo.requestInfo.setHeaders(request.headers());

            if (requestBody != null) {
                httpInfo.requestInfo.setContentType(requestBody.contentType());
                httpInfo.requestInfo.setContentLength(requestBody.contentLength());
            }

            if (!monitorBody || requestBody == null) {
                httpInfo.requestInfo.setExtra(String.format("END %s", request.method()));
            } else if (bodyHasUnknownEncoding(request.headers())) {
                httpInfo.requestInfo.setExtra(String.format("END %s (encoded body omitted)", request.method()));
            } else if (requestBody.isDuplex()) {
                httpInfo.requestInfo.setExtra(String.format("END %s (duplex request body omitted)", request.method()));
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                MediaType contentType = requestBody.contentType();
                Charset charset = null;
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8);
                }

                if (charset == null) {
                    charset = StandardCharsets.UTF_8;
                }

                if (MonitorUtils.isProbablyUtf8(buffer)) {
                    httpInfo.requestInfo.setBody(buffer.readString(charset));
                    httpInfo.requestInfo.setExtra(String.format("END %s (%s-byte body)", request.method(), requestBody.contentLength()));
                } else {
                    httpInfo.requestInfo.setExtra(String.format("END %s (binary %s-byte body omitted)", request.method(), requestBody.contentLength()));
                }
            }
        }

        // insert to db
        httpInfo.id = insert(httpInfo);

        // response
        long startTime = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            httpInfo.responseInfo.setError(e.toString());
            update(httpInfo);
            throw e;
        }

        ResponseBody responseBody = response.body();
        httpInfo.responseInfo.setDate(new Date());
        httpInfo.responseInfo.setDuration(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        httpInfo.responseInfo.setProtocol(response.protocol());
        httpInfo.responseInfo.setCode(response.code());
        httpInfo.responseInfo.setMessage(response.message());
        if (responseBody != null) {
            httpInfo.responseInfo.setContentLength(responseBody.contentLength());
            httpInfo.responseInfo.setContentType(responseBody.contentType());
        }
        if (monitorHeaders) {
            httpInfo.responseInfo.setHeaders(response.headers());

            if (!monitorBody || HttpHeaders.promisesBody(response)) {
                httpInfo.responseInfo.setExtra("END HTTP");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                httpInfo.responseInfo.setExtra("END HTTP (encoded body omitted)");
            } else {
                if (responseBody != null) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE);
                    Buffer buffer = source.getBuffer();
                    Long gzippedLength = null;
                    if ("gzip".equalsIgnoreCase(response.headers().get("Content-Encoding"))) {
                        gzippedLength = buffer.size();
                        GzipSource gzipSource = new GzipSource(buffer.clone());
                        buffer = new Buffer();
                        buffer.writeAll(gzipSource);
                    }

                    MediaType contentType = responseBody.contentType();
                    Charset charset = null;
                    if (contentType != null) {
                        charset = contentType.charset(StandardCharsets.UTF_8);
                    }

                    if (charset == null) {
                        charset = StandardCharsets.UTF_8;
                    }

                    if (!MonitorUtils.isProbablyUtf8(buffer)) {

                        httpInfo.responseInfo.setExtra(String.format("END HTTP (binary %s-byte body omitted)", buffer.size()));
                        update(httpInfo);
                        return response;
                    }

                    if (responseBody.contentLength() != 0L) {
                        httpInfo.responseInfo.setBody(buffer.clone().readString(charset));
                    }
                    if (gzippedLength != null) {
                        httpInfo.responseInfo.setExtra(String.format("END HTTP (binary %s-byte, %s-gzipped-byte body)", buffer.size(), gzippedLength));
                    } else {
                        httpInfo.responseInfo.setExtra(String.format("END HTTP (binary %s-byte body)", buffer.size()));
                    }


                }
            }
        }

        update(httpInfo);
        return response;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");

        if (TextUtils.isEmpty(contentEncoding)) {
            return false;
        }

        return !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    private long insert(HttpInfo httpInfo) {
        LogUtils.d("insert: %s", httpInfo);
        return monitor.insert(httpInfo);
    }

    private void update(HttpInfo httpInfo) {
        LogUtils.i("update: %s", httpInfo);
        monitor.update(httpInfo);
    }
}
