package com.hao.minovel.moudle.activity;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.NovelChapterAdapter;
import com.hao.minovel.moudle.adapter.OnItemClickListener;
import com.hao.minovel.moudle.adapter.TextTypefaceAdapter;
import com.hao.minovel.moudle.entity.ReadInfo;
import com.hao.minovel.moudle.service.LoadHtmlService;
import com.hao.minovel.moudle.service.LoadWebInfo;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.minovelread.ChapterInfo;
import com.hao.minovel.view.minovelread.NovelTextView;
import com.hao.minovel.view.minovelread.NovelTextViewHelp;
import com.hao.minovel.view.minovelread.PullViewLayout;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import java.util.concurrent.LinkedBlockingQueue;


@Bind
public class ReadNovelActivity extends MiBaseActivity implements PullViewLayout.PullViewLayoutListener, View.OnClickListener {
    NovelChapter novelChapter;//当前页下载的小说内容
    NovelTextView novelTextView;
    PullViewLayout novel_show;
    TextView mune_title;
    View readbook_config;//设置界面
    View read_novel_title;//标题
    View left_drawer;//测滑菜单界面
    View mune;//标题栏


    RecyclerView text_typeface;
    NovelChapterAdapter chapterAdapter;
    TextTypefaceAdapter textTypefaceAdapter;
    private final int LOADNOW = 0;
    private final int LOADBEFORE = 1;
    private final int LOADMORE = 2;
    ValueAnimator valueAnimator;
    ShowState nowState = ShowState.SHOWNONE;
    LinkedBlockingQueue<ShowState> stateQueue = new LinkedBlockingQueue();
    NovelTextViewHelp novelTextViewHelp;

    LoadWebInfo loadWebInfo = new LoadWebInfo();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_setting_view:
                if (stateQueue.size() > 0) {
                    Log.i("当前操作", "操作对列不为空,表示当前有操作未执行完成");
                } else {
                    if (nowState != ShowState.SHOWNONE) {
                        stateQueue.add(ShowState.SHOWNONE);
                    }
                    stateQueue.add(ShowState.SHOWTYPEFACE);
                    initDrawer(ShowState.SHOWTYPEFACE);
                    muneSetting();
                }
                break;
            case R.id.novel_all_chapter:
                if (stateQueue.size() > 0) {
                    Log.i("当前操作", "操作对列不为空,表示当前有操作未执行完成");
                } else {
                    if (nowState != ShowState.SHOWNONE) {
                        stateQueue.add(ShowState.SHOWNONE);
                    }
                    stateQueue.add(ShowState.SHOWCHAPTERS);
                    initDrawer(ShowState.SHOWCHAPTERS);
                    muneSetting();
                }
                break;
            case R.id.add_textsize:
                if (novelTextViewHelp != null) {
                    novelTextViewHelp.setTextSize(novelTextViewHelp.getTextSizeSp() + 1);
                    novel_show.setNovelTextViewConfit(novelTextViewHelp);
                }
                break;
            case R.id.reduce_textsize:
                if (novelTextViewHelp != null) {
                    novelTextViewHelp.setTextSize(novelTextViewHelp.getTextSizeSp() - 1);
                    novel_show.setNovelTextViewConfit(novelTextViewHelp);
                }
                break;
            case R.id.add_line_spac:
                if (novelTextViewHelp != null) {
                    novelTextViewHelp.setLineSpacingExtra(novelTextViewHelp.getLineSpacingExtraSp() + 1);
                    novel_show.setNovelTextViewConfit(novelTextViewHelp);
                }
                break;
            case R.id.reduce_line_spac:
                if (novelTextViewHelp != null) {
                    novelTextViewHelp.setLineSpacingExtra(novelTextViewHelp.getLineSpacingExtraSp() - 1);
                    novel_show.setNovelTextViewConfit(novelTextViewHelp);
                }
                break;
            case R.id.back:
                finish();
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
        text_typeface.setLayoutManager(new LinearLayoutManager(AppContext.application));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
        text_typeface.addItemDecoration(recycleViewDivider);

        novelTextView = v.findViewById(R.id.novel_content);
        novel_show = v.findViewById(R.id.novel_show);
        mune = v.findViewById(R.id.mune);
        mune_title = v.findViewById(R.id.mune_title);
        mune_title.setPadding(mune_title.getPaddingLeft(), mune_title.getPaddingTop() + SystemUtil.getStatusBarHeight(this), mune_title.getPaddingRight(), mune_title.getPaddingBottom());

        read_novel_title = v.findViewById(R.id.read_novel_title);
        View read_novel_title_content = v.findViewById(R.id.read_novel_title_content);
        TextView title_name = v.findViewById(R.id.title_name);
        NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(novelChapter.getNovelChapterListUrl());
        if (novelIntroduction != null) {
            title_name.setText(novelIntroduction.getNovelName());
        } else {
            title_name.setText("获取小说名失败");
        }

        read_novel_title_content.setPadding(read_novel_title_content.getPaddingLeft(), read_novel_title_content.getPaddingTop() + SystemUtil.getStatusBarHeight(this), read_novel_title_content.getPaddingRight(), read_novel_title_content.getPaddingBottom());
        read_novel_title.post(new Runnable() {
            @Override
            public void run() {
                read_novel_title.setTranslationY(-read_novel_title.getHeight());
            }
        });

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
        v.findViewById(R.id.add_line_spac).setOnClickListener(this);
        v.findViewById(R.id.add_textsize).setOnClickListener(this);
        v.findViewById(R.id.reduce_line_spac).setOnClickListener(this);
        v.findViewById(R.id.reduce_textsize).setOnClickListener(this);
        v.findViewById(R.id.back).setOnClickListener(this);
        readbook_config.setOnClickListener(this);
        novel_show.setListener(this);
    }

    @Override
    public void loadMore(String url) {
        NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(url);
        if (novelChapter != null) {
            this.novelChapter = novelChapter;
            loadDate(LOADMORE);
        } else {
            Toast.makeText(this, "无下一章", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadBefor(String url) {
        NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(url);
        if (novelChapter != null) {
            this.novelChapter = novelChapter;
            loadDate(LOADBEFORE);
        } else {
            Toast.makeText(this, "无上一章", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initConfig(NovelTextViewHelp novelTextViewHelp) {
        initSettingViewDate(novelTextViewHelp);
    }

    private void initSettingViewDate(NovelTextViewHelp novelTextViewHelp) {
        this.novelTextViewHelp = novelTextViewHelp;
        ((TextView) findViewById(R.id.textsize)).setText(novelTextViewHelp.getTextSizeSp() + "");
        ((TextView) findViewById(R.id.line_spac_size)).setText(novelTextViewHelp.getLineSpacingExtraSp() + "");
    }

    @Override
    public void onPageChange(ChapterInfo chapterInfo) {
        Log.i("显示参数", "章节：" + chapterInfo.getChapterName() + "   " + chapterInfo.getNowChapterUrl());
        try {
            ReadInfo readInfo = new ReadInfo();
            NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(chapterInfo.getNowChapterUrl());
            readInfo.setNovelChapterListUrl(novelChapter.getNovelChapterListUrl());
            readInfo.setNovelChapterUrl(novelChapter.getChapterUrl());
            readInfo.setDate(System.currentTimeMillis());
            readInfo.setPage(novel_show.getContentPageIndex());
            DBManage.saveReadInfo(readInfo);
            Log.i("阅读界面", "保存阅读信息=" + readInfo.toString());
        } catch (Exception e) {
            Log.e("阅读界面", "保存阅读信息失败");
        }
        if (text_typeface.getAdapter() instanceof NovelChapterAdapter) {
            ((NovelChapterAdapter) text_typeface.getAdapter()).setSelect(chapterInfo.getNowChapterUrl());
        }
    }

    @Override
    public boolean clickCenter(PullViewLayout.ClickState clickState) {
        Log.i("当前操作", "点击事件：" + clickState);
        switch (clickState) {
            case onTouch:
                if (nowState != ShowState.SHOWNONE) {
                    return false;
                }
                break;
            case onClick:
                if (nowState != ShowState.SHOWNONE) {
                    if (stateQueue.size() == 0) {
                        stateQueue.add(ShowState.SHOWNONE);
                        muneSetting();
                    }
                    return false;
                }
                break;
            case center:
                if (stateQueue.size() == 0) {
                    stateQueue.add(ShowState.SHOWSETTING);
                    muneSetting();
                }
                break;
        }
        return true;
    }

    private void muneSetting() {
        ShowState state = stateQueue.peek();
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
                        stateQueue.remove(state);
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
                        float topTranslateY = (float) animation.getAnimatedValue() * -read_novel_title.getHeight();
                        read_novel_title.setTranslationY(topTranslateY);
                    } else if (nowState == ShowState.SHOWTYPEFACE || nowState == ShowState.SHOWCHAPTERS) {
                        float translateX = -(float) animation.getAnimatedValue() * left_drawer.getWidth();
                        left_drawer.setTranslationX(translateX);
                    }

                    if ((float) animation.getAnimatedValue() == 1) {
                        nowState = state;
                        stateQueue.remove(state);
                    }
                }
            });
            valueAnimator.start();
        } else if (state == ShowState.SHOWSETTING) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float bottomTranslateY = (1 - (float) animation.getAnimatedValue()) * readbook_config.getHeight();
                    readbook_config.setTranslationY(bottomTranslateY);
                    float topTranslateY = (1 - (float) animation.getAnimatedValue()) * -read_novel_title.getHeight();
                    read_novel_title.setTranslationY(topTranslateY);
                    if ((float) animation.getAnimatedValue() == 1) {
                        nowState = state;
                        stateQueue.remove(state);
                    }
                }
            });
            valueAnimator.start();
        }
    }


    @Override
    protected void doElse() {
        loadDate(LOADNOW);
        initAnimal();
        initChapterAdapter();
        initTypeFaceAdapter();
    }

    @Override
    protected void doOnSetContent(View v) {
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        if (novelChapter == null) {
            String chapterUrl = getIntent().getStringExtra("readInfo");
            novelChapter = chapterUrl == null ? null : DBManage.checkNovelChaptterByUrl(chapterUrl);
            if (novelChapter == null) {
                Toast.makeText(this, "获取小说章节失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case LOADNOW:
                    ReadInfo readInfo = DBManage.checkedReadInfo(novelChapter.getNovelChapterListUrl());
                    if (readInfo != null && readInfo.getNovelChapterUrl().equals(((NovelChapter) msg.obj).getChapterUrl())) {
                        novel_show.setChapter((NovelChapter) msg.obj, readInfo.getPage());
                        Log.i("跳转", "novel_show=" + readInfo.getPage());
                    } else {
                        novel_show.setChapter((NovelChapter) msg.obj);
                    }
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
            Intent intent = new Intent(this, LoadHtmlService.class);
            loadWebInfo.setTask(LoadHtmlService.singlechaptercontent);
            loadWebInfo.setTag(loadState);
            intent.putExtra(LoadHtmlService.TASK, loadWebInfo);
            intent.putExtra(LoadHtmlService.DATE, novelChapter);
            startService(intent);
//            ServiceManage.getInstance().getBinder().sendCmd(new NovolDownTask<NovelChapter>(DownLoadNovelService.NovelDownTag.singlechaptercontent, novelChapter, new DownListener(this.getClass().getName(), true) {
//                @Override
//                public void downInfo(long all, long now) {
//
//                }
//
//                @Override
//                public void startDown() {
//                    switch (loadState) {
//                        case LOADBEFORE:
//                            Toast.makeText(ReadNovelActivity.this, "正在加载上一章节", Toast.LENGTH_LONG).show();
//                            break;
//                        case LOADMORE:
//                            Toast.makeText(ReadNovelActivity.this, "正在加载下一章节", Toast.LENGTH_LONG).show();
//                            break;
//                        case LOADNOW:
//                            Toast.makeText(ReadNovelActivity.this, "正在加载当前章节", Toast.LENGTH_LONG).show();
//                            break;
//
//                    }
//                }
//
//                @Override
//                public void endDown(int state) {
//                    if (state == SpiderUtils.Success) {
//                        switch (loadState) {
//                            case LOADBEFORE:
//                                handler.sendMessage(handler.obtainMessage(LOADBEFORE, novelChapter));
//                                break;
//                            case LOADMORE:
//                                handler.sendMessage(handler.obtainMessage(LOADMORE, novelChapter));
//                                break;
//                            case LOADNOW:
//                                handler.sendMessage(handler.obtainMessage(LOADNOW, novelChapter));
//                                break;
//                        }
//                    }
//                }
//            }));
        } else {
            handler.sendMessage(handler.obtainMessage(loadState, novelChapter));
        }
    }


    @Override
    public void eventBusOnEvent(LoadWebInfo str) {
        if (str.getLoadStatus() == SpiderUtils.LOADING) {
            switch (str.getTag()) {
                case LOADBEFORE:
                    Toast.makeText(ReadNovelActivity.this, "正在加载上一章节", Toast.LENGTH_SHORT).show();
                    break;
                case LOADMORE:
                    Toast.makeText(ReadNovelActivity.this, "正在加载下一章节", Toast.LENGTH_SHORT).show();
                    break;
                case LOADNOW:
                    Toast.makeText(ReadNovelActivity.this, "正在加载当前章节", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (str.getLoadStatus() == SpiderUtils.Success) {
            novelChapter = DBManage.checkNovelChaptterByUrl(novelChapter.getChapterUrl());
            switch (str.getTag()) {
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
        } else {
            Toast.makeText(ReadNovelActivity.this, "加载失败，请稍后重试", Toast.LENGTH_LONG).show();
        }
        super.eventBusOnEvent(str);
    }

    private void initDrawer(ShowState nowState) {
        if (nowState == ShowState.SHOWCHAPTERS) {
            initChapterAdapter();
            mune_title.setText("小说章节");
            text_typeface.setAdapter(chapterAdapter);
            text_typeface.scrollToPosition(novelChapter.getId().intValue());
        } else if (nowState == ShowState.SHOWTYPEFACE) {
            initTypeFaceAdapter();
            mune_title.setText("字体");
            text_typeface.setAdapter(textTypefaceAdapter);
        }
    }


    public void initAnimal() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1f);
            valueAnimator.setDuration(300);
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
        if (chapterAdapter == null) {
            chapterAdapter = new NovelChapterAdapter(this).setItemClickLisener(new OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    novelChapter = ((NovelChapterAdapter) text_typeface.getAdapter()).getItem(position);
                    loadDate(LOADNOW);
                }
            });
            chapterAdapter.setNovelChapters(novelChapter);
        }
    }


    private void initTypeFaceAdapter() {
        if (textTypefaceAdapter == null) {
            textTypefaceAdapter = new TextTypefaceAdapter(this).setItemClickLisener(new OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    novel_show.getNovelTextViewHelp().setTypefaceName(((TextTypefaceAdapter) text_typeface.getAdapter()).getItem(position).getTypeFacename());
                    novel_show.refreshView();
                }
            });
        }
    }

}
