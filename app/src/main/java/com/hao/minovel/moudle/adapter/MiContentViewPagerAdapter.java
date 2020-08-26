package com.hao.minovel.moudle.adapter;


import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.moudle.entity.ContentMuneEntity;

import java.util.List;

/**
 * Created by 江俊超 on 2016/12/30 0030.
 * <p>Gihub https://github.com/aohanyao</p>
 * <p>所有ViewPager的适配器</p>
 */

public class MiContentViewPagerAdapter extends FragmentPagerAdapter {
    private List<ContentMuneEntity> mFragments;
    private String[] mTitles;
    private int nowCheck;

    public MiContentViewPagerAdapter(FragmentManager fm, List<ContentMuneEntity> mFragments, String[] mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public MiBaseFragment getItem(int position) {
        return mFragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles != null || mTitles.length > position ? mTitles[position] : "";
    }

    public void setPageRound(float slideOffset, int currentItem) {
        this.nowCheck = currentItem;
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).getFragment().setRound(slideOffset);
        }
    }
}
