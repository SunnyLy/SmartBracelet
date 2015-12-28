package com.smartbracelet.sunny.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.het.comres.view.dialog.PromptUtil;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.ui.fragment.HomeFragment;
import com.smartbracelet.sunny.ui.fragment.SlidingFragment;

import butterknife.InjectView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    /**
     * 记录上次点击返回键的时间，用于控制退出操作
     */
    private long mLastOnKeyBackTime = 0;

    private SlidingFragment mSlidingFramgnet;
    private HomeFragment mHomeFragment;

    public static void startMainActivity(Context context) {
        Intent targetIntent = new Intent(context, MainActivity.class);
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
       // initStatusBarColorTransparent();
    }

    private void initFragment() {
        mSlidingFramgnet = new SlidingFragment();
        mHomeFragment = new HomeFragment();
        mHomeFragment.setDrawerLayout(mDrawerLayout);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.hold_fragment, mHomeFragment).commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initStatusBarColorTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 处理用户点击返回键操作
     */
    private void doExit() {

        // 双击判断的时间门限
        final long THRESHOLD = 2 * 1000;

        // 获取时间差
        long curOnKeyBackTime = System.currentTimeMillis();
        long diffTime = curOnKeyBackTime - mLastOnKeyBackTime;

        // 更新点击时间
        mLastOnKeyBackTime = curOnKeyBackTime;

        // 如果两次点击返回键的时间大于阈值，则弹出提示toast
        if (diffTime > THRESHOLD) {
            PromptUtil.showToast(this, getString(R.string.base_quit_toast_msg));
        } else {// 否则走退出流程
            exit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
            } else
                doExit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
            }, 500); // 不然太卡
        }
    }

}
