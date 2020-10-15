package com.hao.minovel.moudle.entity;

/**
 * 加载数据的信息
 */
public class LoadWebInfo {
    int task;
    int loadStatus;

    public LoadWebInfo(int task, int loadStatus) {
        this.task = task;
        this.loadStatus = loadStatus;
    }
}
