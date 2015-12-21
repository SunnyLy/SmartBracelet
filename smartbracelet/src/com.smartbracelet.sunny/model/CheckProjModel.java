package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:检测项目
 */
public class CheckProjModel implements Serializable {

    private String type;
    private String time;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CheckProjModel{" +
                "type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
