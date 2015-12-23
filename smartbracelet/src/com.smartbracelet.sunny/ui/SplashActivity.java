package com.smartbracelet.sunny.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.het.common.utils.LogUtils;
import com.smartbracelet.sunny.AppApplication;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;

/**
 * Created by sunny on 15-11-5 下午11:41
 * 闪屏页
 */
public class SplashActivity extends BaseActivity {
    private static final long ALPHA_DURATION = 1000;
    private View mWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWelcome = View.inflate(this, R.layout.activity_splash, null);
        setContentView(mWelcome);
        beginAlpha();
    }

    private void beginAlpha() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(ALPHA_DURATION);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mWelcome.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void redirectTo() {
        LogUtils.e("notFirstStart:" + AppApplication.isFirstStart());
        if (!AppApplication.isFirstStart()) {
            GuiderActivity.startGuiderActivity(mContext);
        } else {
            MainActivity.startMainActivity(mContext);
        }
        finish();
    }

    @Override
    public void initTitleBar() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initParams() {
        // TODO Auto-generated method stub

    }
}
