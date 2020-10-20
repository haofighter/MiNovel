package com.hao.minovel.moudle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hao.annotationengine.Router;
import com.hao.date.RouterContent;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.NovelListAdapter;
import com.hao.minovel.moudle.service.LoadHtmlService;
import com.hao.minovel.moudle.service.LoadWebInfo;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;
import com.hao.minovel.view.recycleviewhelp.MiRecycleView;

import java.util.List;

public class StackFragment extends MiBaseFragment {
    private final static int LOAD_END = 999;
    private final static int LOAD_START = 998;
    private final static int LOAD_ERR = 997;
    NovelType nowLoadNovetype;
    MiRecycleView recyclerView;
    SwipeRefreshLayout refresh_layout;
    int page = 1;
    boolean isLoading = false;
    private LoadWebInfo loadWebInfo = new LoadWebInfo();


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
        NovelListAdapter novelListAdapter = new NovelListAdapter(getContext(), false).setItemOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == R.id.reload || id == R.id.load_layout) {
                    refresh_layout.setRefreshing(true);
                    loadDate();
                } else {
                    NovelIntroduction novelIntroduction = ((NovelListAdapter) recyclerView.getAdapter()).getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("novelDetail", novelIntroduction);
                    Router.getInstance().build(RouterContent.NOVELDETAILACTIVITY, bundle).skip();
                }
            }
        });
        recyclerView.setAdapter(novelListAdapter);
        recyclerView.setScrollListener(new MiRecycleView.RecycleScrollListener() {
            @Override
            public void onScroll(int start, int end) {
                Log.i("滑动显示", "end=" + end);
                if (end == recyclerView.getAdapter().getItemCount() - 1) {
                    if (!isLoading) {
                        page++;
                        loadDate();
                    }
                }
            }
        });

        refresh_layout = v.findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (!loadDate()) {
                    refresh_layout.setRefreshing(false);
                }
            }
        });
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
                        if (!loadDate()) {//如果不需要进行网络请求 直接关闭刷新界面
                            refresh_layout.setRefreshing(false);
                        }
                    }
                    break;
                case LOAD_START:
                    refresh_layout.setRefreshing(true);
                    ((NovelListAdapter) recyclerView.getAdapter()).setLoadState(true);
                    break;
                case LOAD_ERR:
                    ((NovelListAdapter) recyclerView.getAdapter()).setLoadState(false);
                    refresh_layout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void eventBusOnEvent(LoadWebInfo str) {
        if (str.getLoadStatus() == SpiderUtils.LOADING) {
            handler.obtainMessage(LOAD_START);
        } else if (str.getLoadStatus() == SpiderUtils.Success) {
            nowLoadNovetype = DBManage.checkedNextNovelType(nowLoadNovetype.getListUrl());//获取到当前列表页的完整数据
            NovelType nextLoadNoveltype = DBManage.checkedNextNovelType(nowLoadNovetype.getNextListUrl());//获取到下一页的数据
            if (nextLoadNoveltype != null && !TextUtils.isEmpty(nextLoadNoveltype.getListUrl())) {
                nowLoadNovetype = nextLoadNoveltype;
            }
            isLoading = false;
            handler.sendMessage(handler.obtainMessage(LOAD_END, nowLoadNovetype.getType()));
        } else {
            isLoading = false;
            handler.sendMessage(handler.obtainMessage(LOAD_ERR, nowLoadNovetype.getType()));
        }
        super.eventBusOnEvent(str);
    }

    /**
     * 获取数据
     *
     * @return 返回是否需要进行网络请求
     */
    private boolean loadDate() {
        List<NovelIntroduction> novelIntroductions = DBManage.getNovelByType(nowLoadNovetype.getType(), page);
        if (novelIntroductions != null && novelIntroductions.size() > 0) {
            isLoading = false;
            if (page == 1) {
                ((NovelListAdapter) recyclerView.getAdapter()).setDate(novelIntroductions);
                ((NovelListAdapter) recyclerView.getAdapter()).addHead(DBManage.checkedNovelHotByType(nowLoadNovetype.getType()));
            } else {
                ((NovelListAdapter) recyclerView.getAdapter()).addDate(novelIntroductions);
            }
            return false;
        } else {
            isLoading = true;
            Intent intent = new Intent(getContext(), LoadHtmlService.class);
            loadWebInfo.setTask(LoadHtmlService.noveltypelist);
            intent.putExtra(LoadHtmlService.TASK, loadWebInfo);
            intent.putExtra(LoadHtmlService.DATE, nowLoadNovetype);
            getContext().startService(intent);
            return true;
        }
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "BookListFragment=" + offset);
    }
}
