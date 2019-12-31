package com.coopsrc.android.particle;

import com.coopsrc.xandroid.utils.LogUtils;

public class Particle {

    private static final double PI_4 = Math.PI / 4;

    private int width;
    private int height;

    private float x;
    private float y;
    private double rotation;
    private float alpha = 1f;
    private short textureIndex;
    private float halfDiagonal;
    private float dx1;
    private float dy1;
    private float dx2;
    private float dy2;


    public Particle() {
    }

    public Particle(int width, int height, float x, float y, int textureIndex) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.textureIndex = (short) textureIndex;
        updateDiagonal();
        updateCorners();
    }

    private void updateDiagonal() {
        halfDiagonal = (float) Math.sqrt(width * width + height * height) / 2;
    }

    private void updateCorners() {
        dx1 = (float) (Math.cos(PI_4 + rotation) * halfDiagonal);
        dy1 = (float) (Math.sin(PI_4 + rotation) * halfDiagonal);
        dx2 = (float) (Math.cos(PI_4 - rotation) * halfDiagonal);
        dy2 = (float) (Math.sin(PI_4 - rotation) * halfDiagonal);
        LogUtils.w("updateCorners: [%.2f,%.2f] ==> [%.2f,%.2f],[%.2f,%.2f]", width, height, dx1, dy1, dx2, dy2);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

    public float getAlpha() {
        return alpha;
    }

    public short getTextureIndex() {
        return textureIndex;
    }

    public float getDx1() {
        return dx1;
    }

    public float getDy1() {
        return dy1;
    }

    public float getDx2() {
        return dx2;
    }

    public float getDy2() {
        return dy2;
    }

    public void setSize(int width,int height ){
        this.width = width;
        this.height = height;
        updateDiagonal();
        updateCorners();
    }

    public void setWidth(int width) {
        this.width = width;
        updateDiagonal();
        updateCorners();
    }

    public void setHeight(int height) {
        this.height = height;
        updateDiagonal();
        updateCorners();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        updateCorners();
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = (short) textureIndex;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
