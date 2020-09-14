package com.hao.minovel.view.minovelread;

import java.util.ArrayList;
import java.util.List;

public class NovelPageInfo {
    private int page;//当前页所属章节页数
    private List<String> pagecontent;//当前页所属的章节内容
    private String novelChapterUrl;//当前章节小说所属id 用于获取下一章或上一章内容
    private String noveChapterListUrl;//当前小说所属id 用于获取下一章或上一章内容


    public void addContent(String s) {
        if (pagecontent == null) {
            pagecontent = new ArrayList<>();
        }
        pagecontent.add(s);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getPagecontent() {
        return pagecontent;
    }

    public void setPagecontent(List<String> pagecontent) {
        this.pagecontent = pagecontent;
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
}
