package com.smartbracelet.sunny.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.het.common.constant.Configs;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.model.ble.GattAttributes;

import java.util.List;
import java.util.UUID;

/**
 * Created by sunny on 2015/11/21.
 * 蓝牙服务
 * 用来管理蓝牙设备的GattService的连接与数据通信
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLeService extends Service {
    private static final String TAG = BluetoothLeService.class.getSimpleName();

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    //action
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    public static final UUID SMART_BRACELET_MEASUREMENT = UUID.fromString(GattAttributes.SMART_BRACELET);

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;
    public int mConnectedState = STATE_CONNECTED;

    private IBinder mBinder = new LocalBinder();

    private BLEConnectSuccess mBleConnectSuccessCallback;

    public void setBleConnectSuccessCallback(BLEConnectSuccess mBleConnectSuccessCallback) {
        this.mBleConnectSuccessCallback = mBleConnectSuccessCallback;
    }

    /**
     * 蓝牙连接成功接口回调
     */
    public interface BLEConnectSuccess {
        //连接成功把特征字传过去
        void readCharacteristic(BluetoothGattCharacteristic characteristic);

        void characteristicChanged(BluetoothGattCharacteristic characteristic);
    }

    //GATT Service的回调
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //当蓝牙连接状态发生改变时触发
            LogUtils.e("onConnectionStateChange", "status:" + newState);
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //连接上
                LogUtils.e("onConnectionStateChange", "GATT连接成功");
                CommonToast.showToast(BluetoothLeService.this, "连接成功");
                intentAction = ACTION_GATT_CONNECTED;
                boolean discover = mBluetoothGatt.discoverServices();
                LogUtils.e("发现GattService:" + discover);
                broadcastUpdate(intentAction);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //disconnected
                LogUtils.e("onConnectionStateChange", "GATT连接断开");
                CommonToast.showToast(BluetoothLeService.this, "连接断开");
                intentAction = ACTION_GATT_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //当有GattService发现时回调
            LogUtils.e("onServicesDiscovered", "status:" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.e("onConnectionStateChange", "发现GATT服务");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //读特征字
            LogUtils.e("onCharacteristicRead", "status:" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mBleConnectSuccessCallback != null) {
                    mBleConnectSuccessCallback.readCharacteristic(characteristic);
                }
                LogUtils.e("onCharacteristicRead", "读取特征字");
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //写特征字
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //当特征字发生改变时回调
            LogUtils.e("onCharacteristicChanged", "characteristic:" + characteristic);
            if (mBleConnectSuccessCallback != null) {
                mBleConnectSuccessCallback.characteristicChanged(characteristic);
            }
            LogUtils.e("onCharacteristicRead", "特征字变化");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        /**
         * 读取描述内容
         */
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status) {
            LogUtils.e("onCharacteristicRead", "读取特征字中的描述");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattCharacteristic characteristic = descriptor
                        .getCharacteristic();
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
    };

    /**
     * 隐式的发送广播
     *
     * @param intentAction
     */
    private void broadcastUpdate(String intentAction) {
        LogUtils.e("发送不带特征字的广播===========" + intentAction);
        final Intent remoteIntent = new Intent(intentAction);
        sendBroadcast(remoteIntent);
    }

    /**
     * 发送带特征字的隐式广播
     *
     * @param intentAction
     * @param gattCharacteristic
     */
    private void broadcastUpdate(String intentAction, BluetoothGattCharacteristic gattCharacteristic) {
        final Intent intent = new Intent(intentAction);
        LogUtils.e("特征字：", gattCharacteristic.getUuid().toString());
        //对指定的特征字进行处理
        if (GattAttributes.TEST_UUID.equals(gattCharacteristic.getUuid())) {
            int flag = gattCharacteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
            }
            final int value = gattCharacteristic.getIntValue(format, 1);
            LogUtils.e("读取数据：", value + "");
            intent.putExtra(EXTRA_DATA, String.valueOf(value));
        } else {

            final byte[] data = gattCharacteristic.getValue();
            //结果中已经把发送过来的HEX16进制转换成了DEC10进制

            if (data != null && data.length > 0) {
                final StringBuilder sb = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    sb.append(String.format("%02X ", byteChar));//%02X:是C语言中表示16进制，02表示不足2位时前面补0，足2位时不受影响
                    LogUtils.e("byte:" + byteChar);
                }
                intent.putExtra(EXTRA_DATA, sb.toString());
                Log.e("data", "************data : " + sb.toString()
                        + "************");
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    /**
     * 在与蓝牙设备通信完成后，要关闭Gatt，释放资源
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;

    }

    /**
     * 实例化蓝牙适配器
     *
     * @return
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                LogUtils.e("Unable to initialize BluetoothManager");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            LogUtils.e("Unable to initialize BluetoothAdapter");
            return false;
        }

        return true;
    }

    /**
     * 连接指定地址的设备
     *
     * @param address
     * @return
     */
    public boolean connect(final String address) {

        if (mBluetoothAdapter == null || address == null) {
            throw new NullPointerException("the BluetoothAdapter or the remote device " +
                    "address may not to be initialized");
        }
        //在连接设备之前，如果给定的mac地址已经存在，则发起重连，
        if (!TextUtils.isEmpty(mBluetoothDeviceAddress) && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            if (mBluetoothGatt.connect()) {
                mConnectedState = STATE_CONNECTED;
                return true;
            } else {
                mConnectedState = STATE_DISCONNECTED;
                return false;
            }
        }

        //开始连接设备
        LogUtils.e("getRemoteDevice:", "正在获取指定Mac的设备对象");
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            LogUtils.e("device not found.");
            return false;
        }

        //设置成自动连接
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        mBluetoothDeviceAddress = address;
        mConnectedState = STATE_CONNECTING;
        return true;
    }

    /**
     * 取消蓝牙连接
     */
    public void disConnect() {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            throw new NullPointerException("the BluetoothAdapter or BluetoothGatt may not " +
                    "be initialized");
        }
        mBluetoothGatt.disconnect();
    }


    /**
     * 读取特征字
     *
     * @param characteristic
     */
    public void readCharacteristics(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            throw new NullPointerException("the BluetoothAdapter or BluetoothGatt may not " +
                    "be initialized");
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }


    /**
     * 对给定的特征字，是否启动Notification
     *
     * @param characteristic
     * @param enable
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enable) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            throw new NullPointerException("the BluetoothAdapter or BluetoothGatt may not " +
                    "be initialized");
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enable);

        LogUtils.e("设备固定UUID:" + SMART_BRACELET_MEASUREMENT + "\n特征字中携带的UUID:" + characteristic.getUuid());
        if (SMART_BRACELET_MEASUREMENT.equals(characteristic.getUuid())) {
            System.out.println("characteristic:writeDescriptor");
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID
                            .fromString(GattAttributes.TEST_UUID));
            if (descriptor != null) {
                LogUtils.e("指定特征字中的描述not为null");
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean writeSuccess = mBluetoothGatt.writeDescriptor(descriptor);
                LogUtils.e("BluetoothGatt写入描述：" + writeSuccess);
            } else {
                LogUtils.e("指定特征字中的描述为null");
            }
        }
    }

    /**
     * 获取已连接上的蓝牙设备的所有BlueGattServer
     *
     * @return
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

}
