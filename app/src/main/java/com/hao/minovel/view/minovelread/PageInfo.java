package com.hao.minovel.view.minovelread;

public class PageInfo {
    int chapterIndex;
    int contentIndex;

    public PageInfo(int chapterIndex, int contentIndex) {
        this.chapterIndex = chapterIndex;
        this.contentIndex = contentIndex;
    }

    public int getContentIndex() {
        return contentIndex+1;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }
}
