package com.coopsrc.xandroid.dewdrops.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.SCHEME_NATIVE;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.SCHEME_OPEN_GL;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.SCHEME_JAVA;
import static com.coopsrc.xandroid.dewdrops.config.BlurConfig.SCHEME_RENDER_SCRIPT;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 17:54
 */
@IntDef({SCHEME_JAVA, SCHEME_RENDER_SCRIPT, SCHEME_NATIVE, SCHEME_OPEN_GL})
@Retention(RetentionPolicy.SOURCE)
public @interface Scheme {
}
