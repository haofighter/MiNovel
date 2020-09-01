package com.hao.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * 动态换肤原理
 * 源代码中每个布局文件都是由LayoutInflater的工厂加载而来
 */
public class SkinAppActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    Observable observable;
    private ArrayMap<Activity, SkinLayoutInflaiterFactory> mLayoutInflaterFactories = new
            ArrayMap<>();

    public SkinAppActivityLifecycleCallbacks(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        SkinLayoutInflaiterFactory skinLayoutInflaiterFactory = new SkinLayoutInflaiterFactory(activity);
        /**
         *  更新布局视图
         */
        //获得Activity的布局加载器
        try {

            Field mFactory2 = LayoutInflater.class.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
            mFactory2.set(layoutInflater, skinLayoutInflaiterFactory);
        } catch (Exception e) {
            Log.i("日志", "报错了：" + e.getMessage());
        }
        mLayoutInflaterFactories.put(activity, skinLayoutInflaiterFactory);
        observable.addObserver(skinLayoutInflaiterFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaiterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }
}
