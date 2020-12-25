package com.framelibrary.widget.xpopup.core;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.framelibrary.R;
import com.framelibrary.util.dialog.xpopup.KeyboardUtils;
import com.framelibrary.util.dialog.xpopup.XPopupUtils;
import com.framelibrary.widget.xpopup.PopupDrawerLayout;
import com.framelibrary.widget.xpopup.XPopup;
import com.framelibrary.widget.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.enums.PopupStatus;

/**
 * Description: 带Drawer的弹窗
 * Create by dance, at 2018/12/20
 */
public abstract class DrawerPopupView extends BasePopupView {
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    protected FrameLayout drawerContentContainer;
    PopupDrawerLayout drawerLayout;
    float mFraction = 0f;
    Paint paint = new Paint();
    Rect shadowRect;
    int currColor = Color.TRANSPARENT;
    int defaultColor = Color.TRANSPARENT;
    public DrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerContentContainer = findViewById(R.id.drawerContentContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), drawerContentContainer, false);
        drawerContentContainer.addView(contentView);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_drawer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        drawerLayout.enableShadow = popupInfo.hasShadowBg;
        drawerLayout.isCanClose = popupInfo.isDismissOnTouchOutside;
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                beforeDismiss();
                if (popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.beforeDismiss(DrawerPopupView.this);
                doAfterDismiss();
            }

            @Override
            public void onOpen() {
                DrawerPopupView.super.doAfterShow();
            }

            @Override
            public void onDrag(int x, float fraction, boolean isToLeft) {
                drawerLayout.isDrawStatusBarShadow = popupInfo.hasStatusBarShadow;
                if (popupInfo.xPopupCallback != null)
                    popupInfo.xPopupCallback.onDrag(DrawerPopupView.this,
                            x, fraction, isToLeft);
                mFraction = fraction;
                postInvalidate();
            }
        });
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        drawerLayout.setDrawerPosition(popupInfo.popupPosition == null ? PopupPosition.Left : popupInfo.popupPosition);
        drawerLayout.enableDrag = popupInfo.enableDrag;
        drawerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (popupInfo.hasStatusBarShadow) {
            if (shadowRect == null) {
                shadowRect = new Rect(0, 0, getMeasuredWidth(), XPopupUtils.getStatusBarHeight());
            }
            paint.setColor((Integer) argbEvaluator.evaluate(mFraction, defaultColor, XPopup.statusBarShadowColor));
            canvas.drawRect(shadowRect, paint);
        }
    }

    public void doStatusBarColorTransform(boolean isShow) {
        if (popupInfo.hasStatusBarShadow) {
            //状态栏渐变动画
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator,
                    isShow ? Color.TRANSPARENT : XPopup.statusBarShadowColor,
                    isShow ? XPopup.statusBarShadowColor : Color.TRANSPARENT);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currColor = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.setDuration(XPopup.getAnimationDuration()).start();
        }
    }

    @Override
    protected void doAfterShow() {
    }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
        doStatusBarColorTransform(true);
    }

    @Override
    public void doDismissAnimation() {
    }

    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        clearFocus();
        doStatusBarColorTransform(false);
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
//        super.dismiss();
    }

    @Override
    protected ScaleAlphaAnimator getPopupAnimator() {
        return null;
    }

    @Override
    protected View getTargetSizeView() {
        return getPopupImplView();
    }
}
