package com.coopsrc.xandroid.adaptive;

import android.app.Application;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:23
 */
public class ScreenAdaptive {

    private static class Holder {
        private static final ScreenAdaptive INSTANCE = new ScreenAdaptive();
    }

    public static ScreenAdaptive getInstance() {
        return Holder.INSTANCE;
    }

    private ScreenAdaptive() {
    }

    public void init(Application application) {
        init(application, new AdaptationConfig.Builder().build());
    }

    public void init(Application application, AdaptationConfig adaptationConfig) {
        AdaptationCallback adaptationCallback = new AdaptationCallback(application, adaptationConfig);

        application.registerActivityLifecycleCallbacks(adaptationCallback);
        application.registerComponentCallbacks(adaptationCallback);
    }

}
