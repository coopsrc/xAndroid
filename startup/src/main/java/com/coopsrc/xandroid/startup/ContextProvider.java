package com.coopsrc.xandroid.startup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2020-11-02 10:08
 */
public class ContextProvider {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    static Context initContext(Context context) {
        sContext = context.getApplicationContext();
        return sContext;
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
