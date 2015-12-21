package com.smartbracelet.sunny.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.comres.view.CommImageTextView;
import com.het.comres.view.dialog.CommonBottomDialog;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.BreathRateApi;
import com.smartbracelet.sunny.biz.api.StepApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.manager.share.ShareManager;
import com.smartbracelet.sunny.model.UserModel;
import com.tencent.tauth.Tencent;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/17.
 * 我的呼吸
 */
public class MyBreathActivity extends BaseActivity {
    private static final String TAG = MyBreathActivity.class.getSimpleName();

    @InjectView(R.id.smart_bracelet_topbar)
    RelativeLayout commonTopBar;
    @InjectView(R.id.smart_bracelet_title)
    TextView mTitle;
    @InjectView(R.id.left_image)
    ImageView mBack;
    @InjectView(R.id.my_breath_icon)
    ImageView mBreathState;
    @InjectView(R.id.rl_item_test_result)
    RelativeLayout mItemTestResult;


    private UserManager mUserManager;
    private UserModel mUserModel;

    private String mQQTitle = "SmartBracelet";
    private String mShareTitle;
    private String mShareSummary;
    private String mShareTargetUrl;
    private String mShareImageUrl;
    private String mShareAppName;
    private CommonBottomDialog mShareDialog;
    private CommImageTextView mItemWechatMoments;
    private CommImageTextView mItemWechat;
    private CommImageTextView mItemQQ;
    private CommImageTextView mItemQQZone;
    private CommImageTextView mItemSinaWeibo;
    private Button mBtnCancelShare;
    // qq,qqzone
    public static Tencent mTencent;
    private Class<?> mCls = null;
    private int shareType;
    private static final int mWeixinShareType = 1;
    private static final int mWeixinFriendShareType = 2;
    private ShareManager mShareTools;

    // sina
    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;

    public static void startMyBreathActivity(Context context) {
        Intent targetIntent = new Intent(context, MyBreathActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath);
    }


    @Override
    public void initTitleBar() {
        commonTopBar.setBackgroundColor(0xFFA9475E);
        mTitle.setText(getResources().getString(R.string.user_my_breath));
    }

    @Override
    public void initParams() {
        mItemTestResult.setVisibility(View.INVISIBLE);
        mBreathState.setImageResource(R.mipmap.icon_my_bloothpressure_step);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(AppConstant.QQAppID, mContext);
        }
        mShareTools = new ShareManager(mContext);
        mUserManager = UserManager.getInstance();
        mUserModel = mUserManager.getUserModel();
        initShareParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTodayHeartRateData();
    }

    private void initShareParams() {
        mShareSummary = getResources().getString(R.string.share_content);
        mShareImageUrl = getResources().getString(R.string.default_share_img);
        mShareTitle = getResources().getString(R.string.share_title);
        mShareTargetUrl = AppConstant.SHARE_TARGET_URL;
        mShareAppName = getResources().getString(R.string.share_title);

        mShareTools.setmShareImagePath(mShareImageUrl);
        mShareTools.setmShareTitle(mShareTitle);
        mShareTools.setmShareSummary(mShareSummary);
    }

    /**
     * 获取当天的心率信息
     */
    private void getTodayHeartRateData() {

        String userId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
        new BreathRateApi().getBreathRate(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {

            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, userId);
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
