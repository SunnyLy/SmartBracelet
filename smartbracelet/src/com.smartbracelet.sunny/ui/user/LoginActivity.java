package com.smartbracelet.sunny.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

import butterknife.InjectView;

/**
 * Created by sunny on 2015/11/28.
 * 用户登录界面，
 * 在这里，当第一次设置个人资料成功后，要调一次登录接口
 */
public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopbar;

    public static void startLoginActivity(Context context) {
        Intent targetIntent = new Intent(context, LoginActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mCommonTopbar.setTitle("登录");
        mCommonTopbar.setUpNavigateMode();
    }
}
