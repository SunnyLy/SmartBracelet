package com.smartbracelet.sunny.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 描述：
 * 作者： Sunny
 * 日期： 2015-10-15 18:25
 * 版本： v1.0
 */
public class ApkUtils {

    private static String PATH = Environment.getExternalStorageDirectory() + "/update";
    private static String APK_NAME = "clife.apk";
    public static final String APP_VERSION = "appVersion";
    public static final String APK_URI = "apkUri";
    public static final String IS_INSTALL = "isInstall";

    public static File createApkFile() {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File apkFile = new File(file, APK_NAME);
        if (!apkFile.exists()) {
            try {
                apkFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return apkFile;
    }


}
