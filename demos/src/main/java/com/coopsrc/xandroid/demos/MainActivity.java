package com.coopsrc.xandroid.demos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.coopsrc.xandroid.demos.androlua.AndroluaActivity;
import com.coopsrc.xandroid.demos.blur.BlurActivity;
import com.coopsrc.xandroid.demos.downloader.DownloaderActivity;
import com.coopsrc.xandroid.demos.framesequence.FrameSequenceActivity;
import com.coopsrc.xandroid.demos.particle.ParticleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAndrolua(View view) {
        Intent intent = new Intent(this, AndroluaActivity.class);
        startActivity(intent);
    }

    public void openBlur(View view) {
        Intent intent = new Intent(this, BlurActivity.class);
        startActivity(intent);
    }

    public void openDownloader(View view) {
        Intent intent = new Intent(this, DownloaderActivity.class);
        startActivity(intent);
    }

    public void openParticle(View view) {
        Intent intent = new Intent(this, ParticleActivity.class);
        startActivity(intent);
    }

    public void openFrameSequence(View view) {
        Intent intent = new Intent(this, FrameSequenceActivity.class);
        startActivity(intent);
    }
}
