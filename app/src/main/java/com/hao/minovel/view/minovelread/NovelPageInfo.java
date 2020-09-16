package com.hao.minovel.view.minovelread;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NovelPageInfo {
    private int page;//当前页所属章节页数
    private int allPage;//当前章节分页数
    private List<String> pageContent=new ArrayList<>();//当前章节内容
    private String novelChapterUrl;//当前章节小说所属id 用于获取下一章或上一章内容
    private String noveChapterListUrl;//
    private View novelTextView;//当前页展示的小说界面


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public String getNovelChapterUrl() {
        return novelChapterUrl;
    }

    public void setNovelChapterUrl(String novelChapterUrl) {
        this.novelChapterUrl = novelChapterUrl;
    }

    public String getNoveChapterListUrl() {
        return noveChapterListUrl;
    }

    public void setNoveChapterListUrl(String noveChapterListUrl) {
        this.noveChapterListUrl = noveChapterListUrl;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public List<String> getPageContent() {
        return pageContent;
    }

    public void setPageContent(List<String> pageContent) {
        this.pageContent = pageContent;
    }

    public View getNovelTextView() {
        return novelTextView;
    }

    public void setNovelTextView(View novelTextView) {
        this.novelTextView = novelTextView;
    }
}
