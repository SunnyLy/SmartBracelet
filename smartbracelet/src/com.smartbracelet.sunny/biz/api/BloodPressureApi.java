package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.model.BloodPressure;
import com.smartbracelet.sunny.model.TiredModel;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:与疲劳相关API
 */
public class BloodPressureApi extends BaseApi {
    public BloodPressureApi() {
        super();
    }

    /**
     * 3.5获取疲劳度消息
     *
     * @param callback
     * @param userId
     */
    public void getBloodPressure(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_BLOOR_PRESSURE_ACTION, HttpRequest.HttpMethod.GET);

    }

    /**
     * 3.14设置血压消息
     *
     * @param callback
     * @param userId
     * @param model
     */
    public void setBloodPressure(ICallback callback, String userId, BloodPressure model) {
        RequestParams params = modelToParams(model);
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_BLOOD_PRESSURE_ACTION, HttpRequest.HttpMethod.GET);
    }

    /**
     * 3.10 获取时间段血压消息
     *
     * @param callback
     * @param userId
     * @param startTime
     * @param endTime
     */
    public void getTimeBloodPresserue(ICallback callback, String userId, String startTime, String endTime) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("startTime", startTime);
        params.addQueryStringParameter("endTime", endTime);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_TIME_BLOOR_PRESSURE_ACTION, HttpRequest.HttpMethod.GET);

    }
}
