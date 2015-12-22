package com.smartbracelet.sunny.manager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommPrompDialog;
import com.het.comres.view.dialog.CommonLoadingDialog;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.callback.IBleConnectCallback;
import com.smartbracelet.sunny.model.event.BleConnectEvent;
import com.smartbracelet.sunny.service.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/11/21.
 * 公共蓝牙管理类
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CommBluetoothManager implements IBleConnectCallback {

    public Context mContext;

    private static final int SCAN_TEIME_DEF = 10000;//扫描10秒后关闭
    private static final int DEVICE_TYPE = 2;//蓝牙设备类型，可能根据它来自动连接
    private static final String DEVICE_NAME = "deviceName";
    private static String DEVICE_ADDRESS = "";

    private static CommBluetoothManager mInstance;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothService;

    public BluetoothLeService getBluetoothService() {
        return mBluetoothService;
    }

    private BluetoothGattCharacteristic mGattCharacteristic;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<>();

    private Handler mHandler;

    private CommonLoadingDialog mLoadingDialog;

    public CommBluetoothManager(Context context) {
        this.mContext = context;
        mLoadingDialog = new CommonLoadingDialog(context);
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mHandler = new Handler();
    }

    public static CommBluetoothManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CommBluetoothManager.class) {
                if (mInstance == null) {
                    mInstance = new CommBluetoothManager(context);
                }
            }
        }
        return mInstance;
    }

    private List<BluetoothDevice> mBluetoothDeviceLists = new ArrayList<>();

    //蓝牙扫描回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (!mBluetoothDeviceLists.contains(device)) {
                mBluetoothDeviceLists.add(device);
                //如果扫描到的设备某个特性跟指定蓝牙设备协议吻合，即可停止扫描，进行自动连接
                int deviceType = device.getType();
                LogUtils.e("BLE", "name:" + device.getName() + ",address:" + device.getAddress() + ",UUID:" + device.getUuids()
                        + ",deviceType:" + device.getType());
                if (deviceType == DEVICE_TYPE) {
                    DEVICE_ADDRESS = device.getAddress();
                    mLoadingDialog.setText("扫描到蓝牙设备，准备自动连接……");
                    CommonToast.showToast(mContext, "扫描到蓝牙设备，准备自动连接……");
                    autoToConnectBLE();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }
        }
    };

    /**
     * 进行蓝牙自动连接
     */
    private void autoToConnectBLE() {
        Intent connectIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(connectIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    //应用service连接状态的监听接口
    //即App与手机系统服务连接的接口
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //app与系统服务连接成功，开始获取蓝牙服务
            mLoadingDialog.setText("连接连接成功");
            LogUtils.e("onServiceConnected", "ServiceConnection连接成功，开始与设备连接:Mac:" + DEVICE_ADDRESS);
            mBluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothService.initialize()) {
                LogUtils.e("Unable to initialize Bluetooth");
            }

            boolean connect = mBluetoothService.connect(DEVICE_ADDRESS, CommBluetoothManager.this);
            LogUtils.e("连接：" + connect + ",mac:" + DEVICE_ADDRESS);
            if (connect) {
                mLoadingDialog.dismiss();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //app与系统服务连接断开
            LogUtils.e("onServiceDisconnected", "ServiceConnection服务断开");
            mBluetoothService = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    };

    /**
     * 检测手机是否支持蓝牙及蓝牙版本
     */
    public void checkBluetoothVersion() {
        if (!mContext.getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            CommonToast.showToast(mContext, mContext.getResources().getString(R.string.ble_not_support));
            return;
        }

        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            CommonToast.showToast(mContext, mContext.getResources().getString(R.string.ble_not_support_4_0));
            return;
        }
    }

    public void openBLE() {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            //强制打开蓝牙
            mBluetoothAdapter.enable();
        }
        scanBleDevices(true);
    }

    public void closeBLE() {
        if (mBluetoothService != null) {
            mBluetoothService.close();
        }
    }

    public void scanBleDevices(boolean isScan) {
        if (mBluetoothAdapter == null) {
            return;
        }

        if (isScan) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    CommonToast.showToast(mContext, "扫描失败！");
                    hidDialog();
                    showReconnectDialog();
                }
            }, SCAN_TEIME_DEF);
            mBluetoothDeviceLists.clear();
            mLoadingDialog.setText("正在扫描蓝牙设备……");
            mLoadingDialog.show();
            CommonToast.showToast(mContext, "正在扫描蓝牙设备……");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 显示重连对话框
     */
    private void showReconnectDialog() {

        CommPrompDialog reConnectDialog = new CommPrompDialog.Builder(mContext)
                .setTitle("扫描失败")
                .setMessage("扫描失败,重新扫描？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("重扫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanBleDevices(true);
                        dialog.dismiss();
                    }
                }).create();
        reConnectDialog.show();
    }

    private void hidDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private void showDialog(String msg) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.setText(msg);
            mLoadingDialog.show();
        }
    }

    public void unBindBleService() {
        if (mBluetoothService != null) {
            mContext.unbindService(mServiceConnection);
            mBluetoothService = null;
        }
    }


    public void onEventMainThread(BleConnectEvent event) {
        if (event != null) {
            //发起重连
            mLoadingDialog.setText("准备重连……");
            CommonToast.showToast(mContext, "准备重连");
            resetConnect();
        }
    }

    /**
     * 重联
     */
    public void resetConnect() {
        try {
            while (mBluetoothService.mConnectedState != 2) {
                mLoadingDialog.setText("正在重连，请稍后……");
                CommonToast.showToast(mContext, "正在重连，请稍后……");
                mHandler.postDelayed(new resetConnectThread(), 1000);
            }
        } catch (Exception e) {
            LogUtils.e("reset Device error!");
            CommonToast.showToast(mContext, "连接失败！");
            mLoadingDialog.dismiss();

        }
    }

    @Override
    public void startConnect() {

        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.setText("开始连接");
            mLoadingDialog.show();
        }
    }

    @Override
    public void notFoundDevice() {

        showDialog("未发现蓝牙设备");
    }

    @Override
    public void connectSuccess() {

        showDialog("蓝牙连接成功");
    }

    @Override
    public void connectFailure() {
        hidDialog();
        CommonToast.showToast(mContext, "蓝牙断开连接");
    }

    /**
     * 重联线程
     *
     * @author Autumn
     */
    public class resetConnectThread implements Runnable {
        private IBleConnectCallback connectCallback;

        public resetConnectThread() {

        }

        public resetConnectThread(IBleConnectCallback callback) {
            this.connectCallback = callback;
        }
        @Override
        public void run() {
            mBluetoothService.connect(DEVICE_ADDRESS, connectCallback);
        }
    }

}
