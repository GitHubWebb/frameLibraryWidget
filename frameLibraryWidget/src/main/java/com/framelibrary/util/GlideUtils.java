package com.framelibrary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framelibrary.widget.glide.GlideRadiusCornerTransform;

import java.io.Serializable;


/*
 *  项目名：  framelibrary
 *  包名：    com.framelibrary.util
 *  文件名:   GlideUtils
 *  借鉴于:   LGL
 *  创建者:   WANGWX
 *  创建时间:  2020年11月18日11:37:50
 *  描述：    Glide封装
 */
public class GlideUtils {

    /**
     * 检测参数是否合法
     *
     * @param mContext
     * @param path
     * @return
     */
    private static boolean checkEmptyParams(Context mContext, String path) {
        if (StringUtils.isBlank(path))
            return true;
        if (null == mContext)
            return true;
        if (mContext instanceof Activity && (
                ((Activity) mContext).isFinishing() ||
                        ((Activity) mContext).isDestroyed()
        ))
            return true;
        return false;
    }

    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     *
     * @return
     */


    /**
     * 加载指定大小
     *
     * @param width  The width in pixels to use to load the resource.
     * @param height The height in pixels to use to load the resource.
     * @return
     */
    public static DrawableRequestBuilder<? extends Serializable> loadImageViewSize(Context mContext, String path, int width, int height) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return null;

        builder.override(width, height);

        return builder;
    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @return
     */
    private static DrawableRequestBuilder<? extends Serializable> loadImageViewLoding(Context mContext, String path, int lodingImage, int errorImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return null;

        builder.placeholder(lodingImage)
                .error(errorImageView);

        return builder;

    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @return
     */
    private static BitmapTypeRequest<? extends Serializable> loadBitMapImageViewLoding(Context mContext, String path, int lodingImage, int errorImageView) {
        BitmapTypeRequest<? extends Serializable> builder = loadBitMapImageView(mContext, path);
        if (builder == null)
            return null;

        builder.placeholder(lodingImage)
                .error(errorImageView);

        return builder;

    }

    /**
     * 设置加载中以及加载失败图片,以及弧度角度数,dp值
     *
     * @return
     */
    public static void loadImageViewLodingRadius(Context mContext, String path,
                                                 int placeHolderId, int radiusdp, ImageView imageView) {
        DrawableRequestBuilder<? extends Serializable> requestBuilder = loadImageViewLoding(mContext, path, placeHolderId, placeHolderId);
        if (requestBuilder == null)
            return;

        requestBuilder.transform(new GlideRadiusCornerTransform(mContext, radiusdp)).priority(Priority.HIGH);

        glideInto(requestBuilder, imageView);
    }

    /**
     * 设置加载中以及加载失败图片,以及弧度角度数,dp值
     *
     * @return
     */
    public static void loadImageViewLodingRadius(Context mContext, String path,
                                                 int placeHolderId, int radiusdp, SimpleTarget<Bitmap> simpleTarget) {
        BitmapTypeRequest<? extends Serializable> requestBuilder = loadBitMapImageViewLoding(mContext, path, placeHolderId, placeHolderId);
        if (requestBuilder == null)
            return;

        requestBuilder.transform(new GlideRadiusCornerTransform(mContext, radiusdp)).priority(Priority.HIGH);

        glideInto(requestBuilder, simpleTarget);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Context mContext, String path, int width,
                                               int height, ImageView mImageView, int lodingImage, int errorImageView) {
        DrawableRequestBuilder<? extends Serializable> loadImageViewSize = loadImageViewSize(mContext, path, width, height);
        loadImageViewLoding(mContext, path, lodingImage, errorImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .skipMemoryCache(true)
                .into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView
            mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .priority(Priority.NORMAL)
                .into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView
            mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView
            mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .animate(anim)
                .into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView
            mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .thumbnail(0.1f)
                .into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder
                .centerCrop()
                .into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView
            mImageView) {
        GifTypeRequest<? extends Serializable> request = loadGifImageView(mContext, path);
        if (request == null)
            return;

        request.into(mImageView);
    }

    //设置静态GIF加载方式
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView
            mImageView) {
        BitmapTypeRequest<? extends Serializable> request = loadBitMapImageView(mContext, path);
        if (request == null)
            return;

        request
                .into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public static void loadImageViewListener(Context mContext, String path, ImageView
            mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public static void loadImageViewContent(Context mContext, String
            path, SimpleTarget<GlideDrawable> simpleTarget) {
        DrawableRequestBuilder<? extends Serializable> builder = loadImageView(mContext, path);
        if (builder == null)
            return;

        builder.centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }


    //默认生成
    private static DrawableRequestBuilder<? extends Serializable> loadImageView(Context
                                                                                        mContext, String path) {
        if (checkEmptyParams(mContext, path)) return null;

        DrawableRequestBuilder<? extends Serializable> requestBuilder = Glide.with(mContext).load(StringUtils.isNumeric(path) ? Integer.parseInt(path) : path);
        //.placeholder();
        //.into(mImageView);

        return requestBuilder;
    }

    //Gif默认生成
    private static GifTypeRequest<? extends Serializable> loadGifImageView(Context
                                                                                   mContext, String path) {
        if (checkEmptyParams(mContext, path)) return null;

        GifTypeRequest<? extends Serializable> requestBuilder = Glide.with(mContext).load(StringUtils.isNumeric(path) ? Integer.parseInt(path) : path).asGif();
        //.placeholder();
        //.into(mImageView);

        return requestBuilder;
    }

    //BitMap默认生成
    private static BitmapTypeRequest<? extends Serializable> loadBitMapImageView(Context
                                                                                         mContext, String path) {
        if (checkEmptyParams(mContext, path)) return null;

        BitmapTypeRequest<? extends Serializable> requestBuilder = Glide.with(mContext).load(StringUtils.isNumeric(path) ? Integer.parseInt(path) : path).asBitmap();
        //.placeholder();
        //.into(mImageView);

        return requestBuilder;
    }

    public static void glideInto(GenericRequestBuilder builder, ImageView imageView) {
        if (builder == null || imageView == null)
            return;

        builder.into(imageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
    public static void glideInto(GenericRequestBuilder
                                         builder, SimpleTarget<Bitmap> simpleTarget) {
        if (builder == null || simpleTarget == null)
            return;

        builder.into(simpleTarget);
    }

}