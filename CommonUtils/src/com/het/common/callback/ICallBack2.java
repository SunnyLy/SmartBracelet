package com.het.common.callback;

/**
 * @param <T>
 * @author weatherfish
 */
public interface ICallBack2<T> extends ICallback<T> {
    void onStateChange(T t, int id);
}
