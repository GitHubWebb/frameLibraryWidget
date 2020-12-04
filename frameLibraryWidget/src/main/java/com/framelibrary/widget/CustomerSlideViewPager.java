package com.framelibrary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义控制滑动viewPager
 *
 * @author wangweixu
 */
public class CustomerSlideViewPager extends ViewPager {
    //是否可以进行滑动
    private boolean isSlide = false;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public CustomerSlideViewPager(Context context) {
        super(context);
    }

    public CustomerSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSlide;
    }

    @Override
    public void setCurrentItem(int item) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, isSlide);
    }

}
