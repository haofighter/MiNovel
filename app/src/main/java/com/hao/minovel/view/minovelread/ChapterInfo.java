package com.hao.minovel.view.minovelread;

import java.util.ArrayList;
import java.util.List;

public class ChapterInfo {
    private String nowChapterUrl;//当前显示章节的地址 用于去重
    private List<NovelPageInfo> novelPageInfos = new ArrayList<>();//当前章节分页数据

    public String getNowChapterUrl() {
        return nowChapterUrl;
    }

    public void setNowChapterUrl(String nowChapterUrl) {
        this.nowChapterUrl = nowChapterUrl;
    }

    public List<NovelPageInfo> getNovelPageInfos() {
        return novelPageInfos;
    }

    public void setNovelPageInfos(List<NovelPageInfo> novelPageInfos) {
        this.novelPageInfos = novelPageInfos;
    }
}
