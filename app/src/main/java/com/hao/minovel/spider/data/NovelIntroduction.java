package com.hao.minovel.spider.data;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;


/**
 * 小说简介 用于存储小说的名称，作者，地址等
 */
@Entity
public class NovelIntroduction implements Parcelable {
    String novelName;//小说名
    String novelAutho;//作者
    String novelCover;//封面
    String novelIntroduce;//文字介绍
    String novelType;//小说类型
    String novelListUrl;//小说列表地址
    String novelNewChapterTitle;//最新章节
    String novelNewChapterUrl;//最新章节地址
    @Unique
    String novelChapterListUrl;//章节列表地址
    String nowRead;//当前阅读的章节
    String nowReadID;//当前阅读的章节Id
    boolean isComplete;//信息是否完善
    boolean isFav;//是否收藏
    boolean ishot;//是否热门
    long creatTime;


    @Generated(hash = 1794763955)
    public NovelIntroduction(String novelName, String novelAutho, String novelCover,
                             String novelIntroduce, String novelType, String novelListUrl,
                             String novelNewChapterTitle, String novelNewChapterUrl,
                             String novelChapterListUrl, String nowRead, String nowReadID,
                             boolean isComplete, boolean isFav, boolean ishot, long creatTime) {
        this.novelName = novelName;
        this.novelAutho = novelAutho;
        this.novelCover = novelCover;
        this.novelIntroduce = novelIntroduce;
        this.novelType = novelType;
        this.novelListUrl = novelListUrl;
        this.novelNewChapterTitle = novelNewChapterTitle;
        this.novelNewChapterUrl = novelNewChapterUrl;
        this.novelChapterListUrl = novelChapterListUrl;
        this.nowRead = nowRead;
        this.nowReadID = nowReadID;
        this.isComplete = isComplete;
        this.isFav = isFav;
        this.ishot = ishot;
        this.creatTime = creatTime;
    }


    @Generated(hash = 1432430798)
    public NovelIntroduction() {
    }


    protected NovelIntroduction(Parcel in) {
        novelName = in.readString();
        novelAutho = in.readString();
        novelCover = in.readString();
        novelIntroduce = in.readString();
        novelType = in.readString();
        novelListUrl = in.readString();
        novelNewChapterTitle = in.readString();
        novelNewChapterUrl = in.readString();
        novelChapterListUrl = in.readString();
        nowRead = in.readString();
        nowReadID = in.readString();
        isComplete = in.readByte() != 0;
        isFav = in.readByte() != 0;
        ishot = in.readByte() != 0;
        creatTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(novelName);
        dest.writeString(novelAutho);
        dest.writeString(novelCover);
        dest.writeString(novelIntroduce);
        dest.writeString(novelType);
        dest.writeString(novelListUrl);
        dest.writeString(novelNewChapterTitle);
        dest.writeString(novelNewChapterUrl);
        dest.writeString(novelChapterListUrl);
        dest.writeString(nowRead);
        dest.writeString(nowReadID);
        dest.writeByte((byte) (isComplete ? 1 : 0));
        dest.writeByte((byte) (isFav ? 1 : 0));
        dest.writeByte((byte) (ishot ? 1 : 0));
        dest.writeLong(creatTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NovelIntroduction> CREATOR = new Creator<NovelIntroduction>() {
        @Override
        public NovelIntroduction createFromParcel(Parcel in) {
            return new NovelIntroduction(in);
        }

        @Override
        public NovelIntroduction[] newArray(int size) {
            return new NovelIntroduction[size];
        }
    };

    @Override
    public String toString() {
        return "NovelIntroduction{" +
                "小说名='" + novelName + '\n' +
                ", 作者='" + novelAutho + '\n' +
                ", 封面='" + novelCover + '\n' +
                ", 小说类型='" + novelType + '\n' +
                ", 介绍='" + novelIntroduce + '\n' +
                ", 最新章节='" + novelNewChapterTitle + '\n' +
                ", 最新章节地址='" + novelNewChapterUrl + '\n' +
                ", 章节列表地址='" + novelChapterListUrl + '\n' +
                ", 信息是否完善='" + isComplete + '\n' +
                '}';
    }


    public String getNovelName() {
        return this.novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getNovelAutho() {
        return this.novelAutho;
    }

    public void setNovelAutho(String novelAutho) {
        this.novelAutho = novelAutho;
    }

    public String getNovelCover() {
        return this.novelCover;
    }

    public void setNovelCover(String novelCover) {
        this.novelCover = novelCover;
    }

    public String getNovelIntroduce() {
        return this.novelIntroduce;
    }

    public void setNovelIntroduce(String novelIntroduce) {
        this.novelIntroduce = novelIntroduce;
    }

    public String getNovelNewChapterTitle() {
        return this.novelNewChapterTitle;
    }

    public void setNovelNewChapterTitle(String novelNewChapterTitle) {
        this.novelNewChapterTitle = novelNewChapterTitle;
    }

    public String getNovelNewChapterUrl() {
        return this.novelNewChapterUrl;
    }

    public void setNovelNewChapterUrl(String novelNewChapterUrl) {
        this.novelNewChapterUrl = novelNewChapterUrl;
    }

    public String getNovelChapterListUrl() {
        return this.novelChapterListUrl;
    }

    public void setNovelChapterListUrl(String novelChapterListUrl) {
        this.novelChapterListUrl = novelChapterListUrl;
    }


    public boolean getIsComplete() {
        return this.isComplete;
    }


    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getNovelType() {
        return novelType;
    }

    public void setNovelType(String novelType) {
        this.novelType = novelType;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }


    public String getNowRead() {
        return this.nowRead;
    }


    public void setNowRead(String nowRead) {
        this.nowRead = nowRead;
    }


    public String getNowReadID() {
        return this.nowReadID;
    }


    public void setNowReadID(String nowReadID) {
        this.nowReadID = nowReadID;
    }


    public boolean getIsFav() {
        return this.isFav;
    }


    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }


    public String getNovelListUrl() {
        return this.novelListUrl;
    }


    public void setNovelListUrl(String novelListUrl) {
        this.novelListUrl = novelListUrl;
    }


    public long getCreatTime() {
        return this.creatTime;
    }


    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public boolean isIshot() {
        return ishot;
    }

    public void setIshot(boolean ishot) {
        this.ishot = ishot;
    }


    public boolean getIshot() {
        return this.ishot;
    }

    public void setDate(NovelIntroduction novelIntroduction) {
        if (novelIntroduction == null) {
            return;
        }

        if (novelName == null) {
            novelName = novelIntroduction.getNovelName();
        }

        if (novelAutho == null) {
            novelAutho = novelIntroduction.getNovelAutho();
        }

        if (novelCover == null) {
            novelCover = novelIntroduction.getNovelCover();
        }

        if (novelIntroduce == null) {
            novelIntroduce = novelIntroduction.getNovelIntroduce();
        }
        if (novelType == null) {
            novelType = novelIntroduction.getNovelType();
        }

        if (novelNewChapterTitle == null) {
            novelNewChapterTitle = novelIntroduction.getNovelNewChapterTitle();
        }

        if (novelListUrl == null) {
            novelListUrl = novelIntroduction.getNovelListUrl();
        }

        if (novelNewChapterUrl == null) {
            novelNewChapterUrl = novelIntroduction.getNovelNewChapterUrl();
        }

        if (nowRead == null) {
            nowRead = novelIntroduction.getNowRead();
        }
        if (nowReadID == null) {
            nowReadID = novelIntroduction.getNowReadID();
        }
    }
}
