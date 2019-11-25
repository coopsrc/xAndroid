package com.coopsrc.xandroid.androlua;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-11-23 15:35
 */
public class Androlua {

    static {
        System.loadLibrary("androlua");
    }

    public static native String getLuaVersion();

    public static native String getLuaRelease();

    public static native String getLuaCopyright();

    public static native String getLuaAuthors();
}
