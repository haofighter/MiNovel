package com.hao.minovel.view.minovelread;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 用于记录noveltext的绘制信息
 * 由PullViewLayout创建，各noveltext共用
 */
@Entity
public class NovelTextDrawInfo implements Parcelable {
    @Unique
    Long id = 1l;
    float textSize;//字体大小
    int textColor;//字体颜色
    int maxLine;//字体大内容页最大的行数
    int padingTop;//内容页顶部间隔，主要用于处理文字绘制后，底部剩余部分的空间对称处理
    String typeFaceName;//字体类型

    public NovelTextDrawInfo() {
        id = 1l;
    }

    protected NovelTextDrawInfo(Parcel in) {
        textSize = in.readFloat();
        textColor = in.readInt();
        maxLine = in.readInt();
        padingTop = in.readInt();
        typeFaceName = in.readString();
    }

    @Generated(hash = 986813619)
    public NovelTextDrawInfo(Long id, float textSize, int textColor, int maxLine, int padingTop,
                             String typeFaceName) {
        this.id = id;
        this.textSize = textSize;
        this.textColor = textColor;
        this.maxLine = maxLine;
        this.padingTop = padingTop;
        this.typeFaceName = typeFaceName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(textSize);
        dest.writeInt(textColor);
        dest.writeInt(maxLine);
        dest.writeInt(padingTop);
        dest.writeString(typeFaceName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NovelTextDrawInfo> CREATOR = new Creator<NovelTextDrawInfo>() {
        @Override
        public NovelTextDrawInfo createFromParcel(Parcel in) {
            return new NovelTextDrawInfo(in);
        }

        @Override
        public NovelTextDrawInfo[] newArray(int size) {
            return new NovelTextDrawInfo[size];
        }
    };

    public float getTextSize() {
        return textSize;
    }

    public boolean setTextSize(float textSize) {
        MiLog.i( "字体=" + textSize);
        if (textSize < 14) {
            return false;
        } else if (textSize > 34) {
            return false;
        } else {
            this.textSize = textSize;
        }
        DBManage.saveNovelTextViewConfig(this);
        return true;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        DBManage.saveNovelTextViewConfig(this);
    }

    public int getPadingTop() {
        return padingTop;
    }

    public void setPadingTop(int padingTop) {
        this.padingTop = padingTop;
        DBManage.saveNovelTextViewConfig(this);
    }

    public String getTypeFaceName() {
        return typeFaceName;
    }

    public boolean setTypeFaceName(String typeFaceName) {
        if (typeFaceName.equals(this.typeFaceName)) {
            return false;
        }
        this.typeFaceName = typeFaceName;
        DBManage.saveNovelTextViewConfig(this);
        return true;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        DBManage.saveNovelTextViewConfig(this);
    }

    public Long getId() {
        return this.id;
    }

    public int getTextSizeSp() {
        return (int) SystemUtil.px2sp(AppContext.application, textSize);
    }

    public void setId(Long id) {
        this.id = id;
    }


}
