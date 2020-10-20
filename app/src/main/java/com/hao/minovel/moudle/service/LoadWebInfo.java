package com.hao.minovel.moudle.service;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 加载数据的信息
 */
public class LoadWebInfo implements Parcelable {
    String id;
    int task;
    int loadStatus;
    int tag;

    public LoadWebInfo() {
        id = System.currentTimeMillis() + "" + ((int) Math.random() * 10000);
    }

    protected LoadWebInfo(Parcel in) {
        id = in.readString();
        task = in.readInt();
        loadStatus = in.readInt();
        tag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(task);
        dest.writeInt(loadStatus);
        dest.writeInt(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoadWebInfo> CREATOR = new Creator<LoadWebInfo>() {
        @Override
        public LoadWebInfo createFromParcel(Parcel in) {
            return new LoadWebInfo(in);
        }

        @Override
        public LoadWebInfo[] newArray(int size) {
            return new LoadWebInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getLoadStatus() {
        return loadStatus;
    }

    public LoadWebInfo setLoadStatus(int loadStatus) {
        this.loadStatus = loadStatus;
        return this;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
