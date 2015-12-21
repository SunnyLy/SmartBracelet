package com.smartbracelet.sunny.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015-07-20.
 * 查询的肤质测试结果Model
 * <p>描述：修改者</p>
 *
 * @author ~若相惜
 * @version v1.0
 * @date 2015-9-11 下午1:51:25
 */
@SuppressWarnings("serial")
public class SkinCheckResultModelForQuery extends BaseAvg {
    private String skinType;//肤质类型
    private String skinAreaRank;//肤质地区同龄排行
    private String waterTrend;//水分较昨天变化幅度
    private String oilTrend;//油分较昨天变化幅度
    private float lastWater;//最后一次水分
    private String elasticityTrend;//弹性较昨天变化幅度
    private String waterAreaAvg;//水分地区同龄均值
    private String oilAreaAvg;//油分地区同龄均值
    private String elasticityAreaAvg;//弹性地区同龄均值
    private List<SkinTestRecord> skinMeasureRec;
    private String skinAge;//肤质平均
    private String waterAvgT;//T区水分平均
    private String oilAvgT;//T区油分平均
    private String waterAvgU;//U区水分平均
    private String oilAvgU;//U区油分平均
    private String skinProblem;//预测肌肤问题
    private SkinSolution skinSolution;//影响肤质问题
    private String[] tagList;//标签数组


    public SkinCheckResultModelForQuery(String avgWater, String avgOil,
                                        String skinAreaRank, String waterTrend, String oilTrend,
                                        String waterAreaAvg, String oilAreaAvg, String waterAvgT,
                                        String oilAvgT, String waterAvgU, String oilAvgU,
                                        String[] tagList
    ) {
        super();
        this.avgWater = avgWater;
        this.avgOil = avgOil;
        this.skinAreaRank = skinAreaRank;
        this.waterTrend = waterTrend;
        this.oilTrend = oilTrend;
        this.waterAreaAvg = waterAreaAvg;
        this.oilAreaAvg = oilAreaAvg;
        this.waterAvgT = waterAvgT;
        this.oilAvgT = oilAvgT;
        this.waterAvgU = waterAvgU;
        this.oilAvgU = oilAvgU;
        this.tagList = tagList;
    }

    public float getLastWater() {
        return lastWater;
    }

    public void setLastWater(float lastWater) {
        this.lastWater = lastWater;
    }

    /**
     * @return the skinAge
     */
    public String getSkinAge() {
        return skinAge;
    }


    /**
     * @param skinAge the skinAge to set
     */
    public void setSkinAge(String skinAge) {
        this.skinAge = skinAge;
    }


    /**
     * @return the skinSolution
     */
    public SkinSolution getSkinSolution() {
        return skinSolution;
    }


    /**
     * @param skinSolution the skinSolution to set
     */
    public void setSkinSolution(SkinSolution skinSolution) {
        this.skinSolution = skinSolution;
    }


    public void setTagList(String[] tagList) {
        this.tagList = tagList;
    }


    public String[] getTagList() {
        return tagList;
    }


    public SkinCheckResultModelForQuery() {
    }

    public String getSkinProblem() {
        return skinProblem;
    }

    public void setSkinProblem(String skinProblem) {
        this.skinProblem = skinProblem;
    }

    public List<SkinTestRecord> getSkinMeasureRec() {
        return skinMeasureRec;
    }

    public void setSkinMeasureRec(List<SkinTestRecord> skinMeasureRec) {
        this.skinMeasureRec = skinMeasureRec;
    }


    public String getSkinAreaRank() {
        return skinAreaRank;
    }

    public void setSkinAreaRank(String skinAreaRank) {
        this.skinAreaRank = skinAreaRank;
    }

    public String getWaterTrend() {
        return waterTrend;
    }

    public void setWaterTrend(String waterTrend) {
        this.waterTrend = waterTrend;
    }

    public String getOilTrend() {
        return oilTrend;
    }

    public void setOilTrend(String oilTrend) {
        this.oilTrend = oilTrend;
    }

    public String getWaterAreaAvg() {
        return waterAreaAvg;
    }

    public void setWaterAreaAvg(String waterAreaAvg) {
        this.waterAreaAvg = waterAreaAvg;
    }

    public String getOilAreaAvg() {
        return oilAreaAvg;
    }

    public void setOilAreaAvg(String oilAreaAvg) {
        this.oilAreaAvg = oilAreaAvg;
    }

    public String getWaterAvgT() {
        return waterAvgT;
    }

    public void setWaterAvgT(String waterAvgT) {
        this.waterAvgT = waterAvgT;
    }

    public String getOilAvgT() {
        return oilAvgT;
    }

    public void setOilAvgT(String oilAvgT) {
        this.oilAvgT = oilAvgT;
    }

    public String getWaterAvgU() {
        return waterAvgU;
    }

    public void setWaterAvgU(String waterAvgU) {
        this.waterAvgU = waterAvgU;
    }

    public String getOilAvgU() {
        return oilAvgU;
    }

    public void setOilAvgU(String oilAvgU) {
        this.oilAvgU = oilAvgU;
    }

    /* (非 Javadoc) 
     * <p>描述:</p>
	 * @return 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "SkinCheckResultModelForQuery [skinType=" + skinType
                + ", skinAreaRank=" + skinAreaRank + ", waterTrend="
                + waterTrend + ", oilTrend=" + oilTrend + ", elasticityTrend="
                + elasticityTrend + ", waterAreaAvg=" + waterAreaAvg
                + ", oilAreaAvg=" + oilAreaAvg + ", elasticityAreaAvg="
                + elasticityAreaAvg + ", skinMeasureRec=" + skinMeasureRec
                + ", skinAge=" + skinAge + ", waterAvgT=" + waterAvgT
                + ", oilAvgT=" + oilAvgT + ", waterAvgU=" + waterAvgU
                + ", oilAvgU=" + oilAvgU + ", skinSolution=" + skinSolution + ", tagList="
                + Arrays.toString(tagList) + "]";
    }


    public class SkinTestRecord implements Serializable {
        private String measureTime;
        private String oil;
        private String water;
        private String elasticity;

        /**
         * @return the elasticity
         */
        public String getElasticity() {
            return elasticity;
        }

        /**
         * @param elasticity the elasticity to set
         */
        public void setElasticity(String elasticity) {
            this.elasticity = elasticity;
        }

        public SkinTestRecord() {
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
    }


    public String getSkinType() {
        return skinType;
    }


    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }


    public String getElasticityTrend() {
        return elasticityTrend;
    }


    public void setElasticityTrend(String elasticityTrend) {
        this.elasticityTrend = elasticityTrend;
    }


    public String getElasticityAreaAvg() {
        return elasticityAreaAvg;
    }


    public void setElasticityAreaAvg(String elasticityAreaAvg) {
        this.elasticityAreaAvg = elasticityAreaAvg;
    }

}
