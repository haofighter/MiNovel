package com.hao.minovel.view.minovelread;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PullViewLayout extends FrameLayout {
    int dragState = -1;//0  左滑,1 右滑  -1滑动完成
    PullViewLayoutListener pullViewLayoutListener;

    Context mcontext;
    List<NovelViewInfo> allView = new ArrayList<NovelViewInfo>() {
        @Override
        public boolean add(NovelViewInfo novelViewInfo) {
            try {
                for (int i = 0; i < size(); i++) {
                    if (novelViewInfo.getChapterName().equals(get(i).chapterName)) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return super.add(novelViewInfo);
        }
    };
    List<View> head = new ArrayList<>();//前面未填充的view
    List<View> end = new ArrayList<>();//后续未填充的view
    View fristPage;//前一页
    View contentPage;//显示页
    View nextPage;//后一页
    View cachePage;//缓存页 翻页后需要被移除的一页


    public PullViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;
    }


    public void addListViewHead(@Nullable NovelViewInfo v) {
        if (allView.add(v)) {
            head.addAll(0, v.getViews());
            initFirst();
        } else {
            Log.i("阅读界面", "前一章小说章节重复");
        }
    }

    private void initFirst() {
        if (fristPage == null && head.size() > 0) {
            fristPage = head.get(head.size() - 1);
            fristPage.setTranslationX(-getWidth());
            addView(fristPage);
            head.remove(head.size() - 1);
        }
    }

    public void addListViewEnd(@Nullable NovelViewInfo v) {
        if (allView.add(v)) {
            end.addAll(v.getViews());
            initContent();
        } else {
            Log.i("阅读界面", "后一章小说章节重复");
        }
    }


    /**
     * 初始化的时候填充界面 缓存一页
     * 仅初始化的时候调用一次
     */
    private void initContent() {
        if (end.size() > 0) {
            if (contentPage == null) {
                contentPage = end.get(0);
                addView(contentPage, 0);
                end.remove(0);
                initContent();
            } else if (nextPage == null) {
                nextPage = end.get(0);
                addView(nextPage, 0);
                end.remove(0);
            }
            pullViewLayoutListener.onStart(contentPage);
        } else {
            pullViewLayoutListener.noDate();
        }
    }


    public void setPullViewLayoutListener(PullViewLayoutListener pullViewLayoutListener) {
        this.pullViewLayoutListener = pullViewLayoutListener;
    }

    float downX = 0;
    float moveX;
    ValueAnimator valueAnimator;

    private void initAnimal() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(100);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return true;
        }

        if (pullViewLayoutListener != null && pullViewLayoutListener.onTouch()) {
            return super.onTouchEvent(event);
        }

        if (getChildCount() > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveX = event.getX();
                    if (moveX - downX > 0) {
                        dragState = 1;
                    } else if (moveX - downX < 0) {
                        dragState = 0;
                    }
                    if (fristPage != null) {
                        if (dragState == 1) {//右滑 上一页
                            fristPage.setTranslationX(-getWidth() + (moveX - downX));
                            contentPage.setTranslationX(0);
                        } else {
                            if (dragState == 0) {
                                //向左滑动 下一页
                                if (nextPage != null) {
                                    contentPage.setTranslationX((int) (moveX - downX));
                                    fristPage.setTranslationX(-getWidth());
                                }
                            }
                        }
                    } else {//没有上一页
                        if (dragState == 0 && nextPage != null) {
                            //向左滑动 下一页
                            contentPage.setTranslationX((int) (moveX - downX));
                        } else {
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(event.getX() - downX) < 10) {
                        Log.i("点击事件", "点击事件" + "     " + event.getX() + "      " + getWidth());
                        if (event.getX() < getWidth() / 5) {//前一页
                            dragState = 1;
                            downX = 0;
                            moveX = 0;
                            initAnimal();
                            valueAnimator.removeAllUpdateListeners();
                            valueAnimator.addUpdateListener(next);
                            valueAnimator.start();
                        } else if (event.getX() > getWidth() * 4 / 5) {//后一页
                            dragState = 0;
                            downX = 0;
                            moveX = 0;
                            initAnimal();
                            valueAnimator.removeAllUpdateListeners();
                            valueAnimator.addUpdateListener(next);
                            valueAnimator.start();
                        } else {
                            pullViewLayoutListener.onClickCenter();
                        }
                        return true;
                    } else if (Math.abs(moveX - downX) < 10) {//滑动效果未生效
                        initAnimal();
                        valueAnimator.removeAllUpdateListeners();
                        valueAnimator.addUpdateListener(now);
                        valueAnimator.start();
//TODO 未滑动至1/3就松开了
                    } else {//滑动效果生效
                        initAnimal();
                        valueAnimator.removeAllUpdateListeners();
                        valueAnimator.addUpdateListener(next);
                        valueAnimator.start();
//TODO 滑动超过1/3就松开了
                    }
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void changeNextPage() {//下一页
        cachePage = fristPage;
        fristPage = contentPage;
        contentPage = nextPage;
        if (end.size() > 0) {
            nextPage = end.get(0);
            addView(nextPage, 0);
            end.remove(0);
        } else {
            nextPage = null;
        }
        removeView(cachePage);
        if (fristPage != null) {
            fristPage.setTranslationX(-getWidth());
        }
        if (cachePage != null) {
            head.add(cachePage);
        }


        if (nextPage == null) {
            pullViewLayoutListener.needLoadDate(false);
        }
    }

    @Nullable
    public void changeLastPage() {//上一页
        cachePage = nextPage;
        nextPage = contentPage;
        contentPage = fristPage;
        if (head.size() > 0) {
            fristPage = head.get(head.size() - 1);
            addView(fristPage);
            head.remove(head.size() - 1);
        } else {
            fristPage = null;
        }
        removeView(cachePage);
        if (fristPage != null) {
            fristPage.setTranslationX(-getWidth());
        }
        if (cachePage != null) {
            end.add(0, cachePage);
        }


        if (fristPage == null) {
            pullViewLayoutListener.needLoadDate(true);
        }
    }

    @Nullable
    ValueAnimator.AnimatorUpdateListener next = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (dragState == 1) {//右滑动 上一页
                if (fristPage == null) {
                    pullViewLayoutListener.noLast();
                    Toast.makeText(mcontext, "没有上一页了", Toast.LENGTH_SHORT).show();
                    dragState = -1;
                } else {
                    float needMove = -getWidth() + (moveX - downX) - (-getWidth() + (moveX - downX)) * (float) animation.getAnimatedValue();
                    fristPage.setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        changeLastPage();
                        pullViewLayoutListener.lastPage(contentPage);
                        dragState = -1;
                    }
                }
            } else if (dragState == 0) {//向左滑动 下一页
                if (nextPage == null) {
                    pullViewLayoutListener.noNext();
                    Toast.makeText(mcontext, "没有下一页了", Toast.LENGTH_SHORT).show();
                    dragState = -1;
                } else {
                    float needMove = (moveX - downX) + (-getWidth() - (moveX - downX)) * ((float) animation.getAnimatedValue());
                    contentPage.setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        contentPage.setTranslationX(-getWidth() - contentPage.getLeft());//对误差进行纠正
                        changeNextPage();
                        pullViewLayoutListener.nextPage(contentPage);
                        dragState = -1;
                    }
                }
            }
        }
    };

    ValueAnimator.AnimatorUpdateListener now = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //移动的偏移量 由于精度问题 会出现偏差  需要进行偏差纠正
            if (dragState == 1 && fristPage != null) {//右滑动 上一页
                float moveShift = -getWidth() + (moveX - downX) * (1 - (float) animation.getAnimatedValue());
                fristPage.setTranslationX(moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
//                    fristPage.setTranslationX(-fristPage.getLeft());
                    dragState = -1;
                    return;
                }
            } else if (dragState == 0 && contentPage != null) {//左滑动  下一页
                float moveShift = -(moveX - downX) * (1 - (float) animation.getAnimatedValue());
                contentPage.setTranslationX(-moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
//                    Log.i("手势", "修正 " + -contentPage.getLeft());
//                    contentPage.setTranslationX(-contentPage.getLeft());
                    dragState = -1;
                    return;
                }
            }
        }
    };

    public void refreshView() {
        if (fristPage != null) {
            fristPage.invalidate();
            if (fristPage instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) fristPage).getChildCount(); i++) {
                    ((ViewGroup) fristPage).getChildAt(i).invalidate();
                }
            }
        }
        if (contentPage != null) {
            contentPage.invalidate();
            if (contentPage instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) contentPage).getChildCount(); i++) {
                    ((ViewGroup) contentPage).getChildAt(i).invalidate();
                }
            }
        }
        if (nextPage != null) {
            nextPage.invalidate();
            if (nextPage instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) nextPage).getChildCount(); i++) {
                    ((ViewGroup) nextPage).getChildAt(i).invalidate();
                }
            }
        }
    }

    public void clear() {
        removeAllViews();
        head = new ArrayList<>();//前面未填充的view
        end = new ArrayList<>();//后续未填充的view
        fristPage = null;//前一页
        contentPage = null;//显示页
        nextPage = null;//后一页
        cachePage = null;//缓存页 翻页后需要被移除的一页
    }

    public void setPage(int page, NovelViewInfo novelViewInfo) {
        allView.add(novelViewInfo);
        List<View> views = novelViewInfo.getViews();
        int index = 0;
        for (int i = 0; i < views.size(); i++) {
            View v = views.get(i);
            if (v instanceof NovelTextView) {
                if (((NovelTextView) v).getNovelPageInfo().getPage() == page) {
                    index = i;
                    break;
                }
            } else if (v instanceof ViewGroup) {
                for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {
                    if (((ViewGroup) v).getChildAt(j) instanceof NovelTextView) {
                        if (((NovelTextView) ((ViewGroup) v).getChildAt(j)).getNovelPageInfo().getPage() == page) {
                            index = i;
                            break;
                        }
                    }
                }
            }
        }
        head.addAll(views.subList(0, index));
        initFirst();
        end.addAll(views.subList(index, views.size()));
        initContent();

        if (nextPage == null) {
            pullViewLayoutListener.needLoadDate(false);
        }
        if (fristPage == null) {
            pullViewLayoutListener.needLoadDate(true);
        }

    }

    public void setPageBackgroundDrawable(Drawable drawable, @ColorInt int color) {
        List<View> allView = new ArrayList<View>() {
            @Override
            public boolean add(View o) {
                if (o == null) {
                    return false;
                }
                return super.add(o);
            }
        };
        allView.addAll(head);
        allView.addAll(end);
        allView.add(fristPage);
        allView.add(contentPage);
        allView.add(nextPage);
        for (int i = 0; i < allView.size(); i++) {
            allView.get(i).setBackground(drawable);
            allView.get(i).invalidate();
            if (allView.get(i) instanceof ViewGroup) {
                for (int j = 0; j < ((ViewGroup) allView.get(i)).getChildCount(); j++) {
                    if (((ViewGroup) allView.get(i)).getChildAt(j) instanceof TextView) {
                        ((TextView) ((ViewGroup) allView.get(i)).getChildAt(j)).setTextColor(color);
                    }
                }
            }
        }
    }


    public interface PullViewLayoutListener {
        void noDate();

        void nextPage(View v);

        void lastPage(View v);

        void needLoadDate(boolean isHead);

        void noLast();

        void noNext();

        void onClickCenter();

        boolean onTouch();

        void onStart(View v);

    }

    public NovelTextView getContentMiTextPage() {
        return getContentMiTextView(contentPage);
    }

    private NovelTextView getContentMiTextView(View contentPage) {
        NovelTextView novelTextView = null;
        if (contentPage instanceof NovelTextView) {
            novelTextView = (NovelTextView) contentPage;
        } else if (contentPage instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) contentPage).getChildCount(); i++) {
                if (novelTextView == null) {
                    if (((ViewGroup) contentPage).getChildAt(i) instanceof NovelTextView) {
                        return (NovelTextView) ((ViewGroup) contentPage).getChildAt(i);
                    } else {
                        novelTextView = getContentMiTextView(((ViewGroup) contentPage).getChildAt(i));
                    }
                }
            }
        }
        return novelTextView;
    }
}
