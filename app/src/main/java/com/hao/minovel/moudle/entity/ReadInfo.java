package com.hao.minovel.moudle.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 记录阅读的小说信息
 */
@Entity
public class ReadInfo {
    @Id(autoincrement = true)
    Long id;
    @Unique
    String novelChapterListUrl;//上一次阅读的小说章节列表地址
    String novelChapterUrl;//上一次阅读的小说章节
    int page;//上一次阅读的小说当前章节的页数
    long date;//阅读时间


    @Generated(hash = 64149534)
    public ReadInfo(Long id, String novelChapterListUrl, String novelChapterUrl,
                    int page, long date) {
        this.id = id;
        this.novelChapterListUrl = novelChapterListUrl;
        this.novelChapterUrl = novelChapterUrl;
        this.page = page;
        this.date = date;
    }

    @Generated(hash = 687160188)
    public ReadInfo() {
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getNovelChapterListUrl() {
        return this.novelChapterListUrl;
    }

    public void setNovelChapterListUrl(String novelChapterListUrl) {
        this.novelChapterListUrl = novelChapterListUrl;
    }

    public String getNovelChapterUrl() {
        return this.novelChapterUrl;
    }

    public void setNovelChapterUrl(String novelChapterUrl) {
        this.novelChapterUrl = novelChapterUrl;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ReadInfo{" +
                "id=" + id +
                ", novelChapterListUrl='" + novelChapterListUrl + '\'' +
                ", novelChapterUrl='" + novelChapterUrl + '\'' +
                ", page=" + page +
                ", date=" + date +
                '}';
    }
}
