package com.smartbracelet.sunny.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.view.dialog.PromptUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.BreathRateApi;
import com.smartbracelet.sunny.biz.api.HeartRateApi;
import com.smartbracelet.sunny.biz.api.StepApi;
import com.smartbracelet.sunny.biz.api.TiredApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.manager.share.ShareManager;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.BloothPressureEvent;
import com.smartbracelet.sunny.model.event.HeartPressureEvent;
import com.smartbracelet.sunny.model.event.StepEvent;
import com.smartbracelet.sunny.ui.fragment.checkprj.BloothPressureFragment;
import com.smartbracelet.sunny.ui.fragment.checkprj.BreathPressureFragment;
import com.smartbracelet.sunny.ui.fragment.checkprj.HeartRateFragment;
import com.smartbracelet.sunny.ui.fragment.checkprj.MoodFragment;
import com.smartbracelet.sunny.ui.fragment.checkprj.StepFragment;
import com.smartbracelet.sunny.ui.fragment.checkprj.TiredFragment;
import com.smartbracelet.sunny.utils.DateTime;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by sunny on 2015/11/28.
 * 所有检查项目：动态血压，心率，呼吸频率，运动计步，疲劳度等
 * 其分别由各具体的Fragmnet来实现，外层由一个Activity
 */
public class CheckItemActivity extends BaseActivity implements IWeiboHandler.Response {
    public static final String TAG = MyBloothPressureActivity.class.getSimpleName();
    private static Context context;

    @InjectView(R.id.smart_bracelet_topbar)
    RelativeLayout commonTopBar;
    @InjectView(R.id.smart_bracelet_title)
    TextView mTitle;
    @InjectView(R.id.left_image)
    ImageView mBack;
    @InjectView(R.id.smart_bracelet_share)
    ImageView mShare;
    @InjectView(R.id.measure_time_icon)
    ImageView mTimeIcon;
    @InjectView(R.id.smart_bracelet_favorite)
    ImageView mFavorite;

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
    private String mUserId;
    private static final String QUERY_TYPE = "day";

    private BloothPressureFragment mBloothPressureFragment;
    private BreathPressureFragment mBreathPressureFragment;
    private HeartRateFragment mHeartRateFragment;
    private MoodFragment mMoodFragment;
    private StepFragment mStepFragment;
    private TiredFragment mTiredFragment;

    private Intent mIntent;
    private Bundle mBundle;
    private String mFragmentTag;
    private String mTestTime;
    private String mTestValue;

    private long mStartTime;
    private long mEndTime;

    private SunnyHandler mHandler;

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

    public static void startCheckItemActivity(Context context, String tag) {
        Intent targetIntent = new Intent(context, CheckItemActivity.class);
        targetIntent.putExtra(TAG, tag);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_item);
        context = this;

        if (mTencent == null) {
            mTencent = Tencent.createInstance(AppConstant.QQAppID, mContext);
        }
        mShareTools = new ShareManager(mContext);
        mUserManager = UserManager.getInstance();
        mUserId = mUserManager.getUserModel() == null ? "1" : mUserManager.getUserModel().getUserID();
        initFragments();
        initShareParams();
    }

    private void initFragments() {
        mBloothPressureFragment = new BloothPressureFragment();
        mBreathPressureFragment = new BreathPressureFragment();
        mHeartRateFragment = new HeartRateFragment();
        mStepFragment = new StepFragment();
        mMoodFragment = new MoodFragment();
        mTiredFragment = new TiredFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.check_item_content, mBloothPressureFragment)
                .add(R.id.check_item_content, mBreathPressureFragment)
                .add(R.id.check_item_content, mHeartRateFragment)
                .add(R.id.check_item_content, mStepFragment)
                .add(R.id.check_item_content, mTiredFragment)
                .add(R.id.check_item_content, mMoodFragment)
                .commit();

    }

    private void initShareParams() {
        mHandler = new SunnyHandler(mContext);
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

        mIntent = getIntent();
        if (mIntent != null) {
            mFragmentTag = mIntent.getStringExtra(TAG);
            mBundle = mIntent.getExtras();
            if (mBundle != null) {
                if (TextUtils.isEmpty(mFragmentTag)) {
                    mFragmentTag = mBundle.getString(TAG);
                }

                mTestTime = mBundle.getString("testTime");
                mTestValue = mBundle.getString("testValue");
                setValue(mFragmentTag);
            }

            showFragment(mFragmentTag);

        }
    }

    private void setValue(String mFragmentTag) {

        if ("heart".equals(mFragmentTag)) {
            mHeartRateFragment.setTestTime(mTestTime);
            mHeartRateFragment.setTestValue(mTestValue);
        } else if ("blooth".equals(mFragmentTag)) {
            mBloothPressureFragment.setmTestTime(mTestTime);
            mBloothPressureFragment.setmTestValue(mTestValue);
        } else if ("breath".equals(mFragmentTag)) {
            mBreathPressureFragment.setmTestTime(mTestTime);
            mBreathPressureFragment.setmTestValue(mTestValue);
        } else if ("step".equals(mFragmentTag)) {
            mStepFragment.setmTestTime(mTestTime);
            mStepFragment.setmTestValue(mTestValue);
        } else if ("tired".equals(mFragmentTag)) {
            mTiredFragment.setmTestValue(mTestValue);
            mTiredFragment.setmTestTime(mTestTime);
        } else if ("mood".equals(mFragmentTag)) {
            mMoodFragment.setmTestTime(mTestTime);
            mMoodFragment.setmTestValue(mTestValue);
        }
    }

    private void showFragment(String mFragmentTag) {
        if ("blooth".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFFA1192D);
            mTitle.setText(getResources().getString(R.string.user_my_bloothpressure));
            getSupportFragmentManager().beginTransaction().show(mBloothPressureFragment)
                    .hide(mTiredFragment)
                    .hide(mStepFragment)
                    .hide(mHeartRateFragment)
                    .hide(mBreathPressureFragment)
                    .hide(mMoodFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.BLOOTH_PRESSURE));
        } else if ("breath".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFFA9475E);
            mTitle.setText(getResources().getString(R.string.user_my_breath));
            getSupportFragmentManager().beginTransaction().show(mBreathPressureFragment)
                    .hide(mTiredFragment)
                    .hide(mStepFragment)
                    .hide(mHeartRateFragment)
                    .hide(mBloothPressureFragment)
                    .hide(mMoodFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.BREATH));
        } else if ("heart".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFFFF6F3B);
            mTitle.setText(getResources().getString(R.string.user_my_heart_rate));
            getSupportFragmentManager().beginTransaction().show(mHeartRateFragment)
                    .hide(mTiredFragment)
                    .hide(mStepFragment)
                    .hide(mBloothPressureFragment)
                    .hide(mBreathPressureFragment)
                    .hide(mMoodFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.HEART_RATE));
        } else if ("step".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFF22AB63);
            mTitle.setText(getResources().getString(R.string.user_my_step));
            getSupportFragmentManager().beginTransaction().show(mStepFragment)
                    .hide(mTiredFragment)
                    .hide(mBloothPressureFragment)
                    .hide(mHeartRateFragment)
                    .hide(mBreathPressureFragment)
                    .hide(mMoodFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.STEP));
        } else if ("tired".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFFEC1B23);
            mTitle.setText(getResources().getString(R.string.user_my_tired));
            mTimeIcon.setVisibility(View.GONE);
            mFavorite.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().show(mTiredFragment)
                    .hide(mBloothPressureFragment)
                    .hide(mStepFragment)
                    .hide(mHeartRateFragment)
                    .hide(mBreathPressureFragment)
                    .hide(mMoodFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.TIRED));
        } else if ("mood".equals(mFragmentTag)) {
            commonTopBar.setBackgroundColor(0xFFFDA733);
            mTitle.setText(getResources().getString(R.string.user_my_emotion));
            mTimeIcon.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().show(mMoodFragment)
                    .hide(mBloothPressureFragment)
                    .hide(mStepFragment)
                    .hide(mHeartRateFragment)
                    .hide(mBreathPressureFragment)
                    .hide(mTiredFragment)
                    .commit();
            EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.MOOD));
        }
    }


    @Override
    public void initParams() {
    }

    @OnClick({R.id.left_image, R.id.smart_bracelet_share, R.id.measure_time_icon})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                finish();
                break;

            case R.id.measure_time_icon:
                //切换时间
                long currentDate = System.currentTimeMillis();
                mStartTime = DateTime.getFirstDayOfWeek(new Date(currentDate));
                mEndTime = DateTime.getLastDayOfWeek(new Date(currentDate));
                showDialog();
                getDataByTime();
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


    private void getDataByTime() {

        if ("blooth".equals(mFragmentTag)) {
            //动态血压
            getBloothPressureByTime();
        } else if ("heart".equals(mFragmentTag)) {
            //心率
            getHeartPressureByTime();
        } else if ("breath".equals(mFragmentTag)) {
            //呼吸频率
            getBreathPressureByTime();
        } else if ("step".equals(mFragmentTag)) {
            //运动计步
            getStepByTime();
        }

    }

    /**
     * 获取时间段运动计步
     */
    private void getStepByTime() {

        new StepApi().getTimeStep(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                //// TODO: 2015/11/29 所有的获取成功后，都用EventBus来传递数值
                StepEvent stepEvent = new StepEvent();
                stepEvent.setObject(o);
                EventBus.getDefault().post(stepEvent);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                handleFailure(code, msg);
            }
        }, mUserId, QUERY_TYPE, String.valueOf(mStartTime), String.valueOf(mEndTime));
    }

    /**
     * 获取时间段呼吸频率
     */
    private void getBreathPressureByTime() {
        //// TODO: 2015/11/29 这个接口后台还没有加
    }

    /**
     * 获取时间段心率值
     */
    private void getHeartPressureByTime() {

        new HeartRateApi().getHeartPressureByTime(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                HeartPressureEvent stepEvent = new HeartPressureEvent();
                stepEvent.setObject(o);
                EventBus.getDefault().post(stepEvent);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                handleFailure(code, msg);
            }
        }, mUserId, QUERY_TYPE, String.valueOf(mStartTime), String.valueOf(mEndTime));
    }

    /**
     * 获取时间段动态血压值
     */
    private void getBloothPressureByTime() {
        new BloodPressureApi().getTimeBloodPresserue(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                BloothPressureEvent stepEvent = new BloothPressureEvent();
                stepEvent.setObject(o);
                EventBus.getDefault().post(stepEvent);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                handleFailure(code, msg);
            }
        }, mUserId, QUERY_TYPE, String.valueOf(mStartTime), String.valueOf(mEndTime));
    }


    private void handleFailure(int code, String msg) {
        hideDialog();
        CommonToast.showToast(mContext, msg);
    }

    private void startToShare() {

        mShareDialog = new CommonBottomDialog(mContext);
        View view = LayoutInflater.from(CheckItemActivity.this).inflate(
                R.layout.dialog_share, null);
        mItemWechatMoments = (CommImageTextView) view
                .findViewById(R.id.wechatmoments);
        mItemWechat = (CommImageTextView) view.findViewById(R.id.wechat);
        mItemQQ = (CommImageTextView) view.findViewById(R.id.qq);
        mItemQQZone = (CommImageTextView) view.findViewById(R.id.qqzone);
        mItemSinaWeibo = (CommImageTextView) view.findViewById(R.id.sinaweibo);
        mBtnCancelShare = (Button) view.findViewById(R.id.cancel_share);
        mItemWechatMoments.setOnClickListener(CheckItemActivity.this);
        mItemWechat.setOnClickListener(CheckItemActivity.this);
        mItemQQ.setOnClickListener(CheckItemActivity.this);
        mItemQQZone.setOnClickListener(CheckItemActivity.this);
        mItemSinaWeibo.setOnClickListener(CheckItemActivity.this);
        mBtnCancelShare.setOnClickListener(CheckItemActivity.this);
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
                mTencent.shareToQQ(CheckItemActivity.this, params, qqListener);
                break;

            case 2:
                // qqzone
                // 异步的方式分享
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        mTencent.shareToQzone(CheckItemActivity.this, params, qqListener);
                    }
                }).start();
            default:
                break;
        }

    }

    IUiListener qqListener = new IUiListener() {

        @Override
        public void onError(UiError arg0) {

            sendMsg(arg0, 0);

        }

        @Override
        public void onComplete(Object arg0) {
            mShareDialog.dismiss();
            mShareDialog = null;
            sendMsg(null, 1);
        }

        @Override
        public void onCancel() {
            sendMsg(null, 2);

        }
    };

    private void sendMsg(UiError arg0, int i) {
        Message msg = mHandler.obtainMessage();
        msg.what = i;
        if (arg0 != null)
            msg.obj = arg0.errorMessage;
        msg.sendToTarget();
    }

    /**
     * Handler之所以要这样写成static,且里面的变量用WeakReference
     * 是为了防止OOM,起到内在优化的作用
     */
    private static class SunnyHandler extends Handler {

        private WeakReference<Context> contextWeakReference;

        private SunnyHandler(Context context) {
            contextWeakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String info = (String) msg.obj;
                    Toast.makeText(contextWeakReference.get(), info, Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    Toast.makeText(contextWeakReference.get(), contextWeakReference.get().getResources().getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    break;
                default:
                    break;
            }
        }
    }

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
        mWeiboShareAPI.handleWeiboResponse(intent, CheckItemActivity.this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                PromptUtil.showToast(CheckItemActivity.this,
                        R.string.share_sina_success);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                PromptUtil.showToast(CheckItemActivity.this,
                        R.string.share_sina_cancel);
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                PromptUtil.showToast(CheckItemActivity.this,
                        R.string.share_sina_failure);
                break;
        }
    }

}