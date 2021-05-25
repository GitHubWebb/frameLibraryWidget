package com.framelibrary.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.framelibrary.BuildConfig;
import com.framelibrary.util.Constant;
import com.framelibrary.util.DateUtils;
import com.framelibrary.util.logutil.AppDiskLogStrategy;
import com.framelibrary.util.logutil.LoggerUtils;
import com.framelibrary.util.share.DeviceDataShare;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.github.gzuliyujiang.oaid.DeviceID;
import com.hjq.toast.ToastUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
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


    public static FrameLibBaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        context = getApplicationContext();

        //放在其他库初始化前
        /*if (Constant.LOG_PRINT)
            SpiderMan.init(this);*/

        disableAPIDialog();
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
//            e.printStackTrace();
            LoggerUtils.printStackToLog(e);
        }
    }

    public Context getContext() {
        return context;
    }
}
