package com.smartbracelet.sunny.ui.logreg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

/**
 * Created by sunny on 2015/11/21.
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();

    public static void startRegisterActivity(Context context) {
        Intent loginIntent = new Intent(context, RegisterActivity.class);
        context.startActivity(loginIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
