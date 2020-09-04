package com.framelibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限检测开启工具类
 */

public class PermissionCheckUtils {

    /**
     * 请求文件写权限code
     */
    public static final int REQUEST_CODE_WRITE_FILE_PERMISSION = 100;
    /**
     * 请求文件读权限code
     */
    public static final int REQUEST_CODE_READ_FILE_PERMISSION = 101;
    /**
     * 请求拍照权限code
     */
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 102;
    /**
     * 请求扫描二维码权限code
     */
    public static final int REQUEST_CODE_SCAE_PERMISSION = 103;

    /**
     * 请求视频权限code
     */
    public static final int REQUEST_CODE_VIDEO_PERMISSION = 104;

    /**
     * 打开支付宝权限code
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    private static String[] PERMISSIONS_WRITE_FILE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static String[] PERMISSION_READ_FILE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static String[] PERMISSIONS_SCAN_QCODE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static String[] PERMISSIONS_VIDEO = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    public static boolean checkWritePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkReadPermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 启动系统的WIFI连接界面
     */
    public static void startSystemConnectionActivity(Activity activity) {
        Intent intent = null;
        if (DeviceUtils.getBuildLevel() > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        activity.startActivity(intent);
    }


    /**
     * 跳转到权限设置界面
     */
    public static void startAppDetailSettingIntent(Context context) {
        Toast.makeText(context, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_SHORT).show();
        // viVo 点击设置图标>加速白名单>我的app
        //      点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //      点击权限隐私>自启动管理>我的app
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            context.startActivity(appIntent);
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    //检测是否有支付宝
    public static boolean checkAliPayInstalled(PackageManager pm) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(pm);
        return componentName != null;
    }


    /**
     * 请求Ali 支付
     *
     * @param activity
     */
    public static void requestAliPayPermission(Activity activity) {
        // Here, thisActivity is the current libActivity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);

        } else {
        }
    }

    public static boolean openWritePermission(Activity activity) {
        return activity != null && !activity.isFinishing() && (!isNeedCheckPermission() || checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCheckUtils.PERMISSIONS_WRITE_FILE, REQUEST_CODE_WRITE_FILE_PERMISSION));
    }

    public static boolean openReadPermission(Activity activity) {
        return activity != null && !activity.isFinishing() && (!isNeedCheckPermission() || checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionCheckUtils.PERMISSION_READ_FILE, REQUEST_CODE_READ_FILE_PERMISSION));
    }

    /**
     * 设置屏幕亮度权限
     *
     * @param activity
     */
    public static void openScreenLightPermission(Activity activity) {
        //申请android.permission.WRITE_SETTINGS权限的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果当前平台版本大于23平台
            if (!Settings.System.canWrite(activity)) {
                //如果没有修改系统的权限这请求修改系统的权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivityForResult(intent, 0);
            } else {
                //有了权限，你要做什么呢？具体的动作

            }
        }
    }

    public static boolean openCameraPermission(Activity activity) {
        return activity != null && !activity.isFinishing() && (!isNeedCheckPermission() || checkSelfPermission(activity, Manifest.permission.CAMERA, PermissionCheckUtils.PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA_PERMISSION));
    }

    public static boolean checkScanPermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_SCAN_QCODE, REQUEST_CODE_SCAE_PERMISSION);
    }

    public static boolean checkCameraPermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA_PERMISSION);
    }

    public static boolean checkVideoPermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_VIDEO, REQUEST_CODE_VIDEO_PERMISSION);
    }

    private static boolean checkPermission(Activity activity, String[] strings, int requestCode) {
        if (activity == null || activity.isFinishing())
            return false;
        if (!isNeedCheckPermission())
            return true;
        List<String> permissionList = getPermissionList(activity, strings);
        if (permissionList.isEmpty())
            return true;
        String permissions[] = new String[permissionList.size()];
        permissionList.toArray(permissions);
        if (permissionList.isEmpty())
            return true;
        if (!activity.isFinishing())
            activity.requestPermissions(permissions, requestCode);
        return false;
    }


    private static List<String> getPermissionList(Activity activity, String[] strings) {
        List<String> permissionList = new ArrayList<>();
        for (String string : strings) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, string);
            if (checkPermission != PackageManager.PERMISSION_GRANTED)
                permissionList.add(string);
        }
        return permissionList;
    }


    private static boolean checkSelfPermission(Activity activity, String permission, String[] permissionGroup, int requestCode) {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permission);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionGroup, requestCode);
            return false;
        } else
            return true;
    }

    private static boolean isNeedCheckPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 判断是否是8.0系统,是的话需要获取此权限，判断开没开，没开的话处理未知应用来源权限问题,否则直接安装
     */
    public static void checkIsAndroidO(Activity activity) {
        if (Build.VERSION.SDK_INT >= 26) {
            //PackageManager类中在Android Oreo版本中添加了一个方法：判断是否可以安装未知来源的应用
            boolean b = activity.getPackageManager().canRequestPackageInstalls();
            if (b) {
                Log.e("结果", "开始安装");
                //安装应用的逻辑(写自己的就可以)
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 104);
            }
        } else {
            Log.e("结果", "版本<26，开始安装");
        }
    }

    /**
     * 确认所有的权限是否都已授权
     * @param grantResults  PackageManager.PERMISSION_GRANTED 为允许
     * @return
     */
    public static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
