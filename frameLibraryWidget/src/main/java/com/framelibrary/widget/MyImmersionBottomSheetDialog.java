package com.framelibrary.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import static com.framelibrary.util.eyes.StatusBarUtil.getStatusBarHeight;

/**
 * 支持沉浸式的BottomSheetDialog
 *
 * @author wangweixu
 */

public class MyImmersionBottomSheetDialog extends BottomSheetDialog {

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback; // SheetDialog状态改变回调

    public MyImmersionBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public MyImmersionBottomSheetDialog(@NonNull Context context, BottomSheetBehavior.BottomSheetCallback bottomSheetCallback) {
        super(context);
        this.mBottomSheetCallback = bottomSheetCallback;
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

        initView();
    }

    private void initView() {
        final BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior();

        /**
         * 可以监听回调的状态,onStateChanged监听状态的改变,onSlide是拖拽的回调,onStateChanged可以监听到的回调一共有5种:
         *
         * STATE_DRAGGING: 1 被拖拽状态
         * STATE_SETTLING: 2 拖拽松开之后到达终点位置（collapsed or expanded）前的状态。
         * STATE_EXPANDED: 3 完全展开的状态。
         * STATE_COLLAPSED: 4 折叠关闭状态。可通过app:behavior_peekHeight来设置显示的高度,peekHeight默认是0。
         * STATE_HIDDEN: 5 隐藏状态。默认是false，可通过app:behavior_hideable属性设置。
         * BottomSheets控件配合NestedScrollView、RecyclerView使用效果会更好,合理的使用让APP逼格满满。
         *
         */
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // 这里是bottomSheet状态的改变
                boolean canScrollVertically = bottomSheet.canScrollVertically(-1);

                if (mBottomSheetCallback != null)
                    mBottomSheetCallback.onStateChanged(bottomSheet, newState);


//                bottomSheetBehavior.onNestedPreScroll();
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                // 这里是拖拽中的回调，根据slideOffset可以做一些动画
                if (mBottomSheetCallback != null)
                    mBottomSheetCallback.onSlide(view, v);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // for landscape mode

        final BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior();
//        bottomSheetBehavior.setPeekHeight(730);

        /*if (bottomSheetBehavior != null)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/

    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    // 屏蔽滚动事件
    public BottomSheetBehavior setSlide(boolean isSlide) {
        final BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior();
        bottomSheetBehavior.setHideable(isSlide);//fasle此处设置表示禁止BottomSheetBehavior的执行
        return bottomSheetBehavior;
    }

    // 为true默认不折叠
    public BottomSheetBehavior setSkipCollapsed(boolean isSkipCollapsed) {
        final BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior();
        bottomSheetBehavior.setSkipCollapsed(isSkipCollapsed);//true此处设置表示可以通过这个方法来控制默认不折叠：

        return bottomSheetBehavior;
    }

    /**
     * 获取当前展示的状态值
     * STATE_DRAGGING: 1 被拖拽状态
     * STATE_SETTLING: 2 拖拽松开之后到达终点位置（collapsed or expanded）前的状态。
     * STATE_EXPANDED: 3 完全展开的状态。
     * STATE_COLLAPSED: 4 折叠关闭状态。可通过app:behavior_peekHeight来设置显示的高度,peekHeight默认是0。
     * STATE_HIDDEN: 5 隐藏状态。默认是false，可通过app:behavior_hideable属性设置。
     *
     * @return
     */
    public int getBehaviorState() {
        final BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior();
        int state = bottomSheetBehavior.getState();
        return state;
    }

    @Override
    public void dismiss() {
        //因为dismiss之后当前焦点的EditText无法获取，所以自定义一下
        hideKeyBoard();
        super.dismiss();
    }

    public void hideKeyBoard() {
        View view = null;
        InputMethodManager imm = null;
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow() != null) {
            view = getWindow().getCurrentFocus();
        }
        if (null != view && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private BottomSheetBehavior getBottomSheetBehavior() {
        View view1 = getDelegate().findViewById(com.framelibrary.R.id.design_bottom_sheet);
        return BottomSheetBehavior.from(view1);
    }
}