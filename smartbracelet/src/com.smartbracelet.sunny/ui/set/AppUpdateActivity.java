package com.smartbracelet.sunny.ui.set;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.comres.view.dialog.CommPrompDialog;
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.view.layout.ItemLinearLayout;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.AppVersionApi;
import com.smartbracelet.sunny.model.AppVersionModel;
import com.smartbracelet.sunny.service.UpdateService;
import com.smartbracelet.sunny.utils.ApkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.InjectView;

/**
 * Created by sunny on 15-11-5 下午11:46
 * 版本更新
 */
public class AppUpdateActivity extends BaseActivity {

    public static final String TAG = AppUpdateActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopBar;
    @InjectView(R.id.about_version_app)
    ItemLinearLayout mItemAppVersion;
    @InjectView(R.id.about_update)
    TextView mNewVersionInfo;
    private TextView mTextViewAppVersionName;

    private int mAppVersionServer;//App服务器上的版本
    private int mAppVersionLocal;//App本地版本
    private String mAppVersonName;
    private String mAppUrl;//下载地址
    private String isUpdate;//1:强制更新 0：否
    private String mUpdateMsg;//更新日志
    private AppVersionModel mAppModel = new AppVersionModel();

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNBuilder;
    private Notification mNotify;

    private Intent updateService;


    private CommPrompDialog mUpdateDialog;
    private CommPrompDialog.Builder mBuilder;


    public static void startAppUpdateActivity(Context context) {
        Intent targetIntent = new Intent(context, AppUpdateActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_version);

        checkAppVersion();
    }

    @Override
    public void initParams() {
        super.initParams();
        initView();
        initDialog();
        getAppVesrion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateService != null) {
            stopService(updateService);
        }
    }

    private void initDialog() {
        mBuilder = new CommPrompDialog.Builder(mContext);
        updateService = new Intent(mContext, UpdateService.class);
    }

    private void initView() {
        mTextViewAppVersionName = (TextView) ((RelativeLayout) (mItemAppVersion.getChildAt(0)))
                .getChildAt(3);
    }

    private void getAppVesrion() {
        PackageInfo packageInfo = null;
        PackageManager packageManager = getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (packageInfo != null) {
                mAppVersionLocal = packageInfo.versionCode;
                mAppVersonName = packageInfo.versionName;
                mTextViewAppVersionName.setText(mAppVersonName);

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查App版本
     */
    private void checkAppVersion() {
        showDialog(getString(R.string.set_updating));
        new AppVersionApi().getAppVersion(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {
                hideDialog();
                parseJson(o);

            }

            @Override
            public void onFailure(int code, String msg, int id) {
                hideDialog();
                CommonToast.showToast(mContext, msg);

            }
        });

    }

    /**
     * 解析后台返回的json数据
     *
     * @param json
     */
    private void parseJson(String json) {

        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                mAppVersionServer = jsonObject.has("version") ?
                        Integer.valueOf(jsonObject.getString("version")) : 1;
                mAppUrl = jsonObject.has("url") ? jsonObject.getString("url") : "";
                mUpdateMsg = jsonObject.has("msg") ? jsonObject.getString("msg") : "";
                isUpdate = jsonObject.has("isUpdate") ? jsonObject.getString("isUpdate") : "";

                mAppModel.setIsUpdate(isUpdate);
                mAppModel.setMsg(mUpdateMsg);
                mAppModel.setUrl(mAppUrl);
                mAppModel.setVersion(mAppVersionServer + "");
                freshUI();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 刷新UI
     */
    private void freshUI() {

        if (mAppVersionServer > mAppVersionLocal) {
            //有新版本
            mNewVersionInfo.setText("系统检测到有新版本：" + mAppVersionServer);
            //// TODO: 2015/11/28 根据isUpdate来实现是否强制更新
            mBuilder.setTitle("版本更新")
                    .setMessage(mUpdateMsg);
            if ("1".equals(isUpdate)) {
                //强制更新
                mBuilder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //// TODO: 2015/11/28 开始后台下载
                        startUpdate(mAppUrl);
                    }
                });
            } else {
                //否
                mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startUpdate(mAppUrl);
                    }
                });
            }
            mUpdateDialog = mBuilder.create();
            mUpdateDialog.setCanceledOnTouchOutside("1".equals(isUpdate) ? false : true);
            mUpdateDialog.show();
        }

    }

    private void startUpdate(String mAppUrl) {
        File apkFile = ApkUtils.createApkFile();
        updateService.putExtra("appVersion", mAppModel);
        updateService.putExtra("fileName", apkFile.getAbsolutePath());
        updateService.putExtra("downUrl", mAppModel.getUrl());
        mContext.startService(updateService);
    }

    @Override
    public void initTitleBar() {
        mCommonTopBar.setUpNavigateMode();
        mCommonTopBar.setTitle(R.string.set_check_version);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
            if ("1".equals(isUpdate)) {
                return;
            }
            mUpdateDialog.dismiss();
        }
    }
}
