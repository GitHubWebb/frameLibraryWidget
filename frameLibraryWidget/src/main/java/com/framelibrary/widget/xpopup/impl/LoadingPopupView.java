package com.framelibrary.widget.xpopup.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framelibrary.R;
import com.framelibrary.util.dialog.xpopup.XPopupUtils;
import com.framelibrary.widget.xpopup.XPopup;
import com.framelibrary.widget.xpopup.core.CenterPopupView;

/**
 * Description: 加载对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {
    private TextView tv_title;

    /**
     *
     * @param context
     * @param bindLayoutId  layoutId 如果要显示标题，则要求必须有id为tv_title的TextView，否则无任何要求
     */
    public LoadingPopupView(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : R.layout._xpopup_center_impl_loading;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = findViewById(R.id.tv_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getPopupImplView().setElevation(10f);
        }
        if(bindLayoutId==0){
            getPopupImplView().setBackground(XPopupUtils.createDrawable(Color.parseColor("#1f1f1f"), popupInfo.borderRadius));
        }
        setup();
    }
    protected void setup() {
        if (title != null && title.length()!=0 && tv_title != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (tv_title.getText().length() != 0) {
                        TransitionManager.beginDelayedTransition((ViewGroup) tv_title.getParent(), new TransitionSet()
                                .setDuration(XPopup.getAnimationDuration())
                                .addTransition(new ChangeBounds()));
                    }
                    tv_title.setVisibility(VISIBLE);
                    tv_title.setText(title);
                }
            });
        }
    }

    private CharSequence title;

    public LoadingPopupView setTitle(CharSequence title) {
        this.title = title;
        setup();
        return this;
    }
}
