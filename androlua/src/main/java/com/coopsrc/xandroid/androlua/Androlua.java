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

    public static String getLuaVersion() {
        return _getLuaVersion();
    }

    public static String getLuaRelease() {
        return _getLuaRelease();
    }

    public static String getLuaCopyright() {
        return _getLuaCopyright();
    }

    public static String getLuaAuthors() {
        return _getLuaAuthors();
    }

    private static native String _getLuaVersion();

    private static native String _getLuaRelease();

    private static native String _getLuaCopyright();

    private static native String _getLuaAuthors();
}
