package com.hao.minovel.moudle.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MiContentViewPagerAdapter<T> extends PagerAdapter {
    List<View> contents = new ArrayList<>();

    @Override
    public int getCount() {
        return contents.size();
    }

    public void addContents(List<View> content) {
        this.contents.addAll(content);
    }

    public void addContent(View content) {
        this.contents.add(content);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View t = contents.get(position);
        container.addView(t);
        Log.e("MiContentViewPager", "填充le一个view:" + ((View) t).getTag() + "      " + ((View) t).getWidth());
        return t;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View t = contents.get(position);
        container.removeView(t);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
