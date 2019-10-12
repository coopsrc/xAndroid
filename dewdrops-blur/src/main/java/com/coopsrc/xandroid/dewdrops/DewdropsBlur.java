package com.coopsrc.xandroid.dewdrops;

import android.content.Context;

import com.coopsrc.xandroid.dewdrops.processor.IProcessorBuilder;
import com.coopsrc.xandroid.dewdrops.processor.ProcessorBuilder;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 16:48
 */
public class DewdropsBlur {
    static {
        System.loadLibrary("dewdrops-blur");
    }

    public static native String libVersion();

    public static IProcessorBuilder with(Context context) {
        return new ProcessorBuilder(context);
    }
}
