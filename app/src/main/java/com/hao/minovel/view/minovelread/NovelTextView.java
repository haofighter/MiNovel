package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;


import com.hao.minovel.utils.TypeFaceUtils;

import java.util.List;


public class NovelTextView extends AppCompatTextView implements Observer<NovelTextViewHelp> {
    NovelPageInfo novelPageInfo;
    NovelTextViewHelp novelTextViewHelp;

    public NovelTextView(Context context) {
        this(context, null);
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHelp(attrs, defStyleAttr);
    }

    public NovelTextViewHelp getNovelTextViewHelp() {
        return novelTextViewHelp;
    }

    private void initHelp(AttributeSet attrs, int defStyleAttr) {
        NovelTextViewHelp.create(this, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        NovelTextViewHelp.create(this, null, 0).setViewWidthAndHeight(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            List<String> textArray = novelPageInfo.getPageContent();
//        super.onDraw(canvas);
            Paint paint = getPaint();
            paint.setTextSize(novelTextViewHelp.textSize);
            Log.i("显示", "文字大小：" + novelTextViewHelp.textSize);
            paint.setFakeBoldText(false);
            paint.setTypeface(TypeFaceUtils.getTypeFaceByName(novelTextViewHelp.typefaceName));
            paint.setColor(novelTextViewHelp.textColor);
            if (!novelTextViewHelp.orientationVer) {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = novelTextViewHelp.offsetVar + (i % novelTextViewHelp.lineNum) * (novelTextViewHelp.textSize + novelTextViewHelp.lineSpacingExtra) + novelTextViewHelp.textSize;//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = novelTextViewHelp.offsetHor + (novelTextViewHelp.textSize + novelTextViewHelp.wordSpacingExtra) * j + (int) (i / novelTextViewHelp.lineNum) * novelTextViewHelp.viewWidth;
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            } else {
                for (int i = 0; i < textArray.size(); i++) {
                    float drawTextY = novelTextViewHelp.offsetVar + i * (novelTextViewHelp.textSize + novelTextViewHelp.lineSpacingExtra) + novelTextViewHelp.textSize;//间距的数量比文字行数少一行
                    for (int j = 0; j < textArray.get(i).length(); j++) {
                        float drawTextX = novelTextViewHelp.offsetHor + (novelTextViewHelp.textSize + novelTextViewHelp.wordSpacingExtra) * j;
                        canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("NovelTextView", "ondraw报错:" + e.getMessage());
        }
    }


    public NovelPageInfo getNovelPageInfo() {
        return novelPageInfo;
    }

    public void setNovelPageInfo(NovelPageInfo novelPageInfo) {
        this.novelPageInfo = novelPageInfo;
    }

    @Override
    public void onChanged(NovelTextViewHelp novelTextViewHelp) {
        this.novelTextViewHelp = novelTextViewHelp;
        this.novelTextViewHelp.setViewWidthAndHeight(novelTextViewHelp.viewWidth, novelTextViewHelp.viewhigh);
        invalidate();
    }
}
