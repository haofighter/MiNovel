package com.hao.minovel.moudle.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.annotationengine.Router;
import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.log.MiToast;
import com.hao.minovel.moudle.adapter.ShiftAdapter;
import com.hao.minovel.moudle.entity.StackTypeEntity;
import com.hao.minovel.moudle.entity.JumpInfo;
import com.hao.minovel.moudle.service.ServiceManage;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.tinker.service.FloatingButtonService;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.view.RoundLayout;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.util.ArrayList;
import java.util.List;

/**
 * 书架
 */
@Bind(path = "app/ShiftActivity")
public class ShiftActivity extends MiMuneActivity implements View.OnClickListener {
    RoundLayout shiftContent;
    FrameLayout fl_warn;
    RecyclerView shiftList;//书架

    List<StackTypeEntity> muneList = new ArrayList<>();
    long lastBackTime = 0;

    @Override
    protected boolean beforOnCreate() {
        return super.beforOnCreate();
    }

    @Override
    public int layoutContentId() {
        return R.layout.activity_shift_content;
    }

    @Override
    protected void initView(View v) {
        shiftContent = findViewById(R.id.shift_content);
        ((TextView) findViewById(R.id.title_name)).setText("书架");
        shiftList = findViewById(R.id.shift_list);
        fl_warn = findViewById(R.id.fl_warn);
        findViewById(R.id.iv_warn_close).setOnClickListener(this);
        findViewById(R.id.stack).setOnClickListener(this);
        findViewById(R.id.book_icon).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        //取消空白部分灰色阴影效果
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent));
    }

    @Override
    protected void doElse() {
        super.doElse();
        initTranslation();
        initShitfList();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ShiftAdapter) shiftList.getAdapter()).refresh();
    }

    //展示已收藏或阅读过的小说
    private void initShitfList() {
        shiftList.setLayoutManager(new LinearLayoutManager(this));
        shiftList.setAdapter(new ShiftAdapter(this));
    }

    @Override
    public void eventBusOnEvent(JumpInfo jumpInfo) {
        super.eventBusOnEvent(jumpInfo);
        Router.getInstance().build(jumpInfo.getActicityTag(), jumpInfo.getBundle()).skip();
    }

    @Override
    protected void doOnSetContent(View v) {
        super.doOnSetContent(v);
        int padding = (int) SystemUtil.dp2px(this, 10);
        v.findViewById(R.id.title_item).setPadding(padding, SystemUtil.getStatusBarHeight(this) + padding, padding, padding);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackTime > 2000) {
            MiToast.show("再按一次退出应用");
            lastBackTime = System.currentTimeMillis();
        } else {
            AppContext.finishAll();
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
                Router.getInstance().build(ActivityConfig.STACKACTIVITY, null).skip();
                break;
            case R.id.iv_warn_close:
                fl_warn.setVisibility(View.GONE);
                break;
            case R.id.setting:
//                Router.getInstance().build(RouterContent.SETTINGACTIVITY, null).skip();
//                Router.getInstance().build(ActivityConfig.LOADTYPEFACEACTIVITY).skip();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createVirtualDisplay();
                }
//                Router.getInstance().build(ActivityConfig.SUSPENSIONWINDOWMAINACTIVITY).skip();
                break;
            case R.id.search:
//                Router.getInstance().build(ActivityConfig.SEARCHNOVELACTIVITY).skip();
                TinkerInstaller.onReceiveUpgradePatch(getApplication(), Environment.getExternalStorageState()+"/patch.apk");
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10086) {
            Intent intent = new Intent(getApplicationContext(), FloatingButtonService.class);
            intent.putExtra("resultCode", resultCode);
            intent.putExtra("data", data);
            startService(intent);
        }
    }


    MediaProjectionManager mediaProjectionManager;

    /**
     * 创建VirtualDisplay
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createVirtualDisplay() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, 10086);
    }
}
