package com.coopsrc.xandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-08 15:36
 */
public class ContextProvider {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void initContext(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return sContext;
    }
}
