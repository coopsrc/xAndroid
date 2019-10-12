package com.coopsrc.xandroid.dewdrops.processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.coopsrc.xandroid.dewdrops.task.TaskCallback;

import java.util.concurrent.Future;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 18:15
 */
public class BlurProcessor implements IBlurProcessor {
    BlurProcessor(Context context) {
    }

    @Override
    public Bitmap blur(Bitmap bitmap) {
        return null;
    }

    @Override
    public Bitmap blur(View view) {
        return null;
    }

    @Override
    public Future asyncBlur(Bitmap bitmap, TaskCallback callback) {
        return null;
    }

    @Override
    public Future asyncBlur(View view, TaskCallback callback) {
        return null;
    }
}
