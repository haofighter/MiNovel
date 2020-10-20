package com.hao.minovel.moudle.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hao.minovel.db.DBManage;
import com.hao.minovel.log.MiLog;
import com.hao.minovel.spider.SpiderNovelFromBiQu;
import com.hao.minovel.spider.SpiderUtils;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelType;

import org.greenrobot.eventbus.EventBus;

public class LoadHtmlService extends IntentService {
    public static final int none = 100;
    public static final int allTitle = 101;
    public static final int allDetail = 102;
    public static final int novelDetail = 103;
    public static final int singlechaptercontent = 104;
    public static final int novelallchaptercontent = 105;
    public static final int noveltype = 106;
    public static final int noveltypelist = 107;
    public static final String TASK = "task";
    public static final String DATE = "date";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoadHtmlService() {
        super((Math.random() * 10000) + "Load");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LoadWebInfo task = intent.getParcelableExtra(TASK);
        int loadState = SpiderUtils.NoLoad;
        if (task == null) {
            return;
        }
        EventBus.getDefault().post(task.setLoadStatus(SpiderUtils.LOADING));
        MiLog.i("小说", "获取任务：" + task.task + "     id:" + task.id);
        switch (task.task) {
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
                NovelIntroduction singleNovel = intent.getParcelableExtra("date");
                loadState = SpiderNovelFromBiQu.getAllNovelDetailInfo(singleNovel);
                break;
            case singlechaptercontent://下载单章内容
                NovelChapter singleChapter = intent.getParcelableExtra("date");
                loadState = SpiderNovelFromBiQu.getNovelContent(singleChapter);
                break;
            case novelallchaptercontent://下载小说的所有内容
                NovelIntroduction allChapter = intent.getParcelableExtra("date");
                loadState = SpiderNovelFromBiQu.getAllNovelContent(allChapter);
                break;
            case noveltype://获取小说分类
                loadState = SpiderNovelFromBiQu.getNovelType();
                break;
            case noveltypelist://通过类别来获取小说列表
                NovelType novelType = intent.getParcelableExtra("date");
                loadState = SpiderNovelFromBiQu.getTypeNovelList(novelType);
                break;
            default:
                Log.i("小说", "任务：" + task.task + "      id" + task.id);
                break;
        }
        try {
            EventBus.getDefault().post(task.setLoadStatus(loadState));
        } catch (Exception e) {
            Log.i("小说", "任务执行完成执行回调出错:" + e.getMessage());
        }
    }
}
