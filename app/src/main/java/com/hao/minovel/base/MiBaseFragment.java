package com.hao.minovel.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hao.minovel.R;
import com.hao.minovel.moudle.miinterface.FragmentListener;
import com.hao.minovel.view.RoundLayout;

public abstract class MiBaseFragment extends Fragment {
    String TAG = this.getClass().getName();
    protected RoundLayout roundLinearLayout;
    float offset;
    int maxRound = 90;
    protected FragmentListener fragmentListener;


    public abstract @LayoutRes
    int onlayout();

    public abstract void initView(View v);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView  " + offset);
        View v = inflater.inflate(R.layout.fragment_base, container, false);
        roundLinearLayout = v.findViewById(R.id.round_view);
        View content = inflater.inflate(onlayout(), container, false);
        roundLinearLayout.addView(content);
        setRound(offset);
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
        this.offset = offset;
        if (roundLinearLayout != null) {
            roundLinearLayout.setCornerRadius((int) (offset * maxRound));
            roundLinearLayout.invalidate();
        }
    }

    public void setBackGround(@ColorRes int color) {
        roundLinearLayout.setBackgroundResource(color);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        ViewParent viewParent = roundLinearLayout.getParent().getParent();
        if (viewParent instanceof View || viewParent instanceof ViewGroup) {
            ((View) viewParent).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        }
    }

    public MiBaseFragment setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
        return this;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "onHiddenChanged=" + hidden);
        super.onHiddenChanged(hidden);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        Log.i(TAG, "onAttachFragment");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
    }
}
