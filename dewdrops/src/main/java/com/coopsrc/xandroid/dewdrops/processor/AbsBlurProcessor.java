package com.coopsrc.xandroid.dewdrops.processor;

import android.graphics.Bitmap;
import android.view.View;

import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;
import com.coopsrc.xandroid.dewdrops.task.TaskCallback;
import com.coopsrc.xandroid.dewdrops.utils.BitmapUtils;
import com.coopsrc.xandroid.dewdrops.utils.Preconditions;

import java.util.concurrent.Future;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 19:32
 */
public abstract class AbsBlurProcessor implements IBlurProcessor {
    @Mode
    protected int mode;
    @Scheme
    protected int scheme;

    protected int radius;
    protected float sampleFactor;
    protected boolean isForceCopy;
    protected boolean needUpscale;

    protected int translateX;
    protected int translateY;

    AbsBlurProcessor(ProcessorBuilder processorBuilder) {
        mode = processorBuilder.mode;
        scheme = processorBuilder.scheme;
        radius = processorBuilder.radius;
        sampleFactor = processorBuilder.sampleFactor;
        isForceCopy = processorBuilder.isForceCopy;
        needUpscale = processorBuilder.needUpscale;
        translateX = processorBuilder.translateX;
        translateY = processorBuilder.translateY;
    }

    @Override
    public Bitmap blur(Bitmap bitmap) {
        return doBlur(bitmap, true);
    }

    @Override
    public Bitmap blur(View view) {
        Preconditions.checkNotNull(view, "Input view cannot be null!");
        Bitmap viewBitmap = BitmapUtils.getViewBitmap(view, translateX, translateY, sampleFactor);
        Bitmap bitmap = doRealBlur(viewBitmap, true);
        return needUpscale ? BitmapUtils.getScaledBitmap(bitmap, 1.0f / sampleFactor) : bitmap;
    }

    @Override
    public Future asyncBlur(Bitmap bitmap, TaskCallback callback) {
        return null;
    }

    @Override
    public Future asyncBlur(View view, TaskCallback callback) {
        return null;
    }

    private Bitmap doBlur(Bitmap bitmap, boolean concurrent) {
        Preconditions.checkNotNull(bitmap, "bitmap cannot be null!");
        Preconditions.checkArgument(!bitmap.isRecycled(), "bitmap cannot be recycled!");

        if (radius <= 0) {
            radius = 1;
        }

        if (sampleFactor < 1.0f) {
            sampleFactor = 1.0f;
        }

        Bitmap inBitmap;

        if (isForceCopy) {
            inBitmap = bitmap.copy(bitmap.getConfig(), true);
        } else {
            inBitmap = bitmap;
        }
        Bitmap transInBitmap = BitmapUtils.transformBitmap(inBitmap, translateX, translateY);

        Bitmap scaledInBitmap = BitmapUtils.getScaledBitmap(transInBitmap, sampleFactor);

        Bitmap scaledOutBitmap = doRealBlur(scaledInBitmap, concurrent);

        return needUpscale ? BitmapUtils.getScaledBitmap(scaledOutBitmap, 1f / sampleFactor) : scaledOutBitmap;

    }

    protected abstract Bitmap doRealBlur(Bitmap bitmap, boolean concurrent);

    protected void release() {

    }
}
