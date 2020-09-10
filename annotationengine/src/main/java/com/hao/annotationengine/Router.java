package com.hao.annotationengine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.hao.annotetion.task.BaseSaveInfo;
import com.hao.annotetion.task.BindInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dalvik.system.DexFile;

import static com.hao.annotetion.task.Config.routeClassPath;


public class Router {
    private static Application mContext;
    private static Router router;
    static Map<String, BindInfo> allClass = new HashMap<>();
    private static Activity mActivity;

    private Router() {
    }

    public static Router init(Application application) {
        mContext = application;
        try {
            loadSaveInfo();
        } catch (Exception e) {
            Log.e("初始化错误", "加载已绑定的类信息失败");
        }
        return router;
    }


    public static Router getInstance() {
        if (router == null) {
            synchronized (Router.class) {
                if (router == null) {
                    router = new Router();
                }
            }
        }
        return router;
    }

    /**
     * 加载绑定的类信息
     */
    private static void loadSaveInfo() throws Exception {
        Set<String> strings = getFileNameByPackageName(mContext, routeClassPath);

        for (String str : strings) {
            HashMap<String, BindInfo> map = new HashMap<>();
            ((BaseSaveInfo) Class.forName(str).getConstructor().newInstance()).loadInfo(map);
            allClass.putAll(map);
        }
        for (String key : allClass.keySet()) {
            Log.i("绑定数据为", "key=" + key + "      " + allClass.get(key).getDestination().getName());
        }
    }


    /**
     * 得到路由表的类名
     *
     * @param context
     * @param packageName
     * @return
     * @throws PackageManager.NameNotFoundException
     * @throws InterruptedException
     */
    public static Set<String> getFileNameByPackageName(Application context, final String packageName)
            throws PackageManager.NameNotFoundException {
        final Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);
        for (final String path : paths) {
            DexFile dexFile = null;
            try {
                //加载 apk中的dex 并遍历 获得所有包名为 {packageName} 的类
                dexFile = new DexFile(path);
                Enumeration<String> dexEntries = dexFile.entries();
                while (dexEntries.hasMoreElements()) {
                    String className = dexEntries.nextElement();
                    if (!TextUtils.isEmpty(className) && className.startsWith(packageName)) {
                        classNames.add(className);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != dexFile) {
                    try {
                        dexFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return classNames;
    }


    /**
     * 获得程序所有的apk(instant run会产生很多split apk)
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir);
        //instant run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != applicationInfo.splitSourceDirs) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }
        return sourcePaths;
    }

    public BindDetailInfo build(String path, Bundle bundle) {
        return this.build(path, bundle, null);
    }

    public BindDetailInfo build(String path) {
        return this.build(path, null, null);
    }


    public BindDetailInfo build(String path, Bundle bundle, Activity activity) {
        mActivity = activity;
        return new BindDetailInfo(path, bundle);
    }


    public void skipActivity(BindDetailInfo bindDetailInfo) {
        Intent intent = new Intent(mContext, bindDetailInfo.getDestination());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bindDetailInfo.getBundle() != null) {
            intent.putExtras(bindDetailInfo.getBundle());
            if (bindDetailInfo.getBundle().getBoolean("animal") && mActivity != null) {
                ActivityCompat.startActivity(mActivity, intent, bindDetailInfo.getBundle());
                mActivity = null;
            } else {
                mContext.startActivity(intent);
            }
        } else {
            mContext.startActivity(intent);
        }
    }

    public Class skipClass(BindDetailInfo bindDetailInfo) {
        return bindDetailInfo.getDestination();
    }
}
