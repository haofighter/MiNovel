package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;

public class BookListFragment extends MiBaseFragment {

    public static MiBaseFragment newInstance() {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int onlayout() {
        return R.layout.test;
    }

    @Override
    public void initView(View v) {
        TextView textView = v.findViewById(R.id.show_content);
        textView.setText("书单");
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角","BookListFragment="+offset);
    }
}
