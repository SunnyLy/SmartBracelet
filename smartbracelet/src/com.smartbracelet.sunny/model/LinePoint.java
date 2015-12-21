package com.smartbracelet.sunny.model;

/**
 * <p>描述：点坐标</p>
 *
 * @author ~若相惜
 * @version v1.0
 * @date 2015-9-9 下午4:51:58
 */
public class LinePoint {
    public String pointX;//x值
    public float pointY;//y值
    public String measureTime;//时间

    public LinePoint(String pointX, float pointy) {
        super();
        this.pointX = pointX;
        this.pointY = pointy;
    }

    public LinePoint(String pointX, float pointy, String measureTime) {
        super();
        this.pointX = pointX;
        this.pointY = pointy;
        this.measureTime = measureTime;
    }
}
