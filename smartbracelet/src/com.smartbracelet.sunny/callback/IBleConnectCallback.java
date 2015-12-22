package com.smartbracelet.sunny.callback;

/**
 * Created by sunny on 2015/12/22.
 * 蓝牙连接回调监听
 */
public interface IBleConnectCallback {
    void startConnect();

    void notFoundDevice();

    void connectSuccess();

    void connectFailure();
}
