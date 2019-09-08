package com.coopsrc.xandroid.adaptive;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:58
 */
public class AdaptationUtils {

    public static int getStatusBarHeight(Context context) {
        int height = 0;

        Resources resources = context.getResources();

        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        return height;
    }

    public static int getNavBarHeight(Context context) {
        int height = 0;

        Resources resources = context.getResources();

        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        return height;
    }
}
