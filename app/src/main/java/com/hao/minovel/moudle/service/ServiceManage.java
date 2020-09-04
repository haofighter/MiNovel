package com.hao.minovel.moudle.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hao.minovel.db.DBManage;

import static android.content.Context.BIND_AUTO_CREATE;

public class ServiceManage {
    private ServiceManage() {
    }

    private static volatile ServiceManage serviceManage;

    public static ServiceManage getInstance() {
        if (serviceManage == null) {
            synchronized (ServiceManage.class) {
                if (serviceManage == null) {
                    serviceManage = new ServiceManage();
                }
            }
        }
        return serviceManage;
    }

    DownLoadNovelBinder binder;

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownLoadNovelBinder) service;
            if (binder != null) {
//                if (novelUsedUpdate != null && System.currentTimeMillis() - novelUsedUpdate.getLastUpdatime() > 3600000) {
                binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.noveltype));
//                    novelUsedUpdate.setLastUpdatime(System.currentTimeMillis());
                binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.allTitle));
//                }
                DBManage.getNovelTypeByAllNovel();
            }
        }
    };

    //开启小说下载解析服务
    public void startBackRunService(Application application) {
        Intent bindIntent = new Intent(application, DownLoadNovelService.class);
        application.bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    public void unBindBackRunService(Application application) {
        application.unbindService(connection);
    }
}
