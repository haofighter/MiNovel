package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NovelTextViewHelp implements Parcelable {
    @Unique
    @Id(autoincrement = true)
    private Long id;
    protected float wordSpacingExtra;//字间距
    protected float textPadingVar = 0;//垂直方向的间隔距离
    protected float textPadingHor = 0;//横向方向的间隔距离
    protected float textPadingleft = 0;//文字距离左边距离
    protected float textPadingright = 0;//文字距离右边距离
    protected float textPadingtop = 0;//文字距离上边距离
    protected float textPadingbottom = 0;//文字距离下边距离
    protected float offsetHor = 0;//画布横向方向偏移量
    protected float offsetVar = 0;//画布垂直方向偏移量
    protected float lineSpacingExtra;//行间距
    protected int lineTextNum;//每行字数
    protected int lineNum;//容纳的行数
    protected float textSize;//字体大小
    protected boolean orientationVer = false;//是否为横屏
    protected String typefaceName;
    protected int textColor;//颜色
    protected int allPage;
    protected float height;
    protected float width;

    protected NovelTextViewHelp() {
    }

    protected NovelTextViewHelp(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        wordSpacingExtra = in.readFloat();
        textPadingVar = in.readFloat();
        textPadingHor = in.readFloat();
        textPadingleft = in.readFloat();
        textPadingright = in.readFloat();
        textPadingtop = in.readFloat();
        textPadingbottom = in.readFloat();
        offsetHor = in.readFloat();
        offsetVar = in.readFloat();
        lineSpacingExtra = in.readFloat();
        lineTextNum = in.readInt();
        lineNum = in.readInt();
        textSize = in.readFloat();
        orientationVer = in.readByte() != 0;
        typefaceName = in.readString();
        textColor = in.readInt();
        allPage = in.readInt();
        height = in.readFloat();
        width = in.readFloat();
    }

    @Generated(hash = 1799491719)
    public NovelTextViewHelp(Long id, float wordSpacingExtra, float textPadingVar, float textPadingHor, float textPadingleft, float textPadingright, float textPadingtop,
                             float textPadingbottom, float offsetHor, float offsetVar, float lineSpacingExtra, int lineTextNum, int lineNum, float textSize, boolean orientationVer,
                             String typefaceName, int textColor, int allPage, float height, float width) {
        this.id = id;
        this.wordSpacingExtra = wordSpacingExtra;
        this.textPadingVar = textPadingVar;
        this.textPadingHor = textPadingHor;
        this.textPadingleft = textPadingleft;
        this.textPadingright = textPadingright;
        this.textPadingtop = textPadingtop;
        this.textPadingbottom = textPadingbottom;
        this.offsetHor = offsetHor;
        this.offsetVar = offsetVar;
        this.lineSpacingExtra = lineSpacingExtra;
        this.lineTextNum = lineTextNum;
        this.lineNum = lineNum;
        this.textSize = textSize;
        this.orientationVer = orientationVer;
        this.typefaceName = typefaceName;
        this.textColor = textColor;
        this.allPage = allPage;
        this.height = height;
        this.width = width;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeFloat(wordSpacingExtra);
        dest.writeFloat(textPadingVar);
        dest.writeFloat(textPadingHor);
        dest.writeFloat(textPadingleft);
        dest.writeFloat(textPadingright);
        dest.writeFloat(textPadingtop);
        dest.writeFloat(textPadingbottom);
        dest.writeFloat(offsetHor);
        dest.writeFloat(offsetVar);
        dest.writeFloat(lineSpacingExtra);
        dest.writeInt(lineTextNum);
        dest.writeInt(lineNum);
        dest.writeFloat(textSize);
        dest.writeByte((byte) (orientationVer ? 1 : 0));
        dest.writeString(typefaceName);
        dest.writeInt(textColor);
        dest.writeInt(allPage);
        dest.writeFloat(height);
        dest.writeFloat(width);
    }

    public static final Creator<NovelTextViewHelp> CREATOR = new Creator<NovelTextViewHelp>() {
        @Override
        public NovelTextViewHelp createFromParcel(Parcel in) {
            return new NovelTextViewHelp(in);
        }

        @Override
        public NovelTextViewHelp[] newArray(int size) {
            return new NovelTextViewHelp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public int getTextSizeSp() {
        return (int) SystemUtil.px2sp(AppContext.application, textSize);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = SystemUtil.sp2px(AppContext.application, textSize);
        DBManage.saveNovelTextViewConfig(this);
    }

    public int getLineSpacingExtraSp() {
        return (int) SystemUtil.px2sp(AppContext.application, lineSpacingExtra);
    }

    public void setLineSpacingExtra(float lineSpacingExtra) {
        this.lineSpacingExtra = SystemUtil.sp2px(AppContext.application, lineSpacingExtra);
        DBManage.saveNovelTextViewConfig(this);
    }

    public NovelTextViewHelp initConfig(NovelTextView novelTextView, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            Context mContext = novelTextView.getContext();
            TypedArray ta = mContext.obtainStyledAttributes(
                    attrs,
                    R.styleable.NovelTextView,
                    defStyleAttr,
                    0);
            lineSpacingExtra = ta.getDimension(R.styleable.NovelTextView_line_spacing, SystemUtil.sp2px(mContext, 11f));
            wordSpacingExtra = ta.getDimension(R.styleable.NovelTextView_word_spacing, SystemUtil.sp2px(mContext, 2f));
            textPadingleft = ta.getDimension(R.styleable.NovelTextView_padding_left, SystemUtil.sp2px(mContext, 2f));
            textPadingright = ta.getDimension(R.styleable.NovelTextView_padding_right, SystemUtil.sp2px(mContext, 2f));
            textPadingtop = ta.getDimension(R.styleable.NovelTextView_padding_top, SystemUtil.sp2px(mContext, 2f));
            textPadingbottom = ta.getDimension(R.styleable.NovelTextView_padding_bottom, SystemUtil.sp2px(mContext, 2f));
            orientationVer = ta.getBoolean(R.styleable.NovelTextView_orientationVer, false);
            int typeface = ta.getInt(R.styleable.NovelTextView_typefaceName, 0);
            textSize = novelTextView.getTextSize() == 0 ? SystemUtil.sp2px(mContext, 15f) : novelTextView.getTextSize();
            textColor = novelTextView.getCurrentTextColor();
            if (typeface == 0) {
                typefaceName = "HYCYJ.ttf";
            } else if (typeface == 1) {
                typefaceName = "HWCY.TTF";
            } else if (typeface == 2) {
                typefaceName = null;
            }
        } else {
            Log.w("TextViewHelper", "为获取到任何属性");
        }
        DBManage.saveNovelTextViewConfig(this);
        return this;
    }

    protected void initViewSize(NovelTextView novelTextView) {
        this.width = novelTextView.getWidth();
        this.height = novelTextView.getHeight();
        initViewConfig();
    }

    protected void initViewConfig() {
        if (height == 0 || width == 0) {
            throw new NullPointerException("填充的界面参数中宽高为0");
        }
        //绘制文字空间的垂直方向的大小
        float textContentVar = height - textPadingtop - textPadingbottom;
        //绘制文字空间的横向方向的大小
        float textContentHor = width - textPadingleft - textPadingright;
        //计算的每行容纳的文字大小
        lineTextNum = (int) (textContentHor / (textSize + wordSpacingExtra));
        //计算没页容纳文字行数
        lineNum = (int) (textContentVar / (textSize + lineSpacingExtra));

        //计算出去边缘距离和文字占用的位置剩余的位置 并计算出每页文字的位置
        //文本垂直方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingVar = (textContentVar - lineNum * (textSize + lineSpacingExtra)) / 2;
        offsetVar = textPadingVar;
        //文本水平方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingHor = (textContentHor - lineTextNum * (textSize + wordSpacingExtra) + wordSpacingExtra) / 2;//由于最后一个字的位置中包含了一个间距 在调整文字位置时需要进行位置处理 所以需要+上wordSpacingExtra
        offsetHor = textPadingHor + textPadingleft;

        Log.i("text", "边缘间距：" + textPadingVar + "             " + textPadingtop + "    文字：" + lineNum * (textSize + lineSpacingExtra) + "     总高度：" + textContentVar);
    }




    public void setTypefaceName(String typefaceName) {
        this.typefaceName = typefaceName;
        DBManage.saveNovelTextViewConfig(this);
    }

    public Typeface getTypeface() {
        return TypeFaceUtils.getTypeFaceByName(typefaceName);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getWordSpacingExtra() {
        return this.wordSpacingExtra;
    }

    public void setWordSpacingExtra(float wordSpacingExtra) {
        this.wordSpacingExtra = wordSpacingExtra;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingVar() {
        return this.textPadingVar;
    }

    public void setTextPadingVar(float textPadingVar) {
        this.textPadingVar = textPadingVar;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingHor() {
        return this.textPadingHor;
    }

    public void setTextPadingHor(float textPadingHor) {
        this.textPadingHor = textPadingHor;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingleft() {
        return this.textPadingleft;
    }

    public void setTextPadingleft(float textPadingleft) {
        this.textPadingleft = textPadingleft;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingright() {
        return this.textPadingright;
    }

    public void setTextPadingright(float textPadingright) {
        this.textPadingright = textPadingright;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingtop() {
        return this.textPadingtop;
    }

    public void setTextPadingtop(float textPadingtop) {
        this.textPadingtop = textPadingtop;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getTextPadingbottom() {
        return this.textPadingbottom;
    }

    public void setTextPadingbottom(float textPadingbottom) {
        this.textPadingbottom = textPadingbottom;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getOffsetHor() {
        return this.offsetHor;
    }

    public void setOffsetHor(float offsetHor) {
        this.offsetHor = offsetHor;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getOffsetVar() {
        return this.offsetVar;
    }

    public void setOffsetVar(float offsetVar) {
        this.offsetVar = offsetVar;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getLineSpacingExtra() {
        return this.lineSpacingExtra;
    }

    public int getLineTextNum() {
        return this.lineTextNum;
    }

    public void setLineTextNum(int lineTextNum) {
        this.lineTextNum = lineTextNum;
        DBManage.saveNovelTextViewConfig(this);
    }

    public int getLineNum() {
        return this.lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
        DBManage.saveNovelTextViewConfig(this);
    }

    public boolean getOrientationVer() {
        return this.orientationVer;
    }

    public void setOrientationVer(boolean orientationVer) {
        this.orientationVer = orientationVer;
        DBManage.saveNovelTextViewConfig(this);
    }

    public String getTypefaceName() {
        return this.typefaceName;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        DBManage.saveNovelTextViewConfig(this);
    }

    public int getAllPage() {
        return this.allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
        DBManage.saveNovelTextViewConfig(this);
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
        DBManage.saveNovelTextViewConfig(this);
    }
}
