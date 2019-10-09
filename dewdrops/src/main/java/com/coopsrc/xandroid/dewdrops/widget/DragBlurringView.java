package com.coopsrc.xandroid.dewdrops.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.coopsrc.xandroid.dewdrops.DewdropsBlur;
import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.processor.IBlurProcessor;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-09 10:51
 */
public class DragBlurringView extends View {

    private static final int SCALE_RESAMPLE = 4;

    private float oldX;
    private float oldY;

    private View blurredView;

    private Bitmap originalBitmap;
    private Bitmap blurredBitmap;
    private Canvas blurringCanvas;

    private IBlurProcessor blurProcessor;

    public DragBlurringView(Context context) {
        super(context);
        init();
    }

    public DragBlurringView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragBlurringView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        blurProcessor = DewdropsBlur.with(getContext())
                .scheme(BlurConfig.SCHEME_NATIVE)
                .mode(BlurConfig.MODE_GAUSSIAN)
                .radius(4)
                .sampleFactor(1.0f)
                .build();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (blurredView != null && blurProcessor != null) {
            if (prepare()) {
                if (blurredView.getBackground() != null && blurredView.getBackground() instanceof ColorDrawable) {
                    originalBitmap.eraseColor(((ColorDrawable) blurredView.getBackground()).getColor());
                } else {
                    originalBitmap.eraseColor(Color.TRANSPARENT);
                }

                blurredView.draw(blurringCanvas);
                blurredBitmap = blurProcessor.blur(originalBitmap);

                canvas.save();
                canvas.translate(blurredView.getX() - getX(), blurredView.getY() - getY());
                canvas.scale(SCALE_RESAMPLE, SCALE_RESAMPLE);
                canvas.drawBitmap(blurredBitmap, 0, 0, null);
                canvas.restore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getRawX();
                oldY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - oldX;
                float dy = event.getRawY() - oldY;
                offsetLeftAndRight((int) dx);
                offsetTopAndBottom((int) dy);
                oldX = event.getRawX();
                oldY = event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setBlurredView(View blurredView) {
        this.blurredView = blurredView;
    }

    public void setBlurProcessor(IBlurProcessor blurProcessor) {
        this.blurProcessor = blurProcessor;
    }

    private boolean prepare() {

        final int width = blurredView.getWidth();
        final int height = blurredView.getHeight();

        if (blurringCanvas == null) {

            int scaledWidth = width / SCALE_RESAMPLE;
            int scaleHeight = height / SCALE_RESAMPLE;

            if (originalBitmap == null) {
                originalBitmap = Bitmap.createBitmap(scaledWidth, scaleHeight, Bitmap.Config.ARGB_8888);
            }

            if (originalBitmap == null) {
                return false;
            }

            blurringCanvas = new Canvas(originalBitmap);
            blurringCanvas.scale(1.0f / SCALE_RESAMPLE, 1.0f / SCALE_RESAMPLE);
        }

        return true;
    }
}
