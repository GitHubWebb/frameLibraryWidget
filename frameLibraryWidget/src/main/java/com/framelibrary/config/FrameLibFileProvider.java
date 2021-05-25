package com.framelibrary.config;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.framelibrary.BuildConfig;
import com.framelibrary.util.ApplicationUtil;
import com.framelibrary.util.Constant;
import com.framelibrary.util.DateUtils;
import com.framelibrary.util.DeviceUtils;
import com.framelibrary.util.logutil.AppDiskLogStrategy;
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

/**
 * <pre>
 *      author:         wangweixu
 *      date:           2021/05/25 15:01:55
 *      description:    使用ContentProvider机制配置SDK初始化
 *      version:        v1.0
 * </pre>
 */
public class FrameLibFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {

        Context application = getContext().getApplicationContext();
        if (application == null)
            application = DeviceUtils.getApplicationByReflect();

        initData((Application) application);
        initLogger(application);

        return true;
    }


    /**
     * 初始化工具类
     *
     * @param application
     */
    private void initData(Application application) {

        //是否开启日志
        // 方便引用方在onCreat之前修改值,则该值为true的情况下,不走库的赋值
        if (!Constant.LOG_PRINT)
            Constant.LOG_PRINT = BuildConfig.DEBUG;

        ApplicationUtil.getInstance().init(application);

        //不允许多次重复点击
        DateUtils.setClickLimit(true);

        // 每次重新启动都删除SP中已缓存的SelectPopData选中数据
        DeviceDataShare.getDeviceDataShare().init(application);
        DeviceDataShare.getDeviceDataShare().removeBySelectPopDataAll();

    }

    private void initSDK(Application application) {

        //初始化ParallaxBackLayout,目的仿苹果右划关闭页面
        application.registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());

        //初始化Fresco
        Fresco.initialize(application, ImagePipelineConfigFactory.getImagePipelineConfig(application));

        //        Fresco.initialize(this);

        ToastUtils.init(application);

        // 在 Application#onCreate 里调用预取。注意：如果不需要调用`getClientId()`及`getOAID()`，请不要调用这个方法
        DeviceID.register(application);
        // 在需要用到设备标识的地方获取
        // 客户端标识原始值：DeviceID.getClientId()
        // 客户端标识统一格式为MD5：DeviceID.getClientIdMD5()
        // 客户端标识统一格式为SHA1：DeviceID.getClientIdSHA1()
        // 开放匿名设备标识原始值：DeviceID.getOAID()
//        tvDeviceIdResult.setText(String.format("ClientID: %s", DeviceID.getClientIdMD5()));
    }

    private void initLogger(Context application) {

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
        String folder = diskPath
                + File.separatorChar
                + "framelib"
                + File.separatorChar
                + application.getPackageName()
                + File.separatorChar
                + "logger";
//                + Constants.MAIN_PROJECT_BUILD_APP_NAME
//                + "logger";

        HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
        ht.start();
        Handler handler = new AppDiskLogStrategy.WriteHandler(ht.getLooper(), folder, AppDiskLogStrategy.MAX_BYTES);
        AppDiskLogStrategy logStrategy = new AppDiskLogStrategy(handler);


        Logger.addLogAdapter(new DiskLogAdapter(CsvFormatStrategy.newBuilder()
                .logStrategy(logStrategy)
                .build()));

    }
}
