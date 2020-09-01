package com.hao.minovel.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            // 隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏
                    | View.SYSTEM_UI_FLAG_FULLSCREEN); //隐藏状态栏
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | flags);
        }

        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setStatubarTextColor(this, true);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        boolean isfull = beforOnCreate();
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this).inflate(layoutId(), null);
        doOnSetContent(v);
        setContentView(v);
        AppContext.addActivity(this);
        initView();
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

    protected abstract void initView();

    protected abstract void doElse();

    protected abstract void doOnSetContent(View v);

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventBusOnEvent(Object o) {
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
