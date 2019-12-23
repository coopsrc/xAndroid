package com.coopsrc.xandroid.framesequence;

import android.content.Context;
import android.support.rastermill.FrameSequenceDrawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

public class FrameSequenceGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(
                Registry.BUCKET_GIF,
                InputStream.class,
                FrameSequenceDrawable.class,
                new FrameSequenceDecoder(glide.getBitmapPool())
        );
    }
}
