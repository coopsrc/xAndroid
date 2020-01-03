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

package com.coopsrc.xandroid.http.monitor.arch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.coopsrc.xandroid.http.monitor.db.HttpMonitorDatabase;

import java.lang.reflect.InvocationTargetException;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-24 14:34
 */
public class MonitorViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static MonitorViewModelFactory sInstance;

    public static MonitorViewModelFactory getInstance(HttpMonitorDatabase database) {
        if (sInstance == null) {
            sInstance = new MonitorViewModelFactory(database);
        }
        return sInstance;
    }


    private final HttpMonitorDatabase database;

    private MonitorViewModelFactory(HttpMonitorDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (MonitorViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(HttpMonitorDatabase.class).newInstance(database);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }


        return super.create(modelClass);
    }
}
