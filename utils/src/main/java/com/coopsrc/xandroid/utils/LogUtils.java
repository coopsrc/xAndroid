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

    /**
     * Log a verbose message with optional format args.
     */
    public static void v(@NonNull String message, Object... args) {
        PROXY.v(message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    public static void v(Throwable t, @NonNull String message, Object... args) {
        PROXY.v(t, message, args);
    }

    /**
     * Log a verbose exception.
     */
    public static void v(Throwable t) {
        PROXY.v(t);
    }

    /**
     * Log a debug message with optional format args.
     */
    public static void d(@NonNull String message, Object... args) {
        PROXY.d(message, args);
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    public static void d(Throwable t, @NonNull String message, Object... args) {
        PROXY.d(t, message, args);
    }

    /**
     * Log a debug exception.
     */
    public static void d(Throwable t) {
        PROXY.d(t);
    }

    /**
     * Log an info message with optional format args.
     */
    public static void i(@NonNull String message, Object... args) {
        PROXY.i(message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    public static void i(Throwable t, @NonNull String message, Object... args) {
        PROXY.i(t, message, args);
    }

    /**
     * Log an info exception.
     */
    public static void i(Throwable t) {
        PROXY.i(t);
    }

    /**
     * Log a warning message with optional format args.
     */
    public static void w(@NonNull String message, Object... args) {
        PROXY.w(message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    public static void w(Throwable t, @NonNull String message, Object... args) {
        PROXY.w(t, message, args);
    }

    /**
     * Log a warning exception.
     */
    public static void w(Throwable t) {
        PROXY.w(t);
    }

    /**
     * Log an error message with optional format args.
     */
    public static void e(@NonNull String message, Object... args) {
        PROXY.e(message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    public static void e(Throwable t, @NonNull String message, Object... args) {
        PROXY.e(t, message, args);
    }

    /**
     * Log an error exception.
     */
    public static void e(Throwable t) {
        PROXY.e(t);
    }

    /**
     * Log an assert message with optional format args.
     */
    public static void wtf(@NonNull String message, Object... args) {
        PROXY.wtf(message, args);
    }

    /**
     * Log an assert exception and a message with optional format args.
     */
    public static void wtf(Throwable t, @NonNull String message, Object... args) {
        PROXY.wtf(t, message, args);
    }

    /**
     * Log an assert exception.
     */
    public static void wtf(Throwable t) {
        PROXY.wtf(t);
    }

    /**
     * Log at {@code priority} a message with optional format args.
     */
    public static void log(int priority, @NonNull String message, Object... args) {
        PROXY.log(priority, message, args);
    }

    /**
     * Log at {@code priority} an exception and a message with optional format args.
     */
    public static void log(int priority, Throwable t, @NonNull String message, Object... args) {
        PROXY.log(priority, t, message, args);
    }

    /**
     * Log at {@code priority} an exception.
     */
    public static void log(int priority, Throwable t) {
        PROXY.log(priority, t);
    }

    /**
     * A view into LogUtils's planted trees as a tree itself. This can be used for injecting a logger
     * instance rather than using static methods or to facilitate testing.
     */
    @NonNull
    public static Logger asLogger() {
        return PROXY;
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
        return PROXY;
    }

    public static Logger pretty() {
        Logger[] loggers = sLoggers;
        for (Logger logger : loggers) {
            logger.getPretty().set(true);
        }
        return PROXY;
    }

    public static Logger pretty(String tag) {
        Logger[] loggers = sLoggers;
        for (Logger logger : loggers) {
            logger.getExplicitTag().set(tag);
            logger.getPretty().set(true);
        }
        return PROXY;
    }

    /**
     * Add a new logging logger.
     */
    public static void register(@NonNull Logger logger) {
        if (logger == null) {
            throw new NullPointerException("logger == null");
        }
        if (logger == PROXY) {
            throw new IllegalArgumentException("Cannot register LogUtils into itself.");
        }
        synchronized (LOGGER_LIST) {
            LOGGER_LIST.add(logger);
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    /**
     * Adds new logging loggers.
     */
    public static void register(@NonNull Logger... loggers) {
        if (loggers == null) {
            throw new NullPointerException("loggers == null");
        }
        for (Logger logger : loggers) {
            if (logger == null) {
                throw new NullPointerException("loggers contains null");
            }
            if (logger == PROXY) {
                throw new IllegalArgumentException("Cannot register LogUtils into itself.");
            }
        }
        synchronized (LOGGER_LIST) {
            Collections.addAll(LOGGER_LIST, loggers);
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    /**
     * Remove a registered logger.
     */
    public static void unregister(@NonNull Logger logger) {
        synchronized (LOGGER_LIST) {
            if (!LOGGER_LIST.remove(logger)) {
                throw new IllegalArgumentException("Cannot unregister logger which is not planted: " + logger);
            }
            sLoggers = LOGGER_LIST.toArray(new Logger[0]);
        }
    }

    /**
     * Remove all registered trees.
     */
    public static void unregisterAll() {
        synchronized (LOGGER_LIST) {
            LOGGER_LIST.clear();
            sLoggers = LOGGER_ARRAY_EMPTY;
        }
    }

    /**
     * Return a copy of all registered {@linkplain Logger LOGGER_LIST}.
     */
    @NonNull
    public static List<Logger> loggerList() {
        synchronized (LOGGER_LIST) {
            return Collections.unmodifiableList(new ArrayList<>(LOGGER_LIST));
        }
    }


    /**
     * Return a copy of all registered {@linkplain Logger}.
     */
    public static int loggerCount() {
        synchronized (LOGGER_LIST) {
            return LOGGER_LIST.size();
        }
    }

    /**
     * A {@link Logger} that delegates to all registered loggers in the {@linkplain #LOGGER_LIST}.
     */
    private static final Logger PROXY = new Logger() {
        @Override
        public void v(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.v(message, args);
            }
        }

        @Override
        public void v(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.v(t, message, args);
            }
        }

        @Override
        public void v(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.v(t);
            }
        }

        @Override
        public void d(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.d(message, args);
            }
        }

        @Override
        public void d(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.d(t, message, args);
            }
        }

        @Override
        public void d(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.d(t);
            }
        }

        @Override
        public void i(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.i(message, args);
            }
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.i(t, message, args);
            }
        }

        @Override
        public void i(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.i(t);
            }
        }

        @Override
        public void w(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.w(message, args);
            }
        }

        @Override
        public void w(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.w(t, message, args);
            }
        }

        @Override
        public void w(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.w(t);
            }
        }

        @Override
        public void e(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.e(message, args);
            }
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.e(t, message, args);
            }
        }

        @Override
        public void e(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.e(t);
            }
        }

        @Override
        public void wtf(String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.wtf(message, args);
            }
        }

        @Override
        public void wtf(Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.wtf(t, message, args);
            }
        }

        @Override
        public void wtf(Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.wtf(t);
            }
        }

        @Override
        public void log(int priority, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.log(priority, message, args);
            }
        }

        @Override
        public void log(int priority, Throwable t, String message, Object... args) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.log(priority, t, message, args);
            }
        }

        @Override
        public void log(int priority, Throwable t) {
            Logger[] forest = sLoggers;
            for (Logger logger : forest) {
                logger.log(priority, t);
            }
        }

        @Override
        public void log(int priority, String tag, @NonNull String message, Throwable t) {
            throw new AssertionError("Missing override for log method.");
        }
    };

}