package com.hao.plug;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class LoadUtils {
    public static void loadClassLoad(Context context, String apkPath) {


        try {
            //1.反射获取到系统中的BaseDexClassLoader
            Class BaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field DexPathLists = BaseDexClassLoader.getDeclaredField("pathList");
            //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
            DexPathLists.setAccessible(true);//值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查

            //1.反射获取到系统中的BaseDexClassLoader
            Class DexPathList = Class.forName("dalvik.system.DexPathList");
            Field DexElements = DexPathList.getDeclaredField("dexElements");
            //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
            DexElements.setAccessible(true);//值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查v


            //获取到当前宿主的dexElements
            ClassLoader classLoader = context.getClassLoader();
            //获取到宿主的dexpathList
            Object dexpathLists = DexPathLists.get(classLoader);
            Object[] dexElements = (Object[]) DexElements.get(dexpathLists);


            //获取到插件中的dexElements
            ClassLoader dexClassLoader = new DexClassLoader(apkPath, context.getCacheDir().getAbsolutePath(), null, classLoader);
            //获取到宿主的dexpathList
            Object plugdexpathLists = DexPathLists.get(dexClassLoader);
            Object[] plugdexElements = (Object[]) DexElements.get(plugdexpathLists);


            //创建新的数组
            Object[] newElements = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(), dexElements.length + plugdexElements.length);
            System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
            System.arraycopy(plugdexElements, 0, newElements, dexElements.length, plugdexElements.length);

            //将合并的dexElement合并覆盖至已加载的pathlist中
//            DexElements.set(dexpathLists,plugdexElements);
            DexElements.set(dexpathLists, newElements);

            //获取到宿主的dexpathList
            Object objects = DexPathLists.get(classLoader);
            Object[] all = (Object[]) DexElements.get(objects);

            Log.i("日志", newElements.length + "     " + dexElements.length + "        " + plugdexElements.length + "         " + all.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
