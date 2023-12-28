package com.framelibrary.youth.banner;

import androidx.viewpager.widget.ViewPager;

import com.framelibrary.youth.banner.transformer.AccordionTransformer;
import com.framelibrary.youth.banner.transformer.BackgroundToForegroundTransformer;
import com.framelibrary.youth.banner.transformer.CubeInTransformer;
import com.framelibrary.youth.banner.transformer.CubeOutTransformer;
import com.framelibrary.youth.banner.transformer.DefaultTransformer;
import com.framelibrary.youth.banner.transformer.DepthPageTransformer;
import com.framelibrary.youth.banner.transformer.FlipHorizontalTransformer;
import com.framelibrary.youth.banner.transformer.FlipVerticalTransformer;
import com.framelibrary.youth.banner.transformer.ForegroundToBackgroundTransformer;
import com.framelibrary.youth.banner.transformer.RotateDownTransformer;
import com.framelibrary.youth.banner.transformer.RotateUpTransformer;
import com.framelibrary.youth.banner.transformer.ScaleInOutTransformer;
import com.framelibrary.youth.banner.transformer.StackTransformer;
import com.framelibrary.youth.banner.transformer.TabletTransformer;
import com.framelibrary.youth.banner.transformer.ZoomInTransformer;
import com.framelibrary.youth.banner.transformer.ZoomOutSlideTransformer;
import com.framelibrary.youth.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
