package com.coopsrc.android.particle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class BaseParticleView extends FrameLayout {

    private ParticleRenderer renderer;

    public BaseParticleView(Context context) {
        this(context, null);
    }

    public BaseParticleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        attachDisplayView();
        initParticleRenderer();
        setupDisplayView();
    }

    protected abstract void attachDisplayView();

    private void initParticleRenderer() {
        renderer = new ParticleRenderer();
    }

    protected abstract void setupDisplayView();

    protected final ParticleRenderer getRenderer() {
        return renderer;
    }

    public void setTextureAtlasFactory(TextureAtlasFactory factory) {
        renderer.setTextureAtlasFactory(factory);
    }

    public void setParticleSystem(ParticleSystem system) {
        renderer.setParticleSystem(system);
    }

    public abstract void startRendering();

    public abstract void stopRendering();
}
