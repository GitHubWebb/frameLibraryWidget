package com.framelibrary.util;

import android.app.Application;
import android.widget.Toast;

import com.framelibrary.BuildConfig;
import com.framelibrary.util.Constant;

/**
 * Toast弹出类
 *
 * @author wangwx
 */
public class ToastUtils {
    private static Application mInstance = null;
    private static Toast toast;

    public static void init(Application instance) {
        mInstance = instance;
        com.hjq.toast.ToastUtils.init(instance);
    }

    public static void showLongToast(String msg) {
//        Looper.prepare();
        if (mInstance == null)
            return;

        if (isShowToast()) {
            if (toast == null)
                toast = Toast.makeText(mInstance, msg, Toast.LENGTH_LONG);
            else {
                toast.setText(msg);
                toast.setDuration(Toast.LENGTH_LONG);
            }
            toast.show();
        }
//        Looper.loop();// 进入loop中的循环，查看消息队列

    }

    public static void showToast(String msg) {
//        Looper.prepare();

        if (mInstance == null)
            return;

        if (isShowToast()) {
            if (toast == null)
                toast = Toast.makeText(mInstance, msg, Toast.LENGTH_SHORT);
            else {
                toast.setText(msg);
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.show();
        }
//        Looper.loop();// 进入loop中的循环，查看消息队列

    }

    //不受版本限制 展示给用户信息
    public static void showToastPass(String msg) {
//        Looper.prepare();
        com.hjq.toast.ToastUtils.show(msg);
//        Looper.loop();// 进入loop中的循环，查看消息队列

    }

    //不受版本限制 展示给用户信息
    public static void showLongToastPass(String msg) {
//        Looper.prepare();
        com.hjq.toast.ToastUtils.show(msg);
        /*if (msg.indexOf("userid not found") == -1) {
            com.lljjcoder.style.citylist.Toast.ToastUtils.showLongToast(mInstance,msg);
        }*/
//        Looper.loop();// 进入loop中的循环，查看消息队列

    }


    public static void reset() {
        toast = null;

        Toast toast = com.hjq.toast.ToastUtils.getToast();
        if (toast != null)
            com.hjq.toast.ToastUtils.cancel();
    }


    private static boolean isShowToast() {
        //是否开启日志
//        Constant.LOG_PRINT = BuildConfig.DEBUG;

        return Constant.LOG_PRINT;
    }

}