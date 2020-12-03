package com.coopsrc.xandroid.demos.particle.drop;

import com.coopsrc.xandroid.particle.Particle;

public class BeamParticle extends Particle {
    float fade;
    float vy;
    float vr;

    void setup(int size, float x, float y, float fade, float vy, float vr) {
        setSize(size * 2, size);
        setX(x);
        setY(y);
        this.fade = fade;
        this.vy = vy;
        this.vr = vr;
    }
}
