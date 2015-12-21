package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:情绪相关API
 */
public class MoodApi extends BaseApi {
    public MoodApi() {
        super();
    }

    /**
     * 3.17消息通知设置
     *
     * @param callback
     * @param userId
     */
    public void getMood(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_MOOD_ACTION, HttpRequest.HttpMethod.GET);
    }
}
