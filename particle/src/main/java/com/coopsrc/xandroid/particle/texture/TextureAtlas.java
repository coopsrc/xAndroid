package com.coopsrc.xandroid.particle.texture;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class TextureAtlas {
    private int width;
    private int height;
    private List<Region> regions = new ArrayList<>();
    private boolean editable = true;

    public TextureAtlas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Adds bitmap as a region to this TextureAtlas. Keep in mind that textures shouldn't overlap. It's also a good
     * practice to keep some padding around each texture to avoid "texture bleeding".
     *
     * @param x - X coordinate of region
     * @param y - Y coordinate of region
     * @return textureIndex that can be used as a reference to this texture
     */
    public int addRegion(int x, int y, Bitmap bitmap) {
        return addRegion(x, y, false, bitmap);
    }

    /**
     * Adds bitmap as a region to this TextureAtlas. Keep in mind that textures shouldn't overlap. It's also a good
     * practice to keep some padding around each texture to avoid "texture bleeding".
     *
     * @param x         - X coordinate of region
     * @param y         - Y coordinate of region
     * @param cwRotated - set to true if you want this texture to be 90 degree rotated inside atlas. It can be
     *                  useful to optimize packing.
     * @return textureIndex that can be used as a reference to this texture
     */
    public int addRegion(int x, int y, boolean cwRotated, Bitmap bitmap) {
        if (!editable) {
            throw new IllegalStateException("TextureAtlas is not editable anymore");
        }
        regions.add(new Region(x, y, cwRotated, bitmap));
        return regions.size() - 1;
    }

}
