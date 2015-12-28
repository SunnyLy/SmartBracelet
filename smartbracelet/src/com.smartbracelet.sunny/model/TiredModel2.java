package com.smartbracelet.sunny.model;

import java.io.Serializable;

/**
 * Created by sunny on 2015/12/28.
 * 疲劳实体Bean.
 * 本来已经有一个TiredModel了，但为什么还要在这新建一个TiredModel2,
 * 是因为接口里面返回的数据，与要提交的数据对不上号，
 * 理论上来讲，后台是因为把它抽像成一个model的，那样方便维护与修改。
 * 但现在没办法，就只能先委屈下自己了。
 */
public class TiredModel2 implements Serializable {

    /**
     * 服务器返回的值：
     * 1：疲劳
     * 2：平和
     * 3：正常
     * 4：中度疲劳
     * 5：消沉
     */
    private String tired;
    private String time;

    public String getTired() {
        return tired;
    }

    public void setTired(String tired) {
        this.tired = tired;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TiredModel2{" +
                "tired='" + tired + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TiredModel2 that = (TiredModel2) o;

        if (tired != null ? !tired.equals(that.tired) : that.tired != null) return false;
        return !(time != null ? !time.equals(that.time) : that.time != null);

    }

    @Override
    public int hashCode() {
        int result = tired != null ? tired.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
