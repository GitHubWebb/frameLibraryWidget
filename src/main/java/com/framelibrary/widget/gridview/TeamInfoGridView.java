package com.framelibrary.widget.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 取消gridview的滚动，使其能嵌套在scrollview中
 *
 * @author wangwx
 */
public class TeamInfoGridView extends GridView {

    public TeamInfoGridView(Context context) {
        super(context);
    }

    public TeamInfoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TeamInfoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    // 取消gridview的滚动，使其能嵌套在scrollview中
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
