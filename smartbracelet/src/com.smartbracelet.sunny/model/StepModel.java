package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/12/8.
 * 运动计步Model
 */
public class StepModel implements Serializable {

    private String step;
    private String kilometer;
    private String expand;
    private String time;
    private String score;

    public StepModel() {
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "StepModel{" +
                "step='" + step + '\'' +
                ", kilometer='" + kilometer + '\'' +
                ", expand='" + expand + '\'' +
                ", time='" + time + '\'' +
                ", score='" + score + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StepModel stepModel = (StepModel) o;

        if (step != null ? !step.equals(stepModel.step) : stepModel.step != null) return false;
        if (kilometer != null ? !kilometer.equals(stepModel.kilometer) : stepModel.kilometer != null)
            return false;
        if (expand != null ? !expand.equals(stepModel.expand) : stepModel.expand != null)
            return false;
        if (time != null ? !time.equals(stepModel.time) : stepModel.time != null) return false;
        return !(score != null ? !score.equals(stepModel.score) : stepModel.score != null);

    }

    @Override
    public int hashCode() {
        int result = step != null ? step.hashCode() : 0;
        result = 31 * result + (kilometer != null ? kilometer.hashCode() : 0);
        result = 31 * result + (expand != null ? expand.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
