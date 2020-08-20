package com.hao.minovel.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hao.minovel.R;
import com.hao.minovel.view.RoundLinearLayout;

public abstract class MiBaseFragment extends Fragment {
    RoundLinearLayout roundLinearLayout;
    Object flag;
    float offset;
    int maxRound = 90;

    public MiBaseFragment() {
    }

    public MiBaseFragment(Object flag) {
        this.flag = flag;
    }

    public abstract @LayoutRes
    int onlayout();

    public abstract void initView(View v);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        roundLinearLayout = v.findViewById(R.id.round_view);
        View content = inflater.inflate(onlayout(), container, false);
        roundLinearLayout.addView(content);
        roundLinearLayout.setCornerRadius((int) (maxRound * offset));
        initView(roundLinearLayout);
        container.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        return v;
    }

    /**
     * 进度
     *
     * @param offset 0-1
     */
    public void setRound(float offset) {
        if (roundLinearLayout != null) {
            roundLinearLayout.setCornerRadius((int) (offset * maxRound));
            roundLinearLayout.invalidate();
        } else {
            this.offset = offset;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        su
    per.onViewCreated(view, savedInstanceState);
        ViewParent viewParent = roundLinearLayout.getParent().getParent();
        if (viewParent instanceof View || viewParent instanceof ViewGroup) {
            ((View) viewParent).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        }
    }

}
