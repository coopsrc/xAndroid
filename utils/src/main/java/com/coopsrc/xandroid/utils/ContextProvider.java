package com.coopsrc.xandroid.utils;

import android.content.Context;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-08 15:36
 */
public class ContextProvider {
    private static Context sContext;

    public static void initContext(Context context) {
        sContext = context;
    }

    public static Context getAppContext() {
        return sContext.getApplicationContext();
    }
}
