package com.hao.minovel.spider.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class NovelType {
    @Id(autoincrement = true)
    Long id;
    String type;//小说类型
    String from;//0 笔趣阁
    @Unique
    String listUrl;//当前小说列表页
    String lastListUrl;//上一页小说列表页
    String nextListUrl;//下一页小说列表页
    long creatTime;

    @Generated(hash = 12608598)
    public NovelType(Long id, String type, String from, String listUrl,
            String lastListUrl, String nextListUrl, long creatTime) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.listUrl = listUrl;
        this.lastListUrl = lastListUrl;
        this.nextListUrl = nextListUrl;
        this.creatTime = creatTime;
    }

    @Generated(hash = 1553007784)
    public NovelType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListUrl() {
        return this.listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public String getLastListUrl() {
        return this.lastListUrl;
    }

    public void setLastListUrl(String lastListUrl) {
        this.lastListUrl = lastListUrl;
    }

    public String getNextListUrl() {
        return this.nextListUrl;
    }

    public void setNextListUrl(String nextListUrl) {
        this.nextListUrl = nextListUrl;
    }

    public long getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "NovelType{" +
                ", listUrl='" + listUrl + '\'' +
                ", lastListUrl='" + lastListUrl + '\'' +
                ", nextListUrl='" + nextListUrl + '\'' +
                '}';
    }
}
