package com.framelibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.framelibrary.util.KeyBoardUtils;

import static com.framelibrary.util.eyes.StatusBarUtil.getStatusBarHeight;

/**
 * 支持沉浸式的BottomSheetDialog
 *
 * @author wangweixu
 */

public class MyImmersionBottomSheetDialog extends BottomSheetDialog {

    public MyImmersionBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public MyImmersionBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    public MyImmersionBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight = getScreenHeight(getContext());
        // 去除沉浸式通知栏使用有黑边
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
//        int dialogHeight = screenHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    @Override
    public void setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    // 屏蔽滚动事件
    public BottomSheetBehavior setSlide(boolean isSlide) {
        View view1 = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
        bottomSheetBehavior.setHideable(isSlide);//fasle此处设置表示禁止BottomSheetBehavior的执行
        return bottomSheetBehavior;
    }

    // 为true默认不折叠
    public BottomSheetBehavior setSkipCollapsed(boolean isSkipCollapsed) {
        View view1 = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
        bottomSheetBehavior.setSkipCollapsed(isSkipCollapsed);//true此处设置表示可以通过这个方法来控制默认不折叠：

        return bottomSheetBehavior;
    }

    @Override
    public void dismiss() {
        //因为dismiss之后当前焦点的EditText无法获取，所以自定义一下
        KeyBoardUtils.closeKeybord();
        super.dismiss();
    }
}