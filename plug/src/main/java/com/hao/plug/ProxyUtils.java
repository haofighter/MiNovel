package com.hao.plug;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyUtils {
    private final static String proxyIntent = "proxyIntent";

    public static void proxyStartActtivity(Context context) {
/**
 * Activity的跳转
 * startActivity
 * ->startActivityForResult
 * ->mInstrumentation.execStartActivity
 * ->ActivityTaskManager.getService().startActivity
 *
 * ActivityTaskManager.getService()是一个实现了IActivityTaskManager的静态对象
 * 可以使用动态代理及反射 将这个对象替换为代理对象
 */
        String activityManagerName = "android.app.ActivityManager";
        String iActivityManagerSingletonName = "IActivityManagerSingleton";
        String singletonName = "android.util.Singleton";
        String singletonInstanceName = "mInstance";
        String IActivityManagerName = "android.app.IActivityManager";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            activityManagerName = "android.app.ActivityManager";
            iActivityManagerSingletonName = "IActivityManagerSingleton";
            IActivityManagerName = "android.app.IActivityManager";
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            activityManagerName = "android.app.ActivityTaskManager";
            iActivityManagerSingletonName = "IActivityTaskManagerSingleton";
            IActivityManagerName = "android.app.IActivityTaskManager";
        }
        try {
            Class ActivityTaskManager = Class.forName(activityManagerName);
            Field IActivityTaskManagerSingleton = ActivityTaskManager.getDeclaredField(iActivityManagerSingletonName);
            IActivityTaskManagerSingleton.setAccessible(true);
            //获取到系统中的单例对象
            Object singleton = IActivityTaskManagerSingleton.get(null);

            //从单例中获取到IActivityTaskManager对象
            Class Singleton = Class.forName(singletonName);
            Field IActivityTaskManager = Singleton.getDeclaredField(singletonInstanceName);
            IActivityTaskManager.setAccessible(true);
            Object iActivityTaskManagerInstance = IActivityTaskManager.get(singleton);

            Object iActivityTaskManagerInstanceProxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{Class.forName(IActivityManagerName)}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("startActivity".equals(method.getName())) {
                                int intentIndex = -1;

                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        intentIndex = i;
                                    }
                                }
                                if (intentIndex != -1) {
                                    Intent intent = (Intent) args[intentIndex];
                                    Intent proxyIntent = new Intent();
                                    proxyIntent.setClassName("com.hao.plug", "com.hao.plug.ProxyActivity");
                                    proxyIntent.putExtra("proxyIntent", intent);
                                    args[intentIndex] = proxyIntent;
                                }
                            }
                            return method.invoke(iActivityTaskManagerInstance, args);
                        }
                    }
            );

            IActivityTaskManager.set(singleton, iActivityTaskManagerInstanceProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void proxyActivity() {
        //在ams检测activity注册之后 对intentt进行

    }


}
