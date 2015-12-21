package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/28.
 * App版本信息
 */
public class AppVersionModel implements Serializable {

    private String version;//版本号
    private String url;//下载地址
    private String isUpdate;//是否更新(1:强制更新，0：不强制更新）
    private String msg;//App新版本介绍

    public AppVersionModel() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AppVersionModel{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", isUpdate='" + isUpdate + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
