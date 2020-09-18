package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;


import com.hao.minovel.R;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;


public class NovelTextView extends AppCompatTextView {
    List<String> textArray = new ArrayList<>();
    float wordSpacingExtra;//字间距
    float textPadingVar = 0;//垂直方向的间隔距离
    float textPadingHor = 0;//横向方向的间隔距离
    float textPadingleft = 0;//文字距离左边距离
    float textPadingright = 0;//文字距离右边距离
    float textPadingtop = 0;//文字距离上边距离
    float textPadingbottom = 0;//文字距离下边距离
    float offsetHor = 0;//画布横向方向偏移量
    float offsetVar = 0;//画布垂直方向偏移量
    float lineSpacingExtra;//行间距
    boolean orientationVer = false;//是否为横屏
    String typefaceName;
    int lineTextNum;//每行字数
    int lineNum;//容纳的行数

    public NovelTextView(Context context) {
        this(context, null);
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.NovelTextView,
                    defStyleAttr,
                    0);
            lineSpacingExtra = ta.getDimension(R.styleable.NovelTextView_line_spacing, SystemUtil.sp2px(getContext(), 11f));
            wordSpacingExtra = ta.getDimension(R.styleable.NovelTextView_word_spacing, SystemUtil.sp2px(getContext(), 2f));
            textPadingleft = ta.getDimension(R.styleable.NovelTextView_padding_left, SystemUtil.sp2px(getContext(), 2f));
            textPadingright = ta.getDimension(R.styleable.NovelTextView_padding_right, SystemUtil.sp2px(getContext(), 2f));
            textPadingtop = ta.getDimension(R.styleable.NovelTextView_padding_top, SystemUtil.sp2px(getContext(), 2f));
            textPadingbottom = ta.getDimension(R.styleable.NovelTextView_padding_bottom, SystemUtil.sp2px(getContext(), 2f));
            orientationVer = ta.getBoolean(R.styleable.NovelTextView_orientationVer, false);
            int typeface = ta.getInt(R.styleable.NovelTextView_typefaceName, 0);
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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initViewConfig();
    }

    private void initViewConfig() {
        if (getWidth() == 0 || getHeight() == 0) {
            throw new NullPointerException("填充的界面参数中宽高为0");
        }
        //绘制文字空间的垂直方向的大小
        float textContentVar = getHeight() - textPadingtop - textPadingbottom;
        //绘制文字空间的横向方向的大小
        float textContentHor = getWidth() - textPadingleft - textPadingright;
        //计算的每行容纳的文字大小
        lineTextNum = (int) (textContentHor / (getTextSize() + wordSpacingExtra));
        //计算没页容纳文字行数
        lineNum = (int) (textContentVar / (getTextSize() + lineSpacingExtra));

        //计算出去边缘距离和文字占用的位置剩余的位置 并计算出每页文字的位置
        //文本垂直方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingVar = (textContentVar - lineNum * (getTextSize() + lineSpacingExtra)) / 2;
        offsetVar = textPadingVar;
        //文本水平方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingHor = (textContentHor - lineTextNum * (getTextSize() + wordSpacingExtra) + wordSpacingExtra) / 2;//由于最后一个字的位置中包含了一个间距 在调整文字位置时需要进行位置处理 所以需要+上wordSpacingExtra
        offsetHor = textPadingHor + textPadingleft;

        Log.i("text", "边缘间距：" + textPadingVar + "             " + textPadingtop + "    文字：" + lineNum * (getTextSize() + lineSpacingExtra) + "     总高度：" + textContentVar);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Log.i("显示", this.toString() + "onDraw");
            if (textArray.size() == 0 || lineNum == 0 || lineTextNum == 0) {
                return;
            }
            Paint paint = getPaint();
            paint.setTextSize(getTextSize());
            paint.setFakeBoldText(false);
            paint.setTypeface(TypeFaceUtils.getTypeFaceByName(typefaceName));
            paint.setColor(getCurrentTextColor());
            if (!orientationVer) {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = offsetVar + (i % lineNum) * (getTextSize() + lineSpacingExtra) + getTextSize();//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = offsetHor + (getTextSize() + wordSpacingExtra) * j + (int) (i / lineNum) * getWidth();
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            } else {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = offsetVar + i * (getTextSize() + lineSpacingExtra) + getTextSize();//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = offsetHor + (getTextSize() + wordSpacingExtra) * j;
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("NovelTextView", "ondraw报错:" + e.getMessage());
        }
    }


    private List<String> fromateArray(String content) {
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
        Log.i("显示", "格式化：" + lineNum + "      " + lineSpacingExtra);
        return textArray;
    }


    public boolean setDate(String content, int index) {
        List<String> strings = fromateArray(content);
        Log.i("显示", strings.size() + "      " + lineNum);
        if (strings.size() > (index + 1) * lineNum) {
            int start = index * lineNum;
            int end = (index + 1) * lineNum;
            textArray = strings.subList(start, end);
            invalidate();
            return true;
        } else if (strings.size() >= index * lineNum && strings.size() < (index + 1) * lineNum) {
            int start = index * lineNum;
            int end = strings.size();
            textArray = strings.subList(start, end);
            invalidate();
            return false;
        } else {
            return false;
        }
    }

    public List<String> getTextArray() {
        return textArray;
    }
}
