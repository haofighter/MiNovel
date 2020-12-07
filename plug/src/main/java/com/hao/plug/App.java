package com.hao.plug;

import android.app.Application;
import android.os.Environment;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LoadUtils.loadClassLoad(this, "/data/data/com.hao.plug/cache/app-debug.apk");
        LoadUtil.loadClass(this);
    }
}
