package com.smartbracelet.sunny;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.het.common.utils.CommSharePreferencesUtil;
import com.het.common.utils.SystemUtils;

import org.litepal.LitePalApplication;

/**
 * Created by sunny on 15-11-5 下午11:42
 */
public class AppApplication extends Application {
    private static AppApplication mApplication;

    private Handler mainHandle;
//    public static List<WaterBoxRunModel> conditionDataList = new ArrayList<WaterBoxRunModel>();

    public static boolean isFirstStart() {
        return CommSharePreferencesUtil.getBoolean(mApplication.getApplicationContext(), "notFirstStart");
    }

    public static AppApplication getInstance() {
        return mApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initDB();
        String processName = SystemUtils.getProcessName(this,
                android.os.Process.myPid());
        if (processName.equals(this.getPackageName())) {
            initAppContext();
            if (!BuildConfig.DEBUG) {
                catchException();
            }
            startLocation();
        }

    }

    private void initDB() {
        LitePalApplication.initialize(this);
    }

    private void initAppContext() {
        AppContext.getInstance().init(mApplication, AppConstant.APP_ID,
                AppConstant.DEVELOP_KEY);
    }

    private void startLocation() {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    private void catchException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
//                DiskLogger.getLogger().logException(ex, true);
                killMyself();
            }
        });
    }


    private String getAddressName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(name);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();

    }


    public void killMyself() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public Handler getMainHandle() {
        return mainHandle;
    }

    public void setMainHandle(Handler mainHandle) {
        this.mainHandle = mainHandle;
    }
}
