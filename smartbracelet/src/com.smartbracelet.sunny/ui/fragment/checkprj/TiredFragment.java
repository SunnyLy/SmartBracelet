package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.TiredApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.TiredModel;
import com.smartbracelet.sunny.model.event.BaseEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/28.
 */
public class TiredFragment extends BaseFragment {

    private String mTestValue;
    private String mTestTime;
    private String mUserId;
    private UserManager mUserManager;

    @InjectView(R.id.tired_et)
    EditText mContent;
    @InjectView(R.id.tired_cancel)
    Button mBtnCancel;
    @InjectView(R.id.tired_save)
    Button mBtnSave;

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

    @OnClick({R.id.tired_save,R.id.tired_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tired_cancel:
                mContent.setText("");
                break;
            case R.id.tired_save:
                submitt2server();
                break;
        }
    }

    private void submitt2server() {
        String content = mContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            CommonToast.showToast(mContext,getString(R.string.measure_result_hint));
            return;
        }

        showDialog();
        //这里的请求参数是有问题的，后台没有指定哪种类型对应哪种int值，
        //说公司ios都做完了，我想这个应该有发现出来吧，
        TiredModel tiredModel = new TiredModel();
        tiredModel.setDepressed("1");
        tiredModel.setGentle("1");
        tiredModel.setMiddleTired("1");
        tiredModel.setNormal("1");
        tiredModel.setTired("1");
        new TiredApi().setTiredPressure(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                hideDialog();
                CommonToast.showToast(mContext,"保存成功");

            }

            @Override
            public void onFailure(int code, String msg, int id) {

                hideDialog();
                CommonToast.showToast(mContext,msg);
            }
        },mUserId,tiredModel);
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
