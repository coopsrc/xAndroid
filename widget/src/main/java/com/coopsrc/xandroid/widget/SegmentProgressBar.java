/*
 * Copyright (C) 2019 Zhang Tingkuo(zhangtingkuo@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coopsrc.xandroid.widget;

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
    private static final long PREFIX_MASK = 0x1000000000L;
    private static final long SEGMENT_INDEX_MASK = 0x1F00000000L;
    private static final long SEGMENT_SIZE_MASK = 0x10FFFFFFFFL;

    private static final long DEFAULT_SEGMENT_SIZE = 0xFFFFFFFFL; // 4G
    private static final int DEFAULT_SEGMENT_COUNT = 0x10; // 16
    private static final int MAX_INDEX_BIT_COUNT = 4;
    private static final int MIN_DATA_BIT_COUNT = 32;
    private static final long MAX_PROGRESS = 0xFFFFFFFFFL;// 64G
    private static final long MAX_SEGMENTS = 0xFFFFFFFFFL;// 64G
    private static final int MAX_SEGMENT_COUNT = 0x10;// 16

    private TextPaint mTextPaint;
    private float mTextHeight;

    private Paint mSegmentPaint;
    private Paint mProgressPaint;
    private Paint mDividerPaint;

    private long mSegmentIndexMask = SEGMENT_INDEX_MASK;
    private long mSegmentSizeMask = SEGMENT_SIZE_MASK;
    private int mSegmentCount = DEFAULT_SEGMENT_COUNT;
    private long mSegmentSize = DEFAULT_SEGMENT_SIZE;
    private int mIndexBitCount = MAX_INDEX_BIT_COUNT;
    private int mDataBitCount = MIN_DATA_BIT_COUNT;
    private SparseLongArray mSegments = new SparseLongArray(mSegmentCount);

    public static final int FIXED_COUNT = 0;
    public static final int FIXED_SIZE = 1;

    @IntDef({FIXED_COUNT, FIXED_SIZE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SegmentMode {

    }

    @SegmentMode
    private int mSegmentMode = FIXED_COUNT;

    private long mMax = 0L;// 64G
    private long mProgress = 0L;

    private static final int TEXT_ALIGN_START = 0;
    private static final int TEXT_ALIGN_END = 1;


    private static final String DEFAULT_PERCENT_FORMAT = "%.2f";
    private static final String DEFAULT_PROGRESS_FORMAT = "%s/%s";
    private static final String DEFAULT_SIZE_FORMAT = "0.0";

    private String mPercentFormat = DEFAULT_PERCENT_FORMAT;
    private String mProgressFormat = DEFAULT_PROGRESS_FORMAT;
    private String mSizeFormat = DEFAULT_SIZE_FORMAT;
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
        mSegmentPaint.setStrokeWidth(2);

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

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        boolean invalid = mMax > 0 && mMax <= MAX_PROGRESS;

        // draw total progress
        if (mShowProgress && invalid) {
            float progressLength = getWidth() * mProgress / (mMax * 1.0f);
            canvas.drawRect(0, 0, progressLength, getHeight(), mProgressPaint);
        }

        int segmentWidth = getWidth() / mSegmentCount;
        // draw segments
        if (invalid) {
            for (int i = 0; i <= mSegmentCount; i++) {
                int segmentLeft = segmentWidth * i;
                float segmentProgress = mSegments.get(i) / (mSegmentSize * 1.0f);
                LogUtils.d("onDraw: index: %s[%s] --> %s", i, mSegments.get(i), segmentProgress);
                int segmentSize = (int) (segmentWidth * segmentProgress);
                canvas.drawRect(segmentLeft, 0, segmentLeft + segmentSize, getHeight(), mSegmentPaint);
            }
        }

        // draw segment split line.
        if (invalid) {
            for (int i = 0; i < mSegmentCount; i++) {
                canvas.drawLine(segmentWidth * i, 0, segmentWidth * i, getHeight(), mDividerPaint);
            }
        }

        // Draw the text.

        if (mShowPercentText && invalid) {
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

        if (mShowProgressText && invalid) {
            String progressText = String.format(Locale.getDefault(), mProgressFormat,
                    MemoryUnit.format(mProgress, mSizeFormat), MemoryUnit.format(mMax, mSizeFormat));
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

    public void setFixedSegments(long max, @SegmentMode int segmentMode, long value) {

        if (max > MAX_PROGRESS) {
            throw new IllegalStateException("Unsupported progress length, 0xFFFFFFFFFL max.");
        }

        if (value <= 0) {
            throw new IllegalStateException("Illegal value.");
        }

        switch (segmentMode) {
            case FIXED_SIZE:
                setFixedSizeSegments(max, value);
                break;
            case FIXED_COUNT:
                setFixedCountSegments(max, (int) value);
                break;
        }
    }

    public void setFixedSizeSegments(long max, long segmentSize) {
        // TODO: 2019/11/26
//        mSegmentMode = FIXED_SIZE;
//        mMax = max;
//
//        if (segmentSize > MAX_SEGMENTS) {
//            throw new IllegalStateException("Unsupported segment length, 0xFFFFFFFFFL max.");
//        }
//
//        int segmentCount = (int) Math.ceil((double) mMax / segmentSize);
//
//        if (segmentCount > MAX_SEGMENT_COUNT) {
//            mSegmentCount = MAX_SEGMENT_COUNT;
//
//            mSegmentSize = (int) Math.ceil((double) mMax / mSegmentCount);
//        } else {
//            mSegmentCount = segmentCount;
//
//            mSegmentSize = segmentSize;
//        }
    }

    public void setFixedCountSegments(long max, int segmentCount) {
        mSegmentMode = FIXED_COUNT;
        mMax = max;

        //data type scale not a power of two
        // (segmentCount & -segmentCount) != segmentCount
        // (segmentCount & (segmentCount - 1)) != 0
        if ((segmentCount & -segmentCount) != segmentCount) {
            throw new IllegalStateException("Only the power of 2 is supported");
        }

        if (segmentCount > MAX_SEGMENT_COUNT) {
            mSegmentCount = MAX_SEGMENT_COUNT;
        } else {
            mSegmentCount = segmentCount;
        }

        mSegmentSize = mMax / mSegmentCount;

        mSegments.clear();
        mSegments = new SparseLongArray(mSegmentCount);

        mIndexBitCount = (int) Math.log(segmentCount);
        mDataBitCount = MIN_DATA_BIT_COUNT + (MAX_INDEX_BIT_COUNT - mIndexBitCount);

        mSegmentIndexMask = PREFIX_MASK & ((2 << mIndexBitCount) - 1) << mDataBitCount;
        mSegmentSizeMask = PREFIX_MASK & (2 << mDataBitCount) - 1;

        LogUtils.w("setFixedCountSegments: [%s, %s] \r\nSegmentIndexMask: %s\r\nSegmentSizeMask: %s", max, segmentCount,
                Long.toBinaryString(mSegmentIndexMask),
                Long.toBinaryString(mSegmentSizeMask)
        );

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
