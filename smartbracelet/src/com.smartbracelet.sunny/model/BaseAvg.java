package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * <p>描述：肤质水油弹性 平均数</p>
 *
 * @author ~若相惜
 * @version v1.0
 * @date 2015-9-11 下午1:49:44
 */
@SuppressWarnings("serial")
public class BaseAvg implements Serializable {
    public String avgWater;//平均水分
    public String avgOil;//平均油分
    public String avgElasticity;//弹性
    public String measureDate;//日期

    public String getAvgWater() {
        return avgWater;
    }

    public void setAvgWater(String avgWater) {
        this.avgWater = avgWater;
    }

    public String getAvgOil() {
        return avgOil;
    }

    public void setAvgOil(String avgOil) {
        this.avgOil = avgOil;
    }

    public String getAvgElasticity() {
        return avgElasticity;
    }

    public void setAvgElasticity(String avgElasticity) {
        this.avgElasticity = avgElasticity;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }
}
