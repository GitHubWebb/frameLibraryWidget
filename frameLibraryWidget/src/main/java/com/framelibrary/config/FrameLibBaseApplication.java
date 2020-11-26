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
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.hjq.toast.ToastUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.simple.spiderman.SpiderMan;

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
    private DisplayMetrics displayMetrics;
    private SharedPreferences sharedPreferences;
    private static AppManager appManager;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        context = getApplicationContext();

        //是否开启日志
        // 方便引用方在onCreat之前修改值,则该值为true的情况下,不走库的赋值
        if (!Constant.LOG_PRINT)
            Constant.LOG_PRINT = BuildConfig.DEBUG;

        //放在其他库初始化前
        if (Constant.LOG_PRINT)
            SpiderMan.init(this);


        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 是否显示线程，默认显示
                .methodCount(3)         // 显示多少方法 默认 2
                .methodOffset(7)        // 设置方法的偏移量. 默认是 5
                .tag("")                // 已经采用自己的Tag设置方法
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                //返回true，打印日志，返回false ,不打印日志，可调试时返回true,发布时返回false
                return Constant.LOG_PRINT;
            }
        });
        //保存日志到文件中
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separatorChar + "framelib" + File.separatorChar + "logger";

        HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
        ht.start();
        Handler handler = new AppDiskLogStrategy.WriteHandler(ht.getLooper(), folder, AppDiskLogStrategy.MAX_BYTES);
        AppDiskLogStrategy logStrategy = new AppDiskLogStrategy(handler);


        Logger.addLogAdapter(new DiskLogAdapter(CsvFormatStrategy.newBuilder()
                .logStrategy(logStrategy)
                .build()));

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
