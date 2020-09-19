package com.hao.minovel.view.minovelread;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelChapter;

import java.util.ArrayList;

public class PullViewLayout extends FrameLayout {
    int dragState = -1;//0  左滑,1 右滑  -1滑动完成
    int chapterIndex = 0;//章节位于list中的位置
    int contentPageIndex = 0;//当前contentPage显示的章节处于list中的位置
    NovelTextViewHelp novelTextViewHelp;//小说阅读页的配置信息
    Context mcontext;
    View fristPage;//前一页
    View contentPage;//显示页
    View nextPage;//后一页
    View cachePage;//缓存页 翻页后需要被移除的一页
    PullViewLayoutListener listener;//监听


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

    //针对当前数据进行初始化
    public void fromatDate(NovelChapter novelChapter, boolean ishead) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null);
        addView(v, 0);
        v.post(new Runnable() {
            @Override
            public void run() {
                NovelTextView novelTextView = v.findViewById(R.id.novel_content);
                novelTextViewHelp = novelTextView.getNovelTextViewHelp();
                ChapterInfo chapterInfo = new ChapterInfo(novelChapter.getChapterUrl(), novelChapter.getChapterName(), novelChapter.getChapterContent());
                chapterInfo.setNovelTextViewHelp(novelTextViewHelp);
                if (ishead) {
                    allDate.add(0, chapterInfo);
                } else {
                    allDate.add(chapterInfo);
                }
                removeView(v);
                if (contentPage == null) {
                    initContentPage(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
                }
                if (nextPage == null) {
                    initNextPage(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
                }
                if (fristPage == null) {
                    initFirstPage(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
                }
            }
        });
    }

    private void initContentPage(View v) {
        TextView novel_page = v.findViewById(R.id.novel_page);
        TextView novel_title = v.findViewById(R.id.novel_title);
        NovelTextView novelTextView = v.findViewById(R.id.novel_content);
        novelTextView.setNovelTextViewHelp(novelTextViewHelp);
        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
        if (contentPageIndex >= nowChapterInfo.getPage()) {
            contentPageIndex = nowChapterInfo.getPage() - 1;
        }
        novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), contentPageIndex);
        novel_page.setText((contentPageIndex + 1) + "/" + nowChapterInfo.getPage() + "");
        novel_title.setText(nowChapterInfo.getChapterName());
        contentPage = v;
        addView(contentPage);
    }

    private void initNextPage(View v) {
        TextView novel_page = v.findViewById(R.id.novel_page);
        TextView novel_title = v.findViewById(R.id.novel_title);
        NovelTextView novelTextView = v.findViewById(R.id.novel_content);
        novelTextView.setNovelTextViewHelp(novelTextViewHelp);
        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
        if (contentPageIndex + 1 >= allDate.get(chapterIndex).getPage()) {//如果下一页数据超出本章页数，需要取下一章数据
            if (chapterIndex + 1 < allDate.size()) {//如果有下一章
                PageInfo pageInfo = new PageInfo(chapterIndex + 1, 0);
                nowChapterInfo = allDate.get(chapterIndex + 1);
                novel_title.setText(nowChapterInfo.getChapterName());
                novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), 0);
                novel_page.setText(pageInfo.getContentIndex() + "/" + nowChapterInfo.getPage() + "");
                v.setTag(pageInfo);
                nextPage = v;
            } else {
                Toast.makeText(mcontext, "没有下一章的数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            PageInfo pageInfo = new PageInfo(chapterIndex, contentPageIndex + 1);
            novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), contentPageIndex + 1);
            novel_page.setText(pageInfo.getContentIndex() + "/" + nowChapterInfo.getPage() + "");
            novel_title.setText(nowChapterInfo.getChapterName());
            v.setTag(pageInfo);
            nextPage = v;
        }

        if (nextPage != null) {
            addView(nextPage, 0);
        } else {
            if (listener != null) {
                listener.loadMore();
            }
            Toast.makeText(mcontext, "没有下一页", Toast.LENGTH_SHORT).show();
        }
    }

    //是否切换了章节
    private void initFirstPage(View v) {
        TextView novel_page = v.findViewById(R.id.novel_page);
        TextView novel_title = v.findViewById(R.id.novel_title);
        NovelTextView novelTextView = v.findViewById(R.id.novel_content);
        novelTextView.setNovelTextViewHelp(novelTextViewHelp);
        ChapterInfo nowChapterInfo = allDate.get(chapterIndex);
        if (contentPageIndex - 1 < 0) {
            if (chapterIndex - 1 >= 0) {//如果有上一章
                PageInfo pageInfo = new PageInfo(chapterIndex - 1, nowChapterInfo.getPage() - 1);
                nowChapterInfo = allDate.get(chapterIndex - 1);
                novel_title.setText(nowChapterInfo.getChapterName());
                novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), pageInfo.getContentIndex());
                v.setTag(chapterIndex - 1);
                novel_page.setText(pageInfo.getContentIndex() + "/" + nowChapterInfo.getPage() + "");
                fristPage = v;
            } else {
                Toast.makeText(mcontext, "没有上一章的数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            PageInfo pageInfo = new PageInfo(chapterIndex, contentPageIndex - 1);
            novel_title.setText(nowChapterInfo.getChapterName());
            novelTextView.setTextArray(nowChapterInfo.getNovelPageInfos(), pageInfo.getContentIndex());
            novel_page.setText(pageInfo.getContentIndex() + "/" + nowChapterInfo.getPage() + "");
            v.setTag(pageInfo);
            fristPage = v;
        }
        if (fristPage != null) {
            fristPage.setTranslationX(-getWidth());
            addView(fristPage);
        } else {
            if (listener != null) {
                listener.loadBefor();
            }
            Toast.makeText(mcontext, "没有上一页", Toast.LENGTH_SHORT).show();
        }

    }


    public void addChapter(NovelChapter chapterInfo, boolean ishead) {
        fromatDate(chapterInfo, ishead);
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
//                            pullViewLayoutListener.onClickCenter();
                        }
                        return true;
                    } else if (Math.abs(moveX - downX) < 10) {//滑动效果未生效
                        Log.i("点击事件", "滑动效果未生效" + "     " + event.getX() + "      " + getWidth());
                        initAnimal();
                        valueAnimator.removeAllUpdateListeners();
                        valueAnimator.addUpdateListener(now);
                        valueAnimator.start();
//TODO 未滑动至1/3就松开了
                    } else {//滑动效果生效
                        Log.i("点击事件", "滑动效果生效" + "     " + event.getX() + "      " + getWidth());
                        if (event.getX() - downX > 0) {
                            if (fristPage == null) {
                                return true;
                            }
                        } else {
                            if (nextPage == null) {
                                return true;
                            }
                        }
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
        contentPageIndex++;
        cachePage = fristPage;
        fristPage = contentPage;
        contentPage = nextPage;
        chapterIndex = ((PageInfo) contentPage.getTag()).getChapterIndex();
        contentPageIndex = ((PageInfo) contentPage.getTag()).getContentIndex();
        nextPage = null;
        removeView(cachePage);
        initNextPage(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
    }

    @Nullable
    public void changeLastPage() {//上一页
        contentPageIndex--;
        cachePage = nextPage;
        nextPage = contentPage;
        contentPage = fristPage;
        chapterIndex = ((PageInfo) contentPage.getTag()).getChapterIndex();
        contentPageIndex = ((PageInfo) contentPage.getTag()).getContentIndex();
        fristPage = null;
        removeView(cachePage);
        initFirstPage(LayoutInflater.from(getContext()).inflate(R.layout.read_novel_page, null));
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
                    fristPage.setTranslationX((int) needMove);
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
                    contentPage.setTranslationX((int) needMove);
                    if ((float) animation.getAnimatedValue() == 1) {
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
                    dragState = -1;
                    return;
                }
            } else if (dragState == 0 && contentPage != null) {//左滑动  下一页
                float moveShift = -(moveX - downX) * (1 - (float) animation.getAnimatedValue());
                contentPage.setTranslationX(-moveShift);
                if ((float) animation.getAnimatedValue() == 1) {
                    dragState = -1;
                    return;
                }
            }
        }
    };


    public interface PullViewLayoutListener {
        public void loadMore();

        public void loadBefor();

        public void update();
    }

    public void setListener(PullViewLayoutListener listener) {
        this.listener = listener;
    }

    public void setNovelTextViewConfit(NovelTextViewHelp novelTextViewHelp) {
        this.novelTextViewHelp = novelTextViewHelp;
        for (int i = 0; i < allDate.size(); i++) {
            allDate.get(i).setNovelTextViewHelp(novelTextViewHelp);
        }
        initContentPage(contentPage);
        initNextPage(nextPage);
        initFirstPage(fristPage);
    }
}
