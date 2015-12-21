package com.smartbracelet.sunny.model;

import java.io.Serializable;
import java.util.List;

public class SkinSolution implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;
    private String problemPrediction;
    private List<SkinTestRecord> skinMeasureRec;
    private String sleep;
    private String diet;
    private String environment;
    private String skinGuide;

    /**
     * @return the problemPrediction
     */
    public String getProblemPrediction() {
        return problemPrediction;
    }

    /**
     * @param problemPrediction the problemPrediction to set
     */
    public void setProblemPrediction(String problemPrediction) {
        this.problemPrediction = problemPrediction;
    }

    /**
     * @return the skinMeasureRec
     */
    public List<SkinTestRecord> getSkinMeasureRec() {
        return skinMeasureRec;
    }

    /**
     * @param skinMeasureRec the skinMeasureRec to set
     */
    public void setSkinMeasureRec(List<SkinTestRecord> skinMeasureRec) {
        this.skinMeasureRec = skinMeasureRec;
    }

    /**
     * @return the sleep
     */
    public String getSleep() {
        return sleep;
    }

    /**
     * @param sleep the sleep to set
     */
    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    /**
     * @return the diet
     */
    public String getDiet() {
        return diet;
    }

    /**
     * @param diet the diet to set
     */
    public void setDiet(String diet) {
        this.diet = diet;
    }

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment to set
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * @return the skinGuide
     */
    public String getSkinGuide() {
        return skinGuide;
    }

    /**
     * @param skinGuide the skinGuide to set
     */
    public void setSkinGuide(String skinGuide) {
        this.skinGuide = skinGuide;
    }

    /*
     * (非 Javadoc) <p>描述:</p>
     *
     * @return
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SkinSolution [problemPrediction=" + problemPrediction
                + ", skinMeasureRec=" + skinMeasureRec + ", sleep=" + sleep
                + ", diet=" + diet + ", environment=" + environment
                + ", skinGuide=" + skinGuide + "]";
    }

    public class SkinTestRecord {
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

}
