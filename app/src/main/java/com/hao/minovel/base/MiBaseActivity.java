package com.hao.minovel.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hao.minovel.R;
import com.hao.minovel.moudle.entity.JumpInfo;
import com.hao.minovel.moudle.service.LoadWebInfo;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.StatusBarUtil;
import com.hao.minovel.utils.SystemUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public abstract class MiBaseActivity extends AppCompatActivity {
    public List<View> notHideView = new ArrayList();


    private static float sNoncompatDensity;// 系统的Density
    private static float sNoncompatScaleDensity;// 系统的ScaledDensity
    private static final int customDensity = 360;//资源文件设计时的基础数值，主要来源于ui设计时候的参数

    
    private static void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            // 系统的Density
            sNoncompatDensity = appDisplayMetrics.density;
            // 系统的ScaledDensity
            sNoncompatScaleDensity = appDisplayMetrics.scaledDensity;
            // 监听在系统设置中切换字体
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        // 此处以360dp的设计图作为例子
        final float targetDensity = appDisplayMetrics.widthPixels / customDensity;
        final float targetScaledDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        SystemUtil.hideInputWhenTouchOtherView(this, ev, notHideView);
        return super.dispatchTouchEvent(ev);
    }

    //设置状态拦颜色 默认为透明
    protected boolean beforOnCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//禁止截屏
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setStatubarTextColor(this, true);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        boolean isfull = beforOnCreate();
        super.onCreate(savedInstanceState);
        setCustomDensity(this, AppContext.application);
        View v = LayoutInflater.from(this).inflate(layoutId(), null);
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_01));
        doOnSetContent(v);
        setContentView(v);
        AppContext.addActivity(this);
        initView(v);
        doElse();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    protected abstract @LayoutRes
    int layoutId();

    protected abstract void initView(View v);

    protected abstract void doElse();

    protected abstract void doOnSetContent(View v);

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventBusOnEvent(Object o) {
        if (o instanceof String) {
            eventBusOnEvent((String) o);
        } else if (o instanceof JumpInfo) {
            eventBusOnEvent((JumpInfo) o);
        } else if (o instanceof LoadWebInfo) {
            eventBusOnEvent((LoadWebInfo) o);
        }
    }

    public void eventBusOnEvent(String str) {

    }

    public void eventBusOnEvent(LoadWebInfo str) {

    }

    public void eventBusOnEvent(JumpInfo str) {

    }


    //添加点击后不隐藏软键盘的view
    public void addViewForNotHideSoftInput(View v) {
        notHideView.add(v);
    }

    /**
     * 判断是否还有权限未申请
     *
     * @param promision true 表示权限已拥有
     * @return
     */
    protected Boolean initPromission(String[] promision) {
        boolean promiss = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> promission = new ArrayList<>();
            for (int i = 0; i < promision.length; i++) {
                if (checkSelfPermission(promision[i]) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("promiss", promision[i]);
                    promiss = promiss && true;
                } else {
                    Log.i("promiss", "未申请：" + promision[i]);
                    promission.add(promision[i]);
                    promiss = promiss && false;
                }
            }

            String[] str = new String[promission.size()];
            for (int i = 0; i < promission.size(); i++) {
                str[i] = promission.get(i);
            }

            if (str.length != 0) {
                requestPermissions(str, 0);
            }
        }
        return promiss;
    }

    /**
     * 申请未知应用安装权限
     */
    private void checkUnkonwApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                Uri uri = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
                startActivityForResult(intent, 19900);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppContext.removeActivity(this);
    }
}
