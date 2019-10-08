package com.coopsrc.xandroid.dewdrops.processor;

import android.graphics.Bitmap;
import android.view.View;

import com.coopsrc.xandroid.dewdrops.task.TaskCallback;

import java.util.concurrent.Future;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 18:04
 */
public interface IBlurProcessor {
    Bitmap blur(Bitmap bitmap);

    Bitmap blur(View view);

    Future asyncBlur(Bitmap bitmap, TaskCallback callback);

    Future asyncBlur(View view, TaskCallback callback);
}
