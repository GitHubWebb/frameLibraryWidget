package com.framelibrary.util.eyes;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.framelibrary.R;

/**
 * Created by 45900 on 2016/12/7/0007.
 */
public class ToolBarUtility {
    /**
     * 改变通知栏颜色
     */
    public static void setActionBar(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(context, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
        tintManager.setStatusBarTintEnabled(true);
        //设置颜色
        tintManager.setStatusBarTintResource(R.color.color_tool_bar);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Context context, boolean on) {
        Window win = ((Activity) context).getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}

