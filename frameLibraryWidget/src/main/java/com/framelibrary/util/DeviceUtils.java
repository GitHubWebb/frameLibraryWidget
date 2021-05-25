package com.framelibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.framelibrary.config.FrameLibBaseApplication;
import com.framelibrary.util.logutil.LoggerUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 设备工具类
 *
 * @author wangweixu
 * @Date 2017年08月26日
 */
public class DeviceUtils {

    private static final String fileAddressMac = "/sys/class/net/wlan0/address";
    private static final String TAG = "DeviceUtil";
    private static long lastTimeStamp;
    private static long lastTotalRxBytes;

    public static int getColorRes(String colorString) {
        try {
            return Color.parseColor(colorString);
        } catch (IllegalArgumentException e) {
            return Color.WHITE; // 如果获取的颜色值不对,则返回白色
        }
    }

    public static int getColorRes(int color) {
        return getColorRes(FrameLibBaseApplication.getInstance().getContext(), color);
    }

    public static int getColorRes(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    public static Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(FrameLibBaseApplication.getInstance().getContext(), drawable);
    }

    @NonNull
    public static String getStringRes(int str) {
        return FrameLibBaseApplication.getInstance().getContext().getResources().getString(str);
    }

    public static <T extends View> T findViewById(View v, int id) {
        if (v == null || id == 0)
            return null;

        return (T) v.findViewById(id);
    }

    /**
     * 判定是不是竖屏
     *
     * @return
     */
    public static boolean isPortrait() {
        int mOrientation = FrameLibBaseApplication.getInstance().getContext().getResources().getConfiguration().orientation;
        return mOrientation != Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 沉浸式状态栏
     */
    public static void setWindowImmersiveState(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /*
     * 获取状态栏的高度
     * */
    public static int getWindowStateHeight(Context context) {
        int statusBarHeight1 = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    //根据包名判断app 是否安装
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    StringUtils.isBlank(packageName) ? context.getPackageName() : packageName, 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <pre>
     *      author:         wangweixu
     *      date:           2021/05/25 15:42:33
     *      description:    利用反射获取Application
     *      version:        v1.0
     * </pre>
     */
    public static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("you should init first");
            }
            return (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException("you should init first");
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context, String packageName) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    StringUtils.isBlank(packageName) ? context.getPackageName() : packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    public static Boolean checkIsVisible(Context context, View view) {
        // 如果已经加载了，判断广告view是否显示出来，然后曝光
        int screenWidth = getScreenMetrics(context).x;
        int screenHeight = getScreenMetrics(context).y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        //view已不在屏幕可见区域;
        return view.getLocalVisibleRect(rect);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * 将传入的数字转换为百分比
     *
     * @param totalNumber
     * @param number
     */
    public static int convertPercent(int totalNumber, int number) {
        LoggerUtils.I("totalNumber===" + totalNumber + ",number==" + number);
        if (number == 0) {
            return 0;
        }
        double tempTotalNumber = totalNumber;
        double tempNumber = number;
        double progress = tempNumber / tempTotalNumber;
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        String progressStr = numberFormat.format(progress);
        return Integer.parseInt(progressStr.replace("%", ""));
    }

    /**
     * 根据列名获取列号
     *
     * @param columnName
     * @return
     */
    public static String getColumnCode(String columnName) {
        return CabinetBoxConvertUtil.getCabinetCoulmeNo(columnName);
    }

    /**
     * 根据列号获取列名
     *
     * @return
     */
    public static String getColumeName(String cabinetCoulmeNo) {
        if (cabinetCoulmeNo.equals("00")) {
            return "主柜";
        }
        return CabinetBoxConvertUtil.convertDefaultColume(cabinetCoulmeNo);
    }

    /**
     * dp 转化为 px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return dp2px(FrameLibBaseApplication.getInstance(), dpValue);
    }

    /**
     * dp 转化为 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null)
            return 0;

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转化为 dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        return px2dp(FrameLibBaseApplication.getInstance(), pxValue);
    }

    /**
     * px 转化为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        if (context == null)
            return 0;

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(float pxVal) {
        return px2sp(FrameLibBaseApplication.getInstance(), pxVal);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        if (context == null) return 0;
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(float spVal) {
        return sp2px(FrameLibBaseApplication.getInstance(), spVal);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        if (context == null) return 0;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        if (context != null)
            return context.getResources().getDisplayMetrics().widthPixels;
        return 0;
    }

    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        if (context == null)
            return 0;
        else
            return context.getResources().getDisplayMetrics().heightPixels;

    }

    /**
     * SD卡判断
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 是否有网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     * @return
     */
    public static String createAPPFolder(String appName, Application application) {
        return createAPPFolder(appName, application, null);
    }

    /**
     * 创建App文件夹
     *
     * @param appName
     * @param application
     * @param folderName
     * @return
     */
    public static String createAPPFolder(String appName, Application application, String folderName) {
        File root = Environment.getExternalStorageDirectory();
        File folder;
        /**
         * 如果存在SD卡
         */
        if (DeviceUtils.isSDCardAvailable() && root != null) {
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } else {
            /**
             * 不存在SD卡，就放到缓存文件夹内
             */
            root = application.getCacheDir();
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (folderName != null) {
            folder = new File(folder, folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return folder.getAbsolutePath();
    }

    /**
     * 通过Uri找到File
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uri2File(Activity context, Uri uri) {
        File file;
        String[] project = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = context.getContentResolver().query(uri, project, null, null, null);
        if (actualImageCursor != null) {
            int actual_image_column_index = actualImageCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            String img_path = actualImageCursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } else {
            file = new File(uri.getPath());
        }
        if (actualImageCursor != null) actualImageCursor.close();
        return file;
    }


    /**
     * 获取application中指定的meta-data。本例中，调用方法时key就是UMENG_CHANNEL
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager == null)
                return null;

            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null)
                return null;

            if (applicationInfo.metaData == null)
                return null;

            Bundle bundle = applicationInfo.metaData;

            resultData = StringUtils.isBlank(bundle.get(key).toString()) ? null : bundle.get(key).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 设置application中指定的meta-data。本例中，调用方法时key就是UMENG_CHANNEL
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static void setMetaData(Context ctx, String key, String value) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return;
        }

        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager == null)
                return;

            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null)
                return;

            if (applicationInfo.metaData == null)
                return;

            Bundle bundle = applicationInfo.metaData;

            bundle.putString(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置AndroidManifest.xml里 meta的值
     *
     * @param context
     * @return
     */
    public static void setMetaData(Context context, Map<String, Object> appMetaMap) {
        Iterator<Map.Entry<String, Object>> iterator = appMetaMap.entrySet().iterator();
        while (iterator.hasNext()) {
            setMetaData(context, String.valueOf(iterator.next()), appMetaMap.get(iterator.next()).toString());
        }

    }

    // 从Ip获取Mac地址
    public static String getMacAddressFromIp(Context context) {
        String mac_s = "";
        StringBuilder buf = new StringBuilder();
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = ne.getHardwareAddress();
            for (byte b : mac) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac_s = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_s;
    }

    // 获取IP地址
    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    // int IP 转字符串IP
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LoggerUtils.printStackToLog(ex);
        }
        return "0.0.0.0";
    }


    /**
     * 启动系统的WIFI连接界面
     */
    public static void startSystemConnectionActivity(Activity activity) {
        Intent intent = null;
        if (getBuildLevel() > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        activity.startActivity(intent);
    }

    /**
     * 单位为kb/s
     *
     * @param context
     * @return
     */
    public static long getSpeed(Context context) {
        long nowTotalRxBytes = getTotalRxBytes(context);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    public static long getTotalRxBytes(Context context) {
        if (context == null)
            return 0;
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }

    /**
     * 判断是否存在虚拟按键
     *
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = FrameLibBaseApplication.getInstance().getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            LoggerUtils.printStackToLog(e);
        }
        return hasNavigationBar;
    }

/*	private static PowerManager pm;
    private static WakeLock wakeLock;
	*//**
     * 保持CPU不睡眠
     *//*
    public static void keepCpuAlive(Context context,Class<?> clazz) {
		LoggerUtils.E(TAG, "keepCpuAlive==="+clazz.getName());
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,clazz.getName());
		wakeLock.acquire();
	}

	*//**
     * 释放wakeLock对象
     *//*
    public static void destoryWakeLock() {
		if(wakeLock !=null){
			wakeLock.release();
			wakeLock = null;
		}
	}*/
}
