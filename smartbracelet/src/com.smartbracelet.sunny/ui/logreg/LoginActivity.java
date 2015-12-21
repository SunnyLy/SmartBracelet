package com.smartbracelet.sunny.ui.logreg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

/**
 * Created by sunny on 2015/11/21.
 * 登录
 */
public class LoginActivity extends BaseActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    public static void startLoginActivity(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
