package com.framelibrary.util.eyes;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.framelibrary.R;
import com.framelibrary.util.DeviceUtils;
import com.framelibrary.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Eyes {
    private static final String TAG = "Eyes";

    public static void setStatusBarColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.translucentStatusBar(activity);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(@NonNull Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
                                                             Toolbar toolbar, @ColorInt int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.setStatusBarColorForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }

    //根据设置颜色显示状态栏 白底黑字
    public static void setStatusBarLightMode(Activity activity, int color) {
        if (Color.TRANSPARENT == color) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Vision24API(activity);
            } else
                setStatusBarLightModeByTRANSPARENT(activity);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
                if (MIUISetStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                    //设置状态栏为指定颜色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                        activity.getWindow().setStatusBarColor(color);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                        //调用修改状态栏颜色的方法
                        setStatusBarColor(activity, color);
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                    activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().setStatusBarColor(color);

                    //fitsSystemWindow 为 false, 不预留系统栏位置.
                    ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                    View mChildView = mContentView.getChildAt(0);
                    if (mChildView != null) {
                        mChildView.setFitsSystemWindows(true);
                        ViewCompat.requestApplyInsets(mChildView);
                    }
                } else {
                    color = activity.getResources().getColor(R.color.color_tool_bar);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                        activity.getWindow().setStatusBarColor(color);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                        //调用修改状态栏颜色的方法
                        setStatusBarColor(activity, color);
                    }
                }
            }
        }
    }

    //设置透明的状态栏 (沉浸式)
    public static void setStatusBarLightModeByTRANSPARENT(Activity activity) {
        initWindow(activity);
    }

    /**
     * android api不同版本的状态栏处理
     *
     * @param activity
     */
    private static void initWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Vision24API(activity);
        } else if (Build.VERSION.SDK_INT >= 21) {
            Vision21API(activity);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Vision19API(activity);
        }
    }

    private static void Vision19API(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DeviceUtils.dp2px(activity, 20));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.TRANSPARENT);
        decorViewGroup.addView(statusBarView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void Vision21API(Activity activity) {
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private static void Vision24API(Activity activity) {
        try {
            Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
            Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
            field.setAccessible(true);
            field.setInt(activity.getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStatusBarLightForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout,
                                                             CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            EyesLollipop.setStatusBarWhiteForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EyesKitKat.setStatusBarWhiteForCollapsingToolbar(activity, appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        }
    }


    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            Class<? extends Window> clazz = activity.getWindow().getClass();
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            return false;
//            e.printStackTrace();
        }

    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            return false;
//            e.printStackTrace();
        }

    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * @return 返回从BitMap得到的颜色
     */
    public static int getColorFromBitmap(View view) {
        Bitmap bitmap = loadBitmapFromView(view);

        return getColorByBitMap(view, bitmap);
    }

    /**
     * 获取view的bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    public static int getColorByBitMap(View view, Bitmap bitmap) {
        LogUtils.I(TAG, "getBitmapFromView(view)" + view.getWidth() + "" + view.getHeight() + "");
        try {
            int pixel = bitmap.getPixel(100, 500);
           /* Button button = new Button(view.getContext());
            button.setBackground(new BitmapDrawable(bitmap));
            ((ViewGroup) view).addView(button, new ViewGroup.LayoutParams(view.getWidth() / 2, view.getHeight() / 2));*/
            //获取颜色
            int redValue = Color.red(pixel);
            int greenValue = Color.green(pixel);
            int blueValue = Color.blue(pixel);
            Log.i(TAG, "【颜色值】 #" + Integer.toHexString(pixel).toUpperCase());
            bitmap.recycle();
            return pixel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.TRANSPARENT;
    }

}
