package com.hao.minovel.spider.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.hao.minovel.view.minovelread.NovelPageInfo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class NovelChapter implements Parcelable {
    private Long id;
    private String novelChapterListUrl;//小说的id
    private String chapterName;
    @Unique
    private String chapterUrl;
    private String chapterContent;//本章小说内容
    private String nextChapterUrl;//下一章
    private String beforChapterUrl;//上一章
    private boolean isComplete;//信息是否完善
    private long createTime;//创建时间  以for循环的index来区分


    public String getChapterName() {
        return this.chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterUrl() {
        return this.chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getChapterContent() {
        return this.chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    public String getNextChapterUrl() {
        return this.nextChapterUrl;
    }

    public void setNextChapterUrl(String nextChapterUrl) {
        this.nextChapterUrl = nextChapterUrl;
    }

    public String getBeforChapterUrl() {
        return this.beforChapterUrl;
    }

    public void setBeforChapterUrl(String beforChapterUrl) {
        this.beforChapterUrl = beforChapterUrl;
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }


    public String getNovelChapterListUrl() {
        return this.novelChapterListUrl;
    }

    public void setNovelChapterListUrl(String novelChapterListUrl) {
        this.novelChapterListUrl = novelChapterListUrl;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.novelChapterListUrl);
        dest.writeString(this.chapterName);
        dest.writeString(this.chapterUrl);
        dest.writeString(this.chapterContent);
        dest.writeString(this.nextChapterUrl);
        dest.writeString(this.beforChapterUrl);
        dest.writeByte(this.isComplete ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createTime);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected NovelChapter(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.novelChapterListUrl = in.readString();
        this.chapterName = in.readString();
        this.chapterUrl = in.readString();
        this.chapterContent = in.readString();
        this.nextChapterUrl = in.readString();
        this.beforChapterUrl = in.readString();
        this.isComplete = in.readByte() != 0;
        this.createTime = in.readLong();
    }

    @Generated(hash = 1894982023)
    public NovelChapter(Long id, String novelChapterListUrl, String chapterName,
                        String chapterUrl, String chapterContent, String nextChapterUrl,
                        String beforChapterUrl, boolean isComplete, long createTime) {
        this.id = id;
        this.novelChapterListUrl = novelChapterListUrl;
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.chapterContent = chapterContent;
        this.nextChapterUrl = nextChapterUrl;
        this.beforChapterUrl = beforChapterUrl;
        this.isComplete = isComplete;
        this.createTime = createTime;
    }

    @Generated(hash = 922107813)
    public NovelChapter() {
    }

    public static final Creator<NovelChapter> CREATOR = new Creator<NovelChapter>() {
        @Override
        public NovelChapter createFromParcel(Parcel source) {
            return new NovelChapter(source);
        }

        @Override
        public NovelChapter[] newArray(int size) {
            return new NovelChapter[size];
        }
    };

    public List<String> getFromatContentArray() {
        String[] strs = getChapterContent().split("\n");
        return Arrays.asList(strs);
    }
}
