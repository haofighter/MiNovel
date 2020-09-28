package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;


import com.hao.minovel.utils.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class NovelTextView extends AppCompatTextView implements Observer {
    List<String> textArray = new ArrayList<>();
    private NovelTextViewHelp novelTextViewHelp;

    public NovelTextView(Context context) {
        this(context, null);
    }

    public NovelTextView(NovelTextViewHelp novelTextViewHelp, Context context) {
        this(context);
        this.novelTextViewHelp = novelTextViewHelp;
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (novelTextViewHelp == null) {
            novelTextViewHelp = new NovelTextViewHelp().initConfig(this, attrs, defStyleAttr);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        novelTextViewHelp.initViewSize(getWidth(), getHeight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Log.i("显示", this.toString() + "onDraw");
            if (textArray.size() == 0 || novelTextViewHelp.lineNum == 0 || novelTextViewHelp.lineTextNum == 0) {
                return;
            }
            Paint paint = getPaint();
            paint.setTextSize(novelTextViewHelp.getTextSize());
            paint.setFakeBoldText(false);
            paint.setTypeface(novelTextViewHelp.getTypeface());
            paint.setColor(novelTextViewHelp.textColor);
            if (!novelTextViewHelp.orientationVer) {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = novelTextViewHelp.offsetVar + (i % novelTextViewHelp.lineNum) * (getTextSize() + novelTextViewHelp.lineSpacingExtra) + getTextSize();//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = novelTextViewHelp.offsetHor + (getTextSize() + novelTextViewHelp.wordSpacingExtra) * j + (int) (i / novelTextViewHelp.lineNum) * getWidth();
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            } else {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = novelTextViewHelp.offsetVar + i * (getTextSize() + novelTextViewHelp.lineSpacingExtra) + getTextSize();//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = novelTextViewHelp.offsetHor + (getTextSize() + novelTextViewHelp.wordSpacingExtra) * j;
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("NovelTextView", "ondraw报错:" + e.getMessage());
        }
    }


//    public boolean setDate(String content, int index) {
//        List<String> strings = fromateArray(content);
//        Log.i("显示", strings.size() + "      " + lineNum);
//        if (strings.size() > (index + 1) * lineNum) {
//            int start = index * lineNum;
//            int end = (index + 1) * lineNum;
//            textArray = strings.subList(start, end);
//            invalidate();
//            return true;
//        } else if (strings.size() >= index * lineNum && strings.size() < (index + 1) * lineNum) {
//            int start = index * lineNum;
//            int end = strings.size();
//            textArray = strings.subList(start, end);
//            invalidate();
//            return false;
//        } else {
//            return false;
//        }
//    }

    /**
     * @param textArray 当前章节处理好的数据
     * @return 是否正确获取到数据
     */
    public boolean setTextArray(List<String> textArray, int page) {
        try {
            int end = (page + 1) * novelTextViewHelp.lineNum > textArray.size() ? textArray.size() : (page + 1) * novelTextViewHelp.lineNum;
            this.textArray = textArray.subList(page * novelTextViewHelp.lineNum, end);
            invalidate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getTextArray() {
        return textArray;
    }

    public NovelTextViewHelp getNovelTextViewHelp() {
        return novelTextViewHelp;
    }

    public void setNovelTextViewHelp(NovelTextViewHelp novelTextViewHelp) {
        this.novelTextViewHelp = novelTextViewHelp;
    }


    @Override
    public void update(Observable o, Object arg) {
        this.novelTextViewHelp = (NovelTextViewHelp) arg;
        invalidate();
    }
}
