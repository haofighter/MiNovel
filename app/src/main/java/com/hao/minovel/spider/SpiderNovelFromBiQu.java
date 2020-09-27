package com.hao.minovel.spider;


import android.util.Log;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;
import com.hao.minovel.spider.data.NovelTypeHot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.hao.minovel.spider.SpiderUtils.Success;
import static com.hao.minovel.spider.SpiderUtils.UNKNOWNERR;


public class SpiderNovelFromBiQu {
    public static final String BiQuMainUrl = "http://www.xbiquge.la/";

    /**
     * 获取当前站所有小说信息
     */
    public static int getAllNovel() {
        SpiderResponse spiderResponse = SpiderUtils.getHtml(BiQuMainUrl, "xiaoshuodaquan/");
        if (spiderResponse.code != 0) {
            return spiderResponse.code;
        }
        Document doc = Jsoup.parse(spiderResponse.result);
        Elements rows = doc.select("div[class=novellist]");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Elements list = rows.select("li");
            for (int j = 0; j < list.size(); j++) {
//                String novelType = rows.select("h2").text();
                Elements elements = list.get(j).select("a");
                NovelIntroduction novelIntroduction = new NovelIntroduction();
                novelIntroduction.setNovelName(elements.text());
                novelIntroduction.setNovelChapterListUrl(elements.attr("href"));
                novelIntroduction.setIsComplete(false);
                novelIntroduction.setCreatTime(System.currentTimeMillis());
//                novelIntroduction.setNovelType(novelType);
                novelIntroductions.add(novelIntroduction);
//                Log.i("小说", "名称=" + novelIntroduction.getNovelName() + "   作者：" + novelIntroduction.getNovelAutho() + "     主页地址：" + novelIntroduction.getNovelChapterListUrl());
            }
        }
        DBManage.addNovelIntrodution(novelIntroductions);
        return spiderResponse.code;
    }

    public static int getAllNovelDetailInfo(NovelIntroduction novelIntroduction) {
        SpiderResponse htmlNovelChapterList = SpiderUtils.getHtml(BiQuMainUrl, novelIntroduction.getNovelChapterListUrl());
        if (htmlNovelChapterList.code != 0) {
            return htmlNovelChapterList.code;
        }
        return getAllNovelDetailInfo(novelIntroduction, htmlNovelChapterList.result);
    }

    //完善小说详情小说详情
    public static int getAllNovelDetailInfo(NovelIntroduction novelIntroduction, String html) {
        String htmlNovelChapterList = html;
        try {
            Document htmlNovelChapterListDoc = Jsoup.parse(htmlNovelChapterList);
            novelIntroduction.setNovelAutho(htmlNovelChapterListDoc.select("div#info").select("p").get(0).text().replace("作", "").replace("者", "").replace("：", "").replace(" ", ""));
            novelIntroduction.setNovelCover(htmlNovelChapterListDoc.select("div#sidebar").select("div#fmimg").select("img").attr("src"));
            novelIntroduction.setNovelNewChapterUrl(htmlNovelChapterListDoc.select("div#info").select("p").get(3).select("a").attr("href"));
            novelIntroduction.setNovelNewChapterTitle(htmlNovelChapterListDoc.select("div#info").select("p").get(3).select("a").text());
            novelIntroduction.setNovelIntroduce(htmlNovelChapterListDoc.select("div#intro").select("p").get(1).text());
            novelIntroduction.setIsComplete(true);
            DBManage.addNovelIntrodution(novelIntroduction);
            getNovelAllChapterTitle(novelIntroduction, htmlNovelChapterListDoc);
        } catch (Exception e) {
            return UNKNOWNERR;
        }
        return Success;
    }

    public static boolean getNovelAllChapterTitle(NovelIntroduction novelIntroduction, Document html) {
        try {
            Elements frist = html.select("div[class=box_con]");
            List<NovelChapter> chapters = new ArrayList<>();
            Elements selectChapter = frist.select("div#list").select("dd");
            for (int i = 0; i < selectChapter.size(); i++) {
                String chapterUrl = selectChapter.get(i).select("a").attr("href");
                String chapterName = selectChapter.get(i).select("a").text();
                chapters.add(new NovelChapter((long) i, novelIntroduction.getNovelChapterListUrl(), chapterName, CheckedUrl(chapterUrl), "", "", "", false, i));
//                Log.i("下载章节", "chapterName=" + chapterName);
            }
            DBManage.addNovelChapter(chapters);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int getNovelContent(NovelChapter novelChapter) {
        if (novelChapter.getChapterUrl() == null || novelChapter.getChapterUrl().equals("")) {
            return UNKNOWNERR;
        }

        SpiderResponse htmlNovelChapterList = SpiderUtils.getHtml(BiQuMainUrl, novelChapter.getChapterUrl());
        if (htmlNovelChapterList.code != 0) {
            return htmlNovelChapterList.code;
        }

        Document htmlNovelChapterListDoc = Jsoup.parse(htmlNovelChapterList.result);
        Elements frist = htmlNovelChapterListDoc.select("div[class=box_con]");
        Elements second = frist.select("div[class=bookname]");
        novelChapter.setChapterName(second.select("h1").text());
        Elements thrid = second.select("div[class=bottem1]").select("a");
        try {
            novelChapter.setBeforChapterUrl(CheckedUrl(thrid.get(1).attr("href")));
        } catch (Exception e) {
            novelChapter.setBeforChapterUrl("");
        }
        try {
            novelChapter.setNextChapterUrl(CheckedUrl(thrid.select("a").get(3).attr("href")));
        } catch (Exception e) {
            novelChapter.setNextChapterUrl("");
        }
        novelChapter.setChapterContent(frist.select("div#content").html().replace(" ", "").replace("\n", "").replace("<br>&nbsp;&nbsp;&nbsp;&nbsp;", "\n  ").replace("<br>", "").replace("&nbsp;", "").split("<p>")[0]);
        novelChapter.setIsComplete(true);
        DBManage.updateNovelChapter(novelChapter);

        return htmlNovelChapterList.code;
    }

    //用于URL的检测
    private static String CheckedUrl(String string) {
        if (string.startsWith("http")) {
            return string;
        } else {
            return BiQuMainUrl + string;
        }
    }

    public static int getNovelType() {
        SpiderResponse html = SpiderUtils.getHtml(BiQuMainUrl, "");
        if (html.code != 0) {
            return html.code;
        }
        Document doc = Jsoup.parse(html.result);
        Elements rows = doc.select("div[class=nav]");
        Elements type = rows.select("li");
        List<NovelType> novelTypes = new ArrayList<>();
        for (int i = 2; i < type.size() - 2; i++) {
            NovelType novelType = new NovelType();
            novelType.setFrom("0");
            novelType.setType(type.get(i).select("a").text());
            novelType.setListUrl(type.get(i).select("a").attr("href"));
            novelType.setCreatTime(System.currentTimeMillis());
            novelTypes.add(novelType);
        }
        DBManage.addNovelType(novelTypes);
        return html.code;
    }

    //查询未完善的章节 进行数据完善
    public static int getAllNovelContent(NovelIntroduction novelIntroduction) {
        NovelChapter novelChapter = DBManage.checkNovelAllNoChapterContent(novelIntroduction);
        if (novelChapter != null) {
            return getNovelContent(novelChapter);
        } else {
            return UNKNOWNERR;
        }
    }

    public static int getTypeNovelList(NovelType novelType) {
        SpiderResponse html = SpiderUtils.getHtml(BiQuMainUrl, novelType.getListUrl());
        if (html.code != 0) {
            return html.code;
        }
        Document doc = Jsoup.parse(html.result);
        Elements rows = doc.select("div#newscontent");
        Elements list = rows.select("div[class=l]");


        Elements novelTypeListContennt = list.select("div[class=page_b]");
        Elements novelTypeList = novelTypeListContennt.select("tbody").select("tr").select("td").select("div#pagelink");
        novelType.setLastListUrl(novelTypeList.select("a[class=prev]").attr("href"));
        novelType.setNextListUrl(novelTypeList.select("a[class=next]").attr("href"));
        Log.i("加载数据  完善数据", novelType.toString());
        if (novelType.getListUrl().equals("") || novelType.getNextListUrl().equals("")) {
            MiLog.i("完善列表数据失败", novelTypeListContennt.html());
        } else {
            DBManage.addNovelType(novelType);
            NovelType nextNovelType = new NovelType();
            nextNovelType.setCreatTime(System.currentTimeMillis());
            nextNovelType.setListUrl(novelType.getNextListUrl());
            nextNovelType.setLastListUrl(novelType.getListUrl());
            nextNovelType.setFrom(novelType.getFrom());
            nextNovelType.setType(novelType.getType());
            DBManage.addNovelType(nextNovelType);
            Log.i("加载数据  存储下一页数据", nextNovelType.toString());
        }


        Elements node = list.select("li");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (Element e : node) {
            String name = e.select("span[class=s2]").select("a").text();
            String novelUrl = e.select("span[class=s2]").select("a").attr("href");
            String newChaptername = e.select("span[class=s3]").text();
            String newChapterUrl = e.select("span[class=s3]").attr("href");
            String author = e.select("span[class=s5]").text();
            NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(novelUrl);
            if (novelIntroduction == null) {
                novelIntroduction = new NovelIntroduction();
                novelIntroduction.setNovelName(name);
                novelIntroduction.setNovelListUrl(novelType.getListUrl());
            }
            novelIntroduction.setNovelNewChapterTitle(newChaptername);
            novelIntroduction.setNovelNewChapterUrl(newChapterUrl);
            novelIntroduction.setNovelAutho(author);
            novelIntroduction.setNovelType(novelType.getType());
            novelIntroduction.setNovelChapterListUrl(novelUrl);
            novelIntroduction.setCreatTime(System.currentTimeMillis());
            novelIntroductions.add(novelIntroduction);
            Log.i("小说", "名称=" + novelIntroduction.getNovelName() + "   作者：" + novelIntroduction.getNovelAutho() + "     主页地址：" + novelIntroduction.getNovelChapterListUrl() + "  " + novelIntroduction.getNovelType());
        }
        DBManage.addNovelIntrodution(novelIntroductions);


        List<NovelTypeHot> novelTypeHots = new ArrayList<>();
        Elements hotrows = doc.select("div#hotcontent");
        Elements hotlist = hotrows.select("div[class=item]");
        for (Element e : hotlist) {
            String novelUrl = e.select("a").attr("href");
            String name = e.select("a").select("img").attr("alt");
            String cover = e.select("a").select("img").attr("src");
            String auther = e.select("dl").select("dt").select("span").text();
            String introduce = e.select("dl").select("dd").text();
            NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(novelUrl);
            if (novelIntroduction == null) {
                novelIntroduction = new NovelIntroduction();
                novelIntroduction.setNovelType(novelType.getType());
                novelIntroduction.setNovelAutho(auther);
                novelIntroduction.setNovelName(name);
                novelIntroduction.setNovelChapterListUrl(novelUrl);
                novelIntroduction.setCreatTime(System.currentTimeMillis());
                novelIntroduction.setNovelIntroduce(introduce);
                DBManage.addNovelIntrodution(novelIntroduction);
            } else if (!novelIntroduction.isComplete()) {
                novelIntroduction.setNovelCover(cover);
                novelIntroduction.setNovelAutho(auther);
                novelIntroduction.setNovelIntroduce(introduce);
                novelIntroduction.setNovelType(novelType.getType());
                DBManage.addNovelIntrodution(novelIntroduction);
            }


            NovelTypeHot novelTypeHot = new NovelTypeHot();
            novelTypeHot.setType(novelType.getType());
            novelTypeHot.setChapterlistUrl(novelUrl);
            novelTypeHots.add(novelTypeHot);

            Log.i("小说", "保存热门：" + novelTypeHot.getType() + "      " + novelTypeHot.getChapterlistUrl() + "       " + auther + "      " + cover + "      " + name);
        }
        DBManage.addNovelTypeHot(novelTypeHots);
        return html.code;
    }
}
