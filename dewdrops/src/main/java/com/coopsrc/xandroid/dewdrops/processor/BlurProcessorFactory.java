package com.coopsrc.xandroid.dewdrops.processor;

import com.coopsrc.xandroid.dewdrops.config.BlurConfig;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 19:18
 */
class BlurProcessorFactory {
    static IBlurProcessor create(ProcessorBuilder processorBuilder) {
        IBlurProcessor blurProcessor = null;

        switch (processorBuilder.scheme) {

            case BlurConfig.SCHEME_NATIVE:
                blurProcessor = new NativeBlurProcessor(processorBuilder);
                break;
            case BlurConfig.SCHEME_OPEN_GL:
                break;
            case BlurConfig.SCHEME_JAVA:
                blurProcessor = new OriginalBlurProcessor(processorBuilder);
                break;
            case BlurConfig.SCHEME_RENDER_SCRIPT:
                break;
            default:
                throw new IllegalArgumentException("Unsupported blur scheme!");
        }

        return blurProcessor;
    }
}
