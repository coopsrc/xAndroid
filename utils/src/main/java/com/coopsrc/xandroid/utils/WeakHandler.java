package com.coopsrc.xandroid.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-03-23 15:50
 */
public abstract class WeakHandler<T> extends Handler {
    private final WeakReference<T> mRef;

    public WeakHandler(Looper looper, T ref) {
        super(looper);
        mRef = new WeakReference<>(ref);
    }

    public WeakHandler(T ref) {
        mRef = new WeakReference<>(ref);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        T referent = mRef.get();
        if (referent == null) {
            return;
        }

        handleMessage(msg, referent);
    }

    /**
     * Subclasses must implement this to receive messages.
     *
     * <p>If the WeakReference is cleared this method will no longer be called.
     *
     * @param msg      the message to handle
     * @param referent the referent. Guaranteed to be non null.
     */
    protected abstract void handleMessage(Message msg, @NonNull T referent);
}
