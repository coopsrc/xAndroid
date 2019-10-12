package com.coopsrc.xandroid.dewdrops.processor;

import android.graphics.Bitmap;

import androidx.core.util.Preconditions;

import com.coopsrc.xandroid.dewdrops.blur.OriginalBlur;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 19:52
 */
 class OriginalBlurProcessor extends AbsBlurProcessor {
    OriginalBlurProcessor(ProcessorBuilder processorBuilder) {
        super(processorBuilder);
    }

    @Override
    protected Bitmap doRealBlur(Bitmap bitmap, boolean concurrent) {
        Preconditions.checkNotNull(bitmap, "bitmap cannot be null!");
        Preconditions.checkArgument(!bitmap.isRecycled(), "bitmap cannot be recycled!");

        try {
            if (concurrent) {
                // TODO: 19-10-9
                OriginalBlur.doFullBlur(mode, bitmap, radius);
            } else {
                OriginalBlur.doFullBlur(mode, bitmap, radius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
