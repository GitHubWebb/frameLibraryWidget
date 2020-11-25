package com.framelibrary.widget.image;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framelibrary.R;
import com.framelibrary.util.DeviceUtils;
import com.framelibrary.util.GlideUtils;
import com.framelibrary.widget.viewpager.ShowImagesViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 参照CSDN 整理/封装出 适合快速使用的图片预览框架
 * PhotoView 是一款扩展自Android ImageView ，支持通过单点/多点触摸来进行图片缩放的智能控件。
 * <p>
 * 特性：
 * <p>
 * 支持单点/多点触摸，即时缩放图片；
 * 支持平滑滚动；
 * 在滑动父控件下能够运行良好；（例如：ViewPager）
 *
 * @author wangweixu
 * @Date 2020年09月03日16:02:05
 * @see "https://blog.csdn.net/cwh1010714845/article/details/78587695"
 */
public class ShowImagesDialog extends Dialog {

    private View mView;
    private Context mContext;
    private int mCurrentPositionItem = 0; // 要定位到的图片位置
    private TextView mIndexText;
    private ShowImagesViewPager mViewPager;
    private List<String> mTitles, mImgUrls;
    private List<View> mViews;
    private ShowImagesAdapter mAdapter;

    /**
     * 显示图片弹窗
     *
     * @param context 当前承载上下文
     * @param imgUrls 图片集合
     */
    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls) {
        super(context, R.style.transparentBgDialog);
        if (context == null || imgUrls == null)
            return;

        onCreate(context, imgUrls);
    }

    /**
     * 显示图片弹窗
     *
     * @param context             当前承载上下文
     * @param imgUrls             图片集合
     * @param currentPositionItem 当前第几页 很可能点击的图片不是第一个,所以要跳到当前图片位置
     */
    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls, int currentPositionItem) {
        super(context, R.style.transparentBgDialog);

        this.mCurrentPositionItem = currentPositionItem;
        onCreate(context, imgUrls);
    }

    // 初始化页面
    private void onCreate(@NonNull Context context, List<String> imgUrls) {
        this.mContext = context;//传入上下文
        this.mImgUrls = imgUrls == null ? new ArrayList<>() : imgUrls;//传入图片String数组

        mCurrentPositionItem = mImgUrls.size() < mCurrentPositionItem + 1 ? 0 : mCurrentPositionItem;
        initView(mCurrentPositionItem);
        initData();
    }

    private void initView(int currentPositionItem) {
        mView = View.inflate(mContext, R.layout.dialog_images_brower, null);//通过inflate()方法找到我们写好的包含ViewPager的布局文件
        mIndexText = (TextView) mView.findViewById(R.id.tv_image_index);
        mViewPager = (ShowImagesViewPager) mView.findViewById(R.id.vp_images);//找到ViewPager控件并且实例化

        mViews = new ArrayList<>();//创建一个控件的数组，我们可以在ViewPager中加入很多图片，滑动改变图片
        mTitles = new ArrayList<>();

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(currentPositionItem, false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = DeviceUtils.deviceHeight(mContext);
        wl.width = DeviceUtils.deviceWidth(mContext);
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        //EXACT_SCREEN_HEIGHT,EXACT_SCREEN_WIDTH为自定义Config类中的静态变量
        //在MainActivity中会给这两个变量赋值，分别对应手机屏幕高度和宽度
        //在这里我们通过WindowManager.LayoutParams把当前dialog的大小设置为全屏
    }

    private void initData() {
        //当PhotoView被点击时，添加相应的点击事件
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();//点击图片后，返回到原来的界面
            }

            @Override
            public void onOutsidePhotoTap() {

            }

        };
        for (int i = 0; i < mImgUrls.size(); i++) {
            final PhotoView photoView = new PhotoView(mContext);
            //创建一个PhotoView对象
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            //我们通过ViewGroup.LayoutParams来设置子控件PhotoView的大小
            photoView.setOnPhotoTapListener(listener);//给PhotoView添加点击事件
            GlideUtils.loadImageViewLodingRadius(mContext, mImgUrls.get(i), R.mipmap.ic_page_indicator, 0, new SimpleTarget<Bitmap>() {
                //这是Glide的一个回调方法
                //我们首先定义了一个SimpleTarget，然后把它通过into方法传入。
                // 这样当Glide去服务器请求图片成功之后，
                // 它会把请求到的图片资源作为GlideDrawable传递回来，
                // 你可以使用这个GlideDrawable进行自己想要的操作,
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    photoView.setImageBitmap(resource);
                    //我们把回调过来的图片资源加载到PhotoView中
                    //大家有没有发现一个细节，我在布局文件中并没有创建PhotoView控件，而是创建的ViewPager
                    //因为我们需要在ViewPager中加入多个PhotoView以达到图片翻页的功能
                    //因此我们在加载图片时，想要把图片加载到任意的图片控件中，就需要Glide回调方法
                }
            });
            mViews.add(photoView);//最后把我们加载的所有PhotoView传给View数组

            mTitles.add(i + "");

        }

        mAdapter = new ShowImagesAdapter(mViews, mTitles);//给适配器传入图片控件数组
        mViewPager.setAdapter(mAdapter);
        mIndexText.setText(1 + "/" + mImgUrls.size());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //给ViewPager添加监听事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexText.setText(position + 1 + "/" + mImgUrls.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class ShowImagesAdapter extends PagerAdapter {


        private List<View> views;
        private List<String> titles;

        public ShowImagesAdapter(List<View> views, List<String> titles) {
            this.views = views;
            this.titles = titles;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
            //确定一个页面视图是否关联到一个特定的对象。
        }

        @Override
        public int getCount() {
            return views.size();
            //得到控件数组个数
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles == null ? "" : titles.get(position);
        }

        //一般来说，destroyitem在viewpager移除一个item时调用。
        // viewpage一般都会缓冲3个item，即一开始就会调用3次instantiateItem,
        // 当向右滑动，到第3页时，第1页的item会被调用到destroyitem
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }
    }

    // 重新定义展示方法,避免数据异常时候调用
    @Override
    public void show() {
        if (mContext == null || (mImgUrls == null || mImgUrls.isEmpty()))
            return;

        super.show();
    }
}