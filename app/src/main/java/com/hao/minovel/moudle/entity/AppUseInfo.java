package com.hao.minovel.moudle.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class AppUseInfo {
    @Unique
    int id;
    long loadAllNovelNameTime = 0;



    @Generated(hash = 937709977)
    public AppUseInfo() {
    }

    public AppUseInfo(int id) {
        this.id = id;
    }

    @Generated(hash = 94196526)
    public AppUseInfo(int id, long loadAllNovelNameTime) {
        this.id = id;
        this.loadAllNovelNameTime = loadAllNovelNameTime;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLoadAllNovelNameTime() {
        return this.loadAllNovelNameTime;
    }

    public void setLoadAllNovelNameTime(long loadAllNovelNameTime) {
        this.loadAllNovelNameTime = loadAllNovelNameTime;
    }


}
