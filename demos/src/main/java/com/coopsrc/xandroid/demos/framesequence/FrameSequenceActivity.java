package com.coopsrc.xandroid.demos.framesequence;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.coopsrc.xandroid.demos.R;

import java.io.InputStream;
import java.util.HashSet;

public class FrameSequenceActivity extends AppCompatActivity {

    private FrameSequenceDrawable frameSequenceDrawable;
    private int mGifResourceId = R.raw.banner_gif;
    private int mWebpResourceId = R.raw.banner_webp;
    private int mResourceId;

    // This provider is entirely unnecessary, just here to validate the acquire/release process
    private class CheckingProvider implements FrameSequenceDrawable.BitmapProvider {
        HashSet<Bitmap> mBitmaps = new HashSet<>();

        @Override
        public Bitmap acquireBitmap(int minWidth, int minHeight) {
            Bitmap bitmap = Bitmap.createBitmap(minWidth + 1, minHeight + 4, Bitmap.Config.ARGB_8888);
            mBitmaps.add(bitmap);
            return bitmap;
        }

        @Override
        public void releaseBitmap(Bitmap bitmap) {
            if (!mBitmaps.contains(bitmap)) {
                throw new IllegalStateException();
            }
            mBitmaps.remove(bitmap);
            bitmap.recycle();
        }

        public boolean isEmpty() {
            return mBitmaps.isEmpty();
        }
    }

    final CheckingProvider mProvider = new CheckingProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_sequence);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mResourceId = mWebpResourceId;

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSequenceDrawable.start();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSequenceDrawable.stop();
            }
        });
        findViewById(R.id.vis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSequenceDrawable.setVisible(true, true);
            }
        });
        findViewById(R.id.invis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSequenceDrawable.setVisible(false, true);
            }
        });
        findViewById(R.id.circle_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSequenceDrawable.setCircleMaskEnabled(!frameSequenceDrawable.getCircleMaskEnabled());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        View drawableView = findViewById(R.id.drawableview);
        InputStream is = getResources().openRawResource(mResourceId);

        FrameSequence fs = FrameSequence.decodeStream(is);
        frameSequenceDrawable = new FrameSequenceDrawable(fs, mProvider);
        frameSequenceDrawable.setOnFinishedListener(new FrameSequenceDrawable.OnFinishedListener() {
            @Override
            public void onFinished(FrameSequenceDrawable drawable) {
                Toast.makeText(getApplicationContext(),
                        "The animation has finished", Toast.LENGTH_SHORT).show();
            }
        });
        drawableView.setBackground(frameSequenceDrawable);
    }

    @Override
    protected void onPause() {
        super.onPause();

        View drawableView = findViewById(R.id.drawableview);

        frameSequenceDrawable.destroy();
        if (!mProvider.isEmpty()) {
            throw new IllegalStateException("All bitmaps not recycled");
        }

        frameSequenceDrawable = null;
        drawableView.setBackground(null);
    }
}
