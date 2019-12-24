package com.coopsrc.android.particle.widget;

import android.content.Context;
import android.util.AttributeSet;

public class GLTextureParticleView extends BaseParticleView {
    private GLTextureView glTextureView;

    public GLTextureParticleView(Context context) {
        super(context);
    }

    public GLTextureParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GLTextureParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void attachDisplayView() {
        glTextureView = new GLTextureView(getContext());
        glTextureView.setLayoutParams(getChildLayoutParams());
        addView(glTextureView);
    }

    @Override
    protected void setupDisplayView() {
        glTextureView.setEGLContextClientVersion(2);
        glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glTextureView.setOpaque(false);
        glTextureView.setRenderer(getRenderer());
        glTextureView.setRenderMode(GLTextureView.RENDERMODE_CONTINUOUSLY);
    }

    public void startRendering() {
        glTextureView.onResume();
    }

    public void stopRendering() {
        glTextureView.onPause();
    }
}
