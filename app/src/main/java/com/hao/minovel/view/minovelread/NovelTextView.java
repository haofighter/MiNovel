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


import com.hao.minovel.utils.SystemUtil;
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
        novelTextViewHelp.initViewSize(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Log.i("显示", this.toString() + "onDraw");
            TextPaint paint = getPaint();
            paint.setColor(getTextColors().getDefaultColor());
            Layout layout = new StaticLayout(getText(), paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
            layout.draw(canvas);
        } catch (Exception e) {
            Log.e("NovelTextView", "ondraw报错:" + e.getMessage());
        }
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
