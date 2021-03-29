package com.framelibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framelibrary.config.FrameLibBaseApplication;

import java.io.ByteArrayOutputStream;

/**
 * 整合图片 Drawable Bitmap byte 相关工具类
 *
 * @author wangweixu
 * @Date 2021年03月29日14:27:07
 */
public class ImageUtils {


    /**
     * 重新设置Bitmap 大小 并放置在iv中
     *
     * @param urlBitmap
     * @param width     固定高度 可以是屏幕高度
     * @param imageView
     */
    public static Bitmap resetImageViewSize(Bitmap urlBitmap, float width, ImageView imageView) {
        float urlWidth = urlBitmap.getWidth();
        float urlHeight = urlBitmap.getHeight();
        float deviceWidth = width;
        float newBitMapHeight = deviceWidth / (urlWidth / urlHeight);
        return resetImageViewSize(urlBitmap, deviceWidth, imageView, newBitMapHeight);
    }

    public static Bitmap resetImageViewSize(Bitmap urlBitmap, float deviceWidth, ImageView imageView, float newBitMapHeight) {
        Bitmap updateSizeBitmap = setImgSize(urlBitmap, deviceWidth, newBitMapHeight);
        imageView.setImageBitmap(updateSizeBitmap);
        return updateSizeBitmap;
    }

    /**
     * 根据尺寸重新设置Bitmap
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap setImgSize(Bitmap bm, float newWidth, float newHeight) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        float sx = newWidth / w;//要强制转换，不转换我的在这总是死掉。
        float sy = newHeight / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bm, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }

    //Bitmap  转换byte[]
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Bytes(bm, Bitmap.CompressFormat.PNG, 100);
    }

    /**
     * Bitmap to bytes.
     *
     * @param bitmap  The bitmap.
     * @param format  The format of bitmap.
     * @param quality The quality.
     * @return bytes
     */
    public static byte[] Bitmap2Bytes(@Nullable final Bitmap bitmap, @NonNull final Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || format == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        return baos.toByteArray();
    }

    //byte[] 转换 Bitmap
    public static Bitmap Bytes2Bimap(byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Bytes to drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bitmap2Drawable(Bytes2Bimap(bytes));
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @return bytes
     */
    public static byte[] drawable2Bytes(@Nullable final Drawable drawable) {
        return drawable == null ? null : Bitmap2Bytes(drawable2Bitmap(drawable));
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @param format   The format of bitmap.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format, int quality) {
        return drawable == null ? null : Bitmap2Bytes(drawable2Bitmap(drawable), format, quality);
    }


    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(@Nullable final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(FrameLibBaseApplication.getInstance().getResources(), bitmap);
    }

    //Drawable --> Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) return null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;

    }


    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setDrawingCacheEnabled(true);
        view.setWillNotCacheDrawing(false);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (null == drawingCache || drawingCache.isRecycled()) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            drawingCache = view.getDrawingCache();
            if (null == drawingCache || drawingCache.isRecycled()) {
                bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            } else {
                bitmap = Bitmap.createBitmap(drawingCache);
            }
        } else {
            bitmap = Bitmap.createBitmap(drawingCache);
        }
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        return bitmap;
    }

}
