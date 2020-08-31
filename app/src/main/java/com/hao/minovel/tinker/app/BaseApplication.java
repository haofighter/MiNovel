package com.hao.minovel.tinker.app;

import android.app.Application;

import com.hao.annotationengine.Router;
import com.hao.skin.SkinManager;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);
        SkinManager.init(this);
    }
}
