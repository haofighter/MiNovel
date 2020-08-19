package com.hao.minovel.moudle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.base.MiBaseFragment;
import com.hao.minovel.moudle.adapter.MiContentViewPagerAdapter;
import com.hao.minovel.moudle.adapter.MiContentViewPagerAdapter1;
import com.hao.minovel.moudle.fragment.ShiftFragment;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.RoundLinearLayout;
import com.hao.minovel.view.viewpagerhelp.CardPageTransformer;
import com.hao.minovel.view.viewpagerhelp.OnPageTransformerListener;
import com.hao.minovel.view.viewpagerhelp.PageTransformerConfig;

import java.util.ArrayList;
import java.util.List;

@Bind
public class MiContentActivity extends MiBaseActivity {
    DrawerLayout drawerLayout;
    ViewPager viewContent;
    RoundLinearLayout viewpagerParent;
    boolean isShowDraw;
    List<MiBaseFragment> viewList = new ArrayList<>();


    @Override
    protected int layoutId() {
        return R.layout.activity_mi_shift;
    }

    @Override
    protected void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        viewpagerParent = findViewById(R.id.viewpager_parent);
        //取消阴影效果
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent));
        viewContent = findViewById(R.id.view_content);


        findViewById(R.id.change_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewContent.setCurrentItem((viewContent.getCurrentItem() + 1) % viewContent.getAdapter().getCount());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void doElse() {
        initViewPage();
        dodrawerDrag();
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
        for (int i = 0; i < 5; i++) {
            viewList.add(ShiftFragment.newInstance(i));
        }
        MiContentViewPagerAdapter1 viewPager = new MiContentViewPagerAdapter1(getSupportFragmentManager(), viewList, null);

//        viewContent.setOffscreenPageLimit(viewList.size() * 2);
//        viewContent.setPageTransformer(true, CardPageTransformer.getBuild()//建造者模式
//                .addAnimationType(PageTransformerConfig.ROTATION)//默认动画 default animation rotation  旋转  当然 也可以一次性添加两个  后续会增加更多动画
////                .setRotation(-45)//旋转角度
//                .addAnimationType(PageTransformerConfig.ALPHA)//默认动画 透明度 暂时还有问题
//                .setViewType(PageTransformerConfig.RIGHT)
//                .setOnPageTransformerListener(new OnPageTransformerListener() {
//                    @Override
//                    public void onPageTransformerListener(View page, float position) {
//                        //你也可以在这里对 page 实行自定义动画 cust anim
//                    }
//                })
//                .setTranslationOffset(100)
////                .setScaleOffset(80)
//                .create(viewContent));
        viewContent.setAdapter(viewPager);
//        viewContent.setCurrentItem(viewList.size() - 1);
    }


}
