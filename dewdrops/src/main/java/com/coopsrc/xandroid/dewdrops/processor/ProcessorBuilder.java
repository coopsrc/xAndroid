package com.coopsrc.xandroid.dewdrops.processor;

import android.content.Context;

import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;


/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 19:17
 */
public class ProcessorBuilder implements IProcessorBuilder {

    final Context context;

    @Mode
    int mode = BlurConfig.MODE_GAUSSIAN;
    @Scheme
    int scheme = BlurConfig.SCHEME_RENDER_SCRIPT;

    int radius = BlurConfig.DEFAULT_RADIUS;
    float sampleFactor = BlurConfig.DEFAULT_SAMPLE_FACTOR;
    boolean isForceCopy = false;
    boolean needUpscale = true;

    int translateX = 0;
    int translateY = 0;

    public ProcessorBuilder(Context context) {
        this.context = context;
    }

    @Override
    public IProcessorBuilder mode(int mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public IProcessorBuilder scheme(int scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public IProcessorBuilder radius(int radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public IProcessorBuilder sampleFactor(float sampleFactor) {
        this.sampleFactor = sampleFactor;
        return this;
    }

    @Override
    public IProcessorBuilder forceCopy(boolean isForceCopy) {
        this.isForceCopy = isForceCopy;
        return this;
    }

    @Override
    public IProcessorBuilder needUpscale(boolean needUpscale) {
        this.needUpscale = needUpscale;
        return this;
    }

    @Override
    public IProcessorBuilder translateX(int translateX) {
        this.translateX = translateX;
        return this;
    }

    @Override
    public IProcessorBuilder translateY(int translateY) {
        this.translateY = translateY;
        return this;
    }

    @Override
    public BlurProcessor build() {
        return new BlurProcessor(this);
    }
}