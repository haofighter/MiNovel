package com.hao.minovel.moudle.activity;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hao.annotationengine.Router;
import com.hao.date.RouterContent;
import com.hao.minovel.R;
import com.hao.minovel.base.MiDrawerActivity;
import com.hao.minovel.moudle.adapter.ShiftMuneAdapter;
import com.hao.minovel.moudle.entity.ShiftMuneEntity;
import com.hao.minovel.moudle.entity.StackTypeEntity;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.recycleviewhelp.MiRecycleView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用同一个侧滑菜单
 */
public abstract class MiMuneActivity extends MiDrawerActivity {
    MiRecycleView mune;
    List<ShiftMuneEntity> muneList = new ArrayList<>();
    boolean isShowDraw;

    @Override
    public int layoutDrawerId() {
        return R.layout.activity_shift_drawer;
    }

    @Override
    protected void initView(@Nullable View v) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void doOnSetContent(@NotNull View v) {
        super.doOnSetContent(v);
        mune = v.findViewById(R.id.mune);
        mune.setPadding(0, SystemUtil.getStatusBarHeight(this), 0, 0);
    }

    @Override
    protected void doElse() {
        super.doElse();
        initMune();
        dodrawerDrag();
    }

    private void initMune() {
        muneList.add(new ShiftMuneEntity(R.mipmap.bookshelf, "书架", RouterContent.SHIFTACTIVITY));
        muneList.add(new ShiftMuneEntity(R.mipmap.booklist, "书库", RouterContent.STACKACTIVITY));
        mune.setLayoutManager(new LinearLayoutManager(this));
        ShiftMuneAdapter contentMuneAdapter = new ShiftMuneAdapter(this, muneList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance().build(muneList.get((Integer) v.getTag()).getRouterActivityTag(), null).skip();
                drawerLayout.closeDrawers();
            }
        });
        mune.setAdapter(contentMuneAdapter);
    }


    float lastslideOffset;

    void dodrawerDrag() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                contentShow.setPadding((int) (drawerView.getWidth() * slideOffset), 0, 0, 0);
                ViewGroup.LayoutParams layoutParams = drawerLayout.getLayoutParams();
                layoutParams.width = (int) (SystemUtil.getScreenSize(MiMuneActivity.this).widthPixels + drawerView.getWidth() * slideOffset);
                drawerLayout.setLayoutParams(layoutParams);

                if (slideOffset == 1) {
                    contentShow.setPadding(drawerView.getWidth(), 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiMuneActivity.this).widthPixels + drawerView.getWidth();
                    drawerLayout.setLayoutParams(layoutParams);
                } else if ((slideOffset == 0)) {
                    contentShow.setPadding(0, 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiMuneActivity.this).widthPixels;
                    drawerLayout.setLayoutParams(layoutParams);
                }
//                mi_activity.setScaleX((float) (1 - (0.1 * slideOffset)));
//                mi_activity.setScaleY((float) (1 - (0.1 * slideOffset)));
//                mi_activity.setRotationY(slideOffset * -10);
                lastslideOffset = slideOffset;
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
}
