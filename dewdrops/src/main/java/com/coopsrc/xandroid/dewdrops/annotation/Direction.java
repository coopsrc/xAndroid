package com.coopsrc.xandroid.dewdrops.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.DIRECTION_FULL;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.DIRECTION_HORIZONTAL;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.DIRECTION_VERTICAL;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 17:53
 */
@IntDef({DIRECTION_FULL, DIRECTION_HORIZONTAL, DIRECTION_VERTICAL})
@Retention(RetentionPolicy.SOURCE)
public @interface Direction {
}
