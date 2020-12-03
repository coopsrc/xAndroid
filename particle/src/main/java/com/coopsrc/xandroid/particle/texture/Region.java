package com.coopsrc.xandroid.particle.texture;

import android.graphics.Bitmap;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-12-19 18:04
 */
public class Region {
    public final int x;
    public final int y;
    public final boolean cwRotated;
    public final Bitmap bitmap;

    Region(int x, int y, boolean cwRotated, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.cwRotated = cwRotated;
        this.bitmap = bitmap;
    }
}
