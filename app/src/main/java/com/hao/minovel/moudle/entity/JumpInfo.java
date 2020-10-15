package com.hao.minovel.moudle.entity;

import android.os.Bundle;

/**
 * 用于EventBus触发时发送的记录跳转页面及通讯信息
 */
public class JumpInfo {
    String acticityTag;
    Bundle bundle;

    public JumpInfo(String acticityTag) {
        this.acticityTag = acticityTag;
    }

    public JumpInfo(String acticityTag, Bundle bundle) {
        this.acticityTag = acticityTag;
        this.bundle = bundle;
    }

    public String getActicityTag() {
        return acticityTag;
    }

    public void setActicityTag(String acticityTag) {
        this.acticityTag = acticityTag;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
