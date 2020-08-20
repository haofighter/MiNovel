package com.hao.minovel.moudle.activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.moudle.adapter.ContentMuneAdapter;
import com.hao.minovel.moudle.adapter.MiContentViewPagerAdapter1;
import com.hao.minovel.moudle.entity.ContentMuneEntity;
import com.hao.minovel.moudle.fragment.BookListFragment;
import com.hao.minovel.moudle.fragment.SearchFragment;
import com.hao.minovel.moudle.fragment.ShiftFragment;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.RoundLinearLayout;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

@Bind
public class MiContentActivity extends MiBaseActivity {
    DrawerLayout drawerLayout;
    ViewPager viewContent;
    RoundLinearLayout viewpagerParent;
    RecyclerView contentMune;
    boolean isShowDraw;
    List<ContentMuneEntity> muneList = new ArrayList<>();


    @Override
    protected int layoutId() {
        return R.layout.activity_mi_shift;
    }

    @Override
    protected void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        contentMune = findViewById(R.id.content_mune);
        viewpagerParent = findViewById(R.id.viewpager_parent);
        //取消阴影效果
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent));
        viewContent = findViewById(R.id.view_content);
    }


    @Override
    protected void doElse() {
        initMune();
        initContentMune();
        initViewPage();
        dodrawerDrag();
    }

    private void initMune() {
        muneList.add(new ContentMuneEntity(R.mipmap.bookshelf, "书架", ShiftFragment.newInstance()));
        muneList.add(new ContentMuneEntity(R.mipmap.search, "搜索", SearchFragment.newInstance()));
        muneList.add(new ContentMuneEntity(R.mipmap.booklist, "书单", BookListFragment.newInstance()));
    }

    private void initContentMune() {
        contentMune.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, (int) SystemUtil.dp2px(this, 10), R.color.transparent);
        contentMune.addItemDecoration(recycleViewDivider);
        ContentMuneAdapter contentMuneAdapter = new ContentMuneAdapter(this, muneList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewContent.setCurrentItem((Integer) v.getTag());
            }
        });
        contentMune.setAdapter(contentMuneAdapter);
    }

    float lastslideOffset = 0;

    private void dodrawerDrag() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.i("drawer", "slideOffset=" + slideOffset + "     drawerView=" + drawerView.getWidth());

                viewpagerParent.setPadding((int) (drawerView.getWidth() * slideOffset), 0, 0, 0);
                ViewGroup.LayoutParams layoutParams = drawerLayout.getLayoutParams();
                layoutParams.width = (int) (SystemUtil.getScreenSize(MiContentActivity.this).widthPixels + drawerView.getWidth() * slideOffset);
                drawerLayout.setLayoutParams(layoutParams);

                if (slideOffset == 1) {
                    viewpagerParent.setPadding(drawerView.getWidth(), 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiContentActivity.this).widthPixels + drawerView.getWidth();
                    drawerLayout.setLayoutParams(layoutParams);
                } else if ((slideOffset == 0)) {
                    viewpagerParent.setPadding(0, 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiContentActivity.this).widthPixels;
                    drawerLayout.setLayoutParams(layoutParams);
                }
                viewContent.setScaleX((float) (1 - (0.1 * slideOffset)));
                viewContent.setScaleY((float) (1 - (0.1 * slideOffset)));
                viewContent.setRotationY(slideOffset * -10);
                lastslideOffset = slideOffset;

                ((MiContentViewPagerAdapter1) viewContent.getAdapter()).setPageRound(slideOffset, viewContent.getCurrentItem());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isShowDraw = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isShowDraw = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    private void initViewPage() {
        MiContentViewPagerAdapter1 viewPager = new MiContentViewPagerAdapter1(getSupportFragmentManager(), muneList, null);
        viewContent.setAdapter(viewPager);
    }


}
