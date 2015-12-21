package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:疲劳度Model
 */
public class TiredModel implements Serializable {

    private String normal;//正常值
    private String gentle;//平和
    private String tired;//疲劳
    private String middleTired;//中度疲劳
    private String depressed;//消沉

    public TiredModel() {
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getGentle() {
        return gentle;
    }

    public void setGentle(String gentle) {
        this.gentle = gentle;
    }

    public String getTired() {
        return tired;
    }

    public void setTired(String tired) {
        this.tired = tired;
    }

    public String getMiddleTired() {
        return middleTired;
    }

    public void setMiddleTired(String middleTired) {
        this.middleTired = middleTired;
    }

    public String getDepressed() {
        return depressed;
    }

    public void setDepressed(String depressed) {
        this.depressed = depressed;
    }

    @Override
    public String toString() {
        return "TiredModel{" +
                "normal='" + normal + '\'' +
                ", gentle='" + gentle + '\'' +
                ", tired='" + tired + '\'' +
                ", middleTired='" + middleTired + '\'' +
                ", depressed='" + depressed + '\'' +
                '}';
    }
}
