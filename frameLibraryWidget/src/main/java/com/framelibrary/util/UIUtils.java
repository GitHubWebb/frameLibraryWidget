package com.framelibrary.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.framelibrary.R;
import com.framelibrary.config.FrameLibBaseApplication;

import java.lang.reflect.Field;

/**
 * UI页面工具类
 *
 * @author wangwx
 */

public class UIUtils {

    //根据反射设置TabLayout 的下划线宽度
    public static void setTabWidth(final TabLayout tabLayout, final int padding) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);


                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 根据尺寸重新设置Bitmap
     *
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap setImgSize(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * Textview追加图片方法
     *
     * @param textView 文本控件
     * @param content  内容
     * @param res      图片id
     * @param maxLines 最大行数
     */
    public static void textViewAddImage(TextView textView, String content, int res,
                                        int maxLines) {
        if (res == 0)
            textView.setText(content);
        else {
            textView.setMaxLines(maxLines);// 设置最大行数
            final Drawable drawable = FrameLibBaseApplication.getInstance().getBaseContext().getResources().getDrawable(res);// 获取图片
            TextPaint paint = textView.getPaint();// 获取文本控件字体样式
            Paint.FontMetrics fm = paint.getFontMetrics();
            int textFontHeight = (int) Math.ceil(fm.descent - fm.top) + 2;// 计算字体高度座位图片高度
            int imageWidth = drawable.getIntrinsicWidth() * textFontHeight
                    / drawable.getIntrinsicHeight();// 计算图片根据字体大小等比例缩放后的宽度
            drawable.setBounds(0, ScreenUtils.dip2px(1), imageWidth,
                    textFontHeight);// 缩放图片
            int maxWidth = textView.getLayoutParams().width;// 获取文本控件最大宽度
            if (paint.measureText(content) > maxWidth) {// 如果文本大于一行才进入复杂的计算逻辑
                String s = "";// 临时存储截取的字符串
                int start = 0;// 截取的起始位置
                int end = 1;// 截取的结束位置
                int lines = 1;// 计算的行数
                boolean flag = false;// 已经计算到了最大行但是该行文本加图片的宽度超出文本框的宽度时，设置该标记将进行文本递减拼接测量
                do {
                    s = content.substring(start, end);// 截取制定位置的字符串
                    float width = paint.measureText(s);// 测量截取的字符串宽度
                    if (width < maxWidth) {// 截取的文字长度小于控件宽度
                        if (lines == maxWidth) {// 如果已经是最大行
                            if (width + imageWidth < maxWidth) {
                                // 文本宽度+图片宽度小于控件宽度
                                if (width + imageWidth + paint.measureText("...") < maxWidth) {
                                    // 文本宽度+图片宽度+省略号宽度大于控件宽度
                                    if (flag) {
                                        // 递减测量的第一次进入并且满足上一个if则停止循环
                                        content = content.substring(0, end);// 文案切割
                                        content += "...";// 文案拼接省略号
                                        break;
                                    } else {// 还在进行递增测量，结束位置+1
                                        end++;
                                    }
                                } else {
                                    // 文本宽度+图片宽度+省略号宽度大于控件宽度，因为已经是最大行（lines ==
                                    // maxWidth）需要对文字做递减测量，结束位置-1
                                    end--;
                                }
                            } else {
                                // 文本宽度+图片宽度大于控件宽度，因为已经是最大行（lines == maxWidth）
                                // 需要对文字做递减测量，结束位置-1，flag置为true
                                end--;
                                flag = true;
                            }
                        } else {
                            // 截取文字的上限位置+1
                            end++;
                        }
                    } else {
                        // 截取的文字长度大于控件宽度，一行的位置已经确定，下一行的起始位置为当前结束位置-1，行数+1
                        start = end - 1;
                        lines++;
                    }

                } while (end <= content.length() && lines <= maxLines);
            }
            // 文本后面拼接图片
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            content += "   *";
            SpannableString spanString = new SpannableString(content);
            spanString.setSpan(span, content.length() - 1, content.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spanString);
        }
    }


    /**
     * 设置Span样式,常用于可带点击的,《用户协议》或《隐私政策》
     *
     * @param textView
     * @param colorText 要设置的文本
     * @author wangweixu
     */
    public static void setClickBlueSpan(TextView textView, String colorText, View.OnClickListener onClickListener) {
        String text = textView.getText().toString();
        if (StringUtils.isBlank(text) || text.indexOf(colorText) <= 0)
            return;

        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(view);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(DeviceUtils.getColorRes("#0064FE"));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan, text.indexOf(colorText), text.indexOf(colorText) + colorText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        /*ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(DisplayUtils.getColorRes(R.color.btn_IOS_dialog_color_tv));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan2, 10, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
         */

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(DeviceUtils.getColorRes("#0064FE"));
        spannableString.setSpan(colorSpan, text.indexOf(colorText), text.indexOf(colorText) + colorText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        /*ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(DisplayUtils.getColorRes(R.color.btn_IOS_dialog_color_tv));
        spannableString.setSpan(colorSpan2, 10, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);*/
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());//为防止点击事件失效,加上这句
    }


    /**
     * makeTextViewResizable(tv, 4, "SeeMore");
     * 这将在第4行末尾写入“SeeMore”而不是“……”
     * 现在不需要在xml中写这些行
     * <p>
     * android:ellipsize="end"
     * android:maxLines="3"
     *
     * @param tv
     * @param maxSize    最大尺寸
     * @param maxLine
     * @param expandText
     * @see "http://www.voidcn.com/article/p-byeezlsw-bto.html"
     */
    public static void makeTextViewResizable(final TextView tv, final int maxSize, final int maxLine, final String expandText) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine <= 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + "" + expandText;
                    tv.setText(text);
                } else if (tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String tvText = tv.getText().toString();
                    String text = "";
                    if (lineEndIndex <= maxSize)
                        text = tvText;
                    else
                        text = tv.getText().subSequence(0, tvText.length() > maxSize ? maxSize : tvText.length()) + "" + expandText;
//                        text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                }
            }
        });

    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     * 截取RelativeLayout
     **/
    public static Bitmap getRelativeLayoutBitmap(RelativeLayout relativeLayout) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            h += relativeLayout.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        relativeLayout.draw(canvas);
        return bitmap;
    }

    /**
     * 截取LinearLayout
     **/
    public static Bitmap getLinearLayoutBitmap(LinearLayout linearLayout) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            h += linearLayout.getChildAt(i).getHeight();
            h += 20;
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(linearLayout.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     *
     * @param backBitmap  在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {

        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            LogUtils.D("backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，左右拼接
     *
     * @param leftBitmap
     * @param rightBitmap
     * @param isBaseMax   是否以宽度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_LR(Bitmap leftBitmap, Bitmap rightBitmap, boolean isBaseMax) {

        if (leftBitmap == null || leftBitmap.isRecycled()
                || rightBitmap == null || rightBitmap.isRecycled()) {
            LogUtils.D("leftBitmap=" + leftBitmap + ";rightBitmap=" + rightBitmap);
            return null;
        }
        int height = 0; // 拼接后的高度，按照参数取大或取小
        if (isBaseMax) {
            height = leftBitmap.getHeight() > rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        } else {
            height = leftBitmap.getHeight() < rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        }

        // 缩放之后的bitmap
        Bitmap tempBitmapL = leftBitmap;
        Bitmap tempBitmapR = rightBitmap;

        if (leftBitmap.getHeight() != height) {
            tempBitmapL = Bitmap.createScaledBitmap(leftBitmap, (int) (leftBitmap.getWidth() * 1f / leftBitmap.getHeight() * height), height, false);
        } else if (rightBitmap.getHeight() != height) {
            tempBitmapR = Bitmap.createScaledBitmap(rightBitmap, (int) (rightBitmap.getWidth() * 1f / rightBitmap.getHeight() * height), height, false);
        }

        // 拼接后的宽度
        int width = tempBitmapL.getWidth() + tempBitmapR.getWidth();

        // 定义输出的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 缩放后两个bitmap需要绘制的参数
        Rect leftRect = new Rect(0, 0, tempBitmapL.getWidth(), tempBitmapL.getHeight());
        Rect rightRect = new Rect(0, 0, tempBitmapR.getWidth(), tempBitmapR.getHeight());

        // 右边图需要绘制的位置，往右边偏移左边图的宽度，高度是相同的
        Rect rightRectT = new Rect(tempBitmapL.getWidth(), 0, width, height);

        canvas.drawBitmap(tempBitmapL, leftRect, leftRect, null);
        canvas.drawBitmap(tempBitmapR, rightRect, rightRectT, null);
        return bitmap;
    }

    /**
     * 截取除了导航栏之外的整个屏幕
     */
    public static Bitmap screenShotWholeScreen(Activity activity) {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     *
     * @param isBaseMax 是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            LogUtils.D("topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (isBaseMax) {
            width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        } else {
            width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;

        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width, (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
        } else if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width, (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
        }

        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

        Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        return bitmap;
    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param num1   长边的比例
     * @param num2   短边的比例
     * @return
     */
    public static Bitmap ImageCrop(Bitmap bitmap, int num1, int num2,
                                   boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else {
            if (w > h * num2 / num1) {
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }


    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {

        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        // 下面这句是关键
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
        // false);
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap ImageCropWithRect(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int nw, nh, retX, retY;
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            nw = w / 2;
            nh = w;
            retX = w / 4;
            retY = (h - w) / 2;
        }

        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

}
