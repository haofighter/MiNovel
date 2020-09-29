package com.hao.minovel.view.minovelread;

import java.util.ArrayList;
import java.util.List;

public class ChapterInfo {
    private String nowChapterUrl;//当前显示章节的地址 用于去重
    private String chapterName;//当前显示章节名
    private String content;//当前显示内容
    private List<String> novelPageInfos = new ArrayList<>();//当前章节分页数据
    private int page;//当前章节分页数

    public ChapterInfo(String nowChapterUrl, String chapterName, String content) {
        this.nowChapterUrl = nowChapterUrl;
        this.chapterName = chapterName;
        this.content = content;
    }

    public String getNowChapterUrl() {
        return nowChapterUrl;
    }

    public void setNowChapterUrl(String nowChapterUrl) {
        this.nowChapterUrl = nowChapterUrl;
    }

    public int getPage() {
        return page;
    }

    public String getChapterName() {
        return chapterName;
    }

    public List<String> getNovelPageInfos() {
        return novelPageInfos;
    }

    public void setNovelPageInfos(List<String> novelPageInfos) {
        this.novelPageInfos = novelPageInfos;
    }

    public void setNovelTextViewHelp(NovelTextViewHelp novelTextViewHelp) {
        novelTextViewHelp.initViewConfig();
        novelPageInfos = novelTextViewHelp.fromateArray(content);
        page = novelTextViewHelp.allPage;
    }


}
