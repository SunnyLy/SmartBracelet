package com.smartbracelet.sunny.biz.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.het.common.callback.ICallback;
import com.het.common.utils.GsonUtil;
import com.het.common.utils.LogUtils;
import com.het.common.utils.MD5Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.smartbracelet.sunny.AppConstant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:Api基类
 */
public class BaseApi {

    private static final String DEVELOP_KEY = "BB80F668FC9C4127D3C5C7A9B";
    protected String mSign = "sign";
    private String time = "time";

    private HttpUtils mHttpUtils;
    private RequestParams requestParams;

    public HttpUtils getHttpUtils() {
        return mHttpUtils;
    }

    public RequestParams getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    public BaseApi() {
        mHttpUtils = new HttpUtils();
        mHttpUtils.configCurrentHttpCacheExpiry(1000 * 10);
        requestParams = new RequestParams("UTF-8");
    }

    /**
     * 提交请求
     *
     * @param callback
     * @param url
     * @param method
     */
    public void realCommit(final ICallback callback, String url, String action, HttpRequest.HttpMethod method) {
        url = url + action;
        requestParams.addQueryStringParameter(time, Long.toString(System.currentTimeMillis() / 1000));
        setSign(true);
        if (AppConstant.DEBUG) {
            LogUtils.i("请求路径:" + url + ",请求参数:" + getRequestParams().getQueryStringParams().toString());
            List<NameValuePair> params = getRequestParams().getQueryStringParams();
            if (params != null && params.size() > 0) {
                StringBuilder sb = new StringBuilder(params.size());
                sb.append(url);
                sb.append("?");
                for (int i = 0; i < params.size(); i++) {
                    sb.append(params.get(i).getName()).append("=").append(params.get(i).getValue())
                            .append("&");
                }
                sb.deleteCharAt(sb.lastIndexOf("&"));
                LogUtils.i("完整请求路径:" + sb.toString());
            }
        }
        mHttpUtils.send(method, url, getRequestParams(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (AppConstant.DEBUG)
                    LogUtils.i("返回结果：" + responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String errorCode = jsonObject.getString("errorCode");
                    String wardCode = jsonObject.getString("wardCode");
                    if ("0000".equals(errorCode)) {
                        callback.onSuccess(responseInfo.result, -1);
                    } else {
                        callback.onFailure(Integer.valueOf(errorCode), "服务器错误", -1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(0, e.getMessage(), -1);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

                callback.onFailure(e.getExceptionCode(), s, -1);
            }
        });

    }

    /**
     * 请求参数签名
     *
     * @param sign
     * @return
     */
    public RequestParams setSign(boolean sign) {
        List<NameValuePair> nameValuePairs = null;
        String name;
        String value;
        StringBuilder sb = new StringBuilder();
        if (sign) {
            nameValuePairs = requestParams.getQueryStringParams();
            if (nameValuePairs != null && nameValuePairs.size() > 0) {
                for (NameValuePair nameValuePair : nameValuePairs) {
                    name = nameValuePair.getName();
                    value = nameValuePair.getValue();
                    if (!TextUtils.isEmpty(value))
                        sb.append(name).append("=").append(value).append("&");
                }
                sb.append("develop_key=" + DEVELOP_KEY);
                String md5 = MD5Utils.string2MD5(sb.toString());
                requestParams.addQueryStringParameter(mSign, md5);
            }
        }

        return requestParams;
    }

    /**
     * 把Model利用反射拼接成参数
     *
     * @param model
     * @return
     */
    public RequestParams modelToParams(Object model) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Field[] fields = model.getClass().getDeclaredFields();
        Method method;
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            if (field.getName().equals("status")) {
                continue;
            }
            StringBuilder nameBuilder = new StringBuilder(field.getName());
            nameBuilder.setCharAt(0, (nameBuilder.charAt(0) + "").toUpperCase().charAt(0));
            try {
                method = model.getClass().getMethod("get" + nameBuilder);
                if (method.invoke(model) != null) {

                    nameValuePairs.add(new BasicNameValuePair(field.getName(), method.invoke(model) + ""));
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        requestParams.addQueryStringParameter(nameValuePairs);
        return requestParams;

    }
}
