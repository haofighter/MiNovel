package com.hao.minovel.log;

import android.os.Environment;
import android.util.Log;

import com.hao.minovel.utils.DataUtils;
import com.hao.minovel.utils.FileUtils;

import java.io.File;

public class MiLog {
    static boolean isShowLog = true;
    static boolean isSaveLog = true;


    public static void i(String tag, String msg) {
        if (isShowLog) {
            Log.i(tag, msg);
        }
        if (isSaveLog) {
            File file = new File(Environment.getExternalStorageDirectory() + "/log/" + tag + "_" + DataUtils.getStringDateYMD() + ".txt");
            if (file.exists()) {
                String string = tag + "     " + msg;
                FileUtils.writeToFile(file, string);
            } else {
                String string = tag + "     " + msg;
                FileUtils.checkFile(file);
                FileUtils.byteToFile(string.getBytes(), file);
            }
        }
    }

    public static void clear(int time) {
        File file = new File(Environment.getExternalStorageDirectory() + "/log");
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        String[] fileNames = files[i].getName().split("_");
                        long fileCreatTime = Long.parseLong(fileNames[1].substring(0, 8));
                        if (Long.parseLong(DataUtils.getStringDateYMD()) - fileCreatTime > time) {
                            MiLog.i("日志清除", "删除文件：" + files[i].getPath() + "   删除结果：" + files[i].delete());
                        }
                    } catch (Exception e) {
                        if (files[i].isDirectory()) {
                            MiLog.i("日志清除", "删除文件：" + files[i].getPath() + "   删除结果：" + FileUtils.delAllFile(files[i].getPath()));
                        } else {
                            MiLog.i("日志清除", "删除文件：" + files[i].getPath() + "   删除结果：" + files[i].delete());
                        }
                    }
                }
            }
        } catch (Exception e) {
            MiLog.i("日志清除", "失败 原因" + e.getMessage());
            if (file.exists()) {
                FileUtils.delAllFile(file.getPath());
            }
        }
    }
}
