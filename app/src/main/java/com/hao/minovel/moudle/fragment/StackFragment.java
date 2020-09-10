package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.NovelListAdapter;
import com.hao.minovel.moudle.service.DownListener;
import com.hao.minovel.moudle.service.DownLoadNovelService;
import com.hao.minovel.moudle.service.NovolDownTask;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;
import com.hao.minovel.tinker.app.App;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class StackFragment extends MiBaseFragment {
    private final static int LOAD_END = 999;
    private final static int LOAD_START = 998;
    NovelType nowLoadNovetype;
    RecyclerView recyclerView;
    boolean isloadend;
    int page = 1;


    public static MiBaseFragment newInstance(NovelType novelType) {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new StackFragment(novelType);
        fragment.setArguments(args);
        return fragment;
    }

    public StackFragment(NovelType nowLoadNovetype) {
        this.nowLoadNovetype = nowLoadNovetype;
    }

    @Override
    public int onlayout() {
        return R.layout.fragment_stack;
    }


    @Override
    public void initView(View v) {
        recyclerView = v.findViewById(R.id.novel_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        NovelListAdapter novelListAdapter = new NovelListAdapter(getContext(), true);
        recyclerView.setAdapter(novelListAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDate();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_END:
                    if (((String) msg.obj).equals(nowLoadNovetype.getType())) {
                        loadDate();
                    }
                    break;
                case LOAD_START:
                    break;
            }
        }
    };


    private void loadDate() {
        List<NovelIntroduction> novelIntroductions = DBManage.getNovelByType(nowLoadNovetype.getType(), page);
        Log.i("fragment调用", " 当前查询到" + nowLoadNovetype.getType() + novelIntroductions.size() + "本");
        if (novelIntroductions != null && novelIntroductions.size() > 0) {
            if (page == 1) {
                ((NovelListAdapter) recyclerView.getAdapter()).setDate(novelIntroductions);
                ((NovelListAdapter) recyclerView.getAdapter()).addHead(DBManage.checkedNovelHotByType(nowLoadNovetype.getType()));
            } else {
                ((NovelListAdapter) recyclerView.getAdapter()).addDate(novelIntroductions);
            }
            isloadend = true;
        } else {
            ServiceManage.getInstance().getBinder().sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.noveltypelist, nowLoadNovetype, new DownListener(this.getClass().getName(), true) {
                @Override
                public void downInfo(long all, long now) {

                }

                @Override
                public void startDown() {
                    handler.obtainMessage(LOAD_START);
                }

                @Override
                public void endDown() {
                    nowLoadNovetype = DBManage.checkedNextNovelType(nowLoadNovetype.getListUrl());//获取到当前列表页的完整数据
                    Log.i("fragment调用  本次数据", nowLoadNovetype.toString());
                    nowLoadNovetype = DBManage.checkedNextNovelType(nowLoadNovetype.getNextListUrl());//获取到下一页的数据
                    Log.i("fragment调用  下一页数据", nowLoadNovetype.toString() + "       " + nowLoadNovetype.getId());
                    Message message = handler.obtainMessage(LOAD_END, nowLoadNovetype.getType());
                    handler.sendMessage(message);
                }
            }, true));
        }
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "BookListFragment=" + offset);
    }


}
