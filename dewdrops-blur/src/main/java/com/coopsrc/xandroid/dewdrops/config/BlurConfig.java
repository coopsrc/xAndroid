package com.coopsrc.xandroid.dewdrops.config;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-08 19:23
 */
public class BlurConfig {
    public static final int DIRECTION_FULL = 0;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;

    public static final int MODE_GAUSSIAN = 1;
    public static final int MODE_STACK = 2;
    public static final int MODE_BOX = 3;

    public static final int SCHEME_JAVA = 1;
    public static final int SCHEME_NATIVE = 2;
    public static final int SCHEME_RENDER_SCRIPT = 3;
    public static final int SCHEME_OPEN_GL = 4;

    public static final int DEFAULT_RADIUS = 5;
    public static final int RS_MAX_RADIUS = 25;
    public static final float DEFAULT_SAMPLE_FACTOR = 4.0f;
    public static final int DEFAULT_BLUR_SCALE = 4;
    public static final int MAX_BLUR_SCALE = 5;
}
