package com.framelibrary.util.dialog;

import android.content.Context;

import com.framelibrary.util.StringUtils;
import com.framelibrary.widget.xpopup.XPopup;
import com.framelibrary.widget.xpopup.impl.LoadingPopupView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    /**
     * 多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
     * <p>
     * 通过静态方法创建ScheduledExecutorService的实例
     */
    private static ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);

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
        showDialog(msg);

        openCancelable(openCancelable);
        isTouchDismiss(openCancelable);
    }

    /**
     * 去除Handler调用, 直接在该方法中进行判断显示
     *
     * @param message
     * @author wangwx
     * <p>
     */
    private static void showDialog(String message) {
        if (loadingPopupView == null)
            init(message);
        else
            loadingPopupView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingPopupView.setTitle(message).show();
                }
            }, 1000);
    }

    /**
     * @param flag
     * @方法说明:允许加载条转动的时候去点击系统返回键
     * @方法名称:openCancelable
     * @返回值:void
     */
    public static void openCancelable(boolean flag) {
        // 延时任务
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (loadingPopupView != null) {
                    loadingPopupView.popupInfo.isDismissOnBackPressed = (flag);
                }
            }
        }, 200, TimeUnit.MILLISECONDS); // 这里2百毫秒

    }

    /**
     * @param isdimiss
     * @方法说明:允许点击对话框触摸消失
     * @方法名称:isTouchDismiss
     * @返回值:void
     */
    public static void isTouchDismiss(boolean isdimiss) {
        // 延时任务
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (loadingPopupView != null) {
                    loadingPopupView.popupInfo.isDismissOnTouchOutside = (isdimiss);
                }
            }
        }, 200, TimeUnit.MILLISECONDS); // 这里2百毫秒
    }

    /**
     * @方法说明:让警告框消失
     * @方法名称:dismiss
     * @返回值:void
     */
    public static void dismiss() {
        dismiss(0);
    }


    // 避免消失过快,闪动,所以延迟关闭
    public static void dismiss(long delay) {
        if (delay < 0) delay = 0;

        // 延时任务
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (loadingPopupView != null) {
                    loadingPopupView.dismiss();
                    loadingPopupView = null;
                    System.gc();
                }
                context = null;
            }
        }, delay, TimeUnit.MILLISECONDS); // 这里2百毫秒

    }


}
