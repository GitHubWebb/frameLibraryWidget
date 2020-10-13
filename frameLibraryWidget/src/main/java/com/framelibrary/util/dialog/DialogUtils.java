package com.framelibrary.util.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framelibrary.R;
import com.framelibrary.listener.CommonDialogInterface;
import com.framelibrary.util.PermissionCheckUtils;

public class DialogUtils {

    private static Dialog instance;

    /**
     * 弹网络请求dialog 数据加载提示框
     *
     * @param activity libActivity 对象
     */
    public static void showDialog(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (instance == null) {
            instance = showProgressDialog(activity, "正在加载数据...", false);
        }
    }

    /**
     * 弹网络请求dialog
     *
     * @param activity
     * @param message
     */
    public static void showDialog(Activity activity, String message) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
//		return;
        if (instance == null) {
            instance = showProgressDialog(activity, message, false);
        }
    }

    /**
     * 弹网络请求dialog
     *
     * @param activity
     * @param message
     * @param backPress 是否允许返回键返回
     */
    public static void showDialog(Activity activity, String message, boolean backPress) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
//		return;
        if (instance == null) {
            instance = showProgressDialog(activity, message, backPress);
        }
    }

    /**
     * 弹网络请求dialog 文件上传
     *
     * @param activity libActivity 对象
     */
    public static void showDialogUploadImage(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (instance == null) {
            instance = showProgressDialog(activity, "图片上传中...", false);
        }
    }

    /**
     * 弹网络请求dialog 文件上传
     *
     * @param activity libActivity 对象
     */
    public static void showDialogUploadVideo(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (instance == null) {
            instance = showProgressDialog(activity, "视频上传中...", false);
        }
    }

    /**
     * 数据提交提示框
     *
     * @param activity libActivity 对象
     */
    public static void showDialogCommitData(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (instance == null) {
            instance = showProgressDialog(activity, "数据正在提交...", false);
        }
    }

    /**
     * 文件压缩提示框
     *
     * @param activity libActivity 对象
     */
    public static void showDialogCompressFile(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (instance == null) {
            instance = showProgressDialog(activity, "文件正在压缩...", false);
        }
    }


    /**
     * 销毁网络请求dialog
     */
    public static void dismissDialog() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissDialog(instance);
                instance = null;
            }
        }, 100);
    }

    /**
     * dialog是否显示
     *
     * @return 是否显示
     */
    public static boolean isShowing() {
        return instance != null;
    }

    /**
     * 关闭网络请求dialog
     *
     * @param progressDialog 关闭网络请求弹窗
     */
    private static void dismissDialog(Dialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog = null;
            }
        }
    }

    /**
     * 网络请求对话框
     *
     * @param context   上下文对象
     * @param message   消息
     * @param backPress 是否允许返回键返回
     * @return 网络请求对话框对象
     */
    private static Dialog showProgressDialog(Context context, String message, boolean backPress) {
        if (!((Activity) context).isFinishing()) {
/*            int deviceWidth = DeviceUtils.deviceWidth(context);
            int deviceHeight = DeviceUtils.deviceHeight(context);
            ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);


            progressDialog.setMessage(message);
            progressDialog.setCancelable(backPress);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();//一定要用mProgressDialog得到当前界面的参数对象，否则就不是设置ProgressDialog的界面了
            params.alpha = 0.8f;//设置进度条背景透明度
            params.height = deviceHeight / 5;//设置进度条的高度
            params.gravity = Gravity.CENTER;//设置ProgressDialog的重心
            params.width =  deviceWidth ;//设置进度条的宽度
            params.dimAmount = 0f;//设置半透明背景的灰度，范围0~1，系统默认值是0.5，1表示背景完全是黑色的,0表示背景不变暗，和原来的灰度一样
            progressDialog.getWindow().setAttributes(params);//把参数设置给进度条，注意，一定要先show出来才可以再设置，不然就没效果了，*/

            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(com.framelibrary.R.id.dialog_view);// 加载布局
            // main.xml中的ImageView
            ImageView spaceshipImage = (ImageView) v.findViewById(com.framelibrary.R.id.loading_img);
            TextView tipTextView = (TextView) v.findViewById(com.framelibrary.R.id.tipTextView);// 提示文字
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, com.framelibrary.R.anim.load_progress_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            spaceshipImage.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            tipTextView.setText(message);// 设置加载信息

            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

            loadingDialog.setCancelable(backPress);// 不可以用“返回键”取消
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            WindowManager.LayoutParams params = loadingDialog.getWindow().getAttributes();//一定要用mProgressDialog得到当前界面的参数对象，否则就不是设置ProgressDialog的界面了
            params.alpha = 1f;//设置进度条背景透明度
//        params.height = deviceHeight / 5;//设置进度条的高度
            params.gravity = Gravity.CENTER;//设置ProgressDialog的重心
//        params.width =  deviceWidth ;//设置进度条的宽度
            params.dimAmount = 0.3f;//设置半透明背景的灰度，范围0~1，系统默认值是0.5，1表示背景完全是黑色的,0表示背景不变暗，和原来的灰度一样
            loadingDialog.getWindow().setAttributes(params);//把参数设置给进度条，注意，一定要先show出来才可以再设置，不然就没效果了，
            loadingDialog.show();

            return loadingDialog;
        } else {
            return null;
        }
    }

    public static void showPermissionDialog(final Context context, String errmsg) {
        if (context == null || ((Activity) context).isFinishing())
            return;
        final Dialog dialog = new Dialog(context, R.style.DialogCicleStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(context, R.layout.dialog_permission_open, null);
        TextView tvMessage = view.findViewById(com.framelibrary.R.id.tv_error_message);
        tvMessage.setText(errmsg);
        TextView button = view.findViewById(com.framelibrary.R.id.tv_confirm);
        button.setText("去设置");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionCheckUtils.startAppDetailSettingIntent(context);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 通用的提示弹窗
     *
     * @param context
     * @param title
     * @param errmsg
     * @param callback
     */
    public static void showAlertDialog(final Context context, String title, String errmsg, CommonDialogInterface callback) {
        if (context == null || ((Activity) context).isFinishing())
            return;
        final Dialog dialog = new Dialog(context, R.style.common_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        //设置dialog弹窗宽高
        WindowManager.LayoutParams params = window.getAttributes();
        //dialog宽高适应子布局xml
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        //dialog宽高适应屏幕
        //WindowManager manager= getWindowManager();
        //Display display= manager.getDefaultDisplay();
        //params.height= (int) (display.getHeight()* 0.8);
        //params.width= (int) (display.getWidth()* 0.5);
        window.setAttributes(params);
        View view = View.inflate(context, R.layout.dialog_common_alter, null);
        TextView tvTitle = view.findViewById(R.id.tv_alter_title);
        TextView tvMessage = view.findViewById(R.id.tv_alter_content);
        if (!TextUtils.isEmpty(title))
            tvTitle.setText(title);
        else
            tvTitle.setText("提示");
        tvMessage.setText(errmsg);
        TextView buttonNo = view.findViewById(R.id.tv_select_no);
        TextView buttonYes = view.findViewById(R.id.tv_select_yes);
        buttonNo.setText("取消");
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onBottonNoClick();
                dialog.dismiss();
            }
        });
        buttonYes.setText("确定");
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onBottonYesClick();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }
}
