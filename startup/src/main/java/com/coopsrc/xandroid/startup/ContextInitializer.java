package com.coopsrc.xandroid.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-11-02 10:07
 */
public class ContextInitializer implements Initializer<Context> {
    @NonNull
    @Override
    public Context create(@NonNull Context context) {
        return ContextProvider.initContext(context);
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
