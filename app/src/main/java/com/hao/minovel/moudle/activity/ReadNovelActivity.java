package com.hao.minovel.moudle.activity;


import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiDrawerActivity;
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
import com.hao.minovel.tinker.app.App;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.view.minovelread.NovelTextView;
import com.hao.minovel.view.minovelread.PullViewLayout;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import java.util.List;


@Bind
public class ReadNovelActivity extends MiDrawerActivity {
    NovelChapter novelChapter;//当前页下载的小说内容
    NovelTextView novelTextView;
    PullViewLayout novel_show;
    RecyclerView text_typeface;
    private final int LOADNOW = 0;
    private final int LOADBEFORE = 1;
    private final int LOADMORE = 2;

    enum ShowState {
        SHOWCHAPTERS,//当前需显示字体列表
        SHOWTYPEFACE;//当前需显示章节列表
    }


    @Override
    public int layoutDrawerId() {
        return R.layout.show_chapters;
    }

    @Override
    public int layoutContentId() {
        return R.layout.activity_read_novel;
    }

    @Override
    protected void initView(View v) {
        text_typeface = v.findViewById(R.id.text_typeface);
        novelTextView = v.findViewById(R.id.novel_content);
        novel_show = v.findViewById(R.id.novel_show);
        novel_show.setListener(new PullViewLayout.PullViewLayoutListener() {
            @Override
            public void loadMore() {
                loadDate(LOADMORE);
            }

            @Override
            public void loadBefor() {
                loadDate(LOADBEFORE);
            }

            @Override
            public void clickCenter() {
                initDrawer(ShowState.SHOWCHAPTERS);
                drawerLayout.openDrawer(Gravity.LEFT);
            }


        });

    }

    @Override
    protected void doElse() {
        super.doElse();
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        loadDate(LOADNOW);
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
                        Toast.makeText(this, "暂未获取到下一章信息", Toast.LENGTH_SHORT).show();
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


    private void initDrawer(ShowState showView) {
        if (showView == ShowState.SHOWCHAPTERS) {
            NovelChapterAdapter adpter = new NovelChapterAdapter(this).setItemClickLisener(new OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    novelChapter = ((NovelChapterAdapter) text_typeface.getAdapter()).getItem(position);
                    loadDate(LOADNOW);
                }
            });
            List<NovelChapter> novelChapters = DBManage.checkedNovelList(novelChapter.getNovelChapterListUrl());
            adpter.setNovelChapters(novelChapters);
            text_typeface.setAdapter(adpter);
            adpter.setSelect(novelChapter.getId().intValue());
            text_typeface.scrollToPosition(novelChapter.getId().intValue());
        } else if (showView == ShowState.SHOWTYPEFACE) {
            TextTypefaceAdapter adpter = new TextTypefaceAdapter(this).setItemClickLisener(new OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    novel_show.getNovelTextViewHelp().setTypefaceName(((TextTypefaceAdapter) text_typeface.getAdapter()).getItem(position).getTypeFacename());
                    novel_show.refreshView();
                }
            });

            text_typeface = findViewById(R.id.text_typeface);
            text_typeface.setLayoutManager(new LinearLayoutManager(AppContext.application));
//        text_typeface.addItemDecoration(new DividerItemDecoration(App.getInstance(), DividerItemDecoration.VERTICAL));
            RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
            text_typeface.addItemDecoration(recycleViewDivider);
            text_typeface.setAdapter(adpter);
        }
    }


}
