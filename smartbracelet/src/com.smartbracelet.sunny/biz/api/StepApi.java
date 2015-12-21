package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 15-11-7 下午11:38
 * 计步消息API
 */
public class StepApi extends BaseApi {

    public StepApi() {
        super();
    }

    /**
     * 3.9 获取计步消息
     *
     * @param callback
     * @param userId
     */
    public void getStep(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_STEP_ACTION, HttpRequest.HttpMethod.GET);

    }

    /**
     * 3.12 获取时间段计步消息
     *
     * @param callback
     * @param userId
     */
    public void getTimeStep(ICallback callback, String userId, String startTime, String endTime) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("startTime", startTime);
        params.addQueryStringParameter("endTime", endTime);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_TIME_STEP_ACTION, HttpRequest.HttpMethod.GET);

    }
}
