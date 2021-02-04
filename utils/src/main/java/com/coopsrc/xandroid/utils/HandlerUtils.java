/*
 * Copyright (C) 2021 Zhang Tingkuo(zhangtingkuo@gmail.com)
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

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2021-02-04 10:16
 */
public class HandlerUtils {

    public static Handler createAsync() {
        return createAsync(Looper.getMainLooper());
    }

    public static Handler createAsync(@NonNull Looper looper) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Handler.createAsync(looper);
        }
        try {
            return Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, boolean.class)
                    .newInstance(looper, null, true);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException ignored) {
        } catch (InvocationTargetException e) {
            return new Handler(looper);
        }
        return new Handler(looper);
    }

    public static Handler createAsync(Handler.Callback callback) {
        return createAsync(Looper.getMainLooper(), callback);
    }

    public static Handler createAsync(@NonNull Looper looper, Handler.Callback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Handler.createAsync(looper, callback);
        }
        try {
            return Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, boolean.class)
                    .newInstance(looper, callback, true);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException ignored) {
        } catch (InvocationTargetException e) {
            return new Handler(looper);
        }
        return new Handler(looper, callback);
    }
}
