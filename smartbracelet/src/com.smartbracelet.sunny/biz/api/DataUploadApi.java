package com.smartbracelet.sunny.biz.api;

import com.het.common.callback.ICallback;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

/**
 * Created by sunny on 2015/11/7.
 * Annotion数据上传
 */
public class DataUploadApi extends BaseApi {
    public DataUploadApi() {
        super();
    }

    /**
     * 3.2 数据上传消息
     *
     * @param callback
     * @param userId
     * @param serial     手环唯一序列号
     * @param sPressure  收缩压
     * @param dBPressure 舒张压
     * @param stepNumber 计步值
     * @param heartBeat  心率值
     */
    public void updateDataAction(ICallback callback, String userId,
                                 String serial, String sPressure,
                                 String dBPressure, String stepNumber,
                                 String heartBeat) {
        RequestParams params = getRequestParams();
        params.addQueryStringParameter("userID", userId);
        params.addQueryStringParameter("serial", serial);
        params.addQueryStringParameter("sPressure", sPressure);
        params.addQueryStringParameter("dBPressure", dBPressure);
        params.addQueryStringParameter("stepNumber", stepNumber);
        params.addQueryStringParameter("heartBeat", heartBeat);
        setRequestParams(params);
        realCommit(callback, AppConstant.REQUEST_URL, AppConstant.UPDATE_DATA_ACTION, HttpRequest.HttpMethod.GET);
    }
}
