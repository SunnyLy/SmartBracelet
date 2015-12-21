package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:获取呼吸频率
 */
public class BreathRateApi extends BaseApi {
    public BreathRateApi() {
        super();
    }

    /**
     * 3.8 获取呼吸频率消息
     *
     * @param callback
     * @param userId
     */
    public void getBreathRate(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_BREATH_RATE_ACTION, HttpRequest.HttpMethod.GET);
    }
}
