package com.hao.minovel.moudle.activity;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiDrawerActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.service.DownListener;
import com.hao.minovel.moudle.service.DownLoadNovelService;
import com.hao.minovel.moudle.service.NovolDownTask;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.view.minovelread.NovelTextView;
import com.hao.minovel.view.minovelread.PullViewLayout;


@Bind
public class ReadNovelActivity extends MiDrawerActivity {
    NovelChapter novelChapter;//当前页下载的小说内容
    NovelTextView novelTextView;
    PullViewLayout novel_show;
    private final int LOADNOW = 0;
    private final int LOADBEFORE = 1;
    private final int LOADMORE = 2;

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
            public void update() {

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


}
