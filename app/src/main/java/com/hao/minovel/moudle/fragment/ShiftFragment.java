package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.view.View;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;

public class ShiftFragment extends MiBaseFragment {

    public static MiBaseFragment newInstance(int pageIndex) {
        Bundle args = new Bundle();
        args.putInt("key", pageIndex);
        MiBaseFragment fragment = new ShiftFragment(pageIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public ShiftFragment(Object flag) {
        super(flag);
    }

    @Override
    public int onlayout() {
        return R.layout.test;
    }

    @Override
    public void initView(View v) {

    }
}
