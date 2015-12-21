package com.smartbracelet.sunny.ui.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.CommSharePreferencesUtil;
import com.het.common.utils.StringUtils;
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.view.edittext.ClearEditText;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.UserApi;
import com.smartbracelet.sunny.ui.MainActivity;
import com.smartbracelet.sunny.ui.user.UserInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/28.
 * 当扫描二维码扫到设备信息后，就跳转至绑定界面
 */
public class BindActivity extends BaseActivity {
    public static final String TAG = BindActivity.class.getSimpleName();


    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopbar;
    @InjectView(R.id.bind_device_mac)
    TextView mTextViewMac;
    @InjectView(R.id.bind_device_phone)
    ClearEditText mEditTextPhone;
    @InjectView(R.id.bind_divice_bind)
    Button mBtnBind;

    private Intent mIntent;
    private String mDeviceMac;

    public static void startBindActivity(Context context, String mac) {
        Intent targetIntent = new Intent(context, BindActivity.class);
        targetIntent.putExtra(TAG, mac);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_bind);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mCommonTopbar.setUpNavigateMode();
        mCommonTopbar.setTitle("设备绑定");
    }

    @Override
    public void initParams() {
        super.initParams();

        initIntentParams();
    }

    private void initIntentParams() {
        mIntent = getIntent();
        if (mIntent != null) {
            mDeviceMac = mIntent.getStringExtra(TAG);
            mTextViewMac.setText(mDeviceMac);
        }
    }

    @OnClick(R.id.bind_divice_bind)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_divice_bind:
                startBind();
                break;
        }
    }

    private void startBind() {
        String phone = mEditTextPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonToast.showToast(mContext, "手机号不能为空");
            return;
        }

        if (!StringUtils.isPhoneNum(phone)) {
            CommonToast.showToast(mContext, "手机号格式错误");
            return;
        }

        showDialog(getString(R.string.binding));
        new UserApi().setMobile(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {

                hideDialog();
                // isSet:0:表示未设置，则跳转至个人信息设置界面
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    if (jsonObject.has("isSet")) {
                        String isSet = jsonObject.getString("isSet");
                        if ("0".equals(isSet)) {
                            UserInfoActivity.startUserInfoActivity(mContext);
                        } else if ("1".equals(isSet)) {
                            MainActivity.startMainActivity(mContext);
                        }

                        CommSharePreferencesUtil.putString(mContext, "smart_serial", mDeviceMac);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int code, String msg, int id) {
                hideDialog();
                CommonToast.showToast(mContext, msg);
            }
        }, mDeviceMac, phone);

    }
}
