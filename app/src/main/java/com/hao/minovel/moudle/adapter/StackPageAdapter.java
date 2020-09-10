package com.hao.minovel.moudle.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hao.minovel.moudle.entity.StackTypeEntity;

import java.util.List;


public class StackPageAdapter extends FragmentPagerAdapter {
    private List<StackTypeEntity> mFragments;

    public StackPageAdapter(FragmentManager fm, List<StackTypeEntity> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getMiBaseFragment();
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getMuneItemm();
    }

    public List<StackTypeEntity> getmFragments() {
        return mFragments;
    }
}
