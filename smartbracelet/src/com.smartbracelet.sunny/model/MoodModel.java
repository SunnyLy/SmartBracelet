package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/12/23.
 * 情绪实体类，用来管理请求获取情绪接口时，后台返回的数据
 */
public class MoodModel implements Serializable {

    private String mood;

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    @Override
    public String toString() {
        return "MoodModel{" +
                "mood='" + mood + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoodModel moodModel = (MoodModel) o;

        return !(mood != null ? !mood.equals(moodModel.mood) : moodModel.mood != null);

    }

    @Override
    public int hashCode() {
        return mood != null ? mood.hashCode() : 0;
    }
}
