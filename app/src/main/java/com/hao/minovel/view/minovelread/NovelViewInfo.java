package com.hao.minovel.view.minovelread;

import android.view.View;

import java.util.List;

public class NovelViewInfo {
    List<View> views;
    String chapterName;

    public NovelViewInfo(List<View> views, String chapterName) {
        this.views = views;
        this.chapterName = chapterName;
    }

    public List<View> getViews() {
        return views;
    }

    public String getChapterName() {
        return chapterName;
    }
}