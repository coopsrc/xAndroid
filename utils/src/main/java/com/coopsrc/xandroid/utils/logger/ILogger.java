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

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 12:17
 */
public interface ILogger {
    /**
     * Log a verbose message with optional format args.
     */
    void v(String message, Object... args);

    /**
     * Log a verbose exception and a message with optional format args.
     */
    void v(Throwable t, String message, Object... args);

    /**
     * Log a verbose exception.
     */
    void v(Throwable t);

    /**
     * Log a debug message with optional format args.
     */
    void d(String message, Object... args);

    /**
     * Log a debug exception and a message with optional format args.
     */
    void d(Throwable t, String message, Object... args);


    /**
     * Log a debug exception.
     */
    void d(Throwable t);

    /**
     * Log an info message with optional format args.
     */
    void i(String message, Object... args);

    /**
     * Log an info exception and a message with optional format args.
     */
    void i(Throwable t, String message, Object... args);


    /**
     * Log an info exception.
     */
    void i(Throwable t);

    /**
     * Log a warning message with optional format args.
     */
    void w(String message, Object... args);

    /**
     * Log a warning exception and a message with optional format args.
     */
    void w(Throwable t, String message, Object... args);

    /**
     * Log a warning exception.
     */
    void w(Throwable t);


    /**
     * Log an error message with optional format args.
     */
    void e(String message, Object... args);


    /**
     * Log an error exception and a message with optional format args.
     */
    void e(Throwable t, String message, Object... args);


    /**
     * Log an error exception.
     */
    void e(Throwable t);


    /**
     * Log an assert message with optional format args.
     */
    void wtf(String message, Object... args);

    /**
     * Log an assert exception and a message with optional format args.
     */
    void wtf(Throwable t, String message, Object... args);

    /**
     * Log an assert exception.
     */
    void wtf(Throwable t);

    /**
     * Log at {@code priority} a message with optional format args.
     */
    void log(int priority, String message, Object... args);

    /**
     * Log at {@code priority} an exception and a message with optional format args.
     */
    void log(int priority, Throwable t, String message, Object... args);

    /**
     * Log at {@code priority} an exception.
     */
    void log(int priority, Throwable t);

}
