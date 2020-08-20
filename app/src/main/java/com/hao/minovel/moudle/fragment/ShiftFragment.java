package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;

public class ShiftFragment extends MiBaseFragment {

    public static MiBaseFragment newInstance() {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new ShiftFragment();
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
        textView.setText("书架");
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "ShiftFragment=" + offset);
    }
}
