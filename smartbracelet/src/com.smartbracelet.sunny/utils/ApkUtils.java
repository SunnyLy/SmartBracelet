package com.smartbracelet.sunny.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
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

    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

}
