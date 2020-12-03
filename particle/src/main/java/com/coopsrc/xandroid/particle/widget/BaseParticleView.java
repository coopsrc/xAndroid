package com.coopsrc.xandroid.particle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.coopsrc.xandroid.particle.ParticleSystem;
import com.coopsrc.xandroid.particle.render.BaseParticleRenderer;
import com.coopsrc.xandroid.particle.render.ParticleRenderer;
import com.coopsrc.xandroid.particle.texture.TextureAtlasFactory;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class BaseParticleView extends FrameLayout {

    private BaseParticleRenderer renderer;

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

    protected void initParticleRenderer() {
        setRenderer(new ParticleRenderer());
    }

    protected abstract void setupDisplayView();

    protected final void setRenderer(BaseParticleRenderer renderer) {
        this.renderer = renderer;
    }

    protected final BaseParticleRenderer getRenderer() {
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
