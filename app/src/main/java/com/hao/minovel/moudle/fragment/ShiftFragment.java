package com.hao.minovel.moudle.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.moudle.adapter.ShiftAdapter;

public class ShiftFragment extends MiBaseFragment implements View.OnClickListener {
    View fl_warn;

    public static MiBaseFragment newInstance() {
        Bundle args = new Bundle();
        MiBaseFragment fragment = new ShiftFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int onlayout() {
        return R.layout.fragment_shelf;
    }

    @Override
    public void initView(View v) {
        setBackGround(R.color.white);
        fl_warn = v.findViewById(R.id.fl_warn);
        RecyclerView mi_book_shift = v.findViewById(R.id.mi_book_shift);
        mi_book_shift.setLayoutManager(new LinearLayoutManager(getContext()));
        mi_book_shift.setAdapter(new ShiftAdapter(getContext()));
    }

    @Override
    public void setRound(float offset) {
        super.setRound(offset);
        Log.i("圆角", "ShiftFragment=" + offset);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
