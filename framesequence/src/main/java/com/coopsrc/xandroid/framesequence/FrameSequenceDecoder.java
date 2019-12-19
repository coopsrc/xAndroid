package com.coopsrc.xandroid.framesequence;

import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.io.IOException;
import java.io.InputStream;

public class FrameSequenceDecoder implements ResourceDecoder<InputStream, FrameSequenceDrawable> {

    private FrameSequenceDrawable.BitmapProvider bitmapProvider;

    public FrameSequenceDecoder(BitmapPool bitmapPool) {
        bitmapProvider = new GlideBitmapProvider(bitmapPool);
    }

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        return true;
    }

    @Nullable
    @Override
    public Resource<FrameSequenceDrawable> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        FrameSequence frameSequence = FrameSequence.decodeStream(source);
        FrameSequenceDrawable drawable = new FrameSequenceDrawable(frameSequence, bitmapProvider);
        return new FrameSequenceDrawableResource(drawable);
    }
}
