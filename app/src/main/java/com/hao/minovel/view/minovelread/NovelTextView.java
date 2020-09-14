package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import com.hao.lib.Util.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;

public class NovelTextView extends AppCompatTextView {
    NovelPageInfo novelPageInfo;
    private List<String> textArray = new ArrayList<>();//每一行数据为一个元素  以行数据为单位
    MiNovelTextViewConfig miTextViewConfig = MiNovelTextViewConfig.getDefoutConfig();

    public NovelTextView(Context context) {
        this(context, null);
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(MiNovelTextViewConfig miTextViewConfig) {
        this.miTextViewConfig = miTextViewConfig;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Paint paint = getPaint();
        paint.setTextSize(miTextViewConfig.textSize);
        Log.i("显示", "文字大小：" + miTextViewConfig.textSize);
        paint.setFakeBoldText(false);
        paint.setTypeface(TypeFaceUtils.getTypeFaceByName(miTextViewConfig.typefaceName));
        paint.setColor(miTextViewConfig.textColor);
        if (!miTextViewConfig.orientationVer) {
            for (int i = 0; i < textArray.size(); i++) {
                float drawTextY = miTextViewConfig.offsetVar + (i % miTextViewConfig.lineNum) * (miTextViewConfig.textSize + miTextViewConfig.lineSpacingExtra) + miTextViewConfig.textSize;//间距的数量比文字行数少一行
                for (int j = 0; j < textArray.get(i).length(); j++) {
                    float drawTextX = miTextViewConfig.offsetHor + (miTextViewConfig.textSize + miTextViewConfig.wordSpacingExtra) * j + (int) (i / miTextViewConfig.lineNum) * miTextViewConfig.viewWidth;
                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                }
            }
        } else {
            for (int i = 0; i < textArray.size(); i++) {
                float drawTextY = miTextViewConfig.offsetVar + i * (miTextViewConfig.textSize + miTextViewConfig.lineSpacingExtra) + miTextViewConfig.textSize;//间距的数量比文字行数少一行
                for (int j = 0; j < textArray.get(i).length(); j++) {
                    float drawTextX = miTextViewConfig.offsetHor + (miTextViewConfig.textSize + miTextViewConfig.wordSpacingExtra) * j;
                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                }
            }
        }
    }

    public MiNovelTextViewConfig getMiTextViewConfig() {
        return miTextViewConfig;
    }


    public void setDate(NovelPageInfo novelPageInfo) {
        this.novelPageInfo = novelPageInfo;
        textArray = novelPageInfo.getPagecontent();
    }

    public NovelPageInfo getNovelPageInfo() {
        return novelPageInfo;
    }

}
