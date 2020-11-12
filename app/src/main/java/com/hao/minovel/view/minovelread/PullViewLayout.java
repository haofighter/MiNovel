package com.hao.minovel.view.minovelread;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PullViewLayout extends FrameLayout {
    int dragState = -1;//0  左滑,1 右滑  -1滑动完成
    NovelTextDrawInfo novelTextDrawInfo = DBManage.chackNovelConfig();//小说阅读页的配置信息
    NovelContentView fristPage;//前一页
    NovelContentView contentPage;//显示页
    NovelContentView nextPage;//后一页
    NovelContentView cachePage;//缓存页 翻页后需要被移除的一页
    PullViewLayoutListener listener;//监听
    float downX = 0;
    float moveX;
    ValueAnimator valueAnimator;

    /**
     * 储存当前阅读的章节信息
     */
    ArrayList<ChapterInfo> allDate = new ArrayList<ChapterInfo>() {

        @Override
        public boolean add(ChapterInfo chapterInfo) {
            for (int i = 0; i < size(); i++) {
                if (chapterInfo.getNowChapterUrl().equals(get(i).getNowChapterUrl())) {
                    return false;
                }
            }
            return super.add(chapterInfo);
        }

        public void add(int index, ChapterInfo chapterInfo) {
            boolean isContent = false;
            for (int i = 0; i < size(); i++) {
                if (chapterInfo.getNowChapterUrl().equals(get(i).getNowChapterUrl())) {
                    isContent = true;
                }
            }
            if (!isContent) {
                super.add(index, chapterInfo);
            }
        }
    };

    /**
     * 用于界面的更新
     */
    public void notifyDateOfChange() {
        NovelTextDrawInfo novelTextDrawInfo = DBManage.chackNovelConfig();
        if (novelTextDrawInfo != null) {
            this.novelTextDrawInfo = novelTextDrawInfo;
        }
        try {
            refresh();
        } catch (Exception e) {
            MiLog.i("错误  界面更新:" + e.getMessage());
        }
    }

    public enum ClickState {
        center, left, right, onTouch, onClick
    }

    public PullViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public void refresh() throws Exception {
        NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(allDate.get(contentPage.getChapterIndex()).getNowChapterUrl());
        int page = contentPage.getNowPage();
        removeAllViews();
        contentPage = null;
        fristPage = null;
        nextPage = null;
        setDate(novelChapter, page);
    }

    /**
     * 获取内容页的当前页数
     *
     * @return
     */
    public int getContentPageIndex() {
        return contentPage.getNowPage();
    }

    public NovelTextDrawInfo getNovelTextDrawInfo() {
        return novelTextDrawInfo;
    }

    public ChapterInfo getNowshow() {
        return allDate.get(contentPage.getChapterIndex());
    }

    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (novelTextDrawInfo == null) {
            novelTextDrawInfo = new NovelTextDrawInfo();
        }
    }


    /**
     * 主要用于切换章节
     *
     * @param novelChapter
     */
    public void setDate(NovelChapter novelChapter) {
        allDate.clear();
        addDate(novelChapter, -1, true);
    }

    /**
     * 跳转至已读章节页
     *
     * @param novelChapter     章节
     * @param contentPageIndex 当前阅读的页
     */
    public void setDate(NovelChapter novelChapter, int contentPageIndex) {
        allDate.clear();
        addDate(novelChapter, contentPageIndex, true);
    }

    /**
     * 主要用于给元数据添加章节
     *
     * @param novelChapter
     * @param ishead       当前添加章节是否为前一章
     */
    public void addDate(NovelChapter novelChapter, boolean ishead) {
        addDate(novelChapter, -1, ishead);
    }

    /**
     * 填充信息
     *
     * @param novelChapter 小说章节信息
     * @param page         跳转的页面
     * @param ishead       是否为前一页
     */
    public void addDate(NovelChapter novelChapter, int page, boolean ishead) {
        ChapterInfo chapterInfo = new ChapterInfo(novelChapter.getChapterUrl(), novelChapter.getChapterName(), novelChapter.getChapterContent());
        if (contentPage != null && contentPage.getNovelContent().getWidth() != 0) {
            MiLog.i("设置数据前    chapterIndex=" + contentPage.getChapterIndex() + "    contentPageIndex=" + contentPage.getNowPage());
            TextPaint mPaint = contentPage.getNovelContent().getPaint();
            mPaint.setSubpixelText(true);
            Layout tempLayout = new StaticLayout(novelChapter.getChapterContent(), mPaint, contentPage.getNovelContent().getWidth(), Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
            for (int i = 0; i < tempLayout.getLineCount(); i++) {
                chapterInfo.getTextArray().add(novelChapter.getChapterContent().substring(tempLayout.getLineStart(i), tempLayout.getLineEnd(i)));
            }
            if (chapterInfo.getTextArray().size() % contentPage.getNovelContent().getMaxLines() == 0) {
                chapterInfo.setPage(chapterInfo.getTextArray().size() / contentPage.getMaxLine());
            } else {
                chapterInfo.setPage(chapterInfo.getTextArray().size() / contentPage.getMaxLine() + 1);
            }

            if (ishead) {
                int allDateSize = allDate.size();
                allDate.add(0, chapterInfo);
                if (allDate.size() > allDateSize && allDateSize != 0) {
                    contentPage.setChapterIndex(contentPage.getChapterIndex() + 1);
                } else if (allDateSize == 0) {//标示此数据为新加入数据 需要重置内容页的定位
                    contentPage.setChapterIndex(0);
                    contentPage.setNowPage(page >= 0 ? page : 0);
                    initContentPage();
                }
            } else {
                allDate.add(chapterInfo);
            }

            initFirstPage();
            initNextPage();
        } else {
            if (contentPage == null) {
                contentPage = new NovelContentView(novelTextDrawInfo, getContext());
                addView(contentPage);
            }
            contentPage.post(new Runnable() {
                @Override
                public void run() {
                    addDate(novelChapter, page, ishead);
                }
            });
        }
    }

    private void initContentPage() {
        if (contentPage == null) {
            contentPage = new NovelContentView(novelTextDrawInfo, getContext());
        }
        ChapterInfo nowChapterInfo = allDate.get(contentPage.getChapterIndex());
        if (contentPage.getNowPage() >= nowChapterInfo.getPage()) {
            contentPage.setNowPage(nowChapterInfo.getPage() - 1);
        }
        contentPage.setContent(allDate.get(contentPage.getChapterIndex()));
        if (contentPage.getParent() == null) {
            addView(contentPage);
        } else {
            invalidate();
        }
        if (listener != null) {
            listener.onPageChange(nowChapterInfo);
        }
    }

    private void initNextPage() {
        if (nextPage == null) {
            nextPage = new NovelContentView(novelTextDrawInfo, getContext());
        }
        ChapterInfo nowChapterInfo = allDate.get(contentPage.getChapterIndex());
        MiLog.i("加载下一页数据  当前页位置：chapterIndex=" + contentPage.getChapterIndex() + "    contentPageIndex=" + contentPage.getNowPage());
        if (contentPage.getNowPage() + 1 >= allDate.get(contentPage.getChapterIndex()).getPage()) {//如果下一页数据超出本章页数，需要取下一章数据
            if (contentPage.getChapterIndex() + 1 < allDate.size()) {//如果有下一章
                nextPage.setChapterIndex(contentPage.getChapterIndex() + 1);
                nextPage.setNowPage(0);
                nextPage.setContent(allDate.get(nextPage.getChapterIndex()));
            } else {
                nextPage = null;
            }
        } else {
            nextPage.setChapterIndex(contentPage.getChapterIndex());
            nextPage.setNowPage(contentPage.getNowPage() + 1);
            nextPage.setContent(nowChapterInfo);
        }

        if (nextPage != null) {
            MiLog.i("下一页 ：chapterIndex：" + nextPage.getChapterIndex() + "    conttentpage：" + nextPage.getNowPage() + "     " + allDate.get(nextPage.getChapterIndex()).getChapterName());
            if (nextPage.getParent() != null) {
                removeView(nextPage);
            }
            addView(nextPage, 0);
        } else {
            if (listener != null) {
                listener.loadMore(nowChapterInfo.getNowChapterUrl());
            }
        }
    }

    //
    //是否切换了章节
    private void initFirstPage() {
        if (fristPage == null) {
            fristPage = new NovelContentView(novelTextDrawInfo, getContext());
        }
        ChapterInfo nowChapterInfo = allDate.get(contentPage.getChapterIndex());
        MiLog.i("加载上一页数据  当前页位置：chapterIndex=" + contentPage.getChapterIndex() + "    contentPageIndex=" + contentPage.getNowPage());
        if (contentPage.getNowPage() - 1 < 0) {
            if (contentPage.getChapterIndex() - 1 >= 0) {//如果有上一章
                nowChapterInfo = allDate.get(contentPage.getChapterIndex() - 1);
                fristPage.setChapterIndex(contentPage.getChapterIndex() - 1);
                fristPage.setNowPage(nowChapterInfo.getPage() - 1);
                fristPage.setContent(nowChapterInfo);
            } else {
                fristPage = null;
            }
        } else {
            fristPage.setChapterIndex(contentPage.getChapterIndex());
            fristPage.setNowPage(contentPage.getNowPage() - 1);
            fristPage.setContent(nowChapterInfo);
        }
        if (fristPage != null) {
            MiLog.i("上一页   chapterIndex：" + fristPage.getChapterIndex() + "    conttentpage：" + fristPage.getNowPage() + "     " + allDate.get(fristPage.getChapterIndex()).getChapterName());

            fristPage.setTranslationX(-getWidth());
            if (fristPage.getParent() != null) {
                removeView(fristPage);
            }
            addView(fristPage);
        } else {
            if (listener != null) {
                listener.loadBefor(nowChapterInfo.getNowChapterUrl());
            }
        }
    }

    private void initAnimal() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(100);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isUsedThisTouch = true;
        if (listener != null) {
            isUsedThisTouch = listener.clickCenter(ClickState.onTouch);
        }

        if (valueAnimator != null && valueAnimator.isRunning()) {
            return true;
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
                        if (dragState == 1 && isUsedThisTouch) {//右滑 上一页
                            fristPage.setTranslationX(-getWidth() + (moveX - downX));
                            contentPage.setTranslationX(0);
                        } else {
                            if (dragState == 0) {
                                //向左滑动 下一页
                                if (nextPage != null && isUsedThisTouch) {
                                    contentPage.setTranslationX((int) (moveX - downX));
                                    fristPage.setTranslationX(-getWidth());
                                }
                            }
                        }
                    } else {//没有上一页
                        if (dragState == 0 && nextPage != null && isUsedThisTouch) {
                            //向左滑动 下一页
                            contentPage.setTranslationX((int) (moveX - downX));
                        } else {
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(event.getX() - downX) < 10) {
                        if (!listener.clickCenter(ClickState.onClick)) {
                            return false;
                        }

                        if (event.getX() < getWidth() / 5) {//前一页
                            if (listener.clickCenter(ClickState.left) && isUsedThisTouch) {
                                dragState = 1;
                                downX = 0;
                                moveX = 0;
                                initAnimal();
                                valueAnimator.removeAllUpdateListeners();
                                valueAnimator.addUpdateListener(next);
                                valueAnimator.start();
                            } else {
                                return false;
                            }
                        } else if (event.getX() > getWidth() * 4 / 5 && isUsedThisTouch) {//后一页
                            if (listener.clickCenter(ClickState.right)) {
                                dragState = 0;
                                downX = 0;
                                moveX = 0;
                                initAnimal();
                                valueAnimator.removeAllUpdateListeners();
                                valueAnimator.addUpdateListener(next);
                                valueAnimator.start();
                            } else {
                                return false;
                            }
                        } else {
                            if (listener != null) {
                                listener.clickCenter(ClickState.center);
                            }
                        }
                        return true;
                    } else {//滑动效果生效
                        if (event.getX() - downX > 0) {
                            if (fristPage == null) {
                                return true;
                            } else {
                                if (listener != null) {
                                    listener.clickCenter(ClickState.left);
                                }
                            }
                        } else {
                            if (nextPage == null) {
                                return true;
                            } else {
                                if (listener != null) {
                                    listener.clickCenter(ClickState.right);
                                }
                            }
                        }
                        if (isUsedThisTouch) {
                            initAnimal();
                            valueAnimator.removeAllUpdateListeners();
                            valueAnimator.addUpdateListener(next);
                            valueAnimator.start();
                        }
                    }
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public synchronized void changeNextPage() {//下一页
        cachePage = fristPage;
        fristPage = contentPage;
        contentPage = nextPage;
        nextPage = null;
        Log.i("小说", "当前页 chapterIndex=" + contentPage.getChapterIndex() + "   contentPageIndex=" + contentPage.getNowPage() + "   当前章节=" + allDate.get(contentPage.getChapterIndex()).getChapterName());
        removeView(cachePage);
        if (listener != null) {
            listener.onPageChange(allDate.get(contentPage.getChapterIndex()));
        }
        initNextPage();
    }

    public synchronized void changeLastPage() {//上一页
        cachePage = nextPage;
        nextPage = contentPage;
        contentPage = fristPage;
        fristPage = null;
        MiLog.i("当前页 chapterIndex=" + contentPage.getChapterIndex() + "   contentPageIndex=" + contentPage.getNowPage() + "   当前章节=" + allDate.get(contentPage.getChapterIndex()).getChapterName());
        removeView(cachePage);
        if (listener != null) {
            listener.onPageChange(allDate.get(contentPage.getChapterIndex()));
        }
        initFirstPage();
    }

    @Nullable
    ValueAnimator.AnimatorUpdateListener next = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (dragState == 1) {//右滑动 上一页

                if (fristPage == null) {
                    dragState = -1;
                } else {
                    float needMove = -getWidth() + (moveX - downX) - (-getWidth() + (moveX - downX)) * (float) animation.getAnimatedValue();
                    fristPage.setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        Log.i("触摸 结束动画", "右滑动 上一页");
                        changeLastPage();
                        dragState = -1;
                    }
                }
            } else if (dragState == 0) {//向左滑动 下一页

                if (nextPage == null) {
                    dragState = -1;
                } else {
                    float needMove = (moveX - downX) + (-getWidth() - (moveX - downX)) * ((float) animation.getAnimatedValue());
                    contentPage.setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
                        Log.i("触摸 结束动画", "左滑动 下一页");
                        contentPage.setTranslationX(-getWidth() - contentPage.getLeft());//对误差进行纠正
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
                fristPage.setTranslationX(moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
                    Log.i("触摸 结束动画", "向右滑动 下一页");
                    dragState = -1;
                    return;
                }
            } else if (dragState == 0 && contentPage != null) {//左滑动  下一页
                float moveShift = -(moveX - downX) * (1 - (float) animation.getAnimatedValue());
                contentPage.setTranslationX(-moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
                    Log.i("触摸 结束动画", "向左滑动 下一页");
                    dragState = -1;
                    return;
                }
            }
        }
    };


    public interface PullViewLayoutListener {
        public void loadMore(String nowUrl);

        public void loadBefor(String nowUrl);

        public void onPageChange(ChapterInfo chapterInfo);

        /**
         * @param state 当前触摸操作的状态
         * @return false 标示事件不进行拦截 可有本控件处理
         * true  拦截事件，不进行操作
         */
        public boolean clickCenter(ClickState state);
    }


    public void setListener(PullViewLayoutListener listener) {
        this.listener = listener;
    }
}
