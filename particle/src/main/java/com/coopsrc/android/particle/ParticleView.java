package com.coopsrc.android.particle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ParticleView extends FrameLayout {
    private GlTextureView glTextureView;
    private ParticleRenderer renderer;

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        attachTextureView();
        setupTextureView();
    }

    private void attachTextureView() {
        glTextureView = new GlTextureView(getContext());
        glTextureView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(glTextureView);
    }

    private void setupTextureView() {
        glTextureView.setEGLContextClientVersion(2);
        glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glTextureView.setOpaque(false);
        renderer = new ParticleRenderer();
        glTextureView.setRenderer(renderer);
        glTextureView.setRenderMode(GlTextureView.RENDERMODE_CONTINUOUSLY);
    }

    public void setTextureAtlasFactory(TextureAtlasFactory factory) {
        renderer.setTextureAtlasFactory(factory);
    }

    public void setParticleSystem(ParticleSystem system) {
        renderer.setParticleSystem(system);
    }

    public void startRendering() {
        glTextureView.onResume();
    }

    public void stopRendering() {
        glTextureView.onPause();
    }
}
