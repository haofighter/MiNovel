package com.hao.minovel.moudle.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.activity.MiContentActivity;
import com.hao.minovel.spider.SpiderNovelFromBiQu;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 用于更新获取小说相关数据
 * 创建一个前台服务 防止GC回收
 */
public class DownLoadNovelService extends Service {

    private String nowScreenTag = "";
    private boolean isrun = true;

//    List<NovolDownTask> tag = new ArrayList<NovolDownTask>() {
//        @Override
//        public boolean add(NovolDownTask novolDownTask) {
//            if (novolDownTask.novelDownTag.equals(NovelDownTag.novelDetail)
//                    || novolDownTask.novelDownTag.equals(NovelDownTag.novelallchapterTitle)
//                    || novolDownTask.novelDownTag.equals(NovelDownTag.novelallchaptercontent)) {
//                if (!(novolDownTask.object instanceof NovelIntroduction)) {
//                    throw new NumberFormatException("小说服务添加任务异常，类型不匹配 novolDownTask.object 不是 NovelIntroduction类型");
//                }
//            } else if (novolDownTask.novelDownTag.equals(NovelDownTag.singlechaptercontent)) {
//                if (!(novolDownTask.object instanceof NovelChapter)) {
//                    throw new NumberFormatException("小说服务添加任务异常，类型不匹配 novolDownTask.object 不是 NovelChapter¬类型");
//                }
//            }
//            return super.add(novolDownTask);
//        }
//
//        @Override
//        public NovolDownTask get(int index) {
//            synchronized (this) {
//                NovolDownTask novolDownTask = super.get(index);
//                Log.i("小说服务", "当前任务数=" + size() + "     当前执行任务：" + novolDownTask.getNovelDownTag());
//                remove(index);
//                return novolDownTask;
//            }
//        }
//    };

    LinkedBlockingQueue<NovolDownTask> tag = new LinkedBlockingQueue<NovolDownTask>();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    DownLoadNovelBinder binder = new DownLoadNovelBinder() {

        @Override
        public void sendCmd(NovolDownTask o) {
            sendCmd(o, tag.size());
        }

        @Override
        public void sendCmd(NovolDownTask o, int index) {
            try {
                Log.i("小说服务", "添加任务=" + o.getNovelDownTag());
                tag.add(o);
                if (o.getDownListener() != null) {
                    Log.i("小说服务", "设置当前前置任务tag=" + o.getDownListener().getTag());
                    nowScreenTag = o.getDownListener().getTag();
                }
            } catch (Exception e) {
                tag.add(o);
            }
        }


        @Override
        public Object getMassage() {
            Log.i("小说服务", "getMassage: ");
            return null;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MiContentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("程序正在运行")
                .setContentText("程序正在运行")
                .setOngoing(false)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
//            NotificationChannel notificationChannel = new NotificationChannel("com.hao.novel", App.getInstance()., NotificationManager.IMPORTANCE_DEFAULT);
//            // 注册通道，注册后除非卸载再安装否则不改变
//            notificationManager.createNotificationChannel(notificationChannel);
//            builder.setChannelId("com.hao.novel");
        }
        notificationManager.notify(11, builder.build());

        Log.i("小说服务", "服务中的线程名：" + Thread.currentThread().getName());
        startDownloadThread();
    }

    private void startDownloadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isrun) {
                    try {
                        NovolDownTask novolDownTask = tag.take();
                        Log.i("小说服务", "小说任务中的线程名：" + Thread.currentThread().getName());
                        SimplePoolExecutor.getInstance().execute(new NovelRunnable(novolDownTask));
                    } catch (Exception e) {
                        Log.e("小说服务", "抛出了一个炸弹" + e.getMessage() + ",but我接住了");
                    }
                }
            }
        }).start();
    }


    class NovelRunnable implements Runnable {
        NovolDownTask novolDownTask;

        public NovelRunnable(NovolDownTask novolDownTask) {
            this.novolDownTask = novolDownTask;
        }

        @Override
        public void run() {
            if (novolDownTask != null && novolDownTask.downListener != null) {
                Log.i("小说服务", "前台任务：" + novolDownTask.downListener.getTag() + "        服务任务：" + nowScreenTag);
            }
            if (novolDownTask != null && novolDownTask.downListener != null && novolDownTask.downListener.getTag().equals(nowScreenTag)) {
                novolDownTask.downListener.startDown();
            }
            Log.i("小说服务", "当前执行任务：" + novolDownTask.getNovelDownTag());
            switch (novolDownTask.getNovelDownTag()) {
                case none://不做任何操作
                    break;
                case allTitle://下载所有的小说标题
                    SpiderNovelFromBiQu.getAllNovel();
                    tag.add(new NovolDownTask(NovelDownTag.allDetail));
                    break;
                case allDetail://完善所有的小说的介绍信息
                    NovelIntroduction novelIntroduction = DBManage.getNoCompleteDetailNovelInfo();
                    if (novelIntroduction != null) {
                        SpiderNovelFromBiQu.getAllNovelDetailInfo(novelIntroduction);
                    }
                    break;
                case novelDetail://下载单本小说的信息 包含章节信息
                    SpiderNovelFromBiQu.getAllNovelDetailInfo((NovelIntroduction) novolDownTask.getObject());
                    break;
                case singlechaptercontent://下载单章内容
                    SpiderNovelFromBiQu.getNovelContent((NovelChapter) novolDownTask.getObject());
                    break;
                case novelallchaptercontent://下载小说的所有内容
                    SpiderNovelFromBiQu.getAllNovelContent((NovelIntroduction) novolDownTask.getObject());
                    break;
                case noveltype://获取小说分类
                    SpiderNovelFromBiQu.getNovelType();
                    break;
                case noveltypelist://通过类别来获取小说列表
                    NovelType novelType = (NovelType) novolDownTask.getObject();
                    SpiderNovelFromBiQu.getTypeNovelList(novelType);
                    break;
            }
            if (novolDownTask.downListener != null && novolDownTask.downListener.getTag().equals(nowScreenTag)) {
                novolDownTask.downListener.endDown();
            }
            Log.i("小说服务", "任务执行完成" + novolDownTask.getNovelDownTag());
        }
    }

    ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("小说服务", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("小说服务", "onDestroy: ");
        isrun = false;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("小说服务", "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.i("小说服务", "onLowMemory: ");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("小说服务", "onTrimMemory: ");
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("小说服务", "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("小说服务", "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("小说服务", "onTaskRemoved: ");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.i("小说服务", "dump: ");
        super.dump(fd, writer, args);
    }


    public enum NovelDownTag {
        none, allTitle, allDetail, novelDetail, singlechaptercontent, novelallchaptercontent, noveltype, noveltypelist
    }
}
