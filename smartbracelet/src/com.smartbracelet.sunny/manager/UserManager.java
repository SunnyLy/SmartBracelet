package com.smartbracelet.sunny.manager;

import android.content.Context;
import android.text.TextUtils;

import com.het.common.callback.ICallback;
import com.het.common.utils.CommSharePreferencesUtil;
import com.het.common.utils.GsonUtil;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.AppContext;
import com.smartbracelet.sunny.biz.api.LoginApi;
import com.smartbracelet.sunny.biz.api.UserApi;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.ChangeUserInfoEvent;
import com.smartbracelet.sunny.model.event.LoginOutEvent;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sunny on 2015/11/20.
 * 用户信息，行为管理 类
 */
public class UserManager {
    private UserModel mUserModel;
    private static UserManager mInstance;
    private Context mContext = AppContext.getInstance().getAppContext();

    public UserManager() {
    }

    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return CommSharePreferencesUtil.getBoolean(mContext, AppConstant.Login.IS_LOGIN);
    }

    /**
     * 登录
     *
     * @param callback 回调
     * @param mobile   绑定手机号
     * @param serial   手环唯一序列号
     * @param mac      手环Mac地址
     */
    public void login(final ICallback callback, String mobile, String serial, String mac) {
        new LoginApi().loginAction(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                callback.onSuccess(o, id);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                callback.onFailure(code, msg, id);
            }
        }, mobile, serial, mac);

    }

    /**
     * 退出登录操作
     */
    public void loginOut() {
        CommSharePreferencesUtil.putBoolean(mContext, AppConstant.Login.IS_LOGIN, false);
        EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.LOGINOUT));
    }

    /**
     * 注册
     *
     * @param callback
     * @param moblie
     * @param pwd
     */
    public void register(ICallback callback, String moblie, String pwd) {

    }

    /**
     * 修改个人信息
     *
     * @param callback
     * @param userModel
     */
    public void setUserInfo(final ICallback callback, UserModel userModel) {
        new UserApi().modifyPersonalInfo(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                callback.onSuccess(o, id);
                EventBus.getDefault().post(new BaseEvent(BaseEvent.EventType.CAHNGE_INFO));
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                callback.onFailure(code, msg, id);
            }
        }, userModel);
    }

    public void getUserInfo(final ICallback callback, String userId) {
        new UserApi().getPersonInfo(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {
                if (!TextUtils.isEmpty(o)) {
                    UserModel userModel = GsonUtil.getGsonInstance().fromJson(o, UserModel.class);
                    setUserMode(userModel);
                }
                callback.onSuccess(o, id);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                callback.onFailure(code, msg, id);
            }
        }, userId);
    }


    /**
     * 用户信息数据更新，存储操作
     *
     * @param userMode
     */
    public void setUserMode(UserModel userMode) {

        if (userMode != null) {
            mUserModel = userMode;
            List<UserModel> userModelList = DataSupport.where("userId= ?", userMode.getUserID()).find(UserModel.class);
            if (userModelList != null && !userModelList.isEmpty()) {
                //更新数据库
                mUserModel.updateAll("userId = ?", userMode.getUserID());
            } else {
                //存储数据库
                mUserModel.save();
            }
        }
    }

    /**
     * 获取UserModel
     *
     * @return
     */
    public UserModel getUserModel() {
        if (mUserModel == null || TextUtils.isEmpty(mUserModel.getUserID())) {
            List<UserModel> userModelList = DataSupport.findAll(UserModel.class);
            if (userModelList != null && userModelList.size() > 0) {
                mUserModel = userModelList.get(0);
            }
        }

        return mUserModel;
    }

}
