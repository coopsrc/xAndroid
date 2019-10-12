package com.coopsrc.xandroid.dewdrops.processor;

import android.graphics.Bitmap;

import androidx.core.util.Preconditions;

import com.coopsrc.xandroid.dewdrops.blur.NativeBlur;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-09 10:17
 */
public class NativeBlurProcessor extends AbsBlurProcessor {
    public NativeBlurProcessor(ProcessorBuilder processorBuilder) {
        super(processorBuilder);
    }

    @Override
    protected Bitmap doRealBlur(Bitmap bitmap, boolean concurrent) {
        Preconditions.checkNotNull(bitmap, "bitmap cannot be null!");
        Preconditions.checkArgument(!bitmap.isRecycled(), "bitmap cannot be recycled!");

        try {
            if (concurrent) {
                // TODO: 19-10-9
                NativeBlur.doFullBlur(mode, bitmap, radius);
            } else {
                NativeBlur.doFullBlur(mode, bitmap, radius);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
