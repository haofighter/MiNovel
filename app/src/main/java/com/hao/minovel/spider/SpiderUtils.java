package com.hao.minovel.spider;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class SpiderUtils {
    public final static int Success = 0;
    public final static int UnknownHostException = -1;
    public final static int TimeoutException = -2;
    public final static int UNKNOWNERR = -3;

    /**
     * 通过网址获取到当前网页的html
     * 获取到的网页数据,使用backCall回调进行处理
     * 拉取操作是在子线程中进行处理
     *
     * @param urlHead   请求的地址头部
     * @param urlString
     * @return
     */
    public static SpiderResponse getHtml(String urlHead, String urlString) {
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
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
//            conn.setRequestProperty("Accept",
//                    "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*");
//            conn.setRequestProperty("Accept-Language", "zh-cn");
//            conn.setRequestProperty("UA-CPU", "x86");
//            conn.setRequestProperty("Accept-Encoding", "gzip");//为什么没有deflate呢
            conn.setRequestProperty("Content-type", "text/html");
            conn.setRequestProperty("Connection", "close"); //keep-Alive，有什么用呢，你不是在访问网站，你是在采集。嘿嘿。减轻别人的压力，也是减轻自己。
            conn.setUseCaches(false);//不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
            Log.i("解析地址", "html=" + html);
            br.close();
            isr.close();
        } catch (UnknownHostException e) {
            return new SpiderResponse(UnknownHostException, null);
        } catch (SocketTimeoutException e) {
            return new SpiderResponse(TimeoutException, null);
        } catch (Exception e) {
            return new SpiderResponse(UNKNOWNERR, e.getMessage());
        } finally {
            return new SpiderResponse(Success, html.toString());
        }
    }


}
