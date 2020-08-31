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
        /**
         *  更新布局视图
         */
        //获得Activity的布局加载器
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            try {
                //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
                //如设置过抛出异常
                //设置 mFactorySet 标签为false
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(layoutInflater, false);

                SkinLayoutInflaiterFactory skinLayoutInflaiterFactory = new SkinLayoutInflaiterFactory(activity);
                LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaiterFactory);
                mLayoutInflaterFactories.put(activity, skinLayoutInflaiterFactory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//
//        observable.addObserver(skinLayoutInflaiterFactory);
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
