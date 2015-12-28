package com.smartbracelet.sunny;

import java.util.SimpleTimeZone;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:全局常量
 */
public class AppConstant {

    public static final String INNER_HOST = "192.168.1.1:8080";
    public static final String OUTER_HOST = "221.238.131.134:8181";
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    public static final String SERVER_URL = "/BraceletSys/";

    public static String HOST = OUTER_HOST;

    public static String REQUEST_URL = HTTP + HOST + SERVER_URL;

    public static String APP_ID = "";
    public static String DEVELOP_KEY = "BB80F668FC9C4127D3C5C7A9B";

    //get
    public static final String LOGIN_ACTION = "loginAction.action";
    public static final String UPDATE_DATA_ACTION = "updateDataAction.action";
    public static final String GET_CURRENT_ACTION = "getCurrentAction.action";
    public static final String GET_MOOD_ACTION = "getMood.action";
    public static final String GET_TIRED_ACTION = "getTired.action";
    public static final String GET_HEART_BEAT_ACTION = "getHeartBeat.action";
    public static final String GET_BLOOR_PRESSURE_ACTION = "getBloodPressure.action";
    public static final String GET_BREATH_RATE_ACTION = "getBreathRate.action";
    public static final String GET_BREATH_RATE_BY_TIME = "getTimeBreathRate.action";
    public static final String GET_STEP_ACTION = "getStep.action";
    public static final String GET_TIME_BLOOR_PRESSURE_ACTION = "getTimeBloodPresserue.action";
    public static final String GET_TIME_STEP_ACTION = "getStep.action";
    public static final String GET_TIME_HEART_PRESSURE_ACTION = "getHeartPressure.action";
    public static final String GET_PERSONAL_ACTION = "getPersonInfo.action";

    //添加收藏
    public static final String ADD_FAVORITE = "addCollect.action";
    //查询收藏
    public static final String QUERY_FAVORITE = "findCollects.action";

    //set
    public static final String SET_BLOOD_PRESSURE_ACTION = "setBloodPressure.action";
    public static final String SET_HEART_PRESSURE_ACTION = "setHearthPressure.action";
    public static final String SET_TIRED_ACTION = "setTiredPressure.action";
    public static final String SET_MESSAGE_NOTIFY_ACTION = "setMessageNotity.action";
    public static final String SET_MOBILE_ACTION = "setMobile.action";
    public static final String SET_PASSWORD_ACTION = "setUserPd.action";

    public static final String MODIFY_PERSONAL_ACTION = "modifyPersonInfo.action";

    public static final String UNBIND_MOBILE_ACTION = "unwarpMobile.action";

    //App版本检查
    public static final String GET_APP_VERSION_ACTION = "getAppVersion.action";


    // 分享-Wechat：已是最新
    public static final String WeiXinAppID = "wx413e6c049cdaaa14";
    public static final String WeiXinAppSecret = "9a876ff21a5d216761c735cda24737c3";

    // sina：因为暂时不做，所以先放个假的。
    public static final String SINA_APP_KEY = "3475229326";
    public static final String SINA_APP_SECRET = "ae17e55e0dd8963661b5c2f23a47a452";
    //QQ：已是最新
    public static final String QQAppID = "1104959645";
    public static final String QQ_APP_KEY = "8qWSwpljIEgbFEeU";

    // 公司官网：已是最新
    public static final String CLIFE_OFFICIAL_URL = " http://www.daorigin.com";
    public static final String SHARE_TARGET_URL = " http://www.daorigin.com";

    public static boolean DEBUG = true;

    public class Login {
        public static final String IS_LOGIN = "isLogin";
    }

    public class Register {
        public static final String IS_REGISTER = "isRegister";
    }

    public class User {
        public static final String USE_ID_DEF = "1";
    }

    /**
     * 返回结果
     */
    public class Result {

        public static final String ERROR_CODE = "errorCode";
        public static final String WARD_CODE = "wardCode";

        public static final String RESULT_SUCCESS = "0000";
    }


}
