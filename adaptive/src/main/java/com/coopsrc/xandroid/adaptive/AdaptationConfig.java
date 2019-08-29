package com.coopsrc.xandroid.adaptive;

import android.content.res.Configuration;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-08-29 10:23
 */
public class AdaptationConfig {

    public static final int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    public static final int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;

    @Documented
    @IntDef({PORTRAIT, LANDSCAPE})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    @interface AdapterOrientation {

    }

    @AdapterOrientation
    private int designOrientation = PORTRAIT;

    private int designWidth = 1080;
    private int designHeight = 1920;

    private int designDensity = 3;

    private AdaptationConfig() {
    }

    @AdapterOrientation
    public int getDesignOrientation() {
        return designOrientation;
    }

    public void setDesignOrientation(@AdapterOrientation int designOrientation) {
        this.designOrientation = designOrientation;
    }

    public int getDesignWidth() {
        return designWidth;
    }

    public void setDesignWidth(int designWidth) {
        this.designWidth = designWidth;
    }

    public int getDesignHeight() {
        return designHeight;
    }

    public void setDesignHeight(int designHeight) {
        this.designHeight = designHeight;
    }

    public int getDesignDensity() {
        return designDensity;
    }

    public void setDesignDensity(int designDensity) {
        this.designDensity = designDensity;
    }

    public static final class Builder {
        private AdaptationConfig mConfig = new AdaptationConfig();

        public Builder designOrientation(@AdapterOrientation int designOrientation) {
            mConfig.designOrientation = designOrientation;

            return this;
        }

        public Builder designWidth(int designWidth) {
            mConfig.designWidth = designWidth;

            return this;
        }

        public Builder designHeight(int designHeight) {
            mConfig.designHeight = designHeight;

            return this;
        }

        public Builder designDensity(int designDensity) {
            mConfig.designDensity = designDensity;

            return this;
        }

        public AdaptationConfig build() {
            return mConfig;
        }
    }
}
