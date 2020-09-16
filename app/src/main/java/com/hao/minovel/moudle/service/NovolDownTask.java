package com.hao.minovel.moudle.service;

public class NovolDownTask<T> {

    DownLoadNovelService.NovelDownTag novelDownTag;
    T object;
    DownListener downListener;
    String tag;


    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag) {
        this(novelDownTag, null);
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, T object) {
        this(novelDownTag, object, null);
    }


    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, T object, DownListener downListener) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.downListener = downListener;
        tag = "task" + (System.currentTimeMillis() + Math.round(10000));
    }

    public DownLoadNovelService.NovelDownTag getNovelDownTag() {
        return novelDownTag;
    }


    public T getObject() {
        return object;
    }


    public DownListener getDownListener() {
        return downListener;
    }

    public String getTag() {
        return tag;
    }
}
