package com.coopsrc.android.particle.render;

import android.opengl.GLSurfaceView;

import com.coopsrc.android.particle.ParticleSystem;
import com.coopsrc.android.particle.texture.TextureAtlasFactory;

public abstract class BaseParticleRenderer implements GLSurfaceView.Renderer {
    protected volatile ParticleSystem particleSystem;
    protected volatile boolean particleSystemNeedsSetup;

    protected volatile TextureAtlasFactory textureAtlasFactory;
    protected volatile boolean textureAtlasNeedsSetup;

    public void setTextureAtlasFactory(TextureAtlasFactory factory) {
        this.textureAtlasFactory = factory;
        textureAtlasNeedsSetup = true;
    }

    public void setParticleSystem(ParticleSystem system) {
        this.particleSystem = system;
        particleSystemNeedsSetup = true;
    }
}
