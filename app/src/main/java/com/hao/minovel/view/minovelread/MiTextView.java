package com.hao.minovel.view.minovelread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hao.minovel.utils.SystemUtil;

import java.util.Observable;
import java.util.Observer;

@SuppressLint("AppCompatCustomView")
public class MiTextView extends TextView implements Observer {

    public MiTextView(Context context) {
        super(context);
    }

    public MiTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setConfig(NovelTextViewHelp novelTextViewHelp) {
        update(null, novelTextViewHelp);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
//
//    @Override
//    public void update(Observable o, Object arg) {
//        if (arg instanceof NovelTextViewHelp) {
//            setTypeface(((NovelTextViewHelp) arg).getTypeface());
//            setTextSize(SystemUtil.px2sp(getContext(), ((NovelTextViewHelp) arg).textSize));
//            setTextColor(((NovelTextViewHelp) arg).textColor);
//        }
//    }
}
