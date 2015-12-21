package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.model.UserModel;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:用户信息API
 */
public class UserApi extends BaseApi {

    public UserApi() {
        super();
    }

    /**
     * 3.18获取个人信息
     *
     * @param callback
     * @param userId
     */
    public void getPersonInfo(final ICallback callback, String userId) {

        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_PERSONAL_ACTION, HttpRequest.HttpMethod.GET);
    }


    /**
     * 3.20修改个人信息
     *
     * @param callback
     * @param userModel
     */
    public void modifyPersonalInfo(ICallback callback, UserModel userModel) {
        RequestParams params = modelToParams(userModel);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.MODIFY_PERSONAL_ACTION, HttpRequest.HttpMethod.GET);
    }

    /**
     * 3.19重新绑定手机号
     *
     * @param callback
     * @param serial   设备唯一序列号
     * @param mobile
     */
    public void setMobile(ICallback callback, String serial, String mobile) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("serial", serial);
        params.addQueryStringParameter("mobile", mobile);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_MOBILE_ACTION, HttpRequest.HttpMethod.GET);
    }

    /**
     * 3.21解除手机绑定
     *
     * @param callback
     * @param userId
     */
    public void unbindMobile(ICallback callback, String userId) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.UNBIND_MOBILE_ACTION, HttpRequest.HttpMethod.GET);
    }


    /**
     * 3.22密码设置
     *
     * @param callback
     * @param userId   户id
     * @param password 密码
     */
    public void setPassword(ICallback callback, String userId, String password) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("Pd", password);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.SET_PASSWORD_ACTION, HttpRequest.HttpMethod.GET);
    }

}
