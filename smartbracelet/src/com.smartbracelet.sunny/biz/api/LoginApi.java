package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * 登录
 */
public class LoginApi extends BaseApi {
    public LoginApi() {
        super();
    }

    /**
     * 3.1 登录消息
     *
     * @param callback
     * @param mobile   手环对应客户手机号码
     * @param serial   手环唯一序列号
     * @param mac      Mac地址
     */
    public void loginAction(ICallback callback, String mobile,
                            String serial, String mac) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("serial", serial);
        params.addQueryStringParameter("mac", mac);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.LOGIN_ACTION, HttpRequest.HttpMethod.GET);
    }
}
