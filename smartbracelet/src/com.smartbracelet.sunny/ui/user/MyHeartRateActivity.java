package com.smartbracelet.sunny.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.TimeUtils;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.HeartRateApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.ui.widget.view.DivisionCircle2;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/17.
 * 我的心率
 */
public class MyHeartRateActivity extends BaseActivity {
    public static final String TAG = MyHeartRateActivity.class.getSimpleName();

    @InjectView(R.id.smart_bracelet_topbar)
    RelativeLayout commonTopBar;
    @InjectView(R.id.smart_bracelet_title)
    TextView mTitle;
    @InjectView(R.id.left_image)
    ImageView mBack;
    @InjectView(R.id.my_breath_icon)
    ImageView mState;
    @InjectView(R.id.my_breath_result_icon)
    ImageView mTestResult;
    @InjectView(R.id.heart_rate_value)
    DivisionCircle2 mHeartRateValue;

    private UserManager mUserManager;
    private UserModel mUserModel;

    private Intent mIntent;
    private Bundle mBundle;

    private String mHeartRate;
    private String mTime;


    public static void startMyHeartRateActivity(Context context, Bundle data) {
        Intent targetIntent = new Intent(context, MyHeartRateActivity.class);
        targetIntent.putExtras(data);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntentParams();
        getTodayHeartRateData();
    }

    private void getIntentParams() {
        mIntent = getIntent();
        if (mIntent != null) {
            mBundle = mIntent.getExtras();
            mHeartRate = mBundle.getString(TAG);
            mTime = mBundle.getString("time");
            freshDivsionView(TextUtils.isEmpty(mHeartRate) ? 0 : Integer.valueOf(mHeartRate));
        }
    }

    private void freshDivsionView(int value) {
        mHeartRateValue.setCurrentValue(value);
        mHeartRateValue.setTime(mTime);
    }

    /**
     * 获取当天的心率信息
     */
    private void getTodayHeartRateData() {

        String userId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
        new HeartRateApi().getHeartBeat(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {

            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, userId);
    }

    @Override
    public void initTitleBar() {
        commonTopBar.setBackgroundColor(0xFFFF6F3B);
        mTitle.setText(getResources().getString(R.string.user_my_heart_rate));
    }

    @Override
    public void initParams() {
        mState.setImageResource(R.mipmap.icon_my_heartrate_state);
        mTestResult.setImageResource(R.mipmap.icon_my_heartrate_result);
        mUserManager = UserManager.getInstance();
        mUserModel = mUserManager.getUserModel();
    }

    @OnClick(R.id.left_image)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                finish();
                break;
        }
    }
}
