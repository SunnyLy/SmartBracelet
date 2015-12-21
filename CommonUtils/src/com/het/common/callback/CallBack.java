package com.het.common.callback;

/**
 * @param <T>
 * @author weatherfish
 */
public class CallBack<T> implements ICallback<T> {

    @Override
    public void onSuccess(T t, int id) {

    }

    @Override
    public void onFailure(int code, String msg, int id) {

    }

}
