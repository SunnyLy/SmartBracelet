package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/23.
 * 测试Model,用于测试
 */
public class TestModel implements Serializable {

    private String result;
    private String userID;
    private String intervalTime;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public TestModel() {
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "result='" + result + '\'' +
                ", userID='" + userID + '\'' +
                ", intervalTime='" + intervalTime + '\'' +
                '}';
    }
}
