package com.hao.minovel.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;


import com.hao.minovel.tinker.app.AppContext;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.arraycopy;

public class FileUtils {

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] fileToBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 根据byte数组，生成文件
     */
    public static boolean byteToFile(byte[] bfile, File file) {
        checkFile(file);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        try {
            if (!file.exists() && file.isDirectory()) {//判断文件目录是否存在
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("转换文件出错", e.getMessage());
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return true;
        }
    }

    /* 把batmap 转file
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param fileName 文件名
     * @param context  .
     * @return 读取assets文件夹下的文本文件
     */
    public static String readAssetsFile(String fileName, Context context) {
        StringBuilder builder = new StringBuilder();
        AssetManager manager = context.getAssets();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    /**
     * @param fileName 文件名
     * @param context  .
     * @return 读取assets文件夹下的文本文件
     */
    public static byte[] readAssetsFileTobyte(String fileName, Context context) {
        byte[] buffer = null;
        AssetManager manager = context.getAssets();
        try {
            InputStream reader = manager.open(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = reader.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            reader.close();
            bos.close();
            buffer = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static byte[] readFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            int length = stream.available();
            byte[] buffer = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = stream.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }

            Log.d("FileUtil",
                    "readFile(FileUtil.java:63)" + file.getName() + "读取成功………………");
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FileUtil",
                    "readBL(FileUtil.java:32)" + e.toString());
        }

        return null;
    }


    public static String readFileToString(File file) {
        try {
            String fileContent = "";
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                fileContent += line;
            }
            br.close();
            isr.close();
            fis.close();
            return fileContent;
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * 保存数据至文件
     *
     * @param string
     * @param file
     * @return
     */
    public static boolean saveStrToFile(String string, File file) {
        try {
            checkFile(file);
            FileWriter writer;
            writer = new FileWriter(file.getPath());
            writer.write("");//清空原文件内容
            writer.write(string);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * 判断文件是否存在  不存在则创建
     *
     * @param file
     */
    public static void checkFile(File file) {
        try {
            String string = file.getPath();
            String[] mirs = string.split("/");
            String checkMirs = "";
            File mir;
            for (int i = 0; i < mirs.length; i++) {
                if (!mirs[i].equals("")) {
                    checkMirs = checkMirs + "/" + mirs[i];
                    mir = new File(checkMirs);
                    if (!mir.exists()) {
                        if (checkMirs.contains(".")) {
                            mir.createNewFile();
                        } else {
                            mir.mkdir();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("错误", "checkFile(FileUtils.java:1016) 文件验证错误" + e.getMessage());
        }
    }


    /**
     * 复制文件夹
     *
     * @param sourcePath
     * @param newPath
     */
    public static void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        } else if (filePath == null) {
            filePath = new String[]{};
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

            if (new File(sourcePath + file.separator + filePath[i]).isFile()) {
                copyFile(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

        }
    }

    /**
     * 复制文件到SD卡中
     *
     * @param posPath     pos路径
     * @param sdDirectory sd卡文件夹
     */
    private void exportFile(String posPath, String sdDirectory) throws IOException {
        File file = new File(posPath);
        if (file.exists()) {
            String newPath = "/storage/sdcard1" + sdDirectory;
            copyDir(posPath, newPath);
        }
    }


    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer = new byte[2097152];
        int readByte = 0;
        while ((readByte = in.read(buffer)) != -1) {
            out.write(buffer, 0, readByte);
        }

        in.close();
        out.close();
    }


    /**
     * 在文件夹中查找文件名带有字符的文件
     *
     * @param rootDirectory
     * @param str
     * @return
     */
    public static String searchFile(String rootDirectory, String str) {
        File file = new File(rootDirectory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File searFile = files[i];
                    if (searFile.isDirectory()) {
                        String searchPath = searchFile(searFile.getPath(), str);
                        if (!searchPath.equals("")) {
                            return searchPath;
                        }
                    } else if (searFile.isFile()) {
                        if (searFile.getName().contains(str)) {
                            return searFile.getPath();
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 删除所有文件
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            Log.i("文件夹", file.getPath() + "     包含文件数：" + files.length);
            for (int i = 0; i < files.length; i++) {
                flag = delAllFile(files[i].getPath());
            }
            flag = file.delete();
        }
        return flag;
    }


    /**
     * 在文件中添加数据
     *
     * @param data 添加数据
     * @return
     */
    public static String writeToFile(File file, String data) {
        FileOutputStream out = null;
        try {
            //创建文件输入流
            out = new FileOutputStream(file, true); //如果追加方式用true
            //写入内容
            StringBuffer sb = new StringBuffer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            sb.append("-----------" + sdf.format(new Date()) + "------------\n");
            sb.append(data + "\n");
            //写入
            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
            return "success";
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        } finally {
            try {
                if (out != null) {
                    out.close();   //关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "false";
    }


    private List<ResolveInfo> getAllPackget() {
        //当前应用pid
        PackageManager packageManager = AppContext.application.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        return packageManager.queryIntentActivities(mainIntent, 0);
//        for (int i = 0; i < apps.size(); i++) {
//            String name = apps.get(i).activityInfo.packageName;
//
//        }
    }

    public boolean isContentApp(String packageName) {
        List<ResolveInfo> resolveInfos = getAllPackget();
        if (resolveInfos == null) {
            return false;
        }
        for (int i = 0; i < resolveInfos.size(); i++) {
            String name = resolveInfos.get(i).activityInfo.packageName;
            if (name.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    //字符串转换为ascii
    public static String stringToAsc(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            int b = (int) c;
            result = result + b;
        }
        return result;
    }


}
