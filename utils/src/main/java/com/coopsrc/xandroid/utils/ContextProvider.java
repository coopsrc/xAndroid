package com.coopsrc.xandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-08 15:36
 */
public class ContextProvider {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    static void initContext(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static Resources getResources() {
        return sContext.getResources();
    }

    public static ApplicationInfo getApplicationInfo() {
        return sContext.getApplicationInfo();
    }
}
