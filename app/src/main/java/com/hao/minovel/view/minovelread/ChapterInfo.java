package com.hao.minovel.view.minovelread;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChapterInfo {
    private String nowChapterUrl;//当前显示章节的地址 用于去重
    private String chapterName;//当前显示章节名
    private String content;//当前显示内容
    private int page = 5;//当前章节分页数
    private List<String> textArray;//当前章节分行数据

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

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTextArray() {
        if (textArray == null) {
            textArray = new ArrayList<>();
        }
        return textArray;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
