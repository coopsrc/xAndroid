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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 15:13
 */
final class DefaultFormatter extends Formatter {

    public DefaultFormatter() {
        super();
    }

    public DefaultFormatter(IPrinter printer) {
        super(printer);
    }

    @Override
    public void println(int priority, @Nullable String tag, @NonNull String message) {
        if (message.length() < MAX_LOG_LENGTH) {
            printer.println(priority, tag, message);
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                printer.println(priority, tag, part);
                i = end;
            } while (i < newline);
        }
    }
}
