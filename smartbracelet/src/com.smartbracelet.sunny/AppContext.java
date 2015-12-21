package com.smartbracelet.sunny;

import android.content.Context;

import com.het.common.utils.HttpUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by sunny on 15-11-5 下午11:42
 */
public class AppContext {

    private static Context appContext;
    private static AppContext instance;
    private static String appId;
    private static String developKey;

    public static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public void init(Context context, String appId, String appSecret) {
        String userAgent = "";
        try {
            userAgent = HttpUtils.genUserAgent(context);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            this.init(context, appId, appSecret, userAgent, 4);
        }
    }

    /**
     * @param context
     * @param appId          get from server
     * @param developKey     get from server
     * @param userAgent      callback for token failed, deal logout
     * @param threadPoolSize the threadPoolSize for volley
     */
    public void init(Context context, String appId, String developKey, String userAgent, int threadPoolSize) {
        AppContext.appId = appId;
        AppContext.developKey = developKey;
        appContext = context.getApplicationContext();
        // RequestManager.init(context, userAgent, threadPoolSize);
    }

    public Context getAppContext() {
        return appContext;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getDevelopKey() {
        return developKey;
    }
}
