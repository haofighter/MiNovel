package com.hao.minovel.moudle.entity;

public class ShiftMuneEntity {
    int itmeImgId;
    String muneItemm;
    String routerActivityTag;

    public ShiftMuneEntity(int itmeImgId, String muneItemm, String routerActivityTag) {
        this.itmeImgId = itmeImgId;
        this.muneItemm = muneItemm;
        this.routerActivityTag = routerActivityTag;
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

    public String getRouterActivityTag() {
        return routerActivityTag;
    }

    public void setRouterActivityTag(String routerActivityTag) {
        this.routerActivityTag = routerActivityTag;
    }
}
