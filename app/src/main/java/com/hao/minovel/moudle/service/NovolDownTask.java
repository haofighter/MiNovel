package com.hao.minovel.moudle.service;

public class NovolDownTask {

    DownLoadNovelService.NovelDownTag novelDownTag;
    Object object;
    DownListener downListener;
    boolean isScreenTask;
    String tag;


    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag) {
        this(novelDownTag, null);
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object) {
        this(novelDownTag, object, null, false);
    }


    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object, DownListener downListener, boolean isScreenTask) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.downListener = downListener;
        this.isScreenTask = isScreenTask;
        tag = "task" + (System.currentTimeMillis() + Math.round(10000));
    }

    public DownLoadNovelService.NovelDownTag getNovelDownTag() {
        return novelDownTag;
    }


    public Object getObject() {
        return object;
    }


    public DownListener getDownListener() {
        return downListener;
    }

    public String getTag() {
        return tag;
    }
}
