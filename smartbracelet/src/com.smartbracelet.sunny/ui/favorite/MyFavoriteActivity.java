package com.smartbracelet.sunny.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

import butterknife.InjectView;

/**
 * Created by sunny on 2015/11/6.
 * Annotion:系统设定
 */
public class MyFavoriteActivity extends BaseActivity {
    public static final String TAG = MyFavoriteActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar commonTopBar;

    public static void startSettingActivity(Context context) {
        Intent targetIntent = new Intent(context, MyFavoriteActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
    }

    @Override
    public void initTitleBar() {
        commonTopBar.setUpNavigateMode();
        commonTopBar.setTitle(R.string.my_favorite);
    }
}
