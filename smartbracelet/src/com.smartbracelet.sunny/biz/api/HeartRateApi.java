package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.model.TiredModel;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:心率相关API
 */
public class HeartRateApi extends BaseApi {

    public HeartRateApi() {
        super();
    }

    /**
     * 3.6获取心率消息
     *
     * @param callback
     * @param userId
     */
    public void getHeartBeat(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_HEART_BEAT_ACTION, HttpRequest.HttpMethod.GET);

    }

    /**
     * 3.15设置心率消息
     *
     * @param callback
     * @param userId
     * @param max      心率最大值
     * @param min      心率最小值
     */
    public void setHearthPressure(ICallback callback, String userId, String max, String min) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("max", max);
        params.addQueryStringParameter("min", min);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_HEART_PRESSURE_ACTION, HttpRequest.HttpMethod.GET);
    }

    /**
     * 3.13 获取指定时间段心率消息
     *
     * @param callback
     * @param userId
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    public void getHeartPressureByTime(ICallback callback, String userId, String startTime, String endTime) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("startTime", startTime);
        params.addQueryStringParameter("endTime", endTime);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_TIME_HEART_PRESSURE_ACTION, HttpRequest.HttpMethod.GET);

    }
}
