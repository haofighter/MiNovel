package com.hao.minovel.moudle.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.moudle.adapter.TextNovelAdapter;
import com.hao.sharelib.MMKVManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends MiBaseFragment {
    EditText search_novel_ed;
    RecyclerView novel_list;

    public static MiBaseFragment newInstance() {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int onlayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView(View v) {
        setBackGround(R.color.white);
        search_novel_ed = ((EditText) v.findViewById(R.id.search_novel_ed));
        search_novel_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        novel_list = v.findViewById(R.id.novel_list);
        novel_list.setBackgroundResource(R.color.white);
        novel_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        novel_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        novel_list.setAdapter(new TextNovelAdapter(getActivity())
                .setItemClickLisener(new TextNovelAdapter.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void itemClick(int position, View view, Object object) {

                        saveSearchHistroy(search_novel_ed.getText().toString());

                        if (((TextNovelAdapter)novel_list.getAdapter()).isShowHistory()) {
                            String history = ((List<String>) ((TextNovelAdapter)novel_list.getAdapter()).getDate()).get(position);
                            search_novel_ed.setText(history);
                        } else {
//                            NovelIntroduction novelListItemContent = ((List<NovelIntroduction>) textNovelAdapter.getDate()).get(position);
//                            Intent intent = new Intent(SearchNovelActivity.this, NovelDetailActivity.class);
//                            intent.putExtra("novelId", novelListItemContent.getNovelChapterListUrl());
//                            startActivity(intent);
                        }
                    }
                }));
    }


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
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "SearchFragment=" + offset);
    }


}
