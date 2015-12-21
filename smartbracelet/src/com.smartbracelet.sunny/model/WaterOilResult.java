package com.smartbracelet.sunny.model;

import java.util.List;

/**
 * <p>描述：水油结果model</p>
 *
 * @author ~若相惜
 * @version v1.0
 * @date 2015-9-11 下午1:55:02
 */
@SuppressWarnings("serial")
public class WaterOilResult extends BaseAvg {
    private String wind;
    private List<WaterOilCheckRecord> measureRec;

    public WaterOilResult() {
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public List<WaterOilCheckRecord> getMeasureRec() {
        return measureRec;
    }

    public void setMeasureRec(List<WaterOilCheckRecord> measureRec) {
        this.measureRec = measureRec;
    }

    @Override
    public String toString() {
        return "WaterOilResult{" +
                "avgWater='" + avgWater + '\'' +
                ", avgOil='" + avgOil + '\'' +
                ", wind='" + wind + '\'' +
                ", measureRec=" + measureRec +
                '}';
    }

    /**
     * 水油测试记录
     */
    public class WaterOilCheckRecord {
        private String measureTime;
        private String oil;
        private String water;
        private String skinMeterId;
        private String elasticity;

        public WaterOilCheckRecord() {
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public String getOil() {
            return oil;
        }

        public void setOil(String oil) {
            this.oil = oil;
        }

        public String getWater() {
            return water;
        }

        public void setWater(String water) {
            this.water = water;
        }

        public String getSkinMeterId() {
            return skinMeterId;
        }

        public void setSkinMeterId(String skinMeterId) {
            this.skinMeterId = skinMeterId;
        }

        public String getElasticity() {
            return elasticity;
        }

        public void setElasticity(String elasticity) {
            this.elasticity = elasticity;
        }
    }
}
