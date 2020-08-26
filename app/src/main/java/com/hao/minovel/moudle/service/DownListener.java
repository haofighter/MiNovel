package com.hao.minovel.moudle.service;

public abstract class DownListener {
    String tag;
    boolean isScreen;

    public DownListener(String tag, boolean isScreen) {
        this.tag = tag;
        this.isScreen = isScreen;
    }

    public String getTag() {
        return tag;
    }

    public boolean isScreen() {
        return isScreen;
    }

    public abstract void downInfo(long all, long now);


    public abstract void startDown();

    public abstract void endDown();
}
