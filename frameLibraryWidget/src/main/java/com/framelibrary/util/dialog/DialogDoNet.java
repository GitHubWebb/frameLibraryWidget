package com.framelibrary.util.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.framelibrary.util.StringUtils;
import com.framelibrary.widget.xpopup.XPopup;
import com.framelibrary.widget.xpopup.impl.LoadingPopupView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 网络加载Dialog,采用Xpopup
 *
 * @author wangweixu
 * @Date 2020年11月13日09:38:19
 */
public class DialogDoNet {
    private static final int START_DIALOG = 0;//开始对话框
    private static final int UPDATE_DIALOG = 1;//更新对话框
    private static final int STOP_DIALOG = 2;//销毁对话框
    private static Context context = null;
    private static LoadingPopupView loadingPopupView = null;

//    private static MaterialDialog sMaterialDialog;

    private static Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            String message = "";
            switch (msg.what) {
                case START_DIALOG:// 启动加载框
                    message = (String) msg.obj;
                    init(message);
                    break;
                case UPDATE_DIALOG:// 更新加载框
                    message = (String) msg.obj;
                    if (loadingPopupView == null)
                        init(message);
                    else
                        loadingPopupView.setTitle(message).show();
                    break;
                case STOP_DIALOG:// 停止加载框
                    if (loadingPopupView != null) {
                        loadingPopupView.dismiss();
                        loadingPopupView = null;
                        System.gc();
                    }
                    context = null;
                    break;
            }
        }
    };

    /**
     * @方法说明:加载控件与布局
     * @方法名称:init
     * @返回值:void
     */
    private static void init(String msg) {
        if (null != context) {
            /*sMaterialDialog = new MaterialDialog.Builder(context)
                    .contentGravity(GravityEnum.CENTER)
                    .content(msg)
                    .cancelable(true)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();*/
            loadingPopupView = (LoadingPopupView) new XPopup.Builder(context)
                    .asLoading(StringUtils.isBlank(msg) ? "正在加载中" : msg)
                    .show();
        }
    }

    /**
     * @param msg
     * @方法说明:更新显示的内容
     * @方法名称:UpdateMsg
     * @返回值:void
     */
    public static void UpdateMsg(String msg) {
        Message message = new Message();
        message.what = UPDATE_DIALOG;
        message.obj = msg;
        handler.sendMessage(message);
    }

    /**
     * @param msg
     * @方法说明:启动对话框
     * @方法名称:startLoad
     * @返回值:void
     */
    public static void startLoad(Context context, int msg) {
        startLoadAndCancelable(context, msg, true);
    }

    /**
     * @param msg
     * @方法说明:启动对话框
     * @方法名称:startLoad
     * @返回值:void
     */
    public static void startLoad(Context context, String msg) {
        startLoadAndCancelable(context, msg, true);
    }

    /**
     * @param msg
     * @param openCancelable 配置是否加载框是否允许返回键关闭,默认true,允许关闭
     * @方法说明:启动对话框
     * @方法名称:startLoad
     * @返回值:void
     */
    public static void startLoadAndCancelable(Context context, int msg, boolean openCancelable) {
        String msgStr = context.getResources().getString(msg);

        startLoadAndCancelable(context, msgStr, openCancelable);
    }

    /**
     * @param msg
     * @param openCancelable 配置是否加载框是否允许返回键和点击关闭,默认true,允许关闭
     * @方法说明:启动对话框
     * @方法名称:startLoad
     * @返回值:void
     */
    public static void startLoadAndCancelable(Context context, String msg, boolean openCancelable) {
        DialogDoNet.context = context;
        if (DialogDoNet.context == null) {
            return;
        }
        if (loadingPopupView != null)
            UpdateMsg(msg);
        else {
            Message mssage = new Message();
            mssage.what = START_DIALOG;
            mssage.obj = msg;
            handler.sendMessage(mssage);
        }

        openCancelable(openCancelable);
        isTouchDismiss(openCancelable);
    }

    /**
     * @param flag
     * @方法说明:允许加载条转动的时候去点击系统返回键
     * @方法名称:openCancelable
     * @返回值:void
     */
    public static void openCancelable(boolean flag) {
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                if (loadingPopupView != null) {
                    loadingPopupView.popupInfo.isDismissOnBackPressed = (flag);
                }
            }
        }, 200);// 这里百毫秒

    }

    /**
     * @param isdimiss
     * @方法说明:允许点击对话框触摸消失
     * @方法名称:isTouchDismiss
     * @返回值:void
     */
    public static void isTouchDismiss(boolean isdimiss) {
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                if (loadingPopupView != null) {
                    loadingPopupView.popupInfo.isDismissOnTouchOutside = (isdimiss);
                }
            }
        }, 200);// 这里百毫秒
    }

    /**
     * @方法说明:让警告框消失
     * @方法名称:dismiss
     * @返回值:void
     */
    public static void dismiss() {
        delayDismiss(0);
    }


    // 避免消失过快,闪动,所以延迟关闭
    public static void delayDismiss(long delay) {
        if (delay < 0) delay = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(STOP_DIALOG);

            }
        }, delay);
    }


}
