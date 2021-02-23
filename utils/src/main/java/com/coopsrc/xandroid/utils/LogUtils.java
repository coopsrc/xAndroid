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

package com.coopsrc.xandroid.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.coopsrc.xandroid.utils.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-30 16:40
 */
public final class LogUtils {

    private static final Logger[] LOGGER_ARRAY_EMPTY = new Logger[0];
    private static final List<Logger> LOGGER_LIST = new ArrayList<>();
    private static volatile Logger[] sLoggers = LOGGER_ARRAY_EMPTY;

    private LogUtils() {
        throw new AssertionError("No instances.");
    }

    public static void v(@NonNull String message, Object... args) {
        LoggerDelegate.v(message, args);
    }

    public static void v(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.v(t, message, args);
    }

    public static void v(Throwable t) {
        LoggerDelegate.v(t);
    }

    public static void d(@NonNull String message, Object... args) {
        LoggerDelegate.d(message, args);
    }

    public static void d(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.d(t, message, args);
    }

    public static void d(Throwable t) {
        LoggerDelegate.d(t);
    }

    public static void i(@NonNull String message, Object... args) {
        LoggerDelegate.i(message, args);
    }

    public static void i(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.i(t, message, args);
    }

    public static void i(Throwable t) {
        LoggerDelegate.i(t);
    }

    public static void w(@NonNull String message, Object... args) {
        LoggerDelegate.w(message, args);
    }

    public static void w(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.w(t, message, args);
    }

    public static void w(Throwable t) {
        LoggerDelegate.w(t);
    }

    public static void e(@NonNull String message, Object... args) {
        LoggerDelegate.e(message, args);
    }

    public static void e(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.e(t, message, args);
    }

    public static void e(Throwable t) {
        LoggerDelegate.e(t);
    }

    public static void wtf(@NonNull String message, Object... args) {
        LoggerDelegate.wtf(message, args);
    }

    public static void wtf(Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.wtf(t, message, args);
    }

    public static void wtf(Throwable t) {
        LoggerDelegate.wtf(t);
    }

    public static void json(String content) {
        LoggerDelegate.json(content);
    }

    public static void log(int priority, @NonNull String message, Object... args) {
        LoggerDelegate.log(priority, message, args);
    }

    public static void log(int priority, Throwable t, @NonNull String message, Object... args) {
        LoggerDelegate.log(priority, t, message, args);
    }

    public static void log(int priority, Throwable t) {
        LoggerDelegate.log(priority, t);
    }

    @NonNull
    public static Logger asLogger() {
        return LoggerDelegate;
    }

    /**
     * Set a one-time tag for use on the next logging call.
     */
    @NonNull
    public static Logger tag(String tag) {
        Logger[] loggers = sLoggers;
        for (Logger logger : loggers) {
            logger.getExplicitTag().set(tag);
        }
        return LoggerDelegate;
    }

    @NonNull
    public static Logger pretty() {
        Logger[] loggers = sLoggers;
        for (Logger logger : loggers) {
            logger.getPretty().set(true);
        }
        return LoggerDelegate;
    }

    @NonNull
    public static Logger pretty(String tag) {
        Logger[] loggers = sLoggers;
        for (Logger logger : loggers) {
            logger.getExplicitTag().set(tag);
            logger.getPretty().set(true);
        }
        return LoggerDelegate;
    }

    /**
     * Add a new logging logger.
     */
    public static void register(@NonNull Logger logger) {
        if (logger == null) {
            throw new NullPointerException("logger == null");
        }
        if (logger == LoggerDelegate) {
            throw new IllegalArgumentException("Cannot register LogUtils into itself.");
        }
        synchronized (LOGGER_LIST) {
            LOGGER_LIST.add(logger);
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    public static void register(@NonNull Logger... loggers) {
        if (loggers == null) {
            throw new NullPointerException("loggers == null");
        }
        for (Logger logger : loggers) {
            if (logger == null) {
                throw new NullPointerException("loggers contains null");
            }
            if (logger == LoggerDelegate) {
                throw new IllegalArgumentException("Cannot register LogUtils into itself.");
            }
        }
        synchronized (LOGGER_LIST) {
            Collections.addAll(LOGGER_LIST, loggers);
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    public static void unregister(@NonNull Logger logger) {
        synchronized (LOGGER_LIST) {
            if (!LOGGER_LIST.remove(logger)) {
                throw new IllegalArgumentException("Cannot unregister logger which is not planted: " + logger);
            }
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    public static void unregisterAll() {
        synchronized (LOGGER_LIST) {
            LOGGER_LIST.clear();
            sLoggers = LOGGER_ARRAY_EMPTY;
        }
    }

    @NonNull
    public static List<Logger> loggerList() {
        synchronized (LOGGER_LIST) {
            return Collections.unmodifiableList(new ArrayList<>(LOGGER_LIST));
        }
    }

    public static int loggerCount() {
        synchronized (LOGGER_LIST) {
            return LOGGER_LIST.size();
        }
    }

    private static final Logger LoggerDelegate = new Logger() {
        @Override
        public void v(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.v(message, args);
            }
        }

        @Override
        public void v(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.v(t, message, args);
            }
        }

        @Override
        public void v(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.v(t);
            }
        }

        @Override
        public void d(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.d(message, args);
            }
        }

        @Override
        public void d(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.d(t, message, args);
            }
        }

        @Override
        public void d(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.d(t);
            }
        }

        @Override
        public void i(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.i(message, args);
            }
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.i(t, message, args);
            }
        }

        @Override
        public void i(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.i(t);
            }
        }

        @Override
        public void w(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.w(message, args);
            }
        }

        @Override
        public void w(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.w(t, message, args);
            }
        }

        @Override
        public void w(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.w(t);
            }
        }

        @Override
        public void e(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.e(message, args);
            }
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.e(t, message, args);
            }
        }

        @Override
        public void e(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.e(t);
            }
        }

        @Override
        public void wtf(String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.wtf(message, args);
            }
        }

        @Override
        public void wtf(Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.wtf(t, message, args);
            }
        }

        @Override
        public void wtf(Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.wtf(t);
            }
        }

        @Override
        public void json(String content) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.json(content);
            }
        }

        @Override
        public void log(int priority, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.log(priority, message, args);
            }
        }

        @Override
        public void log(int priority, Throwable t, String message, Object... args) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.log(priority, t, message, args);
            }
        }

        @Override
        public void log(int priority, Throwable t) {
            Logger[] loggers = sLoggers;
            for (Logger logger : loggers) {
                logger.log(priority, t);
            }
        }

        @Override
        public void log(int priority, String tag, @NonNull String message, Throwable t, boolean depthPlus) {
            throw new AssertionError("Missing override for log method.");
        }
    };

}