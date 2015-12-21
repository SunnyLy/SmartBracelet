package com.smartbracelet.sunny.zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.het.common.utils.LogUtils;
import com.het.common.utils.ToastUtils;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.ui.device.BindActivity;
import com.smartbracelet.sunny.zxing.camera.CameraManager;
import com.smartbracelet.sunny.zxing.decode.DecodeThread;
import com.smartbracelet.sunny.zxing.utils.BeepManager;
import com.smartbracelet.sunny.zxing.utils.CaptureActivityHandler;
import com.smartbracelet.sunny.zxing.utils.InactivityTimer;
import com.smartbracelet.sunny.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 */
public final class MipcaActivityCapture extends BaseActivity implements
        SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = MipcaActivityCapture.class.getSimpleName();

    public static final String QCODE = "account/qrcode/";
    static final String BIND_TYPE_WIFI = "1";
    static final String BIND_TYPE_BLE = "2";

    @InjectView(R.id.common_top_bar)
    CommonTopBar commonTopBar;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;

    private BeepManager beepManager;
    private SurfaceView scanPreview = null;

    private ViewfinderView viewfinderView;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;
    private int eventType = 0;
    private Intent mIntent;

    public static void startMipcaActivityCaptureActivity(Context context, int eventType) {
        Intent targetIntent = new Intent(context, MipcaActivityCapture.class);
        targetIntent.putExtra(TAG, eventType);
        context.startActivity(targetIntent);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_capture);
        scanPreview = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);


        mIntent = getIntent();
        if (mIntent != null) {
            eventType = mIntent.getIntExtra(TAG, 0);
            LogUtils.e("eventType=" + eventType);
        }

    }

    @Override
    public void initParams() {
        super.initParams();
        commonTopBar.setUpNavigateMode();
        commonTopBar.setTitle("扫一扫");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(final Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();


//        // 通过这种方式可以获取到扫描的图片
//	bundle.putInt("width", mCropRect.width());
//	bundle.putInt("height", mCropRect.height());
//	bundle.putString("result", rawResult.getText());
//
//	startActivity(new Intent(MipcaActivityCapture.this, ResultActivity.class)
//		.putExtras(bundle));

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                handleText(rawResult.getText());
            }
        }, 800);
    }

    /**
     * 在这里处理扫描到的结果。。。。。
     *
     * @param resultString
     */
    private void handleText(String resultString) {
        Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(resultString)) {
            return;
        }
        //当绑定成功后把扫到的设备唯一序列号存起来
        BindActivity.startBindActivity(mContext, resultString);
        finish();

        //   restartPreviewAfterDelay(100);//重新扫描


    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager,
                        DecodeThread.ALL_MODE);
            }
            mCropRect = viewfinderView.getFramingRect();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }


    private void startQrcode(String scanResult, final int eventType) {

    }

    private void getBind(final String mac, final String devBrandId, final String devTypeId, final String devSubtypeId) {

    }

    /**
     * 跳转至蓝牙绑定页面
     *
     * @param devId
     * @param roomId
     * @param roomName
     * @param devTypeId
     * @param devSubTypeId
     * @param mac
     */
    private void gotoBindSucActivity(String devId, String roomId, String roomName, String devTypeId, String devSubTypeId, String mac) {
    }


    private String[] getDefaultRoomInfo() {
        String[] result = new String[2];
        return result;
    }


    @OnClick(R.id.left_click)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_click) {
            this.finish();
        }
    }
}