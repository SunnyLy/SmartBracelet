package com.smartbracelet.sunny.ui.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

import butterknife.InjectView;

/**
 * Created by sunny on 15-11-5 下午11:46
 * 版本更新
 */
public class AboutActivity extends BaseActivity {

    public static final String TAG = AboutActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopBar;


    public static void startAboutActivity(Context context) {
        Intent targetIntent = new Intent(context, AboutActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initTitleBar() {
        mCommonTopBar.setUpNavigateMode();
        mCommonTopBar.setTitle(R.string.set_about);
    }
}
