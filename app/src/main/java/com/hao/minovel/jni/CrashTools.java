package com.hao.minovel.jni;

import android.content.Context;

import java.io.File;

public class CrashTools {

    public static void init(Context context) {
        File file = new File(context.getExternalCacheDir(), "native_crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        DragCrash.initNativeCrash(file.getAbsolutePath());
    }
}
