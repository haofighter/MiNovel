package com.hao.minovel.moudle.service;

import android.os.Binder;

public abstract class DownLoadNovelBinder<T> extends Binder {
    public abstract void sendCmd(NovolDownTask o);

    public abstract void sendCmd(NovolDownTask o, int index);

    public abstract Object getMassage();

}
