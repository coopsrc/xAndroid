package com.coopsrc.xandroid.framesequence;

import android.graphics.Bitmap;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

class GlideBitmapProvider implements FrameSequenceDrawable.BitmapProvider {
    private final BitmapPool bitmapPool;

    GlideBitmapProvider(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Bitmap acquireBitmap(int minWidth, int minHeight) {
        return bitmapPool.getDirty(minWidth, minHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void releaseBitmap(Bitmap bitmap) {
        bitmapPool.put(bitmap);
    }

}
