package com.hao.minovel.moudle.activity;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiDrawerActivity;
import com.hao.minovel.moudle.service.DownListener;
import com.hao.minovel.moudle.service.DownLoadNovelService;
import com.hao.minovel.moudle.service.NovolDownTask;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.view.minovelread.ChapterInfo;
import com.hao.minovel.view.minovelread.NovelTextView;
import com.hao.minovel.view.minovelread.PullViewLayout;


@Bind
public class ReadNovelActivity extends MiDrawerActivity {
    NovelChapter novelChapter;//当前页下载的小说内容
    NovelTextView novelTextView;
    PullViewLayout novel_show;

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
    }

    @Override
    protected void doElse() {
        super.doElse();
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        loadDate();
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    novel_show.addChapter((NovelChapter) msg.obj);
                    break;
            }
        }
    };

    private void loadDate() {
        if (TextUtils.isEmpty(novelChapter.getChapterContent())) {
            ServiceManage.getInstance().getBinder().sendCmd(new NovolDownTask<NovelChapter>(DownLoadNovelService.NovelDownTag.singlechaptercontent, novelChapter, new DownListener(this.getClass().getName(), true) {
                @Override
                public void downInfo(long all, long now) {

                }

                @Override
                public void startDown() {

                }

                @Override
                public void endDown(int state) {
                    handler.sendMessage(handler.obtainMessage(0, novelChapter));
                }
            }));
        } else {
            handler.sendMessage(handler.obtainMessage(0, novelChapter));
        }
    }
}
