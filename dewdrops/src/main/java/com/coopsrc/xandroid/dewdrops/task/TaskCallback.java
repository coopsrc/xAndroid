package com.coopsrc.xandroid.dewdrops.task;

import android.graphics.Bitmap;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 18:05
 */
public interface TaskCallback {
    void onBlurSuccess(Bitmap bitmap);

    void onBlurFailed(Throwable error);
}
