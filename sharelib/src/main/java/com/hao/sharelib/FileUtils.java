package com.hao.sharelib;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

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
import java.util.ArrayList;
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
    public static String readAssetsFileToString(String fileName, Context context) {
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
     * @throws IOException
     */
    public static void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
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
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
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
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 遍历文件夹查询 包含第一个str的文件
     *
     * @param rootDirectory
     * @param str
     * @param path
     * @return
     */
    public static String searchFile(String rootDirectory, String str, String path) {
        File file = new File(rootDirectory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        String searchPath = searchFile(files[i].getPath(), str, path);
                        if (!searchPath.equals("")) {
                            path = searchPath;
                        }
                    } else if (files[i].getName().contains(str)) {
                        path = files[i].getPath();
                    }
                }
            }
        }
        return path;
    }


    /**
     * 遍历文件夹查询 包含str的所有文件
     *
     * @param rootDirectory 根目录  用于查询文件的主目录
     * @param str           需要查询的文件的标识
     * @return
     */
    public static List<String> searchFiles(String rootDirectory, String str) {
        List<String> strings = new ArrayList<>();
        File file = new File(rootDirectory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        strings.addAll(searchFiles(files[i].getPath(), str));
                    } else if (files[i].getName().contains(str)) {
                        strings.add(files[i].getPath());
                    }
                }
            }
        }
        return strings;
    }


    /**
     * 获取Cache目录
     *
     * @param context context
     * @return File
     */
    public static File getCacheDir(Context context) {
        return context.getExternalCacheDir();
    }
    /**
     * 获取Cache目录 Movie
     *
     * @param context context
     * @return File
     */
    public static File getCacheMovieDir(Context context) {
        String dir = Environment.DIRECTORY_MOVIES;
        return new File(getCacheDir(context), dir);
    }
}
