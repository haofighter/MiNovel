package com.hao.minovel.moudle.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.entity.LoadWebInfo;
import com.hao.minovel.spider.SpiderNovelFromBiQu;
import com.hao.minovel.spider.SpiderResponse;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class LoadHtmlService extends IntentService {
    private final int none = 100;
    private final int allTitle = 101;
    private final int allDetail = 102;
    private final int novelDetail = 103;
    private final int singlechaptercontent = 104;
    private final int novelallchaptercontent = 105;
    private final int noveltype = 106;
    private final int noveltypelist = 107;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoadHtmlService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int task = intent.getIntExtra("task", none);
        int loadState = SpiderUtils.NoLoad;
        switch (task) {
            case none://不做任何操作
                break;
            case allTitle://下载所有的小说标题
                loadState = SpiderNovelFromBiQu.getAllNovel();
                break;
            case allDetail://完善所有的小说的介绍信息
                NovelIntroduction novelIntroduction = DBManage.getNoCompleteDetailNovelInfo();
                if (novelIntroduction != null) {
                    loadState = SpiderNovelFromBiQu.getAllNovelDetailInfo(novelIntroduction);
                }
                break;
            case novelDetail://下载单本小说的信息 包含章节信息
                NovelIntroduction singleNovel = intent.getParcelableExtra("novelIntroduction");
                loadState = SpiderNovelFromBiQu.getAllNovelDetailInfo(singleNovel);
                break;
            case singlechaptercontent://下载单章内容
                NovelChapter singleChapter = intent.getParcelableExtra("novelChapter");
                loadState = SpiderNovelFromBiQu.getNovelContent(singleChapter);
                break;
            case novelallchaptercontent://下载小说的所有内容
                NovelIntroduction allChapter = intent.getParcelableExtra("novelIntroduction");
                loadState = SpiderNovelFromBiQu.getAllNovelContent(allChapter);
                break;
            case noveltype://获取小说分类
                loadState = SpiderNovelFromBiQu.getNovelType();
                break;
            case noveltypelist://通过类别来获取小说列表
                NovelType novelType = intent.getParcelableExtra("novelType");
                loadState = SpiderNovelFromBiQu.getTypeNovelList(novelType);
                break;
        }
        try {
            EventBus.getDefault().post(new LoadWebInfo(task, loadState));
        } catch (Exception e) {
            Log.i("小说服务", "任务执行完成执行回调出错:" + e.getMessage());
        }
    }
}
