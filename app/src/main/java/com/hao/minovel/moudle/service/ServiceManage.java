package com.hao.minovel.moudle.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.entity.AppUseInfo;

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


    public void startSearchAllnovleTitle(Application application) {
        AppUseInfo appUseInfo = DBManage.getAppUseInfo();
        if (appUseInfo != null && System.currentTimeMillis() - appUseInfo.getLoadAllNovelNameTime() > 3600000) {
            LoadWebInfo loadWebInfo = new LoadWebInfo();
            loadWebInfo.setTask(LoadHtmlService.allTitle);
            Intent bindIntent = new Intent(application, LoadHtmlService.class);
            bindIntent.putExtra(LoadHtmlService.TASK, LoadHtmlService.allTitle);
            application.startService(bindIntent);
            appUseInfo.setLoadAllNovelNameTime(System.currentTimeMillis());
            DBManage.saveAppUseinfo(appUseInfo);
        }
    }

}
