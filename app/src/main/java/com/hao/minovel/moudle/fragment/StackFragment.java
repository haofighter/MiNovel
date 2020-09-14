package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hao.annotationengine.Router;
import com.hao.date.RouterContent;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.NovelListAdapter;
import com.hao.minovel.moudle.service.DownListener;
import com.hao.minovel.moudle.service.DownLoadNovelService;
import com.hao.minovel.moudle.service.NovolDownTask;
import com.hao.minovel.moudle.service.ServiceManage;
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
                    page++;
                    loadDate();
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
        refresh_layout.setRefreshing(true);
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
                    break;
                case LOAD_ERR:
                    ((NovelListAdapter) recyclerView.getAdapter()).setLoadState(false);
                    refresh_layout.setRefreshing(false);
                    break;
            }
        }
    };


    /**
     * 获取数据
     *
     * @return 返回是否需要进行网络请求
     */
    private boolean loadDate() {
        List<NovelIntroduction> novelIntroductions = DBManage.getNovelByType(nowLoadNovetype.getType(), page);
        Log.i("fragment调用", "page=" + page + "     当前查询到" + nowLoadNovetype.getType() + novelIntroductions.size() + "本");
        if (novelIntroductions != null && novelIntroductions.size() > 0) {
            if (page == 1) {
                ((NovelListAdapter) recyclerView.getAdapter()).setDate(novelIntroductions);
                ((NovelListAdapter) recyclerView.getAdapter()).addHead(DBManage.checkedNovelHotByType(nowLoadNovetype.getType()));
            } else {
                ((NovelListAdapter) recyclerView.getAdapter()).addDate(novelIntroductions);
            }
            return false;
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
                public void endDown(int state) {
                    NovelType oldNovelType = nowLoadNovetype;
                    try {
                        if (state == SpiderUtils.Success) {
                            nowLoadNovetype = DBManage.checkedNextNovelType(nowLoadNovetype.getListUrl());//获取到当前列表页的完整数据
                            Log.i("fragment调用  本次数据", nowLoadNovetype.toString());
                            nowLoadNovetype = DBManage.checkedNextNovelType(nowLoadNovetype.getNextListUrl());//获取到下一页的数据
                            Log.i("fragment调用  下一页数据", nowLoadNovetype.toString() + "       " + nowLoadNovetype.getId());
                            handler.sendMessage(handler.obtainMessage(LOAD_END, nowLoadNovetype.getType()));
                        } else {
                            handler.sendMessage(handler.obtainMessage(LOAD_ERR, nowLoadNovetype.getType()));
                        }
                    } catch (Exception e) {
                        nowLoadNovetype = oldNovelType;
                        handler.sendMessage(handler.obtainMessage(LOAD_ERR, nowLoadNovetype.getType()));
                    }
                }
            }, true));
            return true;
        }
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "BookListFragment=" + offset);
    }


}
