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

package com.coopsrc.xandroid.adaptive;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:32
 */
class AdaptationCallback implements Application.ActivityLifecycleCallbacks, ComponentCallbacks {

    private final Application mApplication;
    private final AdaptationConfig mAdaptationConfig;

    private float mCompatDensity;
    private float mCompatScaledDensity;

    private static int statusBarHeight;
    private static int navBarHeight;

    AdaptationCallback(Application application, AdaptationConfig adaptationConfig) {
        mApplication = application;
        mAdaptationConfig = adaptationConfig;

        statusBarHeight = AdaptationUtils.getStatusBarHeight(application);
        navBarHeight = AdaptationUtils.getNavBarHeight(application);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        final DisplayMetrics appDisplayMetrics = mApplication.getResources().getDisplayMetrics();
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        if (mCompatDensity == 0) {
            mCompatDensity = appDisplayMetrics.density;
            mCompatScaledDensity = appDisplayMetrics.scaledDensity;
        }

        final float targetDensity = getTargetDensity(appDisplayMetrics, mAdaptationConfig);
        final float targetScaledDensity = targetDensity * (mCompatScaledDensity / mCompatDensity);

        int targetDensityDpi = (int) (DisplayMetrics.DENSITY_DEFAULT * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {

        if (configuration.fontScale > 0) {
            mCompatScaledDensity = mApplication.getResources().getDisplayMetrics().scaledDensity;
        }
    }

    @Override
    public void onLowMemory() {

    }

    private float getTargetDensity(DisplayMetrics displayMetrics, AdaptationConfig adaptationConfig) {
        float targetDensity = displayMetrics.density;
        float densityDpi;

        switch (adaptationConfig.getDesignOrientation()) {
            case AdaptationConfig.PORTRAIT:
                densityDpi = adaptationConfig.getDesignWidth() / adaptationConfig.getDesignDensity();
                targetDensity = displayMetrics.widthPixels / densityDpi;
                break;
            case AdaptationConfig.LANDSCAPE:
                densityDpi = adaptationConfig.getDesignHeight() / adaptationConfig.getDesignDensity();
                targetDensity = displayMetrics.heightPixels / densityDpi;
                break;
        }

        return targetDensity;
    }
}
