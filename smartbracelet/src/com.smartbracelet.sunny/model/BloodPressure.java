package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:血压
 */
public class BloodPressure implements Serializable {

    private String smax;//收缩压最大值
    private String smin;//收缩压最小值
    private String dmax;//舒张压最大值
    private String dmin;//舒张压最小值

    public BloodPressure() {
    }

    public String getSmax() {
        return smax;
    }

    public void setSmax(String smax) {
        this.smax = smax;
    }

    public String getSmin() {
        return smin;
    }

    public void setSmin(String smin) {
        this.smin = smin;
    }

    public String getDmax() {
        return dmax;
    }

    public void setDmax(String dmax) {
        this.dmax = dmax;
    }

    public String getDmin() {
        return dmin;
    }

    public void setDmin(String dmin) {
        this.dmin = dmin;
    }

    @Override
    public String toString() {
        return "BloodPressure{" +
                "smax='" + smax + '\'' +
                ", smin='" + smin + '\'' +
                ", dmax='" + dmax + '\'' +
                ", dmin='" + dmin + '\'' +
                '}';
    }
}
