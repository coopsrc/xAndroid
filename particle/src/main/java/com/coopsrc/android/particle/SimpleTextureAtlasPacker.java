package com.coopsrc.android.particle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleTextureAtlasPacker {

    private static final int PADDING = 1;

    private Matrix rotationMatrix = new Matrix();
    private List<Rect> freeRects = new ArrayList<>();

    public SimpleTextureAtlasPacker() {
        rotationMatrix.postRotate(90);
    }

    /**
     * Generates TextureAtlas from a list of drawables. You also need to provide its width and height because
     * current implementation can't calculate it automatically. Texture indices match with the order of textures in
     * the list.
     * @return generated TextureAtlas
     */
    public TextureAtlas pack(List<Integer> drawableIds, Resources res, int atlasWidth, int atlasHeight) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int id : drawableIds) {
            bitmaps.add(BitmapFactory.decodeResource(res, id));
        }
        return pack(bitmaps, atlasWidth, atlasHeight);
    }

    /**
     * Generates TextureAtlas from a list of bitmaps. You also need to provide its width and height because
     * current implementation can't calculate it automatically. Texture indices match with the order of textures in
     * the list.
     * @return generated TextureAtlas
     */
    public TextureAtlas pack(List<Bitmap> bitmaps, int atlasWidth, int atlasHeight) {
        freeRects.add(new Rect(0, 0, atlasWidth, atlasHeight));
        List<Bitmap> sortedBitmaps = sortBitmaps(bitmaps);
        Rect out = new Rect();
        List<Region> regions = new ArrayList<>();
        for (Bitmap bmp : sortedBitmaps) {
            int index = bitmaps.indexOf(bmp);
            boolean rotate = findPositionForBitmap(bmp.getWidth() + 2 * PADDING, bmp.getHeight() + 2 * PADDING, out);
            if (rotate) {
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), rotationMatrix, false);
            }
            regions.add(new Region(index, out.left + PADDING, out.top + PADDING, rotate, bmp));
            for (int i = 0; i < freeRects.size(); i++) {
                if (splitRect(freeRects.get(i), out)) {
                    freeRects.remove(i--);
                }
            }
            cleanUpFreeRects();
        }
        freeRects.clear();
        TextureAtlas atlas = new TextureAtlas(atlasWidth, atlasHeight);
        List<Region> sortedRegions = sortRegionsToOriginalOrder(regions);
        for (Region r : sortedRegions) {
            atlas.addRegion(r.x, r.y, r.cwRotated, r.bitmap);
        }
        return atlas;
    }

    private List<Bitmap> sortBitmaps(List<Bitmap> bitmaps) {
        List<Bitmap> sorted = new ArrayList<>(bitmaps);
        Collections.sort(sorted, new Comparator<Bitmap>() {
            @Override
            public int compare(Bitmap b1, Bitmap b2) {
                return Math.max(b1.getWidth(), b1.getHeight()) - Math.max(b2.getWidth(), b2.getHeight());
            }
        });
        return sorted;
    }

    private List<Region> sortRegionsToOriginalOrder(List<Region> regions) {
        List<Region> sorted = new ArrayList<>(regions);
        Collections.sort(sorted, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.index - o2.index;
            }
        });
        return sorted;
    }

    private boolean findPositionForBitmap(int bmpWidth, int bmpHeight, Rect out) {
        out.setEmpty();
        int minShortExtra = Integer.MAX_VALUE;
        int maxLongExtra = 0;
        boolean rotate = false;
        for (int i = 0; i < freeRects.size(); ++i) {
            Rect rect = freeRects.get(i);
            for (int j = 0; j < 2; j++) {
                boolean flipped = j == 1;
                int w = flipped ? bmpHeight : bmpWidth;
                int h = flipped ? bmpWidth : bmpHeight;
                if (rect.width() >= w && rect.height() >= h) {
                    int horizontalExtra = rect.width() - w;
                    int verticalExtra = rect.height() - h;
                    int shortExtra = Math.min(horizontalExtra, verticalExtra);
                    int longExtra = Math.max(horizontalExtra, verticalExtra);
                    if (shortExtra < minShortExtra || (shortExtra == minShortExtra && longExtra < maxLongExtra)) {
                        out.set(rect.left, rect.top, rect.left + w, rect.top + h);
                        minShortExtra = shortExtra;
                        maxLongExtra = longExtra;
                        rotate = flipped;
                    }
                }
            }
        }
        if (out.isEmpty()) {
            throw new IllegalArgumentException("TextureAtlas is too small");
        }
        return rotate;
    }

    private boolean splitRect(Rect free, Rect used) {
        if (used.left >= free.right || used.right <= free.left || used.top >= free.bottom || used.bottom <= free.top) {
            return false;
        }
        if (used.left < free.right && used.right > free.left) {
            if (used.top > free.top && used.top < free.bottom) {
                Rect newRect = new Rect(free);
                newRect.bottom = used.top;
                freeRects.add(newRect);
            }
            if (used.bottom < free.bottom) {
                Rect newRect = new Rect(free);
                newRect.top = used.bottom;
                newRect.bottom = free.bottom;
                freeRects.add(newRect);
            }
        }
        if (used.top < free.bottom && used.bottom > free.top) {
            if (used.left > free.left && used.left < free.right) {
                Rect newRect = new Rect(free);
                newRect.right = used.left;
                freeRects.add(newRect);
            }
            if (used.right < free.right) {
                Rect newRect = new Rect(free);
                newRect.left = used.right;
                newRect.right = free.right;
                freeRects.add(newRect);
            }
        }
        return true;
    }

    private void cleanUpFreeRects() {
        for (int i = 0; i < freeRects.size(); i++)
            for (int j = i + 1; j < freeRects.size(); j++) {
                if (freeRects.get(j).contains(freeRects.get(i))) {
                    freeRects.remove(i--);
                    break;
                }
                if (freeRects.get(i).contains(freeRects.get(j))) {
                    freeRects.remove(j--);
                }
            }
    }

    private class Region {
        final int index;
        final int x;
        final int y;
        final boolean cwRotated;
        final Bitmap bitmap;

        Region(int index, int x, int y, boolean cwRotated, Bitmap bitmap) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.cwRotated = cwRotated;
            this.bitmap = bitmap;
        }
    }

}
