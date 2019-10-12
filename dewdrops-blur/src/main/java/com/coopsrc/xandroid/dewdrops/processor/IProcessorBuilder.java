package com.coopsrc.xandroid.dewdrops.processor;

import com.coopsrc.xandroid.dewdrops.annotation.Mode;
import com.coopsrc.xandroid.dewdrops.annotation.Scheme;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 18:08
 */
public interface IProcessorBuilder {
    IProcessorBuilder mode(@Mode int mode);

    IProcessorBuilder scheme(@Scheme int scheme);

    IProcessorBuilder radius(int radius);

    IProcessorBuilder sampleFactor(float sampleFactor);

    IProcessorBuilder forceCopy(boolean isForceCopy);

    IProcessorBuilder needUpscale(boolean needUpscale);

    IProcessorBuilder translateX(int translateX);

    IProcessorBuilder translateY(int translateY);

    BlurProcessor build();
}
