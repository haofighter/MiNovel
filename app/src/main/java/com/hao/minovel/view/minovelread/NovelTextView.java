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

public class NovelTextView extends AppCompatTextView {
    NovelPageInfo novelPageInfo;
    private List<String> textArray = new ArrayList<>();//每一行数据为一个元素  以行数据为单位
    TextViewHelper textViewHelper;

    public NovelTextView(Context context) {
        this(context, null);
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        textViewHelper = TextViewHelper.create(this, attrs, defStyleAttr);
    }




//    @Override
//    protected void onDraw(Canvas canvas) {
////        super.onDraw(canvas);
//        Paint paint = getPaint();
//        paint.setTextSize(textViewHelper.textSize);
//        Log.i("显示", "文字大小：" + textViewHelper.textSize);
//        paint.setFakeBoldText(false);
//        paint.setTypeface(TypeFaceUtils.getTypeFaceByName(textViewHelper.typefaceName));
//        paint.setColor(textViewHelper.textColor);
//        if (!textViewHelper.orientationVer) {
//            for (int i = 0; i < textArray.size(); i++) {
//                float drawTextY = textViewHelper.offsetVar + (i % textViewHelper.lineNum) * (textViewHelper.textSize + textViewHelper.lineSpacingExtra) + textViewHelper.textSize;//间距的数量比文字行数少一行
//                for (int j = 0; j < textArray.get(i).length(); j++) {
//                    float drawTextX = textViewHelper.offsetHor + (textViewHelper.textSize + textViewHelper.wordSpacingExtra) * j + (int) (i / textViewHelper.lineNum) * textViewHelper.viewWidth;
//                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
//                }
//            }
//        } else {
//            for (int i = 0; i < textArray.size(); i++) {
//                float drawTextY = textViewHelper.offsetVar + i * (textViewHelper.textSize + textViewHelper.lineSpacingExtra) + textViewHelper.textSize;//间距的数量比文字行数少一行
//                for (int j = 0; j < textArray.get(i).length(); j++) {
//                    float drawTextX = textViewHelper.offsetHor + (textViewHelper.textSize + textViewHelper.wordSpacingExtra) * j;
//                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
//                }
//            }
//        }
//    }




    public void setDate(NovelPageInfo novelPageInfo) {
        this.novelPageInfo = novelPageInfo;
        textArray = novelPageInfo.getPagecontent();
    }

    public NovelPageInfo getNovelPageInfo() {
        return novelPageInfo;
    }

}
