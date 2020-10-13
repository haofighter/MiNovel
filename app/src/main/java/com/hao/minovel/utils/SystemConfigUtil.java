package com.hao.minovel.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import com.hao.annotationengine.Router;
import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.activity.NovelDetailActivity;
import com.hao.minovel.moudle.activity.ReadNovelActivity;
import com.hao.minovel.moudle.activity.WelcomeActivity;
import com.hao.minovel.moudle.entity.ReadInfo;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.tinker.app.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于调用一些系统个性化设置
 */
public class SystemConfigUtil {
    static SystemConfigUtil systemConfigUtil = new SystemConfigUtil();
    private Notification.Builder notification;

    private SystemConfigUtil() {
    }

    public static SystemConfigUtil getInstance() {
        if (systemConfigUtil == null) {
            synchronized (Router.class) {
                if (systemConfigUtil == null) {
                    systemConfigUtil = new SystemConfigUtil();
                }
            }
        }
        return systemConfigUtil;
    }

    /**
     * 为App创建动态Shortcuts
     * 图标长按后的显示列表
     */
    public void initDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //①、创建动态快捷方式的第一步，创建ShortcutManager
            ShortcutManager scManager = AppContext.application.getSystemService(ShortcutManager.class);
            List<ReadInfo> read = DBManage.checkedAllReadInfo();
            List<ShortcutInfo> shortcutInfos = new ArrayList<>();
            for (int i = 0; i < read.size(); i++) {
                Intent intent = new Intent(AppContext.application, ReadNovelActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setAction(Intent.ACTION_VIEW);//加该FLAG的目的是让MainActivity作为根activity，清空已有的任务
                NovelChapter novelChapter = DBManage.checkNovelChaptterByUrl(read.get(i).getNovelChapterUrl());
                NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(read.get(i).getNovelChapterListUrl());
                if (novelChapter != null && novelIntroduction != null) {
                    intent.putExtra("readInfo", read.get(i).getNovelChapterUrl());
                    ShortcutInfo scInfo = new ShortcutInfo.Builder(AppContext.application, novelIntroduction.getNovelName())
                            .setShortLabel(novelIntroduction.getNovelName())
                            .setLongLabel(novelChapter.getChapterName())
                            .setIcon(Icon.createWithResource(AppContext.application, R.mipmap.icon_shuji_black))
                            .setIntents(new Intent[]{intent})
                            .build();
                    shortcutInfos.add(scInfo);
                }
            }
            //②、构建动态快捷方式的详细信息


            //            ShortcutInfo scInfoTwo = new ShortcutInfo.Builder(this, "dynamic_two")
            //                    .setShortLabel("Dynamic Activity")
            //                    .setLongLabel("to open dynamic one activity")
            //                    .setIcon(Icon.createWithResource(this, R.mipmap.tool_luck_icon))
            //                    .setIntents(new Intent[]{
            //                            new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),//加该FLAG的目的是让MainActivity作为根activity，清空已有的任务
            //                            new Intent(DynamicASOneActivity.ACTION)
            //                    })
            //                    .build();
            //③、为ShortcutManager设置动态快捷方式集合
            scManager.setDynamicShortcuts(shortcutInfos);

            //如果想为两个动态快捷方式进行排序，可执行下面的代码
            //            ShortcutInfo dynamicWebShortcut = new ShortcutInfo.Builder(AppContext.application, "dynamic_one")
            //                    .setRank(0)
            //                    .build();
            //            ShortcutInfo dynamicActivityShortcut = new ShortcutInfo.Builder(this, "dynamic_two")
            //                    .setRank(1)
            //                    .build();

            //④、更新快捷方式集合
//            scManager.updateShortcuts(shortcutInfos);
        }
    }

    public void creatNotification() {
        NotificationManager notificationManager = (NotificationManager) AppContext.application.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "whatever"; //根据业务执行
            String channelName = "whatever conent"; //这个是channelid 的解释，在安装的时候会展示给用户看
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(AppContext.application, "whatever");
        } else {
            notification = new Notification.Builder(AppContext.application);
        }
        String title = "暂无阅读历史";
        String content = "";
        Intent intent = new Intent(AppContext.application, WelcomeActivity.class);
        List<ReadInfo> readInfos = DBManage.checkedAllReadInfo();
        if (readInfos.size() > 0) {
            try {
                NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(readInfos.get(0).getNovelChapterListUrl());
                title = novelIntroduction.getNovelName();
                NovelChapter chapter = DBManage.checkNovelChaptterByUrl(readInfos.get(0).getNovelChapterUrl());
                content = chapter.getChapterName();
                intent = new Intent(AppContext.application, NovelDetailActivity.class);
                intent.putExtra("novelId", novelIntroduction.getNovelChapterListUrl());
            } catch (Exception e) {

            }
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.application, 0, intent, 0);
        notification.setContentTitle(title)  //设置标题
                .setContentText(content) //设置内容
//                .setWhen(System.currentTimeMillis())  //设置时间
                .setSmallIcon(R.mipmap.icon_shuji_black)  //设置小图标
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(AppContext.application.getResources(), R.mipmap.icon_shuji_black));
        notificationManager.notify(0, notification.build());
    }
}
