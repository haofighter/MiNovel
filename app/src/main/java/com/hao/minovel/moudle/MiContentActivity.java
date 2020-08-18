package com.hao.minovel.moudle;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.moudle.adapter.MiContentViewPagerAdapter;
import com.hao.minovel.tinker.app.AppContext;

import java.util.ArrayList;
import java.util.List;

@Bind
public class MiContentActivity extends MiBaseActivity {
    DrawerLayout drawerLayout;
    ViewPager viewContent;
    List<View> viewList = new ArrayList<>();

    {

        View v = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v.setBackgroundResource(R.color.colorAccent);
        v.setTag(1);
        viewList.add(v);
        View v1 = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v1.setBackgroundResource(R.color.purple);
        v1.setTag(2);
        viewList.add(v1);
        View v2 = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v2.setBackgroundResource(R.color.black);
        v2.setTag(3);
        viewList.add(v2);
        View v3 = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v3.setBackgroundResource(R.color.blue);
        v3.setTag(4);
        viewList.add(v3);
        View v4 = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v4.setBackgroundResource(R.color.red);
        v4.setTag(5);
        viewList.add(v4);
        View v5 = LayoutInflater.from(AppContext.application).inflate(R.layout.test, null, false);
        v5.setBackgroundResource(R.color.green);
        v5.setTag(6);
        viewList.add(v5);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_mi_shift;
    }

    @Override
    protected void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent));
        viewContent = findViewById(R.id.view_content);
    }

    @Override
    protected void doElse() {
        initViewPage();
        dodrawerDrag();
//        drawerLayout.openDrawer(Gravity.LEFT,true);
    }

    float lastslideOffset = 0;

    private void dodrawerDrag() {

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.i("drawer", "slideOffset=" + slideOffset);
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                viewContent.layout((int) (viewContent.getLeft() + drawerView.getWidth() * (slideOffset - lastslideOffset)), viewContent.getTop(), (int) (viewContent.getRight() + drawerView.getWidth() * (slideOffset - lastslideOffset)), viewContent.getBottom());
                viewContent.setScaleX((float) (1 - (0.2 * slideOffset)));
                viewContent.setScaleY((float) (1 - (0.2 * slideOffset)));
                if (slideOffset == 1) {
                    viewContent.layout(drawerView.getWidth(), viewContent.getTop(), drawerView.getWidth() + viewContent.getWidth(), viewContent.getBottom());
                } else if ((slideOffset == 0)) {
                    viewContent.layout(0, viewContent.getTop(), viewContent.getWidth(), viewContent.getBottom());
                }
                lastslideOffset = slideOffset;
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void initViewPage() {
        MiContentViewPagerAdapter viewPager = new MiContentViewPagerAdapter<View>();
        viewPager.addContents(viewList);
        viewContent.setPageTransformer(true, new CardTransfomer(100));
        viewContent.setAdapter(viewPager);
        viewContent.setCurrentItem(viewList.size() - 1);
    }


    private class CardTransfomer implements ViewPager.PageTransformer {

        private float mCardHeight = 100;

        public CardTransfomer(float cardheight) {
            this.mCardHeight = cardheight;
        }

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position <= 0) {
                view.setTranslationX(0f);
                view.setClickable(true);
            } else {
                view.setTranslationX(-view.getWidth() * position);
                float scale = (view.getWidth() - mCardHeight * position) / view.getWidth();
                view.setScaleX(scale);
                view.setScaleY(scale);
                view.setClickable(false);
                view.setTranslationY(mCardHeight * position);
            }
        }
    }

}
