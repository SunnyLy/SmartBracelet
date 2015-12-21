package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.model.TiredModel;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:与疲劳相关API
 */
public class TiredApi extends BaseApi {
    public TiredApi() {
        super();
    }

    /**
     * 3.5获取疲劳度消息
     *
     * @param callback
     * @param userId
     */
    public void getTired(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_TIRED_ACTION, HttpRequest.HttpMethod.GET);

    }

    /**
     * 3.16设置疲劳度值
     *
     * @param callback
     * @param userId
     * @param model
     */
    public void setTiredPressure(ICallback callback, String userId, TiredModel model) {
        RequestParams params = modelToParams(model);
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_TIRED_ACTION, HttpRequest.HttpMethod.GET);
    }
}
