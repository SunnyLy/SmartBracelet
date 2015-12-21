package com.smartbracelet.sunny.model.event;


import com.lidroid.xutils.exception.HttpException;

/**
 * 描述：Apk更新总事件。
 * 作者： Sunny
 * 日期： 2015-10-19 10:29
 * 版本： v1.0
 */
public class ApkUpdateEvent {

    public int progress = 0;
    public int total = 0;
    public boolean downloadSuccess = false;

    public HttpException httpException;
    public String msg;

    public ApkUpdateEvent(HttpException e, String s) {
        httpException = e;
        msg = s;
    }

    public ApkUpdateEvent(int p, int total, boolean downloadSuccess) {
        progress = p;
        this.total = total;
        this.downloadSuccess = downloadSuccess;
    }
}
