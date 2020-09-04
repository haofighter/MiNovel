package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;

public class StackFragment extends MiBaseFragment {

    public static MiBaseFragment newInstance() {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new StackFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int onlayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View v) {
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "BookListFragment=" + offset);
    }
}
