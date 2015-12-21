package com.smartbracelet.sunny.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.CommSharePreferencesUtil;
import com.het.common.utils.LogUtils;
import com.het.common.utils.TimeUtils;
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.adapter.HomeAdapter;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.LoginApi;
import com.smartbracelet.sunny.biz.api.SynthesisApi;
import com.smartbracelet.sunny.biz.biz.UploadThread;
import com.smartbracelet.sunny.manager.CommBluetoothManager;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.CheckProjModel;
import com.smartbracelet.sunny.model.TestModel;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.model.ble.GattAttributes;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.BleConnectEvent;
import com.smartbracelet.sunny.service.BluetoothLeService;
import com.smartbracelet.sunny.ui.user.CheckItemActivity;
import com.smartbracelet.sunny.utils.CRC8;
import com.smartbracelet.sunny.utils.Json2Model;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by sunny on 2015/11/6.
 * 首页Fragment
 * 这个界面，获取数据的接口有点乱，
 * 真不想去跟他死扣了，
 * 就自己在代码里面处理了，所以代码会比较乱。。。。。。
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopBar;
    @InjectView(R.id.home_lv)
    ListView mListView;

    @InjectView(R.id.item_home_blooth_pressure)
    RelativeLayout mItemBloothPressure;
    @InjectView(R.id.item_home_breath_rate)
    RelativeLayout mItemBreathRate;
    @InjectView(R.id.item_home_heart_rate)
    RelativeLayout mItemHeartRate;
    @InjectView(R.id.item_home_mood)
    RelativeLayout mItemMood;
    @InjectView(R.id.item_home_tired)
    RelativeLayout mItemTired;
    @InjectView(R.id.item_home_step)
    RelativeLayout mItemStep;

    //time
    @InjectView(R.id.home_breath_rate_time)
    TextView mBreathRateTime;
    @InjectView(R.id.home_heart_rate_time)
    TextView mHeartRateTime;
    @InjectView(R.id.home_tired_time)
    TextView mTiredTime;
    @InjectView(R.id.home_mood_time)
    TextView mMoodTime;
    @InjectView(R.id.item_time)
    TextView mBloothPressureTime;
    //value
    @InjectView(R.id.home_heart_proj_value)
    TextView mHeartValue;
    @InjectView(R.id.home_mood_proj_value)
    TextView mMoodValue;
    @InjectView(R.id.home_tired_proj_value)
    TextView mTiredValue;
    @InjectView(R.id.home_breath_rate_proj_value)
    TextView mBreathRateValue;
    @InjectView(R.id.item_proj_value)
    TextView mBloothPressureValue;


    private HomeAdapter mHomeAdapter;
    private DrawerLayout mDrawerLayout;
    private UserManager mUserManager;
    private UserModel mUserModel;

    private CommBluetoothManager mBluetoothManager;
    private boolean isSDKSupported = false;//SDK是否支持蓝牙4.0，默认false

    private List<CheckProjModel> checkProjModelList = new ArrayList<>();
    private String bloothPressureMax = null;
    private String bloothPressureMin = null;
    private String bloothPressureType = "bloothPressure";
    private String bloothPressureTime = null;
    private String mElectronic = "";

    private String heartBeatValue = null;
    private String breathPressureValue = null;
    private String tiredPressureValue = null;
    private String moodValue = null;
    private String stepValue = null;
    private String stepPressureValue = null;
    private String userId;
    private String mSerial = "";//手环唯一序列号
    private String[] buffByts = new String[15];//因为数据是按包来传，8个字节一个包，所以要等待数据传完后，再进行解析
    private static final String FRAME_HEAD = "7E";//帧头
    private static final String FRAME_END = "7E";//帧尾
    private int receiveLenth = 0;//已经接收到的数据包的长度
    private static final String DATA_CHECK = "2E";//校验位值，是变化的。要去校验来匹配


    //定时任务
    private Executor mUploadExecutor;
    private ScheduledExecutorService mExecutorService;
    private UploadThread mUploadThread;
    private static final int DEF_UPLOAD_TIME = 5000;

    public void setDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.inject(this, parent);
        EventBus.getDefault().register(this);
        initTopbar(parent);
        initParams();
        getData();
        openBLE();
        return parent;
    }

    private void initParams() {
        mSerial = CommSharePreferencesUtil.getString(mContext, "smart_serial");
        mUserManager = UserManager.getInstance();
        mUserModel = mUserManager.getUserModel();
        userId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothManager = CommBluetoothManager.getInstance(mContext);
            isSDKSupported = true;
        } else {
            isSDKSupported = false;
        }

        mExecutorService = Executors.newSingleThreadScheduledExecutor();
    }


    private void getData() {
        checkProjModelList.clear();
        getTodayAllData();
    }

    /**
     * 获取当天所有数据
     */
    private void getTodayAllData() {

        boolean isLogin = mUserManager.isLogin();
        if (!isLogin) {
            String userId = mUserModel == null ? "1" : mUserModel.getUserID();
            new SynthesisApi().getCurrentAction(new ICallback<String>() {
                @Override
                public void onSuccess(String o, int id) {
                    //// TODO: 2015/11/21 获取当天所有数据成功，刷新界面
                    try {
                        List<CheckProjModel> lists = Json2Model.parseJsonToList(o, "result", CheckProjModel.class);
                        freshUI(lists);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int code, String msg, int id) {
                    CommonToast.showToast(mContext, msg);

                }
            }, userId);
        }

    }

    private void freshUI(List<CheckProjModel> modelList) throws ParseException {
        checkProjModelList.clear();
        if (modelList != null && modelList.size() > 0) {
            for (int i = 0; i < modelList.size(); i++) {
                if (i == 0) {
                    heartBeatValue = modelList.get(0).getValue();
                } else if (i == 1) {
                    stepPressureValue = modelList.get(1).getValue();
                } else if (i == 2) {
                    bloothPressureMax = modelList.get(2).getValue();
                } else if (i == 3) {
                    bloothPressureMin = modelList.get(3).getValue();
                } else if (i == 4) {
                    breathPressureValue = modelList.get(4).getValue();
                } else if (i == 5) {
                    tiredPressureValue = modelList.get(5).getValue();
                } else if (i == 6) {
                    stepValue = modelList.get(6).getValue();
                }
                bloothPressureTime = modelList.get(i).getTime();
                bloothPressureTime = TimeUtils.getWeekName(Long.valueOf(bloothPressureTime));
            }
        }

        mBloothPressureTime.setText(bloothPressureTime);
        mBloothPressureValue.setText(bloothPressureMax + "/" + bloothPressureMin);

        mHeartRateTime.setText(bloothPressureTime);
        mHeartValue.setText(heartBeatValue);

        mBreathRateTime.setText(bloothPressureTime);
        mBreathRateValue.setText(breathPressureValue);

        mTiredTime.setText(bloothPressureTime);
        mTiredValue.setText(tiredPressureValue);

        mMoodTime.setText(bloothPressureTime);
        mMoodValue.setText(stepValue);


    }

    private void initTopbar(View parent) {
        mCommonTopBar.setTitle("SmartBracelet");
        mCommonTopBar.setUpNavigate(R.mipmap.icon_three_line, this);
        mCommonTopBar.setUpImgOption(R.mipmap.img_battery_red);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext.registerReceiver(mGattUpdateReceiver, getBLEIntentFilter());
    }

    private IntentFilter getBLEIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

    @Override
    public void onResume() {
        super.onResume();
        //用登录接口来测试返回值，完后，删掉
        // testLogin();
    }

    private void testLogin() {
        new LoginApi().loginAction(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {

                TestModel testModel = Json2Model.parseJson(o, TestModel.class);
                LogUtils.i("测试结果：" + testModel.toString());
            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, "15914077946", "sh2015102300000001", "00:00:11:EF:AA:01");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBluetoothManager != null)
            mBluetoothManager.scanBleDevices(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothManager != null) {
            mBluetoothManager.unBindBleService();
            mBluetoothManager.closeBLE();
        }
        mContext.unregisterReceiver(mGattUpdateReceiver);
    }

    /**
     * 打开蓝牙，自动搜索指定设备
     */
    private void openBLE() {
        if (isSDKSupported) {
            mBluetoothManager.checkBluetoothVersion();
            mBluetoothManager.openBLE();
        } else {
            CommonToast.showToast(mContext, mContext.getResources().getString(R.string.ble_not_support_4_0));
        }

    }

    @OnClick({R.id.left_click, R.id.item_home_mood, R.id.item_home_tired,
            R.id.item_home_breath_rate, R.id.item_home_blooth_pressure,
            R.id.item_home_heart_rate, R.id.item_home_step})
    @Override
    public void onClick(View v) {
        Intent targetIntent = new Intent(mContext, CheckItemActivity.class);
        Bundle mBundle = new Bundle();
        switch (v.getId()) {
            case R.id.left_click:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.item_home_blooth_pressure:
                mBundle.putString(CheckItemActivity.TAG, "blooth");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", bloothPressureMax + "/" + bloothPressureMin);
                break;
            case R.id.item_home_breath_rate:
                mBundle.putString(CheckItemActivity.TAG, "breath");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", breathPressureValue);
                break;
            case R.id.item_home_heart_rate:
                mBundle.putString(CheckItemActivity.TAG, "heart");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", heartBeatValue);
                break;
            case R.id.item_home_tired:
                mBundle.putString(CheckItemActivity.TAG, "tired");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", tiredPressureValue);
                break;
            case R.id.item_home_mood:
                mBundle.putString(CheckItemActivity.TAG, "mood");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", stepValue);
                break;
            case R.id.item_home_step:
                mBundle.putString(CheckItemActivity.TAG, "step");
                mBundle.putString("testTime", bloothPressureTime);
                mBundle.putString("testValue", stepValue);
                break;

        }
        if (v.getId() != R.id.left_click) {
            targetIntent.putExtras(mBundle);
            mContext.startActivity(targetIntent);
        }

    }


    /**
     * 用于接收蓝牙设备返回数据的广播
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                LogUtils.e(TAG, "GATT连接成功");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                LogUtils.e(TAG, "GATT连接断开");
                EventBus.getDefault().post(new BleConnectEvent());
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                LogUtils.e(TAG, "发现GATT_SERVICES");
                displayGattServices(mBluetoothManager.getBluetoothService()
                        .getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                LogUtils.e("收到数据：", data);
                handleData(data);

            }
        }
    };

    /**
     * 对数据进行一个解析
     * 根据协议文档定义的2个字节为一个测量项的值
     * 下面以计步鞋传送的数据为例
     * eg:7E 2E 01 00 01 01 FF FF 01 01 01 E2 AA 08 7E
     * 除帧头，帧尾，校验位外，每4个字节代表一个数据包值
     * 2E 01 00 01:表示计步值
     * 01 FF FF 01：表示心率
     * 01 01 E2 AA：表示血压
     * 7E:帧头
     * 2E(高位) 01(低位) 00(计步参数) 01(计步包序号)
     *
     * @param data 7E 00 00 20 49 00 00 20 49 20 20 20 20 20 7E
     */
    private void handleData(String data) {

        if (TextUtils.isEmpty(data)) {
            LogUtils.e("接收到的数据不能为空");
            return;
        }
        try {
            String[] values = data.split(" ");
            //先判断帧头
            String frameHead = values[0];
            String frameEnded = values[values.length - 1];
            if (frameHead.equals(FRAME_HEAD) && !frameEnded.equals(FRAME_END)) {
                if (frameHead.equals(FRAME_HEAD)) {
                    //第一个包
                    receiveLenth = values.length;
                    for (int i = 0; i < values.length; i++) {
                        buffByts[i] = values[i];
                    }
                } else {
                    //不是第一个包，那就再判断帧尾
                    String frameEnd = values[values.length - 1];
                    String checkByte = values[values.length - 2];//检验位的值
                    if (frameEnd.equals(FRAME_END)) {
                        //数据包发送完毕
                        for (int j = 0; j < values.length; j++) {
                            buffByts[receiveLenth + 1 + j] = values[j];
                        }

                        StringBuilder dataStringBuilder = new StringBuilder();
                        for (String string : buffByts) {
                            dataStringBuilder.append(string);
                        }
                        boolean result = getByteString(dataStringBuilder.toString(), checkByte);
                        if (result) {
                            //开始解析数据，并上传
                            anlyUploadData(dataStringBuilder.toString());
                        }
                    }
                }

            } else if (frameHead.equals(FRAME_HEAD) && frameEnded.equals(FRAME_END)) {
                String checkByte = values[values.length - 2];//检验位的值
                //数据包发送完毕
                for (int j = 0; j < values.length; j++) {
                    buffByts[j] = values[j];
                }

                StringBuilder dataStringBuilder = new StringBuilder();
                for (String string : buffByts) {
                    dataStringBuilder.append(string + " ");
                }
                  /*  boolean result = getByteString(dataStringBuilder.toString(), checkByte);
                    if (result) {
                        //开始解析数据，并上传
                        anlyUploadData(dataStringBuilder.toString());
                    }*/

                //开始解析数据，并上传
                anlyUploadData(dataStringBuilder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析数据并上传
     *
     * @param data
     */
    private void anlyUploadData(String data) {

        String[] values = data.split(" ");

        //低位在前，高位在后
        //计步
        int step = Integer.parseInt(values[1], 16) * 256//低位
                + Integer.valueOf(values[2], 16);//高位

        //心率
        int heartRate = Integer.parseInt(values[6], 16) * 256
                + Integer.parseInt(values[5], 16);

        //取出电量
        int electronic = Integer.parseInt(values[7], 16);

        //血压
        //因为血压最大值不会超过一位，所以高位，低位分别表示收缩压与收张压
        int bloothMax = Integer.parseInt(values[9], 16);
        int bloothMin = Integer.parseInt(values[10], 16);


        bloothPressureMax = String.valueOf(bloothMax);
        bloothPressureMin = String.valueOf(bloothMin);
        heartBeatValue = String.valueOf(heartRate);
        stepPressureValue = String.valueOf(step);
        mElectronic = String.valueOf(electronic);

        LogUtils.e("最大血压：" + bloothPressureMax + "\n最低血压:" + bloothPressureMin + "\n心率:" + heartBeatValue + "\n计步:" + stepPressureValue + "\n电量:" + mElectronic);

        //刷新UI，显示电量信息
        freshElectronicUI(electronic);


        try

        {
            //把数据同步至服务器
            synchronized (this) {
                mUploadThread = new UploadThread(userId,
                        mSerial, bloothPressureMax, bloothPressureMin,
                        stepPressureValue, heartBeatValue, uploadHanlder);
                mExecutorService.schedule(mUploadThread, DEF_UPLOAD_TIME, TimeUnit.MILLISECONDS);
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

    }

    private void freshElectronicUI(int electronic) {
        LogUtils.e("电量：" + electronic);
        if (electronic == 0) {
            mCommonTopBar.setUpImgOption(R.mipmap.img_battery_bg_disable);
        } else if (electronic > 0 && electronic <= 20) {
            mCommonTopBar.setUpImgOption(R.mipmap.img_scan_top);
        } else {
            mCommonTopBar.setUpImgOption(R.mipmap.img_battery_red);
        }
    }


    /**
     * 获取字节数组
     *
     * @param toString
     * @param checkStr 设备传送过来的数据中的校验值
     */
    private boolean getByteString(String toString, String checkStr) {
        if (TextUtils.isEmpty(toString)) {
            return false;
        }

        try {
            String[] orginal = toString.split(" ");
            StringBuilder orginalStr = new StringBuilder();
            for (String str : orginal) {
                orginalStr.append(str);
            }
            toString = orginalStr.toString();
            byte[] datas = toString.getBytes("UTF-8");
            int checkLen = CRC8.compute(datas);
            String checkResult = String.format("%02X", checkLen);
            //检验成功
//校验失败
            return checkStr.equals(checkResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return false;
        }
    }

    private void displayGattServices(List<BluetoothGattService> supportedGattServices) {
        if (supportedGattServices == null)
            return;
        //循环迭代服务
        for (BluetoothGattService gattService : supportedGattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                gattCharacteristics = gattService
                        .getCharacteristics();
            }
            //循环迭代
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                LogUtils.e("循环遍历GATT服务，获取指定通道UUID============");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (GattAttributes.SMART_BRACELET.equals(gattCharacteristic.getUuid().toString())) {
                        LogUtils.e("与指定的UUID吻合，调setCharacteristicNotification============");
                        mBluetoothManager.getBluetoothService()
                                .setCharacteristicNotification(
                                        gattCharacteristic, true);
                    }
                }
            }
        }
    }

    /**
     * 用来处理数据上传的结果
     */
    private static Handler uploadHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //成功
                    LogUtils.e("数据上传成功。。。。");
                    break;
                case 1:
                    //失败
                    LogUtils.e("数据上传失败。。。。");
                    break;
            }
        }
    };


    /**
     * 来控制首页item项的显示 与隐藏
     *
     * @param baseEvent
     */
    public void onEventMainThread(BaseEvent baseEvent) {
        if (baseEvent == null) {
            return;
        }
        BaseEvent.EventType type = baseEvent.getEventType();
        switch (type) {
            case BLOOTH_PRESSURE_GONE:
                mItemBloothPressure.setVisibility(View.GONE);
                break;
            case BLOOTH_PRESSURE_VISIBLE:
                mItemBloothPressure.setVisibility(View.VISIBLE);
                break;
            case BREATH_PRESSURE_GONE:
                mItemBreathRate.setVisibility(View.GONE);
                break;
            case BREATH_PRESSURE_VISIBLE:
                mItemBreathRate.setVisibility(View.VISIBLE);
                break;
            case HEART_RATE_GONE:
                mItemHeartRate.setVisibility(View.GONE);
                break;
            case HEART_RATE_VISIBLE:
                mItemHeartRate.setVisibility(View.VISIBLE);
                break;
            case TIRED_GONE:
                mItemTired.setVisibility(View.GONE);
                break;
            case TIRED_VISIBLE:
                mItemTired.setVisibility(View.VISIBLE);
                break;
            case MOOD_GONE:
                mItemMood.setVisibility(View.GONE);
                break;
            case MOOD_VISIBLE:
                mItemMood.setVisibility(View.VISIBLE);
                break;
        }
    }
}
