package com.coopsrc.cicada.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseLongArray;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.IntDef;

import com.coopsrc.xandroid.utils.LogUtils;
import com.coopsrc.xandroid.utils.MemoryUnit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-20 20:07
 */
public class SegmentProgressBar extends View {

    /*
     * 0x0FFFFFFFF ~ 0xFFFFFFFFF
     * 0x_index_size 1_8
     */
    private static final long SEGMENT_INDEX_MASK = 0xF00000000L;
    private static final long SEGMENT_SIZE_MASK = 0x0FFFFFFFFL;
    private static final long DEFAULT_SEGMENT_SIZE = 0xFFFFFFFFL; // 4G
    private static final int DEFAULT_SEGMENT_COUNT = 0x10; // 16
    private static final int INDEX_BIT_COUNT = 4;
    private static final int DATA_BIT_COUNT = 32;
    private static final long MAX_PROGRESS = 0xFFFFFFFFFL;// 64G

    private TextPaint mTextPaint;
    private float mTextHeight;

    private Paint mSegmentPaint;
    private Paint mProgressPaint;
    private Paint mDividerPaint;

    private long mSegmentIndexMask = SEGMENT_INDEX_MASK;
    private long mSegmentSizeMask = SEGMENT_SIZE_MASK;
    private int mSegmentCount = DEFAULT_SEGMENT_COUNT;
    private long mSegmentSize = DEFAULT_SEGMENT_SIZE;
    private int mIndexBitCount = INDEX_BIT_COUNT;
    private int mDataBitCount = DATA_BIT_COUNT;
    private SparseLongArray mSegments = new SparseLongArray(mSegmentCount);

    public static final int MODE_COMBINED = 0;
    public static final int MODE_SPLITTED = 1;
    public static final int MODE_ALIGNED = 2;

    @IntDef({MODE_COMBINED, MODE_SPLITTED, MODE_ALIGNED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SegmentMode {

    }

    @SegmentMode
    private int segmentMode = MODE_COMBINED;

    private long mMax = MAX_PROGRESS;// 64G
    private long mProgress = Long.MIN_VALUE;

    private static final int TEXT_ALIGN_START = 0;
    private static final int TEXT_ALIGN_END = 1;


    private static final String DEFAULT_PERCENT_FORMAT = "%.2f";
    private static final String DEFAULT_PROGRESS_FORMAT = "%s/%s";

    private String mPercentFormat = DEFAULT_PERCENT_FORMAT;
    private String mProgressFormat = DEFAULT_PROGRESS_FORMAT;
    private int mTextColor = Color.BLACK;
    private int mSegmentColor = Color.GREEN;
    private int mProgressColor = Color.DKGRAY;
    private int mDividerColor = Color.RED;
    private float mTextSize = 24;
    private boolean mShowPercentText = true;
    private boolean mShowProgressText = true;
    private boolean mShowProgress = false;
    private int mPercentTextAlign = Gravity.START;

    public SegmentProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public SegmentProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SegmentProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SegmentProgressBar, defStyle, 0);

        mPercentFormat = a.getString(
                R.styleable.SegmentProgressBar_percentFormat);
        if (TextUtils.isEmpty(mPercentFormat)) {
            mPercentFormat = DEFAULT_PERCENT_FORMAT;
        }
        mProgressFormat = a.getString(
                R.styleable.SegmentProgressBar_progressFormat);
        if (TextUtils.isEmpty(mProgressFormat)) {
            mProgressFormat = DEFAULT_PROGRESS_FORMAT;
        }
        mTextColor = a.getColor(
                R.styleable.SegmentProgressBar_textColor,
                mTextColor);
        mDividerColor = a.getColor(
                R.styleable.SegmentProgressBar_dividerColor,
                mTextColor);
        mSegmentColor = a.getColor(
                R.styleable.SegmentProgressBar_segmentColor,
                mTextColor);
        mProgressColor = a.getColor(
                R.styleable.SegmentProgressBar_progressColor,
                mTextColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mTextSize = a.getDimension(
                R.styleable.SegmentProgressBar_textSize,
                mTextSize);
        mShowPercentText = a.getBoolean(
                R.styleable.SegmentProgressBar_showPercentText,
                mShowPercentText
        );
        mShowProgressText = a.getBoolean(
                R.styleable.SegmentProgressBar_showProgressText,
                mShowProgressText
        );
        mShowProgress = a.getBoolean(
                R.styleable.SegmentProgressBar_showProgress,
                mShowProgress
        );
        int percentTextAlign = a.getInteger(
                R.styleable.SegmentProgressBar_percentTextAlign,
                TEXT_ALIGN_START
        );
        if (percentTextAlign == TEXT_ALIGN_START) {
            mPercentTextAlign = Gravity.START;
        } else {
            mPercentTextAlign = Gravity.END;
        }

        a.recycle();
        mSegmentPaint = new Paint();
        mSegmentPaint.setColor(mSegmentColor);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(mDividerColor);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // draw total progress
        if (mShowProgress) {
            float progressLength = getWidth() * mProgress / (mMax * 1.0f);
            canvas.drawRect(0, 0, progressLength, getHeight(), mProgressPaint);
        }

        // draw segments
        int segmentWidth = getWidth() / mSegmentCount;
        for (int i = 0; i < mSegmentCount; i++) {
            int segmentLeft = segmentWidth * i;
            float segmentProgress = mSegments.get(i) / (mSegmentSize * 1.0f);
            LogUtils.d("onDraw: index: %s[%s] --> %s", i, mSegments.get(i), segmentProgress);
            int segmentSize = (int) (segmentWidth * segmentProgress);
            canvas.drawRect(segmentLeft, 0, segmentLeft + segmentSize, getHeight(), mSegmentPaint);
        }

        // draw segment split line.
        for (int i = 0; i < mSegmentCount; i++) {
            canvas.drawLine(segmentWidth * i, 0, segmentWidth * i, getHeight(), mDividerPaint);
        }

        // Draw the text.

        if (mShowPercentText) {
            String percent = String.format(Locale.getDefault(), mPercentFormat, mProgress * 100 / (mMax * 1.0f)) + "%";
            float percentWidth = mTextPaint.measureText(percent);
            if (mPercentTextAlign == Gravity.START) {
                canvas.drawText(percent,
                        paddingLeft,
                        paddingTop + (contentHeight + mTextHeight) / 2 + mTextSize / 2,
                        mTextPaint);
            } else {
                canvas.drawText(percent,
                        paddingLeft + contentWidth - percentWidth,
                        paddingTop + (contentHeight + mTextHeight) / 2 + mTextSize / 2,
                        mTextPaint);
            }
        }

        if (mShowProgressText) {
            String progressText = String.format(Locale.getDefault(), mProgressFormat,
                    MemoryUnit.format(mProgress), MemoryUnit.format(mMax));
            float progressWidth = mTextPaint.measureText(progressText);
            canvas.drawText(progressText,
                    paddingLeft + (contentWidth - progressWidth) / 2,
                    paddingTop + (contentHeight + mTextHeight) / 2 + mTextSize / 2,
                    mTextPaint);
        }

    }

    public String getProgressFormat() {
        return mProgressFormat;
    }

    public void setProgressFormat(String progressFormat) {
        mProgressFormat = progressFormat;
        invalidateTextPaintAndMeasurements();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidateTextPaintAndMeasurements();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    public SparseLongArray getSegments() {
        return mSegments;
    }

    public void setSegmentCount(int segmentCount) {
        if (segmentCount <= 0 || segmentCount > DEFAULT_SEGMENT_COUNT) {
            return;
        }

        //data type scale not a power of two
        // (segmentCount & -segmentCount) != segmentCount
        // (segmentCount & (segmentCount - 1)) != 0
        if ((segmentCount & -segmentCount) != segmentCount) {
            return;
        }

        mSegmentCount = segmentCount;
        mSegments.clear();
        mSegments = new SparseLongArray(mSegmentCount);

        mMax = mSegmentCount * mSegmentSize;

        invalidate();
    }

    public void setSegment(long segment) {
        int index = (int) ((segment & mSegmentIndexMask) >> mDataBitCount);
        long size = segment & mSegmentSizeMask;

        LogUtils.v("setSegment:[%s] %s --> %s --> %s M", segment, index, size, MemoryUnit.BYTE.toMegaByte(size));

        setSegment(index, size);
    }

    public void setSegment(int index, long size) {
        LogUtils.i("setSegment: %s --> %s / %s", index, size, mSegmentSize);
        if (index < 0 || index >= mSegmentCount) {
            return;
        }

        if (size <= 0) {
            size = 0;
        }

        if (size >= mSegmentSize) {
            size = mSegmentSize;
        }

        mSegments.put(index, size);

        mProgress = calculateProgress();

        invalidate();
    }

    private long calculateProgress() {
        long progress = 0L;

        for (int i = 0; i < mSegmentCount; i++) {
            progress += mSegments.get(i);
        }

        return progress;
    }
}
