package com.hao.minovel.moudle.activity;

import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.log.MiToast;
import com.hao.minovel.moudle.adapter.ContentMuneAdapter;
import com.hao.minovel.moudle.adapter.MiContentViewPagerAdapter;
import com.hao.minovel.moudle.entity.ContentMuneEntity;
import com.hao.minovel.moudle.fragment.ShiftFragment;
import com.hao.minovel.moudle.fragment.StackFragment;
import com.hao.minovel.moudle.miinterface.FragmentListener;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.RoundLayout;

import java.util.ArrayList;
import java.util.List;


@Bind
public class MiContentActivity extends MiBaseActivity implements View.OnClickListener, FragmentListener {
    DrawerLayout drawerLayout;
    RoundLayout mi_activity;
    FrameLayout fl_warn;
    ViewPager viewContent;
    RecyclerView contentMune;
    boolean isShowDraw;
    List<ContentMuneEntity> muneList = new ArrayList<>();
    long lastBackTime = 0;

    @Override
    protected boolean beforOnCreate() {
        return super.beforOnCreate();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_mi_content;
    }

    @Override
    protected void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mi_activity = findViewById(R.id.mi_activity);
        viewContent = findViewById(R.id.view_content);
        contentMune = findViewById(R.id.content_mune);
        fl_warn = findViewById(R.id.fl_warn);
        findViewById(R.id.iv_warn_close).setOnClickListener(this);
        findViewById(R.id.stack).setOnClickListener(this);
        findViewById(R.id.book_icon).setOnClickListener(this);
        //取消空白部分灰色阴影效果
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent));
    }


    @Override
    protected void doElse() {
        initTranslation();
        initMune();
        initContentMune();
        initViewPage();
        dodrawerDrag();
    }

    @Override
    protected void doOnSetContent(View v) {
        int padding = (int) SystemUtil.dp2px(this, 10);
        v.findViewById(R.id.title_item).setPadding(padding, SystemUtil.getStatusBarHeight(this) + padding, padding, padding);
        v.findViewById(R.id.content_mune).setPadding(0, SystemUtil.getStatusBarHeight(this), 0, 0);
    }

    @Override
    public void eventBusOnEvent(Object o) {
        if (o instanceof String) {
            switch ((String) o) {
                case "toStack"://切换到书库界面
                    changePage(1);
                    break;
            }
        }
    }


    private void changePage(int page) {
        ContentMuneEntity contentMuneEntity = muneList.get(page);
        if (contentMuneEntity.getFragment() instanceof StackFragment) {//表示当前页为书库页
            ((ContentMuneAdapter) contentMune.getAdapter()).setType(ContentMuneAdapter.Type.stack);
        }
    }


    private void initMune() {
        muneList.add(new ContentMuneEntity(R.mipmap.bookshelf, "书架", ShiftFragment.newInstance().setFragmentListener(this)));
        muneList.add(new ContentMuneEntity(R.mipmap.booklist, "书库", StackFragment.newInstance().setFragmentListener(this)));
//        muneList.add(new ContentMuneEntity(R.mipmap.search, "查询", SearchFragment.newInstance().setFragmentListener(this)));
    }

    private void initContentMune() {
        contentMune.setLayoutManager(new LinearLayoutManager(this));
        ContentMuneAdapter contentMuneAdapter = new ContentMuneAdapter(this, muneList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewContent.setCurrentItem((Integer) v.getTag());
            }
        });
        contentMune.setAdapter(contentMuneAdapter);
    }


    float lastslideOffset;

    private void dodrawerDrag() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                mi_activity.setPadding((int) (drawerView.getWidth() * slideOffset), 0, 0, 0);
                ViewGroup.LayoutParams layoutParams = drawerLayout.getLayoutParams();
                layoutParams.width = (int) (SystemUtil.getScreenSize(MiContentActivity.this).widthPixels + drawerView.getWidth() * slideOffset);
                drawerLayout.setLayoutParams(layoutParams);

                if (slideOffset == 1) {
                    mi_activity.setPadding(drawerView.getWidth(), 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiContentActivity.this).widthPixels + drawerView.getWidth();
                    drawerLayout.setLayoutParams(layoutParams);
                } else if ((slideOffset == 0)) {
                    mi_activity.setPadding(0, 0, 0, 0);
                    layoutParams.width = SystemUtil.getScreenSize(MiContentActivity.this).widthPixels;
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


    private void initViewPage() {
        MiContentViewPagerAdapter viewPager = new MiContentViewPagerAdapter(getSupportFragmentManager(), muneList, null);
        viewContent.setAdapter(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackTime > 2000) {
            MiToast.show("再按一次退出应用");
            lastBackTime = System.currentTimeMillis();
        } else {
            AppContext.finishAll();
            ServiceManage.getInstance().unBindBackRunService(AppContext.application);
        }
    }

    //转场动画
    private void initTranslation() {
        ImageView book_icon = findViewById(R.id.book_icon);
        /**
         * 1、设置相同的TransitionName
         */
//        ViewCompat.setTransitionName(book_icon, "avatar");
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());

            getWindow().setExitTransition(new Fade());
            /**
             * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
             */
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());
            transitionSet.addTarget(book_icon);
            getWindow().setSharedElementEnterTransition(transitionSet);
            getWindow().setSharedElementExitTransition(transitionSet);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_icon:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.stack:
//                Router.getInstance().build(RouterContent.STACKACTIVITY, null).skip();
                break;
            case R.id.iv_warn_close:
                fl_warn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void sendMessage(Object o) {

    }
}
