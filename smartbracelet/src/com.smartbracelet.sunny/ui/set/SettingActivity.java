package com.smartbracelet.sunny.ui.set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.het.common.callback.ICallback;
import com.het.comres.view.dialog.PromptUtil;
import com.het.comres.view.layout.ItemLinearLayout;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.UserApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.ui.user.UserInfoActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/6.
 * Annotion:系统设定
 */
public class SettingActivity extends BaseActivity {
    public static final String TAG = SettingActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar commonTopBar;
    @InjectView(R.id.set_personal)
    ItemLinearLayout mItemPersonal;
    @InjectView(R.id.set_check_version)
    ItemLinearLayout mItemCheckVersion;
    @InjectView(R.id.set_about)
    ItemLinearLayout mItemAbout;
    @InjectView(R.id.set_close)
    ItemLinearLayout mItemClose;
    @InjectView(R.id.set_unbind)
    ItemLinearLayout mItemUnbind;

    private UserManager mUserManger;
    private UserModel mUserModel;
    private String mUserId;

    public static void startSettingActivity(Context context) {
        Intent targetIntent = new Intent(context, SettingActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initTitleBar() {
        commonTopBar.setUpNavigateMode();
        commonTopBar.setTitle(R.string.system_set);
    }

    @Override
    public void initParams() {
        super.initParams();
        mUserManger = UserManager.getInstance();
        mUserModel = mUserManger.getUserModel();
        mUserId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
    }

    @OnClick({R.id.set_unbind, R.id.set_close, R.id.set_about,
            R.id.set_check_version, R.id.set_personal})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_personal:
                UserInfoActivity.startUserInfoActivity(mContext);
                break;
            case R.id.set_check_version:
                AppUpdateActivity.startAppUpdateActivity(mContext);
                break;
            case R.id.set_about:
                AboutActivity.startAboutActivity(mContext);
                break;
            case R.id.set_close:
                showDialog(getResources().getString(R.string.set_close_confirm), getResources().getString(R.string.base_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.set_unbind:
                showDialog(getResources().getString(R.string.set_unbind_confirm), getResources().getString(R.string.base_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        new UserApi().unbindMobile(new ICallback() {
                            @Override
                            public void onSuccess(Object o, int id) {

                            }

                            @Override
                            public void onFailure(int code, String msg, int id) {

                            }
                        }, mUserId);
                    }
                });
                break;
        }
    }

    private void showDialog(String msg, String postiveBtn, DialogInterface.OnClickListener listener) {
        PromptUtil.showPromptDialog(mContext, null, msg,
                postiveBtn, listener);
    }
}
