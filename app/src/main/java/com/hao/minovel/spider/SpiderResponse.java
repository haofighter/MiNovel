package com.hao.minovel.spider;

/**
 * 获取网页的返回结果
 */
public class SpiderResponse {
    int code;
    String result;

    public SpiderResponse(int code, String result) {
        this.code = code;
        this.result = result;
    }
}