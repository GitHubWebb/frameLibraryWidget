package com.framelibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.framelibrary.config.FrameLibBaseApplication;
import com.framelibrary.util.logutil.LoggerUtils;

import java.lang.reflect.Field;

public class ScreenUtils {

    public static int screenWidth;
    public static int screenHeight;
    public static int screenMin;// 宽高中，小的一边
    public static int screenMax;// 宽高中，较大的值
    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;
    public static int dialogWidth;
    public static int statusbarheight;
    public static int navbarheight;
    private static double RATIO = 0.85;

    static {
        init(FrameLibBaseApplication.getInstance().getApplicationContext());
    }

    private ScreenUtils() {
        throw new AssertionError();
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * density + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * scaleDensity + 0.5f);
    }

    public static int getDialogWidth() {
        dialogWidth = (int) (screenMin * RATIO);
        return dialogWidth;
    }

    public static void init(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;

        LoggerUtils.D("screenWidth=" + screenWidth + " screenHeight=" + screenHeight + " density=" + density);
    }

    public static int getDisplayWidth() {
        if (screenWidth == 0) {
            GetInfo(FrameLibBaseApplication.getInstance().getApplicationContext());
        }
        return screenWidth;
    }

    public static int getDisplayHeight() {
        if (screenHeight == 0) {
            GetInfo(FrameLibBaseApplication.getInstance().getApplicationContext());
        }
        return screenHeight;
    }

    public static void GetInfo(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        screenMax = (screenWidth < screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
        statusbarheight = getStatusBarHeight(context);
        navbarheight = getNavBarHeight(context);
        LoggerUtils.D("screenWidth=" + screenWidth + " screenHeight=" + screenHeight + " density=" + density);
    }

    public static int getStatusBarHeight(Context context) {
        if (statusbarheight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusbarheight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusbarheight == 0) {
            statusbarheight = ScreenUtils.dip2px(25);
        }
        return statusbarheight;
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getScreenMin() {
        if (screenMin == 0) {
            GetInfo(FrameLibBaseApplication.getInstance().getBaseContext());
        }
        return screenMin;
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 获取状态栏的高
     */
    public static int getStatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (0 == statusBarHeight) {
            statusBarHeight = getStatusBarHeightByReflection(context);
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeightByReflection(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        // 默认为38，貌似大部分是这样的
        int x, statusBarHeight = 38;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
