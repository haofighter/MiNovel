package com.hao.minovel.spider;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SpiderNovelFromBaidu {
    static String url = "http://www.xbiquge.la/";

    public static int getSearchNovelList(String searchStr) {
        SpiderResponse spiderResponse = SpiderUtils.getHtml(url, searchStr);
        Log.i("搜索", spiderResponse.code + "");
        if (spiderResponse.code != 0) {
            return spiderResponse.code;
        }
        Document doc = Jsoup.parse(spiderResponse.result);
        Log.i("搜索", doc.html());
        Elements element = doc.select("div#content_left");

        Log.i("搜索", element.toString() + "    124");
        return spiderResponse.code;
    }
}
