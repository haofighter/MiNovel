package com.hao.minovel.spider.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 热门小说
 */
@Entity
public class NovelTypeHot {
    @Id(autoincrement = true)
    Long id;
    String type;//小说类型
    @Unique
    String chapterlistUrl;//当前小说列表页
    long creatTime;


    @Generated(hash = 263548452)
    public NovelTypeHot(Long id, String type, String chapterlistUrl,
            long creatTime) {
        this.id = id;
        this.type = type;
        this.chapterlistUrl = chapterlistUrl;
        this.creatTime = creatTime;
    }

    @Generated(hash = 275151802)
    public NovelTypeHot() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChapterlistUrl() {
        return chapterlistUrl;
    }

    public void setChapterlistUrl(String chapterlistUrl) {
        this.chapterlistUrl = chapterlistUrl;
    }

    public long getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }


}
