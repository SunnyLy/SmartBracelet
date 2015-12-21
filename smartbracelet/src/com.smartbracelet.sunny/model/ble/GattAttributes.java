package com.smartbracelet.sunny.model.ble;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by sunny on 2015/11/21.
 * Gatt属性常量 类
 */
public class GattAttributes {
    private static HashMap<String, String> mAttributes = new HashMap<>();
    //这个是通道UUID，是固定的，是硬件蓝牙协议给定的。蓝牙连接成功后，
    //所有的读，写数据都是在这里面进行的。
    // public static String SMART_BRACELET = "0783b03e-8535-b5a0-7140-a304d2495cb8";
    public static String SMART_BRACELET = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String SMART_BRACELET_CONF = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";


    public static String TEST_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // 通讯协议
        mAttributes.put("0000fff0-0000-1000-8000-00805f9b34fb",
                "SMART_BRACELET_Service");
        //通讯特征字
        mAttributes.put("0000fff4-0000-1000-8000-00805f9b34fb", "SMART_BRACELET_Measurement");

    }

    public static String lookup(String uuid, String defalutName) {
        String name = mAttributes.get(uuid);
        return TextUtils.isEmpty(name) ? defalutName : name;
    }
}
