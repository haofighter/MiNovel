package com.hao.minovel.jni;

public class DragCrash {
    static {
        System.loadLibrary("crash");
    }

    public  static  native void initNativeCrash(String path);

}
