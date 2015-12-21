package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/12/3.
 */
public class TimeStepModel implements Serializable {

    private String date;
    private String step;//步数
    private String speedometer;//里程
    private String energy;//能耗

    public TimeStepModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getSpeedometer() {
        return speedometer;
    }

    public void setSpeedometer(String speedometer) {
        this.speedometer = speedometer;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "TimeStepModel{" +
                "date='" + date + '\'' +
                ", step='" + step + '\'' +
                ", speedometer='" + speedometer + '\'' +
                ", energy='" + energy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeStepModel that = (TimeStepModel) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (step != null ? !step.equals(that.step) : that.step != null) return false;
        if (speedometer != null ? !speedometer.equals(that.speedometer) : that.speedometer != null)
            return false;
        return !(energy != null ? !energy.equals(that.energy) : that.energy != null);

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (step != null ? step.hashCode() : 0);
        result = 31 * result + (speedometer != null ? speedometer.hashCode() : 0);
        result = 31 * result + (energy != null ? energy.hashCode() : 0);
        return result;
    }
}
