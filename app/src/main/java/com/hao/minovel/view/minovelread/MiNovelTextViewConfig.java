package com.hao.minovel.view.minovelread;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.tinker.app.App;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;

public class MiNovelTextViewConfig {
    float lineSpacingExtra;//行间距
    float wordSpacingExtra;//字间距
    float textSize;//文字大小
    int lineTextNum;//每行字数
    int lineNum;//容纳的行数
    float textPadingVar = 0;//垂直方向的间隔距离
    float textPadingHor = 0;//横向方向的间隔距离
    float textPadingleft = 0;//文字距离左边距离
    float textPadingright = 0;//文字距离右边距离
    float textPadingtop = 0;//文字距离上边距离
    float textPadingbottom = 0;//文字距离下边距离
    float offsetHor = 0;//画布横向方向偏移量
    float offsetVar = 0;//画布垂直方向偏移量
    boolean orientationVer = false;//是否为横屏
    float viewhigh = -1;  //view的垂直高度
    float viewWidth = -1;//view的横向高度
    int textColor;
    String typefaceName;
    int background;

    private MiNovelTextViewConfig() {
    }

    static MiNovelTextViewConfig miTextViewConfig;

    public static MiNovelTextViewConfig getDefoutConfig() {
        if (miTextViewConfig == null) {
            miTextViewConfig = new MiNovelTextViewConfig();
            miTextViewConfig.textColor = ContextCompat.getColor(AppContext.application, R.color.novel_text_defult);
            miTextViewConfig.textPadingVar = 0;
            miTextViewConfig.textPadingHor = 0;
            miTextViewConfig.textPadingleft = SystemUtil.dp2px(AppContext.application, 5f);
            miTextViewConfig.textPadingright = SystemUtil.dp2px(AppContext.application, 5f);
            miTextViewConfig.textPadingtop = SystemUtil.dp2px(AppContext.application, 5f);
            miTextViewConfig.textPadingbottom = SystemUtil.dp2px(AppContext.application, 5f);
            miTextViewConfig.setTextSize(SystemUtil.sp2px(AppContext.application, 21f));
            miTextViewConfig.setLineSpacingExtra(SystemUtil.sp2px(AppContext.application, 11f));
            miTextViewConfig.setWordSpacingExtra(SystemUtil.sp2px(AppContext.application, 2f));
            miTextViewConfig.typefaceName = "默认字体";
            miTextViewConfig.background = R.mipmap.bg_readbook_yellow;
            miTextViewConfig.orientationVer = false;
        }
        return miTextViewConfig;
    }

    public static MiNovelTextViewConfig getDefoutConfig(float viewWidth, float viewhigh) {
        if (miTextViewConfig == null) {
            getDefoutConfig();
            miTextViewConfig.viewWidth = viewWidth;
            miTextViewConfig.viewhigh = viewhigh;
        } else {
            miTextViewConfig.viewWidth = viewWidth;
            miTextViewConfig.viewhigh = viewhigh;
        }
        if (viewhigh != 0 && viewWidth != 0) {
            return miTextViewConfig.initViewConfig();
        }
        return miTextViewConfig;
    }

    private MiNovelTextViewConfig initViewConfig() {
        if (viewhigh == 0 || viewWidth == 0) {
            throw new NullPointerException("填充的界面参数中宽高为0");
        }
        //绘制文字空间的垂直方向的大小
        float textContentVar = viewhigh - textPadingtop - textPadingbottom;
        //绘制文字空间的横向方向的大小
        float textContentHor = viewWidth - textPadingleft - textPadingright;
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
        return this;
    }

    public NovelViewInfo initText(NovelChapter novelChapter) {
        List<NovelPageInfo> pageDate = new ArrayList<>();
        List<String> textArray = new ArrayList<>();
        String[] strs = novelChapter.getChapterContent().split("\n");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() <= miTextViewConfig.lineTextNum) {
                textArray.add(strs[i]);
                continue;
            }
            if (strs[i].length() % miTextViewConfig.lineTextNum == 0) {//刚好整除  此段文字刚好后被整数行容纳
                for (int j = 0; j < strs[i].length() / miTextViewConfig.lineTextNum; j++) {
                    textArray.add(strs[i].substring(j * miTextViewConfig.lineTextNum, (j + 1) * miTextViewConfig.lineTextNum));
                }
            } else {
                int needLineNum = strs[i].length() / miTextViewConfig.lineTextNum + 1;
                for (int k = 0; k < needLineNum; k++) {//获取到每段的字符串 判断能够容纳几行
                    String nowLineText = "";
                    if (k < needLineNum - 1) {
                        nowLineText = strs[i].substring(k * miTextViewConfig.lineTextNum, (k + 1) * miTextViewConfig.lineTextNum);
                    } else {
                        nowLineText = strs[i].substring(k * miTextViewConfig.lineTextNum, strs[i].length());
                    }
                    textArray.add(nowLineText);
                    Log.i("小说章节内容", nowLineText);
                }
            }
        }
        if (miTextViewConfig.lineNum != 0) {
            NovelPageInfo novelPageInfo = new NovelPageInfo();
            for (int i = 0; i < textArray.size(); i++) {
                if (i % miTextViewConfig.lineNum == 0) {
                    novelPageInfo = new NovelPageInfo();
                    novelPageInfo.setNovelChapterUrl(novelChapter.getChapterUrl());
                    novelPageInfo.setNoveChapterListUrl(novelChapter.getNovelChapterListUrl());
                    novelPageInfo.addContent(textArray.get(i));
                    novelPageInfo.setPage(i / miTextViewConfig.lineNum);
                    pageDate.add(novelPageInfo);
                } else {
                    novelPageInfo.addContent(textArray.get(i));
                    novelPageInfo.setNovelChapterUrl(novelChapter.getChapterUrl());
                    novelPageInfo.setNoveChapterListUrl(novelChapter.getNovelChapterListUrl());
                    novelPageInfo.setPage(i / miTextViewConfig.lineNum);
                }
            }
        }

        List<View> novelTextViews = new ArrayList<>();
        for (int i = 0; i < pageDate.size(); i++) {
            if (!pageDate.get(i).equals("")) {
                View v = LayoutInflater.from(AppContext.application).inflate(R.layout.novel_content_layout, null);
                NovelTextView miTextView = v.findViewById(R.id.novel_content);
                TextView novel_title = v.findViewById(R.id.novel_title);
                novel_title.setText(novelChapter.getChapterName());
                novel_title.setTextColor(textColor);
                miTextView.init(this);
                miTextView.setTypeface(TypeFaceUtils.getTypeFaceByName(typefaceName));
                v.setBackground(ContextCompat.getDrawable(AppContext.application, background));
                miTextView.setDate(pageDate.get(i));
                novelTextViews.add(v);
            }
        }

        NovelViewInfo novelViewInfo = new NovelViewInfo(novelTextViews, novelChapter.getChapterName());
        return novelViewInfo;
    }

    public MiNovelTextViewConfig setViewWiHi(float viewWidth, float viewhigh) {
        this.viewWidth = viewWidth;
        this.viewhigh = viewhigh;
        return getDefoutConfig(viewWidth, viewhigh);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    public float getTextPadingVar() {
        return textPadingVar;
    }

    public void setTextPadingVar(float textPadingVar) {
        this.textPadingVar = textPadingVar;
    }

    public float getTextPadingHor() {
        return textPadingHor;
    }

    public void setTextPadingHor(float textPadingHor) {
        this.textPadingHor = textPadingHor;
    }

    public float getTextPadingleft() {
        return textPadingleft;
    }

    public void setTextPadingleft(float textPadingleft) {
        this.textPadingleft = textPadingleft;
    }

    public float getTextPadingright() {
        return textPadingright;
    }

    public void setTextPadingright(float textPadingright) {
        this.textPadingright = textPadingright;
    }

    public float getTextPadingtop() {
        return textPadingtop;
    }

    public void setTextPadingtop(float textPadingtop) {
        this.textPadingtop = textPadingtop;
    }

    public float getTextPadingbottom() {
        return textPadingbottom;
    }

    public void setTextPadingbottom(float textPadingbottom) {
        this.textPadingbottom = textPadingbottom;
    }

    public float getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    public void setLineSpacingExtra(float lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
        initViewConfig();
    }

    public float getWordSpacingExtra() {
        return wordSpacingExtra;
    }

    public void setWordSpacingExtra(float wordSpacingExtra) {
        this.wordSpacingExtra = wordSpacingExtra;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        initViewConfig();
    }

    public int getLineTextNum() {
        return lineTextNum;
    }

    public void setLineTextNum(int lineTextNum) {
        this.lineTextNum = lineTextNum;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public float getOffsetHor() {
        return offsetHor;
    }

    public void setOffsetHor(float offsetHor) {
        this.offsetHor = offsetHor;
    }

    public float getOffsetVar() {
        return offsetVar;
    }

    public void setOffsetVar(float offsetVar) {
        this.offsetVar = offsetVar;
    }

    public float getViewhigh() {
        return viewhigh;
    }

    public void setViewhigh(float viewhigh) {
        this.viewhigh = viewhigh;
        initViewConfig();
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
        initViewConfig();
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getTypefaceName() {
        return typefaceName;
    }

    public void setTypefaceName(String typefaceName) {
        this.typefaceName = typefaceName;
    }


}
