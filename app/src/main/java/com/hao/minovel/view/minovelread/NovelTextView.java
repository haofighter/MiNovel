package com.hao.minovel.view.minovelread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;


import com.hao.minovel.log.MiLog;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class NovelTextView extends AppCompatTextView {

    public NovelTextView(Context context) {
        this(context, null);
    }

    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            TextPaint paint = getPaint();
            paint.setColor(getTextColors().getDefaultColor());
            Layout layout = new StaticLayout(getText(), paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
            layout.draw(canvas);
        } catch (Exception e) {
            MiLog.i( "ondraw报错:" + e.getMessage());
        }
    }
}
