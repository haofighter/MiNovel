package com.hao.roundconrtolview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ControlSurfaceView extends ViewGroup {
    View mainChild;
    int padding = 10;
    ShowType showType = ShowType.crl;
    ShowState showState = ShowState.in;

    int theBigWidth = 0;
    int theBigHeight = 0;

    enum ShowType {
        ver, hor, crl
    }

    enum ShowState {
        out, in
    }

    public ControlSurfaceView(Context context) {
        super(context);
    }

    public ControlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("ControlSurfaceView", getTag() + "  onMeasure  left:" + getLeft() + "     top:" + getTop() + "     right:" + getRight() + "      bottom:" + getBottom() + "       Width：" + getWidth() + "        Height：" + getHeight() + "      MeasuredWidth：" + getMeasuredWidth() + "    MeasuredHeight：" + getMeasuredHeight());
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = getPaddingLeft() + getPaddingRight();
        int measureHeight = getPaddingTop() + getPaddingBottom();


        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getMeasuredWidth() > theBigWidth) {
                theBigWidth = getChildAt(i).getMeasuredWidth();
            }
            if (getChildAt(i).getMeasuredHeight() > theBigHeight) {
                theBigHeight = getChildAt(i).getMeasuredHeight();
            }
        }
        if (showType == ShowType.crl && getChildCount() < 2) {
            showType = ShowType.hor;
        }
        switch (showType) {
            case crl:
                if (getChildCount() >= 3) {
                    measureWidth += (theBigWidth + padding) * 2 - padding;
                    measureHeight += (theBigHeight + padding) * 3 - padding;
                } else if (getChildCount() > 4) {
                    measureHeight += (theBigWidth + padding) * getChildCount() - padding;
                    measureWidth += (theBigWidth + padding) * getChildCount() - padding;
                }
                break;
            case hor:
                measureWidth += theBigWidth * getChildCount() - padding * (getChildCount() - 1);
                measureHeight += theBigHeight;
                break;
            case ver:
                measureWidth += theBigWidth;
                measureHeight += theBigHeight * getChildCount() + padding * (getChildCount() - 1);
                break;
        }

        Log.i("ControlSurfaceView", "theBigWidth:" + theBigWidth + "       theBigHeight:" + theBigHeight + "     measureWidth:" + measureWidth + "       measureHeight:" + measureHeight);
        //设置view大小
        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ControlSurfaceView", getTag() + "      onLayout left:" + l + "     top:" + t + "     right:" + r + "      bottom:" + b + "       Width：" + getWidth() + "        Height：" + getHeight());
        for (int i = 0; i < getChildCount(); i++) {
            int nowLeft = 0;
            int nowTop = 0;
            int nowRight = 0;
            int nowBottom = 0;
            switch (showType) {
                case crl:
                    if (getChildCount() >= 3) {
                        if (i == 0) {
                            nowLeft = r - getPaddingRight() - getChildAt(i).getMeasuredWidth();
                            nowRight = r - getPaddingRight();
                            nowTop = (b - t) / 2 - getChildAt(i).getMeasuredHeight() / 2;
                            nowBottom = (b - t) / 2 + getChildAt(i).getMeasuredHeight() / 2;
                        } else {
                            int degree = 180 / (getChildCount() - 1)/2;
                        }
                    } else if (getChildCount() > 4) {
                        if (i == 0) {
                            nowLeft = (r - l) / 2 - getChildAt(i).getMeasuredWidth() / 2;
                            nowRight = (r - l) / 2 + getChildAt(i).getMeasuredWidth() / 2;
                            nowTop = (b - t) / 2 - getChildAt(i).getMeasuredHeight() / 2;
                            nowBottom = (b - t) / 2 + getChildAt(i).getMeasuredHeight() / 2;
                        } else {

                        }
                    }
                    Log.i("ControlSurfaceView", "字view      onLayout left:" + nowLeft + "     top:" + nowTop
                            + "     right:" + nowRight + "      bottom:" + nowBottom + "       Width：" + getWidth() + "        Height：" + getHeight());

                    break;
                case hor:
                    nowLeft = l + getPaddingLeft() + (theBigWidth + padding) * i;
                    nowTop = t + getPaddingTop();
                    nowRight = l + getPaddingLeft() + (theBigWidth + padding) * i + theBigWidth;
                    nowBottom = b - getPaddingBottom();
                    break;
                case ver:
                    nowLeft = l + getPaddingLeft();
                    nowTop = t + getPaddingTop() + (theBigHeight + padding) * i;
                    nowRight = r - getPaddingRight();
                    nowBottom = t + getPaddingTop() + (theBigHeight + padding) * i + theBigHeight;
                    break;
            }
            getChildAt(i).layout(nowLeft, nowTop, nowRight, nowBottom);
            if (i == 0) {
                mainChild = getChildAt(i);
            }
        }
    }


    public void addMainButton(View inflate) {
        addView(inflate, 0);
        requestLayout();
    }

}
