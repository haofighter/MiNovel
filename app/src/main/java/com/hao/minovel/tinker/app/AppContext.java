package com.hao.minovel.tinker.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.hao.minovel.log.MiLog;
import com.hao.skin.SkinManager;

import java.util.HashSet;

import static android.os.Environment.DIRECTORY_PODCASTS;

public class AppContext {
    public static Application application = null;
    public static HashSet<Activity> activitys = new HashSet();
    private static long lastClickTime = 0;//最后一次的点击时间

    public static boolean addActivity(Activity activity) {
        return activitys.add(activity);
    }

    public static boolean removeActivity(Activity activity) {
        return activitys.remove(activity);
    }

    public static void finishAll() {
        for (Activity a : activitys) {
            try {
                if (a != null) {
                    a.finish();
                }
            } catch (Exception e) {
                MiLog.i("错误  Activity err" + a.getLocalClassName() + "  " + e.getMessage());
            }
        }
    }


    public void loadSkin() {
        String path = "";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            path = Environment.getExternalStorageState() + "/resource-debug.apk";
        } else {
            path = application.getExternalFilesDir(DIRECTORY_PODCASTS) + "/resource-debug.apk";
        }
        Log.i("path", path);
        SkinManager.getInstance().loadSkin(path);
    }

    //判断是否重复点击
    public static boolean checkedDoubleClick() {
        if (System.currentTimeMillis() - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }
}
