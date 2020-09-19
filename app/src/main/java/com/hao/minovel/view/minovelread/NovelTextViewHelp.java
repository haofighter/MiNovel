package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.hao.minovel.R;
import com.hao.minovel.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class NovelTextViewHelp {
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
    protected int allPage;


    public NovelTextViewHelp initConfig(NovelTextView novelTextView, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            Context context = novelTextView.getContext();
            TypedArray ta = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.NovelTextView,
                    defStyleAttr,
                    0);
            lineSpacingExtra = ta.getDimension(R.styleable.NovelTextView_line_spacing, SystemUtil.sp2px(context, 11f));
            wordSpacingExtra = ta.getDimension(R.styleable.NovelTextView_word_spacing, SystemUtil.sp2px(context, 2f));
            textPadingleft = ta.getDimension(R.styleable.NovelTextView_padding_left, SystemUtil.sp2px(context, 2f));
            textPadingright = ta.getDimension(R.styleable.NovelTextView_padding_right, SystemUtil.sp2px(context, 2f));
            textPadingtop = ta.getDimension(R.styleable.NovelTextView_padding_top, SystemUtil.sp2px(context, 2f));
            textPadingbottom = ta.getDimension(R.styleable.NovelTextView_padding_bottom, SystemUtil.sp2px(context, 2f));
            orientationVer = ta.getBoolean(R.styleable.NovelTextView_orientationVer, false);
            int typeface = ta.getInt(R.styleable.NovelTextView_typefaceName, 0);
            textSize = novelTextView.getTextSize() == 0 ? SystemUtil.sp2px(context, 15f) : novelTextView.getTextSize();
            if (typeface == 0) {
                typefaceName = null;
            } else if (typeface == 1) {
                typefaceName = "HWCY.TTF";
            } else if (typeface == 2) {
                typefaceName = "HYCYJ.ttf";
            }
        } else {
            Log.w("TextViewHelper", "为获取到任何属性");
        }
        return this;
    }

    protected void initViewConfig(float height, float width) {
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


    public List<String> fromateArray(String content) {
        List<String> textArray = new ArrayList<>();
        String[] strs = content.split("\n");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() <= lineTextNum) {
                textArray.add(strs[i]);
                continue;
            }
            if (strs[i].length() % lineTextNum == 0) {//刚好整除  此段文字刚好后被整数行容纳
                for (int j = 0; j < strs[i].length() / lineTextNum; j++) {
                    textArray.add(strs[i].substring(j * lineTextNum, (j + 1) * lineTextNum));
                }
            } else {
                int needLineNum = strs[i].length() / lineTextNum + 1;
                for (int k = 0; k < needLineNum; k++) {//获取到每段的字符串 判断能够容纳几行
                    String nowLineText = "";
                    if (k < needLineNum - 1) {
                        nowLineText = strs[i].substring(k * lineTextNum, (k + 1) * lineTextNum);
                    } else {
                        nowLineText = strs[i].substring(k * lineTextNum, strs[i].length());
                    }
                    textArray.add(nowLineText);
                }
            }
        }
        allPage = textArray.size() % lineNum == 0 ? textArray.size() / lineNum : textArray.size() / lineNum + 1;
        return textArray;
    }
}
