package com.coopsrc.xandroid.adaptive;

import android.app.Application;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:53
 */
public class AdaptiveApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ScreenAdaptive.getInstance().init(this);
    }
}
