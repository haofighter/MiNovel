package com.hao.minovel.spider.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class NovelType implements Parcelable {
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

    protected NovelType(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        type = in.readString();
        from = in.readString();
        listUrl = in.readString();
        lastListUrl = in.readString();
        nextListUrl = in.readString();
        creatTime = in.readLong();
    }

    public static final Creator<NovelType> CREATOR = new Creator<NovelType>() {
        @Override
        public NovelType createFromParcel(Parcel in) {
            return new NovelType(in);
        }

        @Override
        public NovelType[] newArray(int size) {
            return new NovelType[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(type);
        dest.writeString(from);
        dest.writeString(listUrl);
        dest.writeString(lastListUrl);
        dest.writeString(nextListUrl);
        dest.writeLong(creatTime);
    }
}
