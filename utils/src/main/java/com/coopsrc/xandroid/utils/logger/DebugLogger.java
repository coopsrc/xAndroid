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

package com.coopsrc.xandroid.utils.logger;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coopsrc.xandroid.utils.GsonUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 11:55
 */
public class DebugLogger extends Logger {
    private static final String TAG = "DebugLogger";

    private static final int MAX_TAG_LENGTH = 23;
    private static final int CALL_STACK_INDEX = 5;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    private static final Formatter prettyFormatter = new PrettyFormatter();

    private static final HashMap<String, String> sCachedTag = new HashMap<>();

    public DebugLogger() {
        super();
    }

    @Override
    public void json(String content) {
//        if (isPretty()) {
//            super.json("\n" + GsonUtils.toPrettyFormat(content));
//        } else {
//            super.json(content);
//        }
        super.json("\n" + GsonUtils.toPrettyFormat(content));
    }

    @Nullable
    protected String createStackElementTag(@NonNull StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        // Tag length limit was removed in API 24.
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }

    @Override
    protected final String getTag() {
        String tag = super.getTag();
        if (tag != null) {
            return tag;
        }

        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
    }

    @Override
    public void log(int priority, String tag, @NonNull String message, Throwable t, boolean depthPlus) {
        if (isPretty()) {
            prettyFormatter.println(priority, buildTag(tag), buildMessage(message, depthPlus));
        } else {
            formatter.println(priority, buildTag(tag), buildMessage(message, depthPlus));
        }
    }

    private static String buildTag(@NonNull String tag) {
        String key = String.format(Locale.US, "%s@%s", tag, Thread.currentThread().getName());

        if (!sCachedTag.containsKey(key)) {
            if (TAG.equals(tag)) {
                sCachedTag.put(key, String.format(Locale.US, "|%s|%s|",
                        tag,
                        Thread.currentThread().getName()
                ));
            } else {
                sCachedTag.put(key, String.format(Locale.US, "|%s_%s|%s|",
                        TAG,
                        tag,
                        Thread.currentThread().getName()
                ));
            }
        }

        return sCachedTag.get(key);
    }

    private static String buildMessage(String message, boolean depthPlus) {
        StackTraceElement[] traceElements = new Throwable().getStackTrace();

        int traceDepth = depthPlus ? 6 : 7;

        if (traceElements == null || traceElements.length < traceDepth) {
            return message;
        }
        StackTraceElement traceElement = traceElements[traceDepth];

        return String.format(Locale.US, "%s.%s(%s:%d): %s",
                traceElement.getClassName().substring(traceElement.getClassName().lastIndexOf(".") + 1),
                traceElement.getMethodName(),
                traceElement.getFileName(),
                traceElement.getLineNumber(),
                message
        );
    }
}
