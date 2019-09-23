package com.coopsrc.xandroid.http.monitor.utils;

import com.coopsrc.xandroid.utils.GsonUtils;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import okhttp3.Headers;
import okio.Buffer;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 15:33
 */
public class MonitorUtils {
    public static boolean isProbablyUtf8(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    private static final SimpleDateFormat TIME_SHORT = new SimpleDateFormat("HH:mm:ss SSS", Locale.ROOT);

    private static final SimpleDateFormat TIME_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.ROOT);

    private static String formatData(Date date, SimpleDateFormat format) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    public static String getDateFormatShort(Date date) {
        return formatData(date, TIME_SHORT);
    }

    public static String getDateFormatLong(Date date) {
        return formatData(date, TIME_LONG);
    }

    public static String formatBytes(long bytes) {
        return formatByteCount(bytes, true);
    }

    private static String formatByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String formatHeaders(Headers headers, boolean withMarkup) {
        StringBuilder out = new StringBuilder();

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                out.append((withMarkup) ? "<b>" : "")
                        .append(headers.name(i))
                        .append(": ")
                        .append((withMarkup) ? "</b>" : "")
                        .append(headers.value(i))
                        .append((withMarkup) ? "<br />" : "\n");
            }
        }

        return out.toString();
    }

    public static String formatBody(String body, String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            return formatJson(body);
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            return formatXml(body);
        } else {
            return body;
        }
    }

    private static String formatJson(String json) {
        try {
            return GsonUtils.toJson(json);
        } catch (Exception e) {
            return json;
        }
    }

    private static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }
}
