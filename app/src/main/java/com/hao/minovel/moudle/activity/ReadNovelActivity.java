package com.hao.minovel.moudle.activity;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.NovelChapterAdapter;
import com.hao.minovel.moudle.adapter.OnItemClickListener;
import com.hao.minovel.moudle.adapter.TextTypefaceAdapter;
import com.hao.minovel.moudle.service.DownListener;
import com.hao.minovel.moudle.service.DownLoadNovelService;
import com.hao.minovel.moudle.service.NovolDownTask;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.view.minovelread.NovelTextView;
import com.hao.minovel.view.minovelread.PullViewLayout;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


@Bind
public class ReadNovelActivity extends MiBaseActivity implements PullViewLayout.PullViewLayoutListener, View.OnClickListener {
    NovelChapter novelChapter;//当前页下载的小说内容
    NovelTextView novelTextView;
    PullViewLayout novel_show;
    View readbook_config;//设置界面
    View left_drawer;//测滑菜单界面
    View mune;//标题栏


    RecyclerView text_typeface;
    private final int LOADNOW = 0;
    private final int LOADBEFORE = 1;
    private final int LOADMORE = 2;
    ValueAnimator valueAnimator;
    ShowState nowState = ShowState.SHOWNONE;
    LinkedBlockingQueue<ShowState> stateQueue = new LinkedBlockingQueue();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_setting_view:
                Log.i("当前操作", "点击事件：from_setting_view");
                stateQueue.clear();
                if (nowState != ShowState.SHOWNONE) {
                    stateQueue.add(ShowState.SHOWNONE);
                }
                stateQueue.add(ShowState.SHOWTYPEFACE);
                initDrawer(ShowState.SHOWTYPEFACE);
                muneSetting();
                break;
            case R.id.novel_all_chapter:
                Log.i("当前操作", "点击事件：novel_all_chapter");
                stateQueue.clear();
                if (nowState != ShowState.SHOWNONE) {
                    stateQueue.add(ShowState.SHOWNONE);
                }
                stateQueue.add(ShowState.SHOWCHAPTERS);
                initDrawer(ShowState.SHOWCHAPTERS);
                muneSetting();
                break;
        }
    }


    enum ShowState {
        SHOWCHAPTERS,//当前需显示字体列表
        SHOWTYPEFACE,//当前需显示章节列表
        SHOWNONE,//表示当前为阅读界面
        SHOWSETTING,//表示当前为设置界面
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_read_novel;
    }

    @Override
    protected void initView(View v) {
        text_typeface = v.findViewById(R.id.text_typeface);
        novelTextView = v.findViewById(R.id.novel_content);
        novel_show = v.findViewById(R.id.novel_show);
        mune = v.findViewById(R.id.mune);
        readbook_config = v.findViewById(R.id.readbook_config);

        readbook_config.post(new Runnable() {
            @Override
            public void run() {
                readbook_config.setTranslationY(readbook_config.getHeight());
            }
        });

        left_drawer = v.findViewById(R.id.left_drawer);
        left_drawer.post(new Runnable() {
            @Override
            public void run() {
                left_drawer.setTranslationX(-left_drawer.getWidth());
            }
        });

        v.findViewById(R.id.from_setting_view).setOnClickListener(this);
        v.findViewById(R.id.novel_all_chapter).setOnClickListener(this);
        readbook_config.setOnClickListener(this);
        novel_show.setListener(this);
    }

    @Override
    public void loadMore() {
        loadDate(LOADMORE);
    }

    @Override
    public void loadBefor() {
        loadDate(LOADBEFORE);
    }

    @Override
    public boolean clickCenter(PullViewLayout.ClickState clickState) {
        Log.i("当前操作", "点击事件：" + clickState);
        switch (clickState) {
            case onClick:
                if (nowState != ShowState.SHOWNONE) {
                    stateQueue.clear();
                    stateQueue.add(ShowState.SHOWNONE);
                    muneSetting();
                    return false;
                }
                break;
            case center:
                stateQueue.clear();
                stateQueue.add(ShowState.SHOWSETTING);
                muneSetting();
                break;
        }
        return true;
    }

    private void muneSetting() {
        ShowState state = stateQueue.poll();
        Log.i("当前操作", "当前状态：" + nowState + "        当前操作" + state);
        if (state == null) {
            return;
        }
        if (state == ShowState.SHOWTYPEFACE || state == ShowState.SHOWCHAPTERS) {//打开
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translateX = ((float) animation.getAnimatedValue() - 1) * left_drawer.getWidth();
                    left_drawer.setTranslationX(translateX);
                    if ((float) animation.getAnimatedValue() == 1) {
                        nowState = state;
                    }
                }
            });
            valueAnimator.start();
        } else if (state == ShowState.SHOWNONE) {//如果下一状态为所有界面都关闭
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (nowState == ShowState.SHOWSETTING) {
                        float translateY = (float) animation.getAnimatedValue() * readbook_config.getHeight();
                        readbook_config.setTranslationY(translateY);
                    } else {
                        float translateX = -(float) animation.getAnimatedValue() * left_drawer.getWidth();
                        left_drawer.setTranslationX(translateX);
                    }

                    if ((float) animation.getAnimatedValue() == 1) {
                        nowState = state;
                    }
                }
            });
            valueAnimator.start();
        } else if (state == ShowState.SHOWSETTING) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translateY = (1 - (float) animation.getAnimatedValue()) * readbook_config.getHeight();
                    readbook_config.setTranslationY(translateY);
                    if ((float) animation.getAnimatedValue() == 1) {
                        nowState = state;
                    }
                }
            });
            valueAnimator.start();
        }
    }


    @Override
    protected void doElse() {
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        loadDate(LOADNOW);
        initAnimal();
    }

    @Override
    protected void doOnSetContent(View v) {

    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case LOADNOW:
                    novel_show.setChapter((NovelChapter) msg.obj, false);
                    break;
                case LOADMORE:
                    novel_show.addChapter((NovelChapter) msg.obj, false);
                    break;
                case LOADBEFORE:
                    novel_show.addChapter((NovelChapter) msg.obj, true);
                    break;
            }
        }
    };

    private void loadDate(int loadState) {
        switch (loadState) {
            case LOADBEFORE:
                if (!TextUtils.isEmpty(novelChapter.getBeforChapterUrl())) {
                    NovelChapter search = DBManage.checkNovelChaptterByUrl(novelChapter.getBeforChapterUrl());
                    if (search == null) {
                        Toast.makeText(this, "暂未获取到上一章信息", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        novelChapter = search;
                    }
                }
                break;
            case LOADMORE:
                if (!TextUtils.isEmpty(novelChapter.getNextChapterUrl())) {
                    NovelChapter search = DBManage.checkNovelChaptterByUrl(novelChapter.getNextChapterUrl());
                    if (search == null) {
                        Toast.makeText(this, "暂未获取到下一章信息", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        novelChapter = search;
                    }
                }
                break;
            default:

                break;
        }
        if (TextUtils.isEmpty(novelChapter.getChapterContent())) {
            ServiceManage.getInstance().getBinder().sendCmd(new NovolDownTask<NovelChapter>(DownLoadNovelService.NovelDownTag.singlechaptercontent, novelChapter, new DownListener(this.getClass().getName(), true) {
                @Override
                public void downInfo(long all, long now) {

                }

                @Override
                public void startDown() {
                    switch (loadState) {
                        case LOADBEFORE:
                            Toast.makeText(ReadNovelActivity.this, "正在加载上一章节", Toast.LENGTH_LONG).show();
                            break;
                        case LOADMORE:
                            Toast.makeText(ReadNovelActivity.this, "正在加载下一章节", Toast.LENGTH_LONG).show();
                            break;
                        case LOADNOW:
                            Toast.makeText(ReadNovelActivity.this, "正在加载当前章节", Toast.LENGTH_LONG).show();
                            break;

                    }
                }

                @Override
                public void endDown(int state) {
                    if (state == SpiderUtils.Success) {
                        switch (loadState) {
                            case LOADBEFORE:
                                handler.sendMessage(handler.obtainMessage(LOADBEFORE, novelChapter));
                                break;
                            case LOADMORE:
                                handler.sendMessage(handler.obtainMessage(LOADMORE, novelChapter));
                                break;
                            case LOADNOW:
                                handler.sendMessage(handler.obtainMessage(LOADNOW, novelChapter));
                                break;
                        }
                    }
                }
            }));
        } else {
            handler.sendMessage(handler.obtainMessage(0, novelChapter));
        }
    }


    private void initDrawer(RecyclerView.Adapter adapter) {
        if (nowState == ShowState.SHOWCHAPTERS) {
            text_typeface.setAdapter(adapter);
            ((NovelChapterAdapter) text_typeface.getAdapter()).setSelect(novelChapter.getId().intValue());
            text_typeface.scrollToPosition(novelChapter.getId().intValue());
        } else if (nowState == ShowState.SHOWTYPEFACE) {
            text_typeface = findViewById(R.id.text_typeface);
            text_typeface.setLayoutManager(new LinearLayoutManager(AppContext.application));
            RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
            text_typeface.addItemDecoration(recycleViewDivider);
            text_typeface.setAdapter(adapter);
        }
    }


    public void initAnimal() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1f);
            valueAnimator.setDuration(500);
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    muneSetting();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    private void initChapterAdapter() {
        NovelChapterAdapter adpter = new NovelChapterAdapter(this).setItemClickLisener(new OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, Object object) {
                novelChapter = ((NovelChapterAdapter) text_typeface.getAdapter()).getItem(position);
                loadDate(LOADNOW);
            }
        });
        List<NovelChapter> novelChapters = DBManage.checkedNovelList(novelChapter.getNovelChapterListUrl());
        adpter.setNovelChapters(novelChapters);
    }


    private void initTypeFaceAdapter() {
        TextTypefaceAdapter adpter = new TextTypefaceAdapter(this).setItemClickLisener(new OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, Object object) {
                novel_show.getNovelTextViewHelp().setTypefaceName(((TextTypefaceAdapter) text_typeface.getAdapter()).getItem(position).getTypeFacename());
                novel_show.refreshView();
            }
        });
    }

}
