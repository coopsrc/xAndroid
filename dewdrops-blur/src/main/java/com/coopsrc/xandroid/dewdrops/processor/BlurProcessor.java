package com.coopsrc.xandroid.dewdrops.processor;

import android.graphics.Bitmap;
import android.view.View;

import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.task.TaskCallback;

import java.util.concurrent.Future;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 18:15
 */
public class BlurProcessor implements IBlurProcessor {

    private IBlurProcessor blurProcessorImpl;

    BlurProcessor(ProcessorBuilder processorBuilder) {
        blurProcessorImpl = create(processorBuilder);
    }

    @Override
    public Bitmap blur(Bitmap bitmap) {
        return blurProcessorImpl.blur(bitmap);
    }

    @Override
    public Bitmap blur(View view) {
        return blurProcessorImpl.blur(view);
    }

    @Override
    public Future asyncBlur(Bitmap bitmap, TaskCallback callback) {
        return blurProcessorImpl.asyncBlur(bitmap, callback);
    }

    @Override
    public Future asyncBlur(View view, TaskCallback callback) {
        return blurProcessorImpl.asyncBlur(view, callback);
    }

    private IBlurProcessor create(ProcessorBuilder processorBuilder) {
        IBlurProcessor blurProcessor = null;

        switch (processorBuilder.scheme) {

            case BlurConfig.SCHEME_NATIVE:
                blurProcessor = new NativeBlurProcessor(processorBuilder);
                break;
            case BlurConfig.SCHEME_OPEN_GL:
                blurProcessor = new NativeBlurProcessor(processorBuilder);
                break;
            case BlurConfig.SCHEME_JAVA:
                blurProcessor = new OriginalBlurProcessor(processorBuilder);
                break;
            case BlurConfig.SCHEME_RENDER_SCRIPT:
                blurProcessor = new RenderScriptBlurProcessor(processorBuilder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported blur scheme!");
        }

        return blurProcessor;
    }
}
