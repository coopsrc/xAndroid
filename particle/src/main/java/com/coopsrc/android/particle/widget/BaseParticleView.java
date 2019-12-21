package com.coopsrc.android.particle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.coopsrc.android.particle.ParticleRenderer;
import com.coopsrc.android.particle.ParticleSystem;
import com.coopsrc.android.particle.texture.TextureAtlasFactory;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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

    protected LayoutParams getChildLayoutParams() {
        LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }
}
