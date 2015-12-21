package com.smartbracelet.sunny.manager;

import java.util.Stack;

import android.app.Activity;

/**
 * use for manager Activity task
 *
 * @author sunny
 */
public class ScreenManager {
    private static Stack<Activity> activityStack;

    private static ScreenManager instance;

    private ScreenManager() {
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                if (instance == null) {
                    instance = new ScreenManager();
                }
            }
        }
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * @param clazz 跳转到登录Activity
     */
    public void jump2loginActivity(Class<?> clazz) {
        Activity activity = null;
        while (!activityStack.empty() && activityStack.size() > 1) {
            activity = activityStack.lastElement();
            if (!clazz.isAssignableFrom(activity.getClass())) {
                popActivity(activity);
            }
        }
        //BaseBiz.logout();
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
