package com.framelibrary.widget.xpopup.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.framelibrary.util.dialog.xpopup.KeyboardUtils;
import com.framelibrary.util.dialog.xpopup.XPopupUtils;
import com.framelibrary.widget.xpopup.XPopup;
import com.framelibrary.widget.xpopup.animator.BlurAnimator;
import com.framelibrary.widget.xpopup.animator.EmptyAnimator;
import com.framelibrary.widget.xpopup.animator.PopupAnimator;
import com.framelibrary.widget.xpopup.animator.ScaleAlphaAnimator;
import com.framelibrary.widget.xpopup.animator.ScrollScaleAnimator;
import com.framelibrary.widget.xpopup.animator.ShadowBgAnimator;
import com.framelibrary.widget.xpopup.animator.TranslateAlphaAnimator;
import com.framelibrary.widget.xpopup.animator.TranslateAnimator;
import com.framelibrary.widget.xpopup.impl.FullScreenPopupView;
import com.framelibrary.widget.xpopup.impl.PartShadowPopupView;
import com.framelibrary.widget.xpopup.navbar.OnNavigationBarListener;
import com.lxj.xpopup.enums.PopupStatus;

import java.util.ArrayList;
import java.util.Stack;

import static com.framelibrary.widget.xpopup.animator.PopupAnimation.NoAnimation;

/**
 * Description: 弹窗基类
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends FrameLayout implements OnNavigationBarListener, LifecycleObserver {
    private static Stack<BasePopupView> stack = new Stack<>(); //静态存储所有弹窗对象
    public PopupInfo popupInfo;
    protected PopupAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    protected BlurAnimator blurAnimator;
    private int touchSlop;
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    protected boolean isCreated = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    public BasePopupView(@NonNull Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        shadowBgAnimator = new ShadowBgAnimator(this);
        //  添加Popup窗体内容View
        View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        contentView.setAlpha(0);
        addView(contentView);
    }

    /**
     * 执行初始化
     */
    protected void init() {
//        NavigationBarObserver.getInstance().register(getContext());
//        NavigationBarObserver.getInstance().addOnNavigationBarListener(this);

        //1. 初始化Popup
        if (BasePopupView.this instanceof AttachPopupView) {
            initPopupContent();
        } else if (!isCreated) {
            initPopupContent();
        }
        //apply size dynamic
        if (!(BasePopupView.this instanceof FullScreenPopupView)) {
            XPopupUtils.setWidthHeight(getTargetSizeView(),
                    (getMaxWidth() != 0 && getPopupWidth() > getMaxWidth()) ? getMaxWidth() : getPopupWidth(),
                    (getMaxHeight() != 0 && getPopupHeight() > getMaxHeight()) ? getMaxHeight() : getPopupHeight()
            );
        }
        if (!isCreated) {
            isCreated = true;
            onCreate();
            if (popupInfo.xPopupCallback != null) popupInfo.xPopupCallback.onCreated(this);
        }
        handler.postDelayed(initTask, 50);
    }

    private Runnable initTask = new Runnable() {
        @Override
        public void run() {
            // 如果有导航栏，则不能覆盖导航栏，判断各种屏幕方向
            if (dialog == null || dialog.getWindow() == null) return;
            applySize(XPopupUtils.isNavBarVisible(dialog.getWindow()));
            getPopupContentView().setAlpha(1f);

            //2. 收集动画执行器
            collectAnimator();

            if (popupInfo.xPopupCallback != null) popupInfo.xPopupCallback.beforeShow(BasePopupView.this);
            focusAndProcessBackPress();

            //3. 执行动画
            doShowAnimation();

            doAfterShow();
        }
    };

    private boolean hasMoveUp = false;

    private void collectAnimator() {
        if (BasePopupView.this instanceof AttachPopupView && !(BasePopupView.this instanceof PartShadowPopupView)) {
            if (popupInfo.customAnimator != null) {
                popupContentAnimator = popupInfo.customAnimator;
                popupContentAnimator.targetView = getPopupContentView();
            } else {
                // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                popupContentAnimator = genAnimatorByPopupType();
                if (popupContentAnimator == null) {
                    popupContentAnimator = getPopupAnimator();
                }
            }

            //3. 初始化动画执行器
            if (popupInfo.hasShadowBg) {
                shadowBgAnimator.initAnimator();
            }
            if (popupInfo.hasBlurBg) {
                blurAnimator = new BlurAnimator(this);
                blurAnimator.hasShadowBg = popupInfo.hasShadowBg;
                blurAnimator.decorBitmap = XPopupUtils.view2Bitmap((XPopupUtils.context2Activity(this)).getWindow().getDecorView());
                blurAnimator.initAnimator();
            }
            if (popupContentAnimator != null) {
                popupContentAnimator.initAnimator();
            }
        } else if (popupContentAnimator == null) {
            // 优先使用自定义的动画器
            if (popupInfo.customAnimator != null) {
                popupContentAnimator = popupInfo.customAnimator;
                popupContentAnimator.targetView = getPopupContentView();
            } else {
                // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                popupContentAnimator = genAnimatorByPopupType();
                if (popupContentAnimator == null) {
                    popupContentAnimator = getPopupAnimator();
                }
            }

            //3. 初始化动画执行器
            if (popupInfo.hasShadowBg) {
                shadowBgAnimator.initAnimator();
            }
            if (popupInfo.hasBlurBg) {
                blurAnimator = new BlurAnimator(this);
                blurAnimator.hasShadowBg = popupInfo.hasShadowBg;
                blurAnimator.decorBitmap = XPopupUtils.view2Bitmap((XPopupUtils.context2Activity(this)).getWindow().getDecorView());
                blurAnimator.initAnimator();
            }
            if (popupContentAnimator != null) {
                popupContentAnimator.initAnimator();
            }
        }
    }

    @Override
    public void onNavigationBarChange(boolean show) {
        if (!show) {
            applyFull();
        } else {
            applySize(true);
        }
    }

    protected void applyFull() {
    }

    protected void applySize(boolean isShowNavBar) {

    }

    public BasePopupView show() {
        Activity activity = XPopupUtils.context2Activity(this);
        if (activity == null || activity.isFinishing()) {
            return BasePopupView.this;
        }
        if (popupStatus == PopupStatus.Showing) return BasePopupView.this;
        popupStatus = PopupStatus.Showing;
        if (dialog != null && dialog.isShowing()) return BasePopupView.this;
        handler.post(attachTask);
        return this;
    }

    private Runnable attachTask = new Runnable() {
        @Override
        public void run() {
            // 1. add PopupView to its dialog.
            attachDialog();
            if (getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().addObserver(BasePopupView.this);
            }
            //2. 注册对话框监听器
            popupInfo.decorView = (ViewGroup) dialog.getWindow().getDecorView();
            KeyboardUtils.registerSoftInputChangedListener(dialog.getWindow(), BasePopupView.this, new KeyboardUtils.OnSoftInputChangedListener() {
                @Override
                public void onSoftInputChanged(int height) {
                    if (popupInfo != null && popupInfo.xPopupCallback != null) {
                        popupInfo.xPopupCallback.onKeyBoardStateChanged(BasePopupView.this, height);
                    }
                    if (height == 0) { // 说明对话框隐藏
                        XPopupUtils.moveDown(BasePopupView.this);
                        hasMoveUp = false;
                    } else {
                        //when show keyboard, move up
                        //全屏弹窗特殊处理，等show之后再移动
                        if (BasePopupView.this instanceof FullScreenPopupView && popupStatus == PopupStatus.Showing) {
                            return;
                        }
                        if (BasePopupView.this instanceof PartShadowPopupView && popupStatus == PopupStatus.Showing) {
                            return;
                        }
                        XPopupUtils.moveUpToKeyboard(height, BasePopupView.this);
                        hasMoveUp = true;
                    }
                }
            });

            // 3. do init，game start.
            init();
        }
    };

    public FullScreenDialog dialog;

    private void attachDialog() {
        if (dialog == null) {
            dialog = new FullScreenDialog(getContext())
                    .setContent(this);
        }
        dialog.show();
    }

    protected void doAfterShow() {
        handler.removeCallbacks(doAfterShowTask);
        handler.postDelayed(doAfterShowTask, getAnimationDuration());
    }

    private Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Show;
            onShow();
//            focusAndProcessBackPress();
            if (popupInfo != null && popupInfo.xPopupCallback != null)
                popupInfo.xPopupCallback.onShow(BasePopupView.this);
            //再次检测移动距离
            if (dialog != null) {
                if (XPopupUtils.getDecorViewInvisibleHeight(dialog.getWindow()) > 0 && !hasMoveUp) {
                    XPopupUtils.moveUpToKeyboard(XPopupUtils.getDecorViewInvisibleHeight(dialog.getWindow()), BasePopupView.this);
                }
            }
        }
    };

    private ShowSoftInputTask showSoftInputTask;

    public void focusAndProcessBackPress() {
        if (popupInfo != null && popupInfo.isRequestFocus) {
            setFocusableInTouchMode(true);
            requestFocus();
            if (!stack.contains(this)) stack.push(BasePopupView.this);
            // 此处焦点可能被内部的EditText抢走，也需要给EditText也设置返回按下监听
            setOnKeyListener(new BackPressListener());
            if (!popupInfo.autoFocusEditText) showSoftInput(this);

            //let all EditText can process back pressed.
            ArrayList<EditText> list = new ArrayList<>();
            XPopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
            for (int i = 0; i < list.size(); i++) {
                final EditText et = list.get(i);
                et.setOnKeyListener(new BackPressListener());
                if (i == 0 && popupInfo.autoFocusEditText) {
                    et.setFocusable(true);
                    et.setFocusableInTouchMode(true);
                    et.requestFocus();
                    showSoftInput(et);
                }
            }
        }
    }

    protected void showSoftInput(View focusView) {
        if (popupInfo.autoOpenSoftInput) {
            if (showSoftInputTask == null) {
                showSoftInputTask = new ShowSoftInputTask(focusView);
            } else {
                handler.removeCallbacks(showSoftInputTask);
            }
            handler.postDelayed(showSoftInputTask, 10);
        }
    }

    protected void dismissOrHideSoftInput() {
        if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0)
            dismiss();
        else
            KeyboardUtils.hideSoftInput(BasePopupView.this);
    }

    static class ShowSoftInputTask implements Runnable {
        View focusView;
        boolean isDone = false;

        public ShowSoftInputTask(View focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null && !isDone) {
                isDone = true;
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    class BackPressListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && popupInfo != null) {
                if (popupInfo.isDismissOnBackPressed &&
                        (popupInfo.xPopupCallback == null || !popupInfo.xPopupCallback.onBackPressed(BasePopupView.this)))
                    dismissOrHideSoftInput();
                return true;
            }
            return false;
        }
    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     */
    protected PopupAnimator genAnimatorByPopupType() {
        if (popupInfo == null || popupInfo.popupAnimation == null) return null;
        switch (popupInfo.popupAnimation) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case NoAnimation:
                return new EmptyAnimator(getPopupContentView());
        }
        return null;
    }

    protected abstract int getPopupLayoutId();

    /**
     * 如果你自己继承BasePopupView来做，这个不用实现
     *
     * @return
     */
    protected int getImplLayoutId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，用于每种类型的PopupView自定义自己的动画器
     *
     * @return
     */
    protected PopupAnimator getPopupAnimator() {
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，不要去重写。
     */
    protected void initPopupContent() {
    }

    /**
     * do init.
     */
    protected void onCreate() {
    }

    protected void applyDarkTheme() {
    }

    protected void applyLightTheme() {
    }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg) {
            shadowBgAnimator.animateShow();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateShow();
        }
        if (popupContentAnimator != null)
            popupContentAnimator.animateShow();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg) {
            shadowBgAnimator.animateDismiss();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateDismiss();
        }

        if (popupContentAnimator != null)
            popupContentAnimator.animateDismiss();
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。
     * 而且我们对PopupView执行的动画，也是对它执行的动画
     *
     * @return
     */
    public View getPopupContentView() {
        return getChildAt(0);
    }

    public View getPopupImplView() {
        return ((ViewGroup) getPopupContentView()).getChildAt(0);
    }

    public int getAnimationDuration() {
        return popupInfo.popupAnimation == NoAnimation ? 10 : XPopup.getAnimationDuration() + 10;
    }

    /**
     * 弹窗的最大宽度，一般用来限制布局宽度为wrap或者match时的最大宽度
     *
     * @return
     */
    protected int getMaxWidth() {
        return 0;
    }

    /**
     * 弹窗的最大高度，一般用来限制布局高度为wrap或者match时的最大宽度
     *
     * @return
     */
    protected int getMaxHeight() {
        return popupInfo.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }

    protected View getTargetSizeView() {
        return getPopupContentView();
    }

    /**
     * 消失
     */
    public void dismiss() {
        handler.removeCallbacks(attachTask);
        handler.removeCallbacks(initTask);
        if (popupStatus == PopupStatus.Dismissing || popupStatus == PopupStatus.Dismiss) return;
        popupStatus = PopupStatus.Dismissing;
        clearFocus();
        if (popupInfo != null && popupInfo.xPopupCallback != null)
            popupInfo.xPopupCallback.beforeDismiss(this);
        beforeDismiss();
        doDismissAnimation();
        doAfterDismiss();
    }

    /**
     * 会等待弹窗show动画执行完毕再消失
     */
    public void smartDismiss() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delayDismiss(XPopup.getAnimationDuration() + 50);
            }
        });
    }

    public void delayDismiss(long delay) {
        if (delay < 0) delay = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        // PartShadowPopupView要等到完全关闭再关闭输入法，不然有问题
        if (popupInfo != null && popupInfo.autoOpenSoftInput && !(BasePopupView.this instanceof PartShadowPopupView))
            KeyboardUtils.hideSoftInput(this);
        handler.removeCallbacks(doAfterDismissTask);
        handler.postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            if (popupInfo == null) return;
            if (popupInfo.autoOpenSoftInput && BasePopupView.this instanceof PartShadowPopupView)
                KeyboardUtils.hideSoftInput(BasePopupView.this);
            onDismiss();
            if (popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onDismiss(BasePopupView.this);
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null;//no cache, avoid some bad edge effect.
            }
            popupStatus = PopupStatus.Dismiss;
//            NavigationBarObserver.getInstance().removeOnNavigationBarListener(BasePopupView.this);

            if (!stack.isEmpty()) stack.pop();
            if (popupInfo.isRequestFocus) {
                if (!stack.isEmpty()) {
                    stack.get(stack.size() - 1).focusAndProcessBackPress();
                } else {
                    // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                    if (popupInfo.decorView != null) {
                        View needFocusView = popupInfo.decorView.findViewById(android.R.id.content);
                        if (needFocusView != null) {
                            needFocusView.setFocusable(true);
                            needFocusView.setFocusableInTouchMode(true);
                        }
                    }
                }
            }

            // 移除弹窗，GameOver
            if (dialog != null) dialog.dismiss();
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }

    public boolean isShow() {
        return popupStatus != PopupStatus.Dismiss;
    }

    public boolean isDismiss() {
        return popupStatus == PopupStatus.Dismiss;
    }

    public void toggle() {
        if (isShow()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * 开始消失的时候执行一次
     */
    protected void beforeDismiss() {
    }

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() {
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        destroy();
    }

    public void destroy() {
        if (dialog != null) dialog.dismiss();
        onDetachedFromWindow();
        if (popupInfo != null) {
            popupInfo.atView = null;
            popupInfo.watchView = null;
            popupInfo.xPopupCallback = null;
        }
        popupInfo = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stack.clear();
        handler.removeCallbacksAndMessages(null);
//        NavigationBarObserver.getInstance().removeOnNavigationBarListener(BasePopupView.this);
        if (popupInfo != null) {
            if (popupInfo.decorView != null)
                KeyboardUtils.removeLayoutChangeListener(popupInfo.decorView, BasePopupView.this);
            if (popupInfo.isDestroyOnDismiss) { //如果开启isDestroyOnDismiss，强制释放资源
                popupInfo.atView = null;
                popupInfo.watchView = null;
                popupInfo.xPopupCallback = null;
                popupInfo = null;
            }
        }
        popupStatus = PopupStatus.Dismiss;
        showSoftInputTask = null;
        hasMoveUp = false;
        if (blurAnimator != null && blurAnimator.decorBitmap != null && !blurAnimator.decorBitmap.isRecycled()) {
            blurAnimator.decorBitmap.recycle();
            blurAnimator.decorBitmap = null;
        }
    }

    private float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        getPopupContentView().getGlobalVisibleRect(rect);
        if (!XPopupUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && popupInfo.isDismissOnTouchOutside) {
                        dismiss();
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        if (dialog != null && popupInfo != null && popupInfo.isClickThrough)
            dialog.passClick(event);
        return true;
    }

}
