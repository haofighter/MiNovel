package com.hao.minovel.spider.data;


/**
 * 热门小说
 */
public class NovelTypeHot {
    Long id;
    String type;//小说类型
    String chapterlistUrl;//当前小说列表页
    long creatTime;



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
