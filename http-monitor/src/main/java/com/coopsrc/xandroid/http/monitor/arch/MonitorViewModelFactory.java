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
