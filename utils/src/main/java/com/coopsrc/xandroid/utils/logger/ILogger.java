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

    void v(String message, Object... args);

    void v(Throwable t, String message, Object... args);

    void v(Throwable t);

    void d(String message, Object... args);

    void d(Throwable t, String message, Object... args);

    void d(Throwable t);

    void i(String message, Object... args);

    void i(Throwable t, String message, Object... args);

    void i(Throwable t);

    void w(String message, Object... args);

    void w(Throwable t, String message, Object... args);

    void w(Throwable t);

    void e(String message, Object... args);

    void e(Throwable t, String message, Object... args);

    void e(Throwable t);

    void wtf(String message, Object... args);

    void wtf(Throwable t, String message, Object... args);

    void wtf(Throwable t);

    void json(String content);

    void log(int priority, String message, Object... args);

    void log(int priority, Throwable t, String message, Object... args);

    void log(int priority, Throwable t);

}
