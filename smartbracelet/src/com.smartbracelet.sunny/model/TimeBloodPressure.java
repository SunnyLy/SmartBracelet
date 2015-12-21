package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/12/2.
 * 时间段血压Model
 */
public class TimeBloodPressure implements Serializable {

    private String date;//时间点
    private String pressure;//血压值

    public TimeBloodPressure() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "TimeBloodPressure{" +
                "date='" + date + '\'' +
                ", pressure='" + pressure + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeBloodPressure that = (TimeBloodPressure) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return !(pressure != null ? !pressure.equals(that.pressure) : that.pressure != null);

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (pressure != null ? pressure.hashCode() : 0);
        return result;
    }
}
