package com.coopsrc.xandroid.dewdrops.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.MODE_GAUSSIAN;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.MODE_BOX;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.MODE_STACK;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 17:54
 */
@IntDef({MODE_GAUSSIAN, MODE_BOX, MODE_STACK})
@Retention(RetentionPolicy.SOURCE)
public @interface Mode {
}
