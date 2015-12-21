package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:消息通知设置
 */
public class MessageApi extends BaseApi {
    public MessageApi() {
        super();
    }

    /**
     * 3.17消息通知设置
     *
     * @param callback
     * @param userId
     * @param receiver
     */
    public void setMessageNotity(ICallback callback, String userId, boolean receiver) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        if (receiver)
            params.addQueryStringParameter("set", "1");
        else {
            params.addQueryStringParameter("set", "0");
        }
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_MESSAGE_NOTIFY_ACTION, HttpRequest.HttpMethod.GET);
    }
}
