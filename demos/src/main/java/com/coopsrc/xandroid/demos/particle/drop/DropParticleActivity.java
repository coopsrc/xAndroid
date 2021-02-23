package com.coopsrc.xandroid.demos.particle.drop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.coopsrc.xandroid.demos.R;
import com.coopsrc.xandroid.demos.particle.base.BaseRenderActivity;
import com.coopsrc.xandroid.particle.texture.TextureAtlas;
import com.coopsrc.xandroid.particle.texture.TextureAtlasFactory;

public class DropParticleActivity extends BaseRenderActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        particleView.setTextureAtlasFactory(textureAtlasFactory);
        particleView.post(new Runnable() {
            @Override
            public void run() {
                int width = particleView.getWidth();
                int height = particleView.getHeight();
                particleView.setParticleSystem(new BeamParticleSystem(width, height));
            }
        });

    }


    private TextureAtlasFactory textureAtlasFactory = new TextureAtlasFactory() {
        @Override
        public TextureAtlas createTextureAtlas() {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.android);
            TextureAtlas atlas = new TextureAtlas(bmp.getWidth(), bmp.getHeight());
            atlas.addRegion(0, 0, bmp);
            return atlas;
        }
    };
}
