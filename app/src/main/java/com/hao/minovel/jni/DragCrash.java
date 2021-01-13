package com.hao.minovel.jni;

public class DragCrash {
    static {
        System.loadLibrary("crash");
    }

    public static native void initNativeCrash(String path);


    public static native String getString();

    public static native String getString2();

    public static native String initDate(Test test);

}
