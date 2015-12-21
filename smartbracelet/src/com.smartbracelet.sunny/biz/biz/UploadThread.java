package com.smartbracelet.sunny.biz.biz;

import android.os.Handler;
import android.os.Message;

import com.het.common.callback.ICallback;
import com.smartbracelet.sunny.biz.api.DataUploadApi;

/**
 * Created by sunny on 2015/11/29.
 * 数据上传，每5s上传一次
 */
public class UploadThread implements Runnable {
    private String mUserId;
    private String mSerialize;
    private String mBloothMax;
    private String mBloothMin;
    private String mStepTotal;
    private String mHeartRate;

    private Handler mHandler;

    public UploadThread(String mUserId, String mSerialize, String mBloothMax, String mBloothMin,
                        String mStepTotal, String mHeartRate, Handler handler) {
        this.mUserId = mUserId;
        this.mSerialize = mSerialize;
        this.mBloothMax = mBloothMax;
        this.mBloothMin = mBloothMin;
        this.mStepTotal = mStepTotal;
        this.mHeartRate = mHeartRate;
        this.mHandler = handler;
    }

    @Override
    public void run() {

        uploadData();
    }

    private void uploadData() {
        new DataUploadApi().updateDataAction(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                Message success = mHandler.obtainMessage();
                success.what = 0;
                success.obj = o;
                mHandler.sendMessage(success);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                Message success = mHandler.obtainMessage();
                success.what = 1;
                success.obj = msg;
                mHandler.sendMessage(success);
            }
        }, mUserId, mSerialize, mBloothMax, mBloothMin, mStepTotal, mHeartRate);
    }
}
