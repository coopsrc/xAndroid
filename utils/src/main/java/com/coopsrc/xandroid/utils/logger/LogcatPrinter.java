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

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-01-08 15:14
 */
final class LogcatPrinter implements IPrinter {
    @Override
    public void println(int priority, @Nullable String tag, @NonNull String message) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, message);
        } else {
            Log.println(priority, tag, message);
        }
    }
}
