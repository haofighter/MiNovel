package com.hao.minovel.view.minovelread;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.List;

public class PullViewLayout extends FrameLayout {
    int dragState = -1;//0  左滑,1 右滑  -1滑动完成
    int nowChapterPage = 0;//当前contentPage显示的章节处于list中的位置

    Context mcontext;
    MiNovelArrayList<NovelChapter> allView = new MiNovelArrayList<NovelChapter>() {
        @Override
        boolean addNovelChaper(int index, NovelChapter novelChapter) {
            try {
                for (int i = 0; i < size(); i++) {
                    if (novelChapter.getChapterUrl().equals(get(i).getChapterUrl())) {
                        return false;
                    } else if (TextUtils.isEmpty(novelChapter.getChapterContent())) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            add(index, novelChapter);
            return true;
        }

        @Override
        public boolean add(NovelChapter novelChapter) {
            try {
                for (int i = 0; i < size(); i++) {
                    if (novelChapter.getChapterUrl().equals(get(i).getChapterUrl())) {
                        return false;
                    } else if (TextUtils.isEmpty(novelChapter.getChapterContent())) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return super.add(novelChapter);
        }

    };

    NovelPageInfo fristPage;//前一页
    NovelPageInfo contentPage;//显示页
    NovelPageInfo nextPage;//后一页
    NovelPageInfo cachePage;//缓存页 翻页后需要被移除的一页


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


    public void initView() {
        //TODO 初始化VIEW 目前暂定为需要的时候再去创建view
        View v = LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
        addView(v);
        ((NovelTextView) v.findViewById(R.id.novel_content)).setDate(allView.get(0).getFromatContentArray());
    }


    public void addChapter(NovelChapter chapterInfo, boolean isHead) {
        if (allView.size() == 0) {
            if (allView.add(chapterInfo)) {
                clear();
                initView();
            }
        } else {
            if (allView.addNovelChaper(0, chapterInfo)) {
                if (isHead) {
                    nowChapterPage++;
                }
            }
        }
    }


//    //获取到当前页的上一页数据
//    private NovelPageInfo getPreviousPage() {
//        ChapterInfo nowChapter = allView.get(nowChapterPage);
//        if (nowChapter != null) {
//            if (nowChapterPageIndex - 1 >= 0) {
//                return nowChapter.getNovelPageInfos().get(nowChapterPageIndex - 1);
//            } else {
//                if (nowChapterPage - 1 >= 0) {
//                    if (allView.get(nowChapterPage - 1).getNovelPageInfos().size() > 0) {
//                        return allView.get(nowChapterPage + 1).getNovelPageInfos().get(allView.get(nowChapterPage + 1).getNovelPageInfos().size() - 1);
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    //获取到当前页的下一页数据
//    private NovelPageInfo getNextPage() {
//        ChapterInfo nowChapter = allView.get(nowChapterPage);
//        if (nowChapter != null) {
//            if (nowChapter.getNovelPageInfos().size() - 1 > nowChapterPageIndex + 1) {
//                return nowChapter.getNovelPageInfos().get(nowChapterPageIndex + 1);
//            } else {
//                if (allView.size() - 1 > nowChapterPage + 1) {
//                    if (allView.get(nowChapterPage + 1).getNovelPageInfos().size() > 0) {
//                        return allView.get(nowChapterPage + 1).getNovelPageInfos().get(0);
//                    }
//                }
//            }
//        }
//        return null;
//    }


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

//        if (pullViewLayoutListener != null && pullViewLayoutListener.onTouch()) {
//            return super.onTouchEvent(event);
//        }

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
                            fristPage.getView().setTranslationX(-getWidth() + (moveX - downX));
                            contentPage.getView().setTranslationX(0);
                        } else {
                            if (dragState == 0) {
                                //向左滑动 下一页
                                if (nextPage != null) {
                                    contentPage.getView().setTranslationX((int) (moveX - downX));
                                    fristPage.getView().setTranslationX(-getWidth());
                                }
                            }
                        }
                    } else {//没有上一页
                        if (dragState == 0 && nextPage != null) {
                            //向左滑动 下一页
                            contentPage.getView().setTranslationX((int) (moveX - downX));
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
//                            pullViewLayoutListener.onClickCenter();
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

    }

    @Nullable
    public void changeLastPage() {//上一页
        cachePage = nextPage;
        nextPage = contentPage;
        contentPage = fristPage;

    }

    @Nullable
    ValueAnimator.AnimatorUpdateListener next = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (dragState == 1) {//右滑动 上一页
                if (fristPage == null) {
                    Toast.makeText(mcontext, "没有上一页了", Toast.LENGTH_SHORT).show();
                    dragState = -1;
                } else {
                    float needMove = -getWidth() + (moveX - downX) - (-getWidth() + (moveX - downX)) * (float) animation.getAnimatedValue();
                    fristPage.getView().setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        changeLastPage();
                        dragState = -1;
                    }
                }
            } else if (dragState == 0) {//向左滑动 下一页
                if (nextPage == null) {
                    Toast.makeText(mcontext, "没有下一页了", Toast.LENGTH_SHORT).show();
                    dragState = -1;
                } else {
                    float needMove = (moveX - downX) + (-getWidth() - (moveX - downX)) * ((float) animation.getAnimatedValue());
                    contentPage.getView().setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        contentPage.getView().setTranslationX(-getWidth() - contentPage.getView().getLeft());//对误差进行纠正
                        changeNextPage();
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
                fristPage.getView().setTranslationX(moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
//                    fristPage.setTranslationX(-fristPage.getLeft());
                    dragState = -1;
                    return;
                }
            } else if (dragState == 0 && contentPage != null) {//左滑动  下一页
                float moveShift = -(moveX - downX) * (1 - (float) animation.getAnimatedValue());
                contentPage.getView().setTranslationX(-moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
//                    Log.i("手势", "修正 " + -contentPage.getLeft());
//                    contentPage.setTranslationX(-contentPage.getLeft());
                    dragState = -1;
                    return;
                }
            }
        }
    };


    public void clear() {
        removeAllViews();
        fristPage = null;//前一页
        contentPage = null;//显示页
        nextPage = null;//后一页
        cachePage = null;//缓存页 翻页后需要被移除的一页
    }


    public interface PullViewLayoutListener {
        void noDate();

        void nextPage(NovelPageInfo v);

        void lastPage(NovelPageInfo v);

        void needLoadDate(boolean isHead);

        void noLast();

        void noNext();

        void onClickCenter();

        boolean onTouch();

        void onStart(NovelPageInfo v);

    }


    abstract class MiNovelArrayList<T> extends ArrayList<T> {
        abstract boolean addNovelChaper(int index, T t);
    }

}
