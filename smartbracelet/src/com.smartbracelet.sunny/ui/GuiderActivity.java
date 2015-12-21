package com.smartbracelet.sunny.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.het.common.utils.CommSharePreferencesUtil;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.ui.help.HelpActivity;
import com.smartbracelet.sunny.zxing.MipcaActivityCapture;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 15-11-5 下午11:53
 * 引导界面
 */
public class GuiderActivity extends BaseActivity {
    public static final String TAG = GuiderActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopbar;
    @InjectView(R.id.guider_buied)
    Button mBtnBuyed;
    @InjectView(R.id.guider_newer)
    Button mNewer;

    public static void startGuiderActivity(Context context) {
        Intent targetIntent = new Intent(context, GuiderActivity.class);
        context.startActivity(targetIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);
    }

    @Override
    public void initParams() {
        super.initParams();
        mCommonTopbar.setUpNavigateMode();
        mCommonTopbar.setTitle("运营信息");
    }

    @OnClick({R.id.guider_buied, R.id.guider_newer})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guider_newer:
                HelpActivity.startHelpActivity(mContext);
              /*  //方便测试，先跳到主页
                MainActivity.startMainActivity(mContext);
                finish();*/
                break;
            case R.id.guider_buied:
                //打开扫一扫
                MipcaActivityCapture.startMipcaActivityCaptureActivity(mContext, 0);
                break;
        }
    }
}
