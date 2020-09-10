package com.hao.minovel.moudle.entity;

import android.app.Activity;

import com.hao.minovel.base.MiBaseFragment;

/**
 * 主页菜单栏
 */
public class StackTypeEntity {
    String muneItemm;
    MiBaseFragment miBaseFragment;

    public StackTypeEntity(int itmeImgId, String muneItemm, MiBaseFragment miBaseFragment) {
        this.muneItemm = muneItemm;
        this.miBaseFragment = miBaseFragment;
    }


    public String getMuneItemm() {
        return muneItemm;
    }

    public void setMuneItemm(String muneItemm) {
        this.muneItemm = muneItemm;
    }

    public MiBaseFragment getMiBaseFragment() {
        return miBaseFragment;
    }

    public void setMiBaseFragment(MiBaseFragment miBaseFragment) {
        this.miBaseFragment = miBaseFragment;
    }
}