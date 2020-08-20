package com.hao.minovel.moudle.entity;

import com.hao.minovel.base.MiBaseFragment;

public class ContentMuneEntity {
    int itmeImgId;
    String muneItemm;
    MiBaseFragment fragment;

    public ContentMuneEntity(int itmeImgId, String muneItemm, MiBaseFragment fragment) {
        this.itmeImgId = itmeImgId;
        this.muneItemm = muneItemm;
        this.fragment = fragment;
    }

    public int getItmeImgId() {
        return itmeImgId;
    }

    public void setItmeImgId(int itmeImgId) {
        this.itmeImgId = itmeImgId;
    }

    public String getMuneItemm() {
        return muneItemm;
    }

    public void setMuneItemm(String muneItemm) {
        this.muneItemm = muneItemm;
    }

    public MiBaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(MiBaseFragment fragment) {
        this.fragment = fragment;
    }
}
