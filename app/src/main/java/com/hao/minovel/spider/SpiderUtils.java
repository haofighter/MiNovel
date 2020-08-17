package com.hao.minovel.spider;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpiderUtils {
    static int repeateCount = 0;
    static int maxRepeateCount = 3;

    /**
     * 通过网址获取到当前网页的html
     * 获取到的网页数据,使用backCall回调进行处理
     * 拉取操作是在子线程中进行处理
     *
     * @param urlString
     * @return
     */
    public static String getHtml(String urlHead, String urlString) {
        StringBuffer html = new StringBuffer();
        try {
            URL url;
            if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
                url = new URL(urlString);
            } else {
                if (urlString.startsWith("www")) {
                    url = new URL("http://" + urlString);
                } else {
                    url = new URL(urlHead + urlString);
                }
            }
            Log.i("解析地址", "url=" + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
            br.close();
            isr.close();
        } catch (Exception e) {
            if (repeateCount < maxRepeateCount) {
                repeateCount++;
                getHtml(urlHead, urlString);
                if (Looper.getMainLooper() == Looper.myLooper()) {//
                    Log.i("tag", "未获取到网页");
                }
                repeateCount = 0;
            }
        } finally {
            if (html.toString().equals("")) {
                return null;
            } else {
                return html.toString();
            }
        }
    }
}
