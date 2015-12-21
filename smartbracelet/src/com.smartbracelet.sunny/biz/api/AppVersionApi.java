package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/28.
 * App版本检查
 */
public class AppVersionApi extends BaseApi {

    public AppVersionApi() {
        super();
    }

    /**
     * 3.23App版本检查
     *
     * @param callback
     */
    public void getAppVersion(ICallback callback) {
        RequestParams params = getRequestParams();
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.GET_APP_VERSION_ACTION, HttpRequest.HttpMethod.GET);
    }
}
