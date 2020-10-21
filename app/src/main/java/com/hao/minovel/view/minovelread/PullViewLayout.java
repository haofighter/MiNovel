package com.hao.minovel.view.minovelread;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PullViewLayout extends FrameLayout {
    int dragState = -1;//0  左滑,1 右滑  -1滑动完成
    int chapterIndex = 0;//章节位于list中的位置
    int contentPageIndex = 0;//当前contentPage显示的章节处于list中的位置
    NovelTextViewHelp novelTextViewHelp = DBManage.chackNovelConfig();//小说阅读页的配置信息
    NovelContentView fristPage;//前一页
    NovelContentView contentPage;//显示页
    NovelContentView nextPage;//后一页
    NovelContentView cachePage;//缓存页 翻页后需要被移除的一页
    PullViewLayoutListener listener;//监听
    float downX = 0;
    float moveX;
    ValueAnimator valueAnimator;
    ViewObserver viewObserver;

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

        @Override
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

    public enum ClickState {
        center, left, right, onTouch, onClick
    }

    public NovelTextViewHelp getNovelTextViewHelp() {
        return novelTextViewHelp;
    }

    public PullViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /**
     * 获取内容页的当前页数
     *
     * @return
     */
    public int getContentPageIndex() {
        return contentPageIndex;
    }

    //
//
    public PullViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (viewObserver == null) {
            viewObserver = new ViewObserver();
        }
    }


    /**
     * 主要用于切换章节
     *
     * @param novelChapter
     */
    public void setDate(NovelChapter novelChapter) {
        addDate(novelChapter, -1, true);
    }

    /**
     * 跳转至已读章节页
     *
     * @param novelChapter     章节
     * @param contentPageIndex 当前阅读的页
     */
    public void setDate(NovelChapter novelChapter, int contentPageIndex) {
        addDate(novelChapter, contentPageIndex, true);
    }

    /**
     * 主要用于给元数据添加章节
     *
     * @param novelChapter
     * @param ishead       当前添加章节是否为前一章
     */
    public void addDate(NovelChapter novelChapter, boolean ishead) {
        addDate(novelChapter, 0, ishead);
    }

    /**
     * 填充信息
     *
     * @param novelChapter 小说章节信息
     * @param page         跳转的页面
     * @param ishead       是否为前一页
     */
    public void addDate(NovelChapter novelChapter, int page, boolean ishead) {
        if (page == -1) {
            allDate.clear();
        }
        ChapterInfo chapterInfo = new ChapterInfo(novelChapter.getChapterUrl(), novelChapter.getChapterName(), novelChapter.getChapterContent());
        if (contentPage != null && contentPage.getNovelContent().getWidth() != 0) {
            TextPaint mPaint = contentPage.getNovelContent().getPaint();
            mPaint.setSubpixelText(true);
            Layout tempLayout = new StaticLayout(novelChapter.getChapterContent(), mPaint, contentPage.getNovelContent().getWidth(), Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
            for (int i = 0; i < tempLayout.getLineCount(); i++) {
                Log.i("分行", tempLayout.getLineStart(i) + "    " + tempLayout.getLineEnd(i));
                chapterInfo.getTextArray().add(novelChapter.getChapterContent().substring(tempLayout.getLineStart(i), tempLayout.getLineEnd(i)));
            }
            if (chapterInfo.getTextArray().size() % contentPage.getNovelContent().getMaxLines() == 0) {
                chapterInfo.setPage(chapterInfo.getTextArray().size() % contentPage.getNovelContent().getMaxLines());
            } else {
                chapterInfo.setPage(chapterInfo.getTextArray().size() % contentPage.getNovelContent().getMaxLines() + 1);
            }

            if (ishead) {
                allDate.add(0, chapterInfo);
            } else {
                allDate.add(chapterInfo);
            }

            if (allDate.get(chapterIndex) != null) {
                if (page >= 0 && page <= allDate.get(chapterIndex).getPage() - 1) {
                    contentPageIndex = page;
                } else {
                    contentPageIndex = 0;
                }
            } else {
                chapterIndex = allDate.size() - 1;
                contentPageIndex = 0;
            }

            initContentPage();


//        initFirstPage();
//        initNextPage(nextPage);
        } else {
            contentPage = new NovelContentView(getContext());
            addView(contentPage);
            contentPage.post(new Runnable() {
                @Override
                public void run() {
                    addDate(novelChapter, page, ishead);
                }
            });
        }
    }


//
//

    //
//    //针对当前数据进行初始化
//    public void fromatDate(NovelChapter novelChapter, int chagepage) {
////        if (novelTextViewHelp != null) {
////            return;
////        }
//        ViewGroup v = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
//        addView(v, 0);
//        v.post(new Runnable() {
//            @Override
//            public void run() {
//                NovelTextView novelTextView = v.findViewById(R.id.novel_content);
//                if (novelTextViewHelp == null) {
//                    novelTextViewHelp = novelTextView.getNovelTextViewHelp();
//                    DBManage.saveNovelTextViewConfig(novelTextViewHelp);
//                    if (listener != null) {
//                        listener.initConfig(novelTextViewHelp);
//                    }
//                }
//
//                removeView(v);
//                allDate.clear();
//                initContent(novelChapter, chagepage, novelTextView);
//            }
//        });
//    }
//
    private void initContentPage() {
        if (contentPage == null) {
            contentPage = new NovelContentView(getContext());
        }
        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
        if (contentPageIndex >= nowChapterInfo.getPage()) {
            contentPageIndex = nowChapterInfo.getPage() - 1;
        }
        contentPage.setDiraction(chapterIndex, contentPageIndex);
        contentPage.setContent(allDate.get(chapterIndex));
        contentPage.getNovelPage().setText((contentPageIndex + 1) + "/" + nowChapterInfo.getPage() + "");
        contentPage.getNovelTitle().setText(nowChapterInfo.getChapterName());
        if (contentPage.getParent() == null) {
            addView(contentPage);
        }
        if (listener != null) {
            listener.onPageChange(nowChapterInfo);
        }
    }

    //
//    private void initNextPage(ViewGroup v) {
//        if (v == null) {
//            v = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
//        }
//        TextView novel_page = v.findViewById(R.id.novel_page);
//        MiTextView novel_title = v.findViewById(R.id.novel_title);
//        novel_title.setConfig(novelTextViewHelp);
//        NovelTextView novelTextView = v.findViewById(R.id.novel_content);
//        novelTextView.setNovelTextViewHelp(novelTextViewHelp);
//        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
//        if (contentPageIndex + 1 >= allDate.get(chapterIndex).getPage()) {//如果下一页数据超出本章页数，需要取下一章数据
//            if (chapterIndex + 1 < allDate.size()) {//如果有下一章
//                PageInfo pageInfo = new PageInfo(chapterIndex + 1, 0);
//                nowChapterInfo = allDate.get(chapterIndex + 1);
//                novel_title.setText(nowChapterInfo.getChapterName());
//                novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), 0);
//                novel_page.setText((pageInfo.getContentIndex() + 1) + "/" + nowChapterInfo.getPage() + "");
//                v.setTag(pageInfo);
//                nextPage = v;
//            } else {
//                nextPage = null;
//            }
//        } else {
//            PageInfo pageInfo = new PageInfo(chapterIndex, contentPageIndex + 1);
//            novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), contentPageIndex + 1);
//            novel_page.setText((pageInfo.getContentIndex() + 1) + "/" + nowChapterInfo.getPage() + "");
//            novel_title.setText(nowChapterInfo.getChapterName());
//            v.setTag(pageInfo);
//            nextPage = v;
//        }
//
//        if (nextPage != null) {
//            if (nextPage.getParent() != null) {
//                removeView(nextPage);
//            }
//            addView(nextPage, 0);
//        } else {
//            if (listener != null) {
//                listener.loadMore(nowChapterInfo.getNowChapterUrl());
//            }
//        }
//    }
//
    //是否切换了章节
    private void initFirstPage() {
        if (fristPage == null) {
            fristPage = new NovelContentView(getContext());
        }
        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
        if (contentPageIndex - 1 < 0) {
            if (chapterIndex - 1 >= 0) {//如果有上一章
                fristPage.setDiraction(chapterIndex - 1, nowChapterInfo.getPage() - 1);
                nowChapterInfo = allDate.get(chapterIndex - 1);
                fristPage.setContent(nowChapterInfo);
            } else {
                fristPage = null;
            }
        } else {
            fristPage.setDiraction(chapterIndex, contentPageIndex - 1);
            fristPage.setContent(nowChapterInfo);
        }
        if (fristPage != null) {
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

//
//    public void addChapter(NovelChapter novelChapter, boolean ishead) {
//        ChapterInfo chapterInfo = new ChapterInfo(novelChapter.getChapterUrl(), novelChapter.getChapterName(), novelChapter.getChapterContent());
//        chapterInfo.setNovelTextViewHelp(novelTextViewHelp, contentPage.findViewById(R.id.novel_content));
//        if (ishead) {
//            int size = allDate.size();
//            allDate.add(0, chapterInfo);
//            if (allDate.size() - size > 0) {
//                chapterIndex++;
//                contentPage.setTag(new PageInfo(chapterIndex, contentPageIndex));
//            }
//        } else {
//            allDate.add(chapterInfo);
//        }
//
//        initFirstPage(fristPage);
//
//        if (nextPage == null) {
//            nextPage = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
//        }
//        initNextPage(nextPage);
//    }
//
//    public void setChapter(NovelChapter novelChapter) {
//        setChapter(novelChapter, -1);
//    }
//
//    /**
//     * 填充章节数据
//     *
//     * @param novelChapter
//     * @param changePage
//     */
//    public void setChapter(NovelChapter novelChapter, int changePage) {
//        if (novelTextViewHelp != null) {
//            chapterIndex = 0;
//            contentPageIndex = 0;
////            initContent(novelChapter, changePage);
//        }
////        else {//如果数据不完整 直接调用初始化的方法进行完善
//        fromatDate(novelChapter, changePage);
////        }
//    }
//

//
//
//    private void initAnimal() {
//        if (valueAnimator == null) {
//            valueAnimator = ValueAnimator.ofFloat(0, 1);
//            valueAnimator.setDuration(100);
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean isUsedThisTouch = true;
//        if (listener != null) {
//            isUsedThisTouch = listener.clickCenter(ClickState.onTouch);
//        }
//
//        if (valueAnimator != null && valueAnimator.isRunning()) {
//            return true;
//        }
//        if (getChildCount() > 0) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    downX = event.getX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    moveX = event.getX();
//                    if (moveX - downX > 0) {
//                        dragState = 1;
//                    } else if (moveX - downX < 0) {
//                        dragState = 0;
//                    }
//                    if (fristPage != null) {
//                        if (dragState == 1 && isUsedThisTouch) {//右滑 上一页
//                            fristPage.setTranslationX(-getWidth() + (moveX - downX));
//                            contentPage.setTranslationX(0);
//                        } else {
//                            if (dragState == 0) {
//                                //向左滑动 下一页
//                                if (nextPage != null && isUsedThisTouch) {
//                                    contentPage.setTranslationX((int) (moveX - downX));
//                                    fristPage.setTranslationX(-getWidth());
//                                }
//                            }
//                        }
//                    } else {//没有上一页
//                        if (dragState == 0 && nextPage != null && isUsedThisTouch) {
//                            //向左滑动 下一页
//                            contentPage.setTranslationX((int) (moveX - downX));
//                        } else {
//                            return true;
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (Math.abs(event.getX() - downX) < 10) {
//                        if (!listener.clickCenter(ClickState.onClick)) {
//                            return false;
//                        }
//
//                        if (event.getX() < getWidth() / 5) {//前一页
//                            if (listener.clickCenter(ClickState.left) && isUsedThisTouch) {
//                                dragState = 1;
//                                downX = 0;
//                                moveX = 0;
//                                initAnimal();
//                                valueAnimator.removeAllUpdateListeners();
//                                valueAnimator.addUpdateListener(next);
//                                valueAnimator.start();
//                            } else {
//                                return false;
//                            }
//                        } else if (event.getX() > getWidth() * 4 / 5 && isUsedThisTouch) {//后一页
//                            if (listener.clickCenter(ClickState.right)) {
//                                dragState = 0;
//                                downX = 0;
//                                moveX = 0;
//                                initAnimal();
//                                valueAnimator.removeAllUpdateListeners();
//                                valueAnimator.addUpdateListener(next);
//                                valueAnimator.start();
//                            } else {
//                                return false;
//                            }
//                        } else {
//                            if (listener != null) {
//                                listener.clickCenter(ClickState.center);
//                            }
//                        }
//                        return true;
//                    } else {//滑动效果生效
//                        if (event.getX() - downX > 0) {
//                            if (fristPage == null) {
//                                return true;
//                            } else {
//                                if (listener != null) {
//                                    listener.clickCenter(ClickState.left);
//                                }
//                            }
//                        } else {
//                            if (nextPage == null) {
//                                return true;
//                            } else {
//                                if (listener != null) {
//                                    listener.clickCenter(ClickState.right);
//                                }
//                            }
//                        }
//                        if (isUsedThisTouch) {
//                            initAnimal();
//                            valueAnimator.removeAllUpdateListeners();
//                            valueAnimator.addUpdateListener(next);
//                            valueAnimator.start();
//                        }
//                    }
//                    break;
//            }
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
//    }
//
//    public synchronized void changeNextPage() {//下一页
//        cachePage = fristPage;
//        fristPage = contentPage;
//        contentPage = nextPage;
//        chapterIndex = ((PageInfo) contentPage.getTag()).getChapterIndex();
//        contentPageIndex = ((PageInfo) contentPage.getTag()).getContentIndex();
//        nextPage = null;
//        Log.i("显示参数", "下一页 chapterIndex=" + chapterIndex + "   contentPageIndex=" + contentPageIndex + "   当前章节=" + allDate.get(chapterIndex).getChapterName());
//        removeView(cachePage);
//        if (listener != null) {
//            listener.onPageChange(allDate.get(chapterIndex));
//        }
//        initNextPage(nextPage);
//    }
//
//    public synchronized void changeLastPage() {//上一页
//        cachePage = nextPage;
//        nextPage = contentPage;
//        contentPage = fristPage;
//        chapterIndex = ((PageInfo) contentPage.getTag()).getChapterIndex();
//        contentPageIndex = ((PageInfo) contentPage.getTag()).getContentIndex();
//        fristPage = null;
//        Log.i("显示参数", "上一页 chapterIndex=" + chapterIndex + "   contentPageIndex=" + contentPageIndex + "   当前章节=" + allDate.get(chapterIndex).getChapterName());
//        removeView(cachePage);
//        if (listener != null) {
//            listener.onPageChange(allDate.get(chapterIndex));
//        }
//        initFirstPage(fristPage);
//    }
//
//    @Nullable
//    ValueAnimator.AnimatorUpdateListener next = new ValueAnimator.AnimatorUpdateListener() {
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            if (dragState == 1) {//右滑动 上一页
//
//                if (fristPage == null) {
//                    dragState = -1;
//                } else {
//                    float needMove = -getWidth() + (moveX - downX) - (-getWidth() + (moveX - downX)) * (float) animation.getAnimatedValue();
//                    fristPage.setTranslationX((int) needMove);
//                    if ((float) animation.getAnimatedValue() == 1) {
//                        Log.i("触摸 结束动画", "右滑动 上一页");
//                        changeLastPage();
//                        dragState = -1;
//                    }
//                }
//            } else if (dragState == 0) {//向左滑动 下一页
//
//                if (nextPage == null) {
//                    dragState = -1;
//                } else {
//                    float needMove = (moveX - downX) + (-getWidth() - (moveX - downX)) * ((float) animation.getAnimatedValue());
//                    contentPage.setTranslationX((int) needMove);
//                    if ((float) animation.getAnimatedValue() == 1) {
//                        Log.i("触摸 结束动画", "左滑动 下一页");
//                        contentPage.setTranslationX(-getWidth() - contentPage.getLeft());//对误差进行纠正
//                        changeNextPage();
//                        dragState = -1;
//                    }
//                }
//            }
//        }
//    };
//
//    ValueAnimator.AnimatorUpdateListener now = new ValueAnimator.AnimatorUpdateListener() {
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            //移动的偏移量 由于精度问题 会出现偏差  需要进行偏差纠正
//            if (dragState == 1 && fristPage != null) {//右滑动 上一页
//
//                float moveShift = -getWidth() + (moveX - downX) * (1 - (float) animation.getAnimatedValue());
//                fristPage.setTranslationX(moveShift);
//                if ((float) animation.getAnimatedValue() == 1) {
//                    Log.i("触摸 结束动画", "向右滑动 下一页");
//                    dragState = -1;
//                    return;
//                }
//            } else if (dragState == 0 && contentPage != null) {//左滑动  下一页
//                float moveShift = -(moveX - downX) * (1 - (float) animation.getAnimatedValue());
//                contentPage.setTranslationX(-moveShift);
//                if ((float) animation.getAnimatedValue() == 1) {
//                    Log.i("触摸 结束动画", "向左滑动 下一页");
//                    dragState = -1;
//                    return;
//                }
//            }
//        }
//    };
//
//    public void refreshView() {
//        if (viewObserver == null) {
//            viewObserver = new ViewObserver();
//        }
//        viewObserver.deleteObservers();
//
//        if (fristPage != null) {
//            for (int i = 0; i < fristPage.getChildCount(); i++) {
//                if (fristPage.getChildAt(i) instanceof Observer) {
//                    viewObserver.addObserver((Observer) fristPage.getChildAt(i));
//                }
//            }
//
//        }
//
//        if (contentPage != null) {
//            for (int i = 0; i < contentPage.getChildCount(); i++) {
//                if (contentPage.getChildAt(i) instanceof Observer) {
//                    viewObserver.addObserver((Observer) contentPage.getChildAt(i));
//                }
//            }
//        }
//
//        if (nextPage != null) {
//            for (int i = 0; i < nextPage.getChildCount(); i++) {
//                if (nextPage.getChildAt(i) instanceof Observer) {
//                    viewObserver.addObserver((Observer) nextPage.getChildAt(i));
//                }
//            }
//        }
//
//        viewObserver.update(novelTextViewHelp);
//    }
//
//


    //
//    public void setNovelTextViewConfit(NovelTextViewHelp novelTextViewHelp) {
//        this.novelTextViewHelp = novelTextViewHelp;
//        for (int i = 0; i < allDate.size(); i++) {
//            allDate.get(i).setNovelTextViewHelp(novelTextViewHelp, contentPage.findViewById(R.id.novel_content));
//        }
//        initContentPage(contentPage);
//        initNextPage(nextPage);
//        initFirstPage(fristPage);
//
//        if (listener != null) {
//            listener.initConfig(novelTextViewHelp);
//        }
//    }
//
//
    private class ViewObserver extends Observable {
        @Override
        public synchronized void addObserver(Observer o) {
            super.addObserver(o);
        }

        public void update(NovelTextViewHelp novelTextViewHelp) {
            setChanged();
            notifyObservers(novelTextViewHelp);
        }
    }

    public interface PullViewLayoutListener {
        public void loadMore(String nowUrl);

        public void loadBefor(String nowUrl);

        public void initConfig(NovelTextViewHelp novelTextViewHelp);

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
