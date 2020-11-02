package com.hao.minovel.db;

import android.database.Cursor;
import android.util.Log;


import com.hao.minovel.log.MiLog;
import com.hao.minovel.moudle.entity.AppUseInfo;
import com.hao.minovel.moudle.entity.AppUseInfoDao;
import com.hao.minovel.moudle.entity.ReadInfo;
import com.hao.minovel.moudle.entity.ReadInfoDao;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelChapterDao;
import com.hao.minovel.spider.data.NovelContent;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelIntroductionDao;
import com.hao.minovel.spider.data.NovelType;
import com.hao.minovel.spider.data.NovelTypeDao;
import com.hao.minovel.spider.data.NovelTypeHot;
import com.hao.minovel.spider.data.NovelTypeHotDao;
import com.hao.minovel.utils.SystemConfigUtil;
import com.hao.minovel.view.minovelread.NovelTextDrawInfo;

import java.util.ArrayList;
import java.util.List;

public class DBManage {
    /**
     * 批量添加小说数据
     *
     * @param novelIntroductionList
     */
    public static void addNovelIntrodution(final List<NovelIntroduction> novelIntroductionList) {
        MiLog.i("添加修改所有数据：" + novelIntroductionList.size());
        final NovelIntroductionDao novelIntroductionDao = DBCore.getDaoSession().getNovelIntroductionDao();
        long count = novelIntroductionDao.queryBuilder().count();
        if (count > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < novelIntroductionList.size(); i++) {
                        NovelIntroduction novelIntroduction = DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.NovelChapterListUrl.eq(novelIntroductionList.get(i).getNovelChapterListUrl())).limit(1).unique();
                        if (novelIntroduction == null || !novelIntroduction.isComplete()) {
                            novelIntroductionList.get(i).setDate(novelIntroduction);
                            novelIntroductionDao.insertOrReplace(novelIntroductionList.get(i));
                        }
                    }
                    MiLog.i("添加修改所有数据完成");
                }
            }).start();
        } else {
            novelIntroductionDao.insertOrReplaceInTx(novelIntroductionList);
        }
    }

    /**
     * 获取数据库中保存的小说数量
     *
     * @return 小说数量
     */
    public static long getAllNovelCount() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().count();
    }

    /**
     * 添加单个小说数据
     *
     * @param novelIntroduction
     */
    public static void addNovelIntrodution(NovelIntroduction novelIntroduction) {
        DBCore.getDaoSession().getNovelIntroductionDao().insertOrReplace(novelIntroduction);
    }

    public static void addNovelType(List<NovelType> novelTypes) {
        DBCore.getDaoSession().getNovelTypeDao().insertOrReplaceInTx(novelTypes);
    }

    public static void addNovelType(NovelType novelType) {
        DBCore.getDaoSession().getNovelTypeDao().insertOrReplace(novelType);
    }

    public static List<NovelType> getNovelType() {
        return DBCore.getDaoSession().getNovelTypeDao().queryBuilder().where(NovelTypeDao.Properties.From.eq(NovelContent.from)).list();
    }

    public static void addNovelChapter(List<NovelChapter> chapters) {
        DBCore.getDaoSession().getNovelChapterDao().insertOrReplaceInTx(chapters);
    }

    public static void updateNovelChapter(NovelChapter chapters) {
        DBCore.getDaoSession().getNovelChapterDao().insertOrReplaceInTx(chapters);
    }


    /**
     * 通过小说的章节地址来获取小说信息
     *
     * @param chapterListurl
     * @return
     */
    public static NovelIntroduction checkNovelByUrl(String chapterListurl) {
        DBCore.getDaoSession().getNovelIntroductionDao().detachAll();
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(
                NovelIntroductionDao.Properties.NovelChapterListUrl.eq(chapterListurl)).limit(1).unique();
    }

    /**
     * 通过章节地址获取小说章节详情
     *
     * @param url
     * @return
     */
    public static NovelChapter checkNovelChaptterByUrl(String url) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(
                NovelChapterDao.Properties.ChapterUrl.eq(url)).limit(1).unique();
    }

    /**
     * 通过小说ID 和章节id获取小说章节详情
     *
     * @return
     */
    public static NovelChapter checkNovelChaptterById(String chapterListUrl, String chapterUrl) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(
                NovelChapterDao.Properties.NovelChapterListUrl.eq(chapterListUrl), NovelChapterDao.Properties.ChapterUrl.eq(chapterUrl)).limit(1).unique();
    }


    public static List<NovelIntroduction> getAllNovelNoDetail() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().list();
    }

    //获取单个未完成详细信息的小说
    public static NovelIntroduction getNoCompleteDetailNovelInfo() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.IsComplete.eq(false)).limit(1).unique();
    }

    //查询单个未加载的章节内容
    public static NovelChapter checkNovelAllNoChapterContent(NovelIntroduction novelIntroduction) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(novelIntroduction.getNovelChapterListUrl()), NovelChapterDao.Properties.IsComplete.eq(false)).limit(1).unique();
    }

    public static List<NovelIntroduction> getNovelByType(String type, int page) {
        if (page > 0) {
            page = page - 1;
        }
        List<NovelIntroduction> introductions = DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.NovelType.like("%" + type + "%")).offset(10 * page).orderAsc(NovelIntroductionDao.Properties.CreatTime).limit(10).list();
        return introductions;
    }


    public static List<NovelChapter> getChapterById(String chaptyrlerList) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(chaptyrlerList)).orderAsc(NovelChapterDao.Properties.CreateTime).list();

    }

    //通过小说id和章节id查询章节信息
    public static NovelChapter getChapterById(String chapterListUrl, String chapterUrl) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(chapterListUrl), NovelChapterDao.Properties.ChapterUrl.eq(chapterUrl)).limit(1).unique();

    }

    public static List<NovelIntroduction> checkNovelIntrodutionByStr(String string) {
        NovelIntroductionDao novelIntroductionDao = DBCore.getDaoSession().getNovelIntroductionDao();
        List<NovelIntroduction> novelIntroductions = novelIntroductionDao.queryBuilder().where(novelIntroductionDao.queryBuilder()
                .or(NovelIntroductionDao.Properties.NovelName.like("%" + string + "%"),
                        NovelIntroductionDao.Properties.NovelAutho.like("%" + string + "%"))).list();
        return novelIntroductions;
    }


    public static void getNovelTypeByAllNovel() {
        NovelIntroductionDao novelIntroductionDao = DBCore.getDaoSession().getNovelIntroductionDao();
        String strSql = "select * from NOVEL_INTRODUCTION order by NOVEL_TYPE";
        Cursor c = DBCore.getDaoSession().getDatabase().rawQuery(strSql, null);
        if (c.moveToFirst()) {
            String fromId = c.getString(c.getColumnIndex("NOVEL_TYPE"));
            Log.i("分组", "fromId=" + fromId);
        }

        c.close();
//        List<NovelIntroduction> list = novelIntroductionDao.queryBuilder().where(new WhereCondition.StringCondition("1=1 GROUP BY " + NovelIntroductionDao.Properties.NovelType)).list();
//        for (int i = 0; i < list.size(); i++) {
//            Log.i("分组", "i=" + i + "        " + list.get(i).getNovelType() + "\n");
//        }
        Log.i("分组", "完成");
    }


    public static List<NovelChapter> checkedNovelList(String novelChapterListUrl) {
        NovelChapterDao novelChapterDao = DBCore.getDaoSession().getNovelChapterDao();
        novelChapterDao.detachAll();
        return novelChapterDao.queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(novelChapterListUrl)).orderAsc(NovelChapterDao.Properties.CreateTime).list();
    }


    //获取小说列表分类的分组
    public static List<NovelType> groupNovelType() {
        String sql = "SELECT * FROM NOVEL_TYPE GROUP BY NOVEL_TYPE.TYPE";
        Cursor c = DBCore.getDaoSession().getDatabase().rawQuery(sql, null);

        List<NovelType> novelTypes = new ArrayList<>();
        while (c.moveToNext()) {
            NovelType novelType = new NovelType();
            novelType.setId(c.getLong(0));
            novelType.setType(c.getString(1));
            novelType.setFrom(c.getString(2));
            novelType.setListUrl(c.getString(3));
            novelType.setLastListUrl(c.getString(4));
            novelType.setNextListUrl(c.getString(5));
            novelType.setCreatTime(c.getLong(6));
            MiLog.i("数据" + novelType.toString());
            novelTypes.add(novelType);
        }
        return novelTypes;
    }

    //查询下一页同类型的小说类型数据
    public static NovelType checkedNextNovelType(NovelType nowNovelType) {
        return checkedNextNovelType(nowNovelType.getNextListUrl());
    }

    //查询下一页同类型的小说类型数据
    public static NovelType checkedNextNovelType(String novelTypeUrl) {
        if (novelTypeUrl == null || novelTypeUrl.equals("")) {
            return null;
        }
        NovelTypeDao novelTypeDao = DBCore.getDaoSession().getNovelTypeDao();
        novelTypeDao.detachAll();
        return novelTypeDao.queryBuilder().where(NovelTypeDao.Properties.ListUrl.eq(novelTypeUrl)).limit(1).unique();
    }

    public static void addNovelTypeHot(List<NovelTypeHot> novelTypeHot) {
        NovelTypeHotDao novelTypeHotDao = DBCore.getDaoSession().getNovelTypeHotDao();
        novelTypeHotDao.insertOrReplaceInTx(novelTypeHot);
    }

    public static List<NovelTypeHot> checkedNovelHotByType(String type) {
        return DBCore.getDaoSession().getNovelTypeHotDao().queryBuilder().where(NovelTypeHotDao.Properties.Type.eq(type)).limit(6).list();
    }

    /**
     * 查询所有已阅读的小说信息
     *
     * @return
     */
    public static List<ReadInfo> checkedAllReadInfo() {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        return readInfoDao.queryBuilder().orderDesc(ReadInfoDao.Properties.Date).list();
    }

    /**
     * 删除保存的小说阅读信息
     *
     * @param readInfo
     */
    public static void removeReadInfo(ReadInfo readInfo) {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        readInfoDao.delete(readInfo);
        SystemConfigUtil.getInstance().initDynamicShortcuts();
    }


    /**
     * 获取到小说的阅读信息
     *
     * @param novelDetailUrl 小说对应的链接地址
     * @return
     */
    public static ReadInfo checkedReadInfo(String novelDetailUrl) {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        return readInfoDao.queryBuilder().where(ReadInfoDao.Properties.NovelChapterListUrl.eq(novelDetailUrl)).limit(1).unique();
    }

    /**
     * 获取当前APP的运行信息
     *
     * @return
     */
    public static AppUseInfo getAppUseInfo() {
        AppUseInfo appUseInfo = DBCore.getDaoSession().getAppUseInfoDao().queryBuilder().orderDesc(AppUseInfoDao.Properties.LoadAllNovelNameTime).limit(1).unique();
        if (appUseInfo == null) {
            appUseInfo = new AppUseInfo(0);
        }
        return appUseInfo;
    }

    /**
     * 添加或修改运行信息
     *
     * @param appUseInfo
     */
    public static void saveAppUseinfo(AppUseInfo appUseInfo) {
        DBCore.getDaoSession().getAppUseInfoDao().insertOrReplace(appUseInfo);
    }

    /**
     * 保存小说阅读信息
     *
     * @param readInfo
     */
    public static void saveReadInfo(ReadInfo readInfo) {
        DBCore.getDaoSession().getReadInfoDao().insertOrReplaceInTx(readInfo);
        SystemConfigUtil.getInstance().initDynamicShortcuts();
    }

    public static NovelTextDrawInfo chackNovelConfig() {
        return DBCore.getDaoSession().getNovelTextDrawInfoDao().queryBuilder().limit(1).unique();
    }

    public static void saveNovelTextViewConfig(NovelTextDrawInfo novelTextDrawInfo) {
        DBCore.getDaoSession().getNovelTextDrawInfoDao().insertOrReplace(novelTextDrawInfo);
    }
}