package com.coopsrc.xandroid.demos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.coopsrc.xandroid.demos.androlua.AndroluaActivity;
import com.coopsrc.xandroid.demos.blur.BlurActivity;
import com.coopsrc.xandroid.demos.downloader.DownloaderActivity;
import com.coopsrc.xandroid.demos.framesequence.FrameSequenceActivity;
import com.coopsrc.xandroid.demos.particle.ParticleActivity;
import com.coopsrc.xandroid.utils.LogUtils;
import com.coopsrc.xandroid.utils.logger.DebugLogger;
import com.coopsrc.xandroid.utils.logger.Logger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testLogUtils();
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

    public final void testLogUtils() {
        Exception exception = new Exception("Test Exception");
        LogUtils.v("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.d("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.i("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.tag("AAAA").i("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty().w("testLogUtils: [%d, %d] kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", 1920, 1080);
        LogUtils.e((Throwable) exception);
        LogUtils.e("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty().e((Throwable) exception, "testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.wtf("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty().w("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty("Particle").w("testLogUtils: [%d, %d]", 1920, 1080);
//

        LogUtils.i("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.tag("Particle").i("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty().i("testLogUtils: [%d, %d]", 1920, 1080);
        LogUtils.pretty("Particle").i("testLogUtils: [%d, %d]", 1920, 1080);
    }
}
