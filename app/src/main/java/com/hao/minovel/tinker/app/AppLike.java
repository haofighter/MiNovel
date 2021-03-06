package com.hao.minovel.tinker.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.hao.annotationengine.Router;
import com.hao.minovel.jni.CrashTools;
import com.hao.minovel.tinker.TinkerManager;
import com.hao.minovel.tinker.Log.MyLogImp;
import com.hao.minovel.utils.SystemConfigUtil;
import com.hao.minovel.utils.TypeFaceUtils;
import com.hao.skin.SkinManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;


@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.hao.minovel.tinker.app.App",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class AppLike extends DefaultApplicationLike {

    private Notification.Builder notification;

    public AppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);

    }

    private void init(Application application) {
        Router.init(application);
        SkinManager.init(application);
//        SystemConfigUtil.getInstance().creatNotification();
        TypeFaceUtils.init(application);
        CrashTools.init(application);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        AppContext.application = getApplication();
        init(AppContext.application);

        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }


}
