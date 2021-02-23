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

import java.util.regex.Pattern;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 15:15
 */
public abstract class Formatter implements IPrinter {

    protected final IPrinter mPrinter;

    static final int MAX_LOG_LENGTH = 4000;
    static final int MAX_TAG_LENGTH = 23;
    static final int CALL_STACK_INDEX = 5;
    static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    public Formatter() {
        mPrinter = new LogcatPrinter();
    }

    public Formatter(IPrinter printer) {
        this.mPrinter = printer;
    }
}
