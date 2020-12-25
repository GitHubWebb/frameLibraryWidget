package com.framelibrary.util;

import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.framelibrary.config.FrameLibBaseApplication;

/**
 * 软键盘工具类
 *
 * @author wangweixu
 * @Date 2020年11月11日19:05:07
 */
public class KeyBoardUtils {

    /* public boolean dispatchTouchEvent(MotionEvent ev) {
       if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/


    /**
     * 最可靠的打开软键盘
     *
     * @param mEditText 输入框
     */

    public static void openKeybord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            mEditText.requestFocus();
            imm.showSoftInput(mEditText, 0);
        }
    }

    public static void openKeybord2(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * 最方便的关闭软键盘
     *
     * @param view 任何一个存在布局中的view
     */
    public static void closeKeybord(View view) {
        InputMethodManager imm = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void closeKeybord() {
        InputMethodManager imm = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v     view
     * @param event 动作
     * @return 是否显示隐藏输入框
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();

            // 点击EditText的事件，忽略它。
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token 焦点
     */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null)
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示软件盘方法的其中一种
     */
    protected void showSoftInput(final EditText mEditText) {
        if (mEditText != null) {
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            final InputMethodManager im = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null)
                mEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        im.showSoftInput(mEditText, 0);
                    }
                });
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) FrameLibBaseApplication.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.requestFocus();
        if (imm != null)
            imm.showSoftInput(view, 0);
    }
}
