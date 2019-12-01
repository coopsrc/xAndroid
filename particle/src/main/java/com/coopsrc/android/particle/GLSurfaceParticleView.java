package com.coopsrc.android.particle;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class GLSurfaceParticleView extends BaseParticleView {
    private GLSurfaceView glSurfaceView;

    public GLSurfaceParticleView(Context context) {
        super(context);
    }

    public GLSurfaceParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GLSurfaceParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void attachDisplayView() {
        glSurfaceView = new GLSurfaceView(getContext());
        glSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(glSurfaceView);
    }

    @Override
    protected void setupDisplayView() {
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        glSurfaceView.setZOrderOnTop(true);
        glSurfaceView.setRenderer(getRenderer());
        glSurfaceView.setRenderMode(GLTextureView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void startRendering() {
        glSurfaceView.onResume();
    }

    @Override
    public void stopRendering() {
        glSurfaceView.onPause();
    }
}
