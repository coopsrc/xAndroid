package com.coopsrc.xandroid.dewdrops;

import android.content.Context;
import android.util.Log;

import com.coopsrc.xandroid.dewdrops.processor.IProcessorBuilder;
import com.coopsrc.xandroid.dewdrops.processor.ProcessorBuilder;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 16:48
 */
public class DewdropsBlur {
    private static final String TAG = "DewdropsBlur";

    static {
        System.loadLibrary("dewdrops");
    }

    public static native String libVersion();

    public static IProcessorBuilder with(Context context) {
        Log.i(TAG, "with: libVersion: " + libVersion());
        return new ProcessorBuilder(context);
    }
}
