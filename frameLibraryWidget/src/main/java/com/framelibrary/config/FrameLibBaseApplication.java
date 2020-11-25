package com.framelibrary.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.framelibrary.BuildConfig;
import com.framelibrary.util.Constant;
import com.framelibrary.util.DateUtils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.hjq.toast.ToastUtils;
import com.simple.spiderman.SpiderMan;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * FrameLibrary application
 *
 * @author wangweixu
 * @Date 2020年10月30日10:58:51
 */
public class FrameLibBaseApplication extends MultiDexApplication {

    protected static FrameLibBaseApplication instance;
    private Context context;
    private DisplayMetrics displayMetrics;
    private SharedPreferences sharedPreferences;
    private static AppManager appManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //是否开启日志
        Constant.LOG_PRINT = BuildConfig.DEBUG;

        //放在其他库初始化前
        if (Constant.LOG_PRINT)
            SpiderMan.init(this);

        instance = this;

        context = getApplicationContext();

        disableAPIDialog();
        initData();

    }

    /**
     * 反射 禁止弹窗 Detected problems with API 弹窗 屏蔽解决方案
     */
    private void disableAPIDialog() {

        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化工具类
     */
    private void initData() {

        //初始化ParallaxBackLayout,目的仿苹果右划关闭页面
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());

        //不允许多次重复点击
        DateUtils.setClickLimit(true);
        displayMetrics = getResources().getDisplayMetrics();
        //初始化Fresco
        Fresco.initialize(instance, ImagePipelineConfigFactory.getImagePipelineConfig(this));

//        Fresco.initialize(this);

        appManager = AppManager.getAppManager();

        ToastUtils.init(instance);

    }

    public static FrameLibBaseApplication getInstance() {
        return instance;
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + "unlock_date", MODE_MULTI_PROCESS);
        }
        return sharedPreferences;
    }

    public Context getContext() {
        return context;
    }

    public static AppManager getAppManager() {
        return appManager;
    }
}
