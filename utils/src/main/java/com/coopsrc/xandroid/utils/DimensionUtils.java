package com.coopsrc.xandroid.utils;

import android.content.Context;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-12-05 17:09
 */
public class DimensionUtils {
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }
}
