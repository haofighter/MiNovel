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
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hao.minovel.R;
import com.hao.minovel.utils.StatusBarUtil;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.RoundLayout;

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
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setStatubarTextColor(this, true);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        beforOnCreate();
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventBusOnEvent(Object o) {
    }

    ;

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
    protected void initPromission(String[] promision) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> promission = new ArrayList<>();
            for (int i = 0; i < promision.length; i++) {
                if (checkSelfPermission(promision[i]) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("promiss", promision[i]);
                } else {
                    Log.i("promiss", "未申请：" + promision[i]);
                    promission.add(promision[i]);
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
    }


    private void checkUnkonwApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                Uri uri = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
                startActivityForResult(intent, 19900);
            }
        }
    }
}
