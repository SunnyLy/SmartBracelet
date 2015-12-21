package com.smartbracelet.sunny.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:
 */
public class UserModel extends DataSupport implements Serializable {

    private String userID;
    private String sex;//1:男，0：女
    private String birthday;
    private String weight;
    private String height;
    private String adornHand;//1:左手，2：右手
    private String mobile;

    public UserModel() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAdornHand() {
        return adornHand;
    }

    public void setAdornHand(String adornHand) {
        this.adornHand = adornHand;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userID='" + userID + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", adornHand='" + adornHand + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
