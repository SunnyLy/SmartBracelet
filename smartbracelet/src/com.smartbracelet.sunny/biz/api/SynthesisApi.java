package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:首页综合信息API
 */
public class SynthesisApi extends BaseApi {
    public SynthesisApi() {
        super();
    }

    /**
     * 3.3 获取当天消息
     *
     * @param callback
     * @param userId
     */
    public void getCurrentAction(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_CURRENT_ACTION, HttpRequest.HttpMethod.GET);
    }
}
