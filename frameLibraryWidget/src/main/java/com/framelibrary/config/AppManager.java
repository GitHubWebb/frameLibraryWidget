package com.framelibrary.config;

import android.app.Activity;

import com.framelibrary.util.LogUtils;
import com.framelibrary.util.ObjectUtils;
import com.framelibrary.util.StringUtils;

import java.util.Stack;

/**
 * activity堆栈式管理
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }

        return instance;
    }

    /**
     * 获取指定的Activity 实例
     *
     * @param clsName 注意该值用于是在不能接受到Activity的Module使用
     */
    public static Activity getActivity(String clsName) {
        Class clazz = null;
        try {
            clazz = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LogUtils.printStackToLog(e, "getActivity", clsName);
            return null;
        }
        return getActivity(clazz);
    }

    /**
     * 获取指定的Activity 实例
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (ObjectUtils.isEquals(activity.getClass(), cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return 栈中最后一个 或者 null （当栈中没有数据时）
     */
    public Activity currentActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && getActivity(activity.getClass()) != null) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.isEmpty()) {
            Activity a = activityStack.pop();
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
    }

}
