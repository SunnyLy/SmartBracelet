package com.smartbracelet.sunny.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.het.comres.view.dialog.CommonLoadingDialog;
import com.smartbracelet.sunny.AppApplication;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.manager.ScreenManager;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.LoginOutEvent;

import butterknife.ButterKnife;

/**
 * Created by sunny on 15-11-5.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public Context mContext;
    public Resources mResources;
    public CommonLoadingDialog mCommonLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        mResources = getResources();
        ScreenManager.getScreenManager().pushActivity(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        initTitleBar();
        initParams();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            setIntent(intent);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        ScreenManager.getScreenManager().popActivity(this);
    }

    public void initTitleBar() {
    }

    public void initParams() {
    }

    public void showDialog() {
        if (mCommonLoadingDialog == null) {
            mCommonLoadingDialog = new CommonLoadingDialog(mContext);
        }
        mCommonLoadingDialog.show();
    }

    public void showDialog(String title) {
        if (mCommonLoadingDialog == null) {
            mCommonLoadingDialog = new CommonLoadingDialog(mContext);
            mCommonLoadingDialog.setText(title);
        }
        mCommonLoadingDialog.show();
    }

    public void hideDialog() {
        if (mCommonLoadingDialog != null && mCommonLoadingDialog.isShowing()) {
            mCommonLoadingDialog.dismiss();
            mCommonLoadingDialog = null;
        }
    }

    public boolean isDialogShow() {
        return mCommonLoadingDialog != null;
    }

    /**
     * 退出程序
     */
    public void exit() {
        AppApplication.getInstance().killMyself();
    }


    public void onEventMainThread(BaseEvent baseEvent) {
        BaseEvent.EventType eventType = baseEvent.getEventType();
        switch (eventType) {
            case LOGIN:
                break;
            case LOGINOUT:
                break;
            case REGISTER:
                break;
            case CAHNGE_INFO:
                break;
        }
    }

}
