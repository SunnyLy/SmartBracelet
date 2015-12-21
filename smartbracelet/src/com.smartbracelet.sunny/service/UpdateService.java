package com.smartbracelet.sunny.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.het.common.utils.CommSharePreferencesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.smartbracelet.sunny.AppContext;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.model.event.ApkUpdateEvent;
import com.smartbracelet.sunny.model.AppVersionModel;
import com.smartbracelet.sunny.model.event.PromptDialogEvent;

import java.io.File;
import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;

/**
 * 描述：APK后台更新服务
 * 作者： Sunny
 * 日期： 2015-10-19 09:40
 * 版本： v1.0
 */
public class UpdateService extends Service {

    private Context mContext;

    private HttpUtils mDownloadUtils;
    private HttpHandler mDownloadHandler;
    private String downUrl;
    private String fileName;
    private Uri uri;

    private AppVersionModel mAppVersion;
    private Notification mNotify;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDownloadHandler != null) {
            mDownloadHandler.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initDownloadParams(intent);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        mDownloadHandler = mDownloadUtils.download(downUrl, fileName, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                uri = Uri.fromFile(responseInfo.result);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(uri,
                        "application/vnd.android.package-archive");
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, installIntent, 0);
                mNotify.defaults = Notification.DEFAULT_SOUND;
                //   mNotify.setLatestEventInfo(mContext, "智能手环" + mAppVersion.getVersion() + ".apk", "下载完成", pendingIntent);
                mNotificationManager.notify(1, mNotify);
                CommSharePreferencesUtil.putInt(mContext, "appVersion", Integer.valueOf(mAppVersion.getVersion()));
                CommSharePreferencesUtil.putString(mContext, "apkUri", responseInfo.result.getAbsolutePath());

                EventBus.getDefault().post(new ApkUpdateEvent(100, 100, true));
                //显示全局的安装对话框
                PromptDialogEvent promptDialogEvent = new PromptDialogEvent(getResources().getString(R.string.common_sure));
                promptDialogEvent.setTitle("C-Life");
                promptDialogEvent.setMsg(getResources().getString(R.string.dialog_install_now));
                promptDialogEvent.setPositiveInfo(getResources().getString(R.string.common_sure));
                promptDialogEvent.setOnPositiveListener(onPositiveListener);
                promptDialogEvent.setOnCancelListener(onCancelListener);
                EventBus.getDefault().post(promptDialogEvent);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                EventBus.getDefault().post(new ApkUpdateEvent(e, s));
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                EventBus.getDefault().post(new ApkUpdateEvent((int) current, (int) total, false));
                DecimalFormat df1 = new DecimalFormat("0.00%");
                mNBuilder.setProgress((int) total, (int) current, false);
                mNBuilder.setSmallIcon(R.drawable.ic_launcher);
                mNBuilder.setContentText("下载进度:" + df1.format((double) current / (double) total));
                mNBuilder.setContentTitle("智能手环" + mAppVersion.getVersion() + ".apk");
                mNotify = mNBuilder.build();
                mNotify.flags = Notification.FLAG_AUTO_CANCEL;
                mNotify.icon = R.drawable.ic_launcher;
                mNotificationManager.notify(1, mNotify);
            }
        });
    }

    private DialogInterface.OnClickListener onPositiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            startInstall(uri);
        }
    };

    private DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            CommSharePreferencesUtil.putBoolean(mContext, "isInstall", false);
        }
    };

    private void startInstall(Uri uri) {
        if (uri != null) {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppContext.getInstance().getAppContext().startActivity(intent1);
        }
    }

    private void initDownloadParams(Intent intent) {
        mDownloadUtils = new HttpUtils();
        if (intent != null) {
            downUrl = intent.getStringExtra("downUrl");
            fileName = intent.getStringExtra("fileName");
            mAppVersion = (AppVersionModel) intent.getSerializableExtra("appVersion");
        }

        initNotify();
    }

    private void initNotify() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNBuilder = new NotificationCompat.Builder(mContext);
    }
}
