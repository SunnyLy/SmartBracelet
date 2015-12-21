package com.smartbracelet.sunny.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.het.common.callback.ICallback;
import com.het.comres.view.CommImageTextView;
import com.het.comres.view.dialog.CommonBottomDialog;
import com.het.comres.view.dialog.PromptUtil;
import com.het.comres.widget.CommonTopBar;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.manager.share.ShareManager;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/17.
 * 我的血压
 */
public class MyBloothPressureActivity extends BaseActivity implements IWeiboHandler.Response {
    private static final String TAG = MyBloothPressureActivity.class.getSimpleName();

    @InjectView(R.id.smart_bracelet_topbar)
    RelativeLayout commonTopBar;
    @InjectView(R.id.smart_bracelet_title)
    TextView mTitle;
    @InjectView(R.id.left_image)
    ImageView mBack;
    @InjectView(R.id.smart_bracelet_share)
    ImageView mShare;
    @InjectView(R.id.my_breath_icon)
    ImageView mState;
    @InjectView(R.id.my_breath_result_icon)
    ImageView mTestResult;

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

    private UserManager mUserManager;

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

    public static void startMyBloothPressureActivity(Context context) {
        Intent targetIntent = new Intent(context, MyBloothPressureActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooth_pressure);

        if (mTencent == null) {
            mTencent = Tencent.createInstance(AppConstant.QQAppID, mContext);
        }
        mShareTools = new ShareManager(mContext);
        mUserManager = UserManager.getInstance();
        initShareParams();
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

    @Override
    public void initTitleBar() {
        commonTopBar.setBackgroundColor(0xFFA1192D);
        mTitle.setText(getResources().getString(R.string.user_my_bloothpressure));
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTodayBloothPressureData();
    }

    /**
     * 获取当天的血压信息
     */
    private void getTodayBloothPressureData() {

        String userId = mUserManager.getUserModel() == null ? AppConstant.User.USE_ID_DEF :
                mUserManager.getUserModel().getUserID();
        new BloodPressureApi().getBloodPressure(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {

            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, userId);
    }

    @Override
    public void initParams() {
        mState.setImageResource(R.mipmap.icon_my_bloothpressure_step);
        mTestResult.setImageResource(R.mipmap.icon_my_bloothpressure_result);
    }

    @OnClick({R.id.left_image, R.id.smart_bracelet_share})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                finish();
                break;

            case R.id.smart_bracelet_share:
                startToShare();
                break;

            case R.id.wechatmoments:
                mShareDialog.dismiss();
                // 朋友圈
                mShareTools.shareToWeixin(mShareTargetUrl, mShareTitle, mShareImageUrl, mShareSummary, mWeixinFriendShareType);
                break;

            case R.id.wechat:
                mShareDialog.dismiss();
                // 微信好友
                mShareTools.shareToWeixin(mShareTargetUrl, mShareTitle, mShareImageUrl, mShareSummary, mWeixinShareType);
                break;
            case R.id.qq:
                mShareDialog.dismiss();
                // qq
                shareType = 1;
                startShareToQQ(shareType);
                break;
            case R.id.qqzone:
                //QQ空间
                break;

            case R.id.sinaweibo:
                //sinaweibo
                break;

            case R.id.cancel_share:
                if (mShareDialog != null && mShareDialog.isShowing()) {
                    mShareDialog.dismiss();
                }
                break;

        }
    }

    private void startToShare() {

        mShareDialog = new CommonBottomDialog(mContext);
        View view = LayoutInflater.from(MyBloothPressureActivity.this).inflate(
                R.layout.dialog_share, null);
        mItemWechatMoments = (CommImageTextView) view
                .findViewById(R.id.wechatmoments);
        mItemWechat = (CommImageTextView) view.findViewById(R.id.wechat);
        mItemQQ = (CommImageTextView) view.findViewById(R.id.qq);
        mItemQQZone = (CommImageTextView) view.findViewById(R.id.qqzone);
        mItemSinaWeibo = (CommImageTextView) view.findViewById(R.id.sinaweibo);
        mBtnCancelShare = (Button) view.findViewById(R.id.cancel_share);
        mItemWechatMoments.setOnClickListener(this);
        mItemWechat.setOnClickListener(this);
        mItemQQ.setOnClickListener(this);
        mItemQQZone.setOnClickListener(this);
        mItemSinaWeibo.setOnClickListener(this);
        mBtnCancelShare.setOnClickListener(this);
        mShareDialog.setViewContent(view);
        if (mShareDialog != null && !mShareDialog.isShowing())
            mShareDialog.show();

    }

    private void startShareToQQ(int shareType2) {
        final Bundle params = new Bundle();
        if (shareType2 == 1) {
            // QQ
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mQQTitle);
            // 这条分享消息被好友点击后的跳转URL。
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareTargetUrl);
            // 分享的消息摘要，最长50个字
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareSummary);
            // 分享的图片URL
            if (!TextUtils.isEmpty(mShareImageUrl)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareImageUrl);
            }
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mShareAppName);
        } else if (shareType2 == 2) {
            // QQZone
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mQQTitle);
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mShareSummary);
            if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
                // app分享不支持传目标链接
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
                        mShareTargetUrl);
            }
            // 支持传多个imageUrl
            ArrayList<String> imageUrls = new ArrayList<String>();
            if (!TextUtils.isEmpty(mShareImageUrl)) {
                imageUrls.add(mShareImageUrl);
            }
            // String imageUrl = "XXX";
            // params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
                    imageUrls);
        }

        doShareToQQ(params, shareType2);

    }

    private void doShareToQQ(final Bundle params, int shareType2) {
        switch (shareType2) {
            case 1:
                // qq
                mTencent.shareToQQ(this, params, qqListener);
                break;

            case 2:
                // qqzone
                // 异步的方式分享
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        mTencent.shareToQzone(MyBloothPressureActivity.this, params, qqListener);
                    }
                }).start();
            default:
                break;
        }

    }

    IUiListener qqListener = new IUiListener() {

        @Override
        public void onError(UiError arg0) {

            //sendMsg(arg0, 0);

        }

        @Override
        public void onComplete(Object arg0) {
            mShareDialog.dismiss();
            mShareDialog = null;
            // sendMsg(null, 1);
        }

        @Override
        public void onCancel() {
            //sendMsg(null, 2);

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  refreshLogoutButton();
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                PromptUtil.showToast(MyBloothPressureActivity.this,
                        R.string.share_sina_success);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                PromptUtil.showToast(MyBloothPressureActivity.this,
                        R.string.share_sina_cancel);
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                PromptUtil.showToast(MyBloothPressureActivity.this,
                        R.string.share_sina_failure);
                break;
        }
    }

}
