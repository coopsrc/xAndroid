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

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 11:54
 */
public abstract class Logger implements ILogger {
    private final ThreadLocal<String> explicitTag = new ThreadLocal<>();
    private final ThreadLocal<Boolean> pretty = new ThreadLocal<>();

    private static final Formatter defaultFormatter = new DefaultFormatter();

    final Formatter formatter;

    public Logger() {
        formatter = defaultFormatter;
    }

    public Logger(Formatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Log a verbose message with optional format args.
     */
    @Override
    public void v(String message, Object... args) {
        prepareLog(Log.VERBOSE, null, message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    @Override
    public void v(Throwable t, String message, Object... args) {
        prepareLog(Log.VERBOSE, t, message, args);
    }

    /**
     * Log a verbose exception.
     */
    @Override
    public void v(Throwable t) {
        prepareLog(Log.VERBOSE, t, null);
    }

    /**
     * Log a debug message with optional format args.
     */
    @Override
    public void d(String message, Object... args) {
        prepareLog(Log.DEBUG, null, message, args);
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    @Override
    public void d(Throwable t, String message, Object... args) {
        prepareLog(Log.DEBUG, t, message, args);
    }

    /**
     * Log a debug exception.
     */
    @Override
    public void d(Throwable t) {
        prepareLog(Log.DEBUG, t, null);
    }

    /**
     * Log an info message with optional format args.
     */
    @Override
    public void i(String message, Object... args) {
        prepareLog(Log.INFO, null, message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    @Override
    public void i(Throwable t, String message, Object... args) {
        prepareLog(Log.INFO, t, message, args);
    }

    /**
     * Log an info exception.
     */
    @Override
    public void i(Throwable t) {
        prepareLog(Log.INFO, t, null);
    }

    /**
     * Log a warning message with optional format args.
     */
    @Override
    public void w(String message, Object... args) {
        prepareLog(Log.WARN, null, message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    @Override
    public void w(Throwable t, String message, Object... args) {
        prepareLog(Log.WARN, t, message, args);
    }

    /**
     * Log a warning exception.
     */
    @Override
    public void w(Throwable t) {
        prepareLog(Log.WARN, t, null);
    }

    /**
     * Log an error message with optional format args.
     */
    @Override
    public void e(String message, Object... args) {
        prepareLog(Log.ERROR, null, message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    @Override
    public void e(Throwable t, String message, Object... args) {
        prepareLog(Log.ERROR, t, message, args);
    }

    /**
     * Log an error exception.
     */
    @Override
    public void e(Throwable t) {
        prepareLog(Log.ERROR, t, null);
    }

    /**
     * Log an assert message with optional format args.
     */
    @Override
    public void wtf(String message, Object... args) {
        prepareLog(Log.ASSERT, null, message, args);
    }

    /**
     * Log an assert exception and a message with optional format args.
     */
    @Override
    public void wtf(Throwable t, String message, Object... args) {
        prepareLog(Log.ASSERT, t, message, args);
    }

    /**
     * Log an assert exception.
     */
    @Override
    public void wtf(Throwable t) {
        prepareLog(Log.ASSERT, t, null);
    }

    /**
     * Log at {@code priority} a message with optional format args.
     */
    @Override
    public void log(int priority, String message, Object... args) {
        prepareLog(priority, null, message, args);
    }

    /**
     * Log at {@code priority} an exception and a message with optional format args.
     */
    @Override
    public void log(int priority, Throwable t, String message, Object... args) {
        prepareLog(priority, t, message, args);
    }

    /**
     * Log at {@code priority} an exception.
     */
    @Override
    public void log(int priority, Throwable t) {
        prepareLog(priority, t, null);
    }

    /**
     * Return whether a message at {@code priority} should be logged.
     */
    protected boolean isLoggable(int priority) {
        return true;
    }

    /**
     * Return whether a message at {@code priority} or {@code tag} should be logged.
     */
    protected boolean isLoggable(@Nullable String tag, int priority) {
        //noinspection deprecation
        return isLoggable(priority);
    }

    private void prepareLog(int priority, Throwable throwable, String message, Object... args) {
        // Consume tag even when message is not loggable so that next message is correctly tagged.
        boolean depthPlus = getExplicitTag().get() != null || getPretty().get() != null;
        String tag = getTag();

        if (!isLoggable(tag, priority)) {
            return;
        }
        if (message != null && message.length() == 0) {
            message = null;
        }
        if (message == null) {
            if (throwable == null) {
                return; // Swallow message if it's null and there's no throwable.
            }
            message = getStackTraceString(throwable);
        } else {
            if (args != null && args.length > 0) {
                message = formatMessage(message, args);
            }
            if (throwable != null) {
                message += "\n" + getStackTraceString(throwable);
            }
        }

        log(priority, tag, message, throwable, depthPlus);
    }

    /**
     * Formats a log message with optional arguments.
     */
    private String formatMessage(@NonNull String message, @NonNull Object[] args) {
        return String.format(message, args);
    }

    private String getStackTraceString(Throwable t) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public ThreadLocal<String> getExplicitTag() {
        return explicitTag;
    }

    public ThreadLocal<Boolean> getPretty() {
        return pretty;
    }

    @Nullable
    protected String getTag() {
        String tag = explicitTag.get();
        if (tag != null) {
            explicitTag.remove();
        }
        return tag;
    }

    protected boolean isPretty() {
        Boolean isPretty = pretty.get();
        if (isPretty != null) {
            pretty.remove();

            return isPretty;
        }

        return false;
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See {@link Log} for constants.
     * @param tag      Explicit or inferred tag. May be {@code null}.
     * @param message  Formatted log message. May be {@code null}, but then {@code t} will not be.
     * @param t        Accompanying exceptions. May be {@code null}, but then {@code message} will not be.
     */
    protected abstract void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t, boolean depthPlus);
}