package com.het.common.callback;

/**
 * Created by weatherfish on 2015/3/12.
 */
public interface ICallback<T> {
    /**
     * call when volley access success and the response code is SUCCESS
     */
    void onSuccess(T t, int id);

    /**
     * call when volley access success and the response code is not SUCCESS
     */
    void onFailure(int code, String msg, int id);
}
