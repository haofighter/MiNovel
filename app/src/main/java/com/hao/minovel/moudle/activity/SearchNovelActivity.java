package com.hao.minovel.moudle.activity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hao.annotationengine.Router;
import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.TextNovelAdapter;
import com.hao.minovel.spider.SpiderNovelFromBaidu;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.sharelib.MMKVManager;

import java.util.ArrayList;
import java.util.List;

@Bind(path = "app/SearchNovelActivity")
public class SearchNovelActivity extends MiBaseActivity {

    private RecyclerView novel_list;
    private EditText search_novel_ed;
    List<NovelIntroduction> novelIntroductionList;

    TextNovelAdapter textNovelAdapter = new TextNovelAdapter(AppContext.application)
            .setItemClickLisener(new TextNovelAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void itemClick(int position, View view, Object object) {
                    if (!AppContext.checkedDoubleClick()) {
                        return;
                    }

                    saveSearchHistroy(search_novel_ed.getText().toString());

                    if (textNovelAdapter.isShowHistory()) {
                        String history = ((List<String>) textNovelAdapter.getDate()).get(position);
                        search_novel_ed.setText(history);
                    } else {
                        NovelIntroduction novelListItemContent = ((List<NovelIntroduction>) textNovelAdapter.getDate()).get(position);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("novelDetail", novelListItemContent);
                        Router.getInstance().build(ActivityConfig.NOVELDETAILACTIVITY, bundle).skip();
                    }
                }
            });

    private void saveSearchHistroy(String string) {
        if (string != null && !string.equals("")) {
            String history = (String) MMKVManager.getInstance().get("searchHistroy", "");
            if (!history.contains(string)) {
                MMKVManager.getInstance().put("searchHistroy", history + "_0u0_" + string);
            }
        }
    }

    private List<String> getSearchHistrory() {
        String history = (String) MMKVManager.getInstance().get("searchHistroy", "");
        List<String> strings = new ArrayList<>();
        String[] historys = history.split("_0u0_");
        for (int i = 0; i < historys.length; i++) {
            if (!historys[i].equals("")) {
                strings.add(0, historys[i]);
            }
        }
        return strings;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_search_novel;
    }

    @Override
    protected void initView(View v) {
        search_novel_ed = v.findViewById(R.id.search_novel_ed);
        addViewForNotHideSoftInput(search_novel_ed);
        search_novel_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    textNovelAdapter.setShowHistory(getSearchHistrory());
                } else {
                    novelIntroductionList = DBManage.checkNovelIntrodutionByStr(s.toString());
                    ((TextNovelAdapter) (novel_list.getAdapter())).setmNovelPage(novelIntroductionList);
                }
            }
        });


        novel_list = v.findViewById(R.id.novel_list);
        novel_list.setBackgroundResource(R.color.white);
        novel_list.setLayoutManager(new LinearLayoutManager(AppContext.application));
        novel_list.addItemDecoration(new DividerItemDecoration(AppContext.application, DividerItemDecoration.VERTICAL));
        novel_list.setAdapter(textNovelAdapter);

        findViewById(R.id.search_layout).setPadding(0, SystemUtil.getStatusBarHeight(this), 0, 0);
        findViewById(R.id.search_novel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpiderNovelFromBaidu.getSearchNovelList("");
            }
        });


    }

    @Override
    protected void doElse() {
        textNovelAdapter.setShowHistory(getSearchHistrory());
    }

    @Override
    protected void doOnSetContent(View v) {

    }
}
