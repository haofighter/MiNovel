package com.hao.minovel.view.minovelread;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class NovelTextViewHelp {
    float lineSpacingExtra;//行间距
    NovelTextView novelTextView;//当前绑定的textview
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

    public NovelTextViewHelp(NovelTextView novelTextView) {
        this.novelTextView = novelTextView;
    }

    /**
     * 初始化NovelTextView的配置信息
     * 只有novelTextViewHelp为空的时候才会进行一次初始化
     * 当保存的novelTextViewHelp不为空时不做任何处理
     *
     * @param novelTextView
     * @param attrs
     * @param defStyleAttr
     * @return
     */
    public static NovelTextViewHelp create(NovelTextView novelTextView, AttributeSet attrs, int defStyleAttr) {
        NovelTextViewHelp novelTextViewHelp = new NovelTextViewHelp(novelTextView);
        if (attrs != null) {
            TypedArray ta = novelTextView.getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.NovelTextView,
                    defStyleAttr,
                    0);
            novelTextViewHelp.lineSpacingExtra = ta.getDimension(R.styleable.NovelTextView_line_spacing, SystemUtil.sp2px(novelTextView.getContext(), 11f));
            novelTextViewHelp.wordSpacingExtra = ta.getDimension(R.styleable.NovelTextView_word_spacing, SystemUtil.sp2px(novelTextView.getContext(), 2f));
            novelTextViewHelp.textPadingleft = ta.getDimension(R.styleable.NovelTextView_padding_left, SystemUtil.sp2px(novelTextView.getContext(), 2f));
            novelTextViewHelp.textPadingright = ta.getDimension(R.styleable.NovelTextView_padding_right, SystemUtil.sp2px(novelTextView.getContext(), 2f));
            novelTextViewHelp.textPadingtop = ta.getDimension(R.styleable.NovelTextView_padding_top, SystemUtil.sp2px(novelTextView.getContext(), 2f));
            novelTextViewHelp.textPadingbottom = ta.getDimension(R.styleable.NovelTextView_padding_bottom, SystemUtil.sp2px(novelTextView.getContext(), 2f));
            novelTextViewHelp.orientationVer = ta.getBoolean(R.styleable.NovelTextView_orientationVer, false);
            int typeface = ta.getInt(R.styleable.NovelTextView_typefaceName, 0);
            if (typeface == 0) {
                novelTextViewHelp.typefaceName = null;
            } else if (typeface == 1) {
                novelTextViewHelp.typefaceName = "HWCY.TTF";
            } else if (typeface == 2) {
                novelTextViewHelp.typefaceName = "HYCYJ.ttf";
            }
            novelTextViewHelp.calculateLineInfo();
        } else {
            Log.w("TextViewHelper", "为获取到任何属性");
        }
        return novelTextViewHelp;
    }


    public void setViewWidthAndHeight(float width, float height) {
        this.viewWidth = width;
        this.viewhigh = height;
        calculateLineInfo();
    }

    private View createNewView(NovelPageInfo novelPageInfo) {
        if (novelTextView != null) {
            View v = LayoutInflater.from(novelTextView.getContext()).inflate(R.layout.read_novel_page, null);
            NovelTextView novelTextView = v.findViewById(R.id.novel_content);
            novelTextView.setNovelPageInfo(novelPageInfo);
            return v;
        }
        return null;
    }

    public ChapterInfo initDate(NovelTextView novelTextView, NovelChapter novelChapter) {
        List<String> textArray = new ArrayList<>();
        String[] strs = novelChapter.getChapterContent().split("\n");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() <= novelTextView.novelTextViewHelp.lineTextNum) {
                textArray.add(strs[i]);
                continue;
            }
            if (strs[i].length() % novelTextView.novelTextViewHelp.lineTextNum == 0) {//刚好整除  此段文字刚好后被整数行容纳
                for (int j = 0; j < strs[i].length() / novelTextView.novelTextViewHelp.lineTextNum; j++) {
                    textArray.add(strs[i].substring(j * novelTextView.novelTextViewHelp.lineTextNum, (j + 1) * novelTextView.novelTextViewHelp.lineTextNum));
                }
            } else {
                int needLineNum = strs[i].length() / novelTextView.novelTextViewHelp.lineTextNum + 1;
                for (int k = 0; k < needLineNum; k++) {//获取到每段的字符串 判断能够容纳几行
                    String nowLineText = "";
                    if (k < needLineNum - 1) {
                        nowLineText = strs[i].substring(k * novelTextView.novelTextViewHelp.lineTextNum, (k + 1) * novelTextView.novelTextViewHelp.lineTextNum);
                    } else {
                        nowLineText = strs[i].substring(k * novelTextView.novelTextViewHelp.lineTextNum, strs[i].length());
                    }
                    textArray.add(nowLineText);
                    Log.i("小说章节内容", nowLineText);
                }
            }
        }
        ChapterInfo chapterInfo = new ChapterInfo();
        chapterInfo.setNowChapterUrl(novelChapter.getChapterUrl());
        if (novelTextView.novelTextViewHelp.lineNum != 0) {
            NovelPageInfo novelPageInfo = new NovelPageInfo();
            for (int i = 0; i < textArray.size(); i++) {//遍历当前章节已进行行数处理的数据
                if (i % novelTextView.novelTextViewHelp.lineNum == 0) {//如果于行数取余等于0则表示需要新建一页
                    novelPageInfo = new NovelPageInfo();
                    novelPageInfo.setNovelChapterUrl(novelChapter.getChapterUrl());
                    novelPageInfo.setNoveChapterListUrl(novelChapter.getNovelChapterListUrl());
                    novelPageInfo.getPageContent().add(textArray.get(i));
                    novelPageInfo.setPage(i / novelTextView.novelTextViewHelp.lineNum);
                    novelPageInfo.setNovelTextView(createNewView(novelPageInfo));
                    chapterInfo.getNovelPageInfos().add(novelPageInfo);
                } else {
                    novelPageInfo.getPageContent().add(textArray.get(i));
                    novelPageInfo.setNovelChapterUrl(novelChapter.getChapterUrl());
                    novelPageInfo.setNoveChapterListUrl(novelChapter.getNovelChapterListUrl());
                    novelPageInfo.setPage(i / novelTextView.novelTextViewHelp.lineNum);
                }
            }

        }
        return chapterInfo;
    }


    /**
     * 计算当前所绑定的{@com.hao.minovel.view.minovelread.NovelTextView}绘制所需要的信息
     *
     * @return
     */
    private NovelTextViewHelp calculateLineInfo() {
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
}
