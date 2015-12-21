package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.TiredApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.event.BaseEvent;

import butterknife.ButterKnife;

/**
 * Created by sunny on 2015/11/28.
 */
public class TiredFragment extends BaseFragment {

    private String mTestValue;
    private String mTestTime;
    private String mUserId;
    private UserManager mUserManager;

    public void setmTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public void setmTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tired, null);
        ButterKnife.inject(this, view);
        initParams();
        return view;
    }

    private void initParams() {
        mUserManager = UserManager.getInstance();
        mUserId = mUserManager.getUserModel() == null ? "1" : mUserManager.getUserModel().getUserID();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("TiredFragment,onResume====");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("TiredFragment,onHiddenChanged========");
    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.TIRED) {
            getTiredData();
        }
    }

    private void getTiredData() {
        showDialog();
        new TiredApi().getTired(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                parseJsonObject(o);
            }

            @Override
            public void onFailure(int code, String msg, int id) {

                hideDialog();
                CommonToast.showToast(mContext, msg);
            }
        }, mUserId);
    }

    private void parseJsonObject(Object o) {

    }
}
