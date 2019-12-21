package com.coopsrc.xandroid.demos.particle.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coopsrc.android.particle.widget.BaseParticleView;
import com.coopsrc.xandroid.demos.R;

public abstract class BaseRenderActivity extends AppCompatActivity {

    protected BaseParticleView particleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particle_render);
        particleView = findViewById(R.id.particle_view);
    }


    @Override
    protected void onResume() {
        super.onResume();
        particleView.startRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        particleView.stopRendering();
    }
}
