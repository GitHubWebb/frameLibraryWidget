package com.wwx.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.framelibrary.ui.activity.select_photo.MultiImageSelectorActivity;
import com.framelibrary.util.LogUtils;
import com.framelibrary.util.PermissionCheckUtils;
import com.framelibrary.util.UIUtils;
import com.framelibrary.util.dialog.DialogDoNet;

import java.util.ArrayList;
import java.util.List;

import static com.framelibrary.config.FrameLibBaseApplication.getInstance;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public final int SELECT_PHOTO_FROM_SDCARD = 1;

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        TextView tvTest = findViewById(R.id.tv_test);
        tvTest.setText("Hello");

        mActivity = this;
        UIUtils.makeTextViewResizable(tvTest, 4, 1, "...");

        LogUtils.D("activity");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.D("activity1");
                    }
                }).start();

            }
        });
        photoShow();
    }

    private void photoShow() {
        final List<String> urls = new ArrayList<>();
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511198824138&di=cec97b6363a1bce28b8499a31b78df83&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F3e282f8762696b0bbb3ed16a5dc193c718e5aff9.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511793592&di=08b8de22336028a68c9a0bbbe7a9066d&imgtype=jpg&er=1&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2F7ea7878cfbddb27bb8a23e2407bfa7a48655f317.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511198824136&di=f4dc71ffdbbb16d9e3d496cf8add5376&imgtype=0&src=http%3A%2F%2Foss.tan8.com%2Fresource%2Fattachment%2F2017%2F201707%2F962b7304d0adc7e2e1d374dc6786e302.jpg");
        findViewById(R.id.btn_show_imgs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openPictureChoose();
                /*popupView = new XPopup.Builder(mActivity)
                        .asLoading("正在加载中")
                        .show();*/
                DialogDoNet.startLoad(mActivity,"正在加载中");
                startActivity(new Intent(getInstance().getContext(), SplitEditActivity.class));
//                startSelectImage();
//                new ShowImagesDialog(MainActivity.this, urls).show();
            }
        });
    }

   /* private void openPictureChoose() {
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
//                .setCropMode() // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(6) // 可选择图片的数量
//                .setMinSelectNum()// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(true) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
//                .setCheckedBoxDrawable() // 选择图片样式
//                .setRecordVideoDefinition() // 视频清晰度
//                .setRecordVideoSecond() // 视频秒数
//                .setCustomQQ_theme()// 可自定义QQ数字风格，不传就默认是蓝色风格
//                .setGif()// 是否显示gif图片，默认不显示
//                .setCropW() // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//                .setCropH() // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
//                .setMaxB() // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb左右
//                .setPreviewColor() //预览字体颜色
//                .setCompleteColor() //已完成字体颜色
//                .setPreviewTopBgColor()//预览图片标题背景色
//                .setPreviewBottomBgColor() //预览底部背景色
//                .setBottomBgColor() //图片列表底部背景色
//                .setGrade() // 压缩档次 默认三档
//                .setCheckNumMode() //QQ选择风格
//                .setCompressQuality() // 图片裁剪质量,默认无损
//                .setImageSpanCount() // 每行个数
//                .setSelectMedia() // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(2) // 1 系统自带压缩 2 luban压缩
//                .setCompressW() // 压缩宽 如果值大于图片原始宽高无效
//                .setCompressH() // 压缩高 如果值大于图片原始宽高无效
//                .setThemeStyle() // 设置主题样式
//                .setPicture_title_color() // 设置标题字体颜色
//                .setPicture_right_color() // 设置标题右边字体颜色
//                .setLeftBackDrawable() // 设置返回键图标
//                .setStatusBar() // 设置状态栏颜色，默认是和标题栏一致
//                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
//                .setClickVideo()// 点击声音
                .create();

        PictureConfig.getInstance().openPhoto(this, resultCallback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:

                    break;
            }
        }
    }

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            // 多选回调
//            selectMedia = resultList;
            Log.i("callBack_result", resultList.size() + "");
            LocalMedia media = resultList.get(0);
            String path = "";
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图地址
                path = media.getPath();
            }
            LogUtils.D(TAG, "onSelectSuccess( resultList = )" + path);
            *//*if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }*//*
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
            LogUtils.D(TAG, "onSelectSuccess( media = )" + media);
            *//*selectMedia.add(media);
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }*//*
        }
    };*/


    /**
     * 选择图片
     */
    protected Intent goToMultiImageSelectedIntent() {
        Intent pictureIntent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                6);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者
        // 多选/MultiImageSelectorActivity.MODE_MULTI)
        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        return pictureIntent;
    }

    //打开选择图片
    public void startSelectImage() {
        if (!PermissionCheckUtils.checkCameraPermission(this)) {
            return;
        }
        if (!PermissionCheckUtils.checkReadPermission(this)) {
            return;
        }
        Intent intent = goToMultiImageSelectedIntent();
        startActivityForResult(intent, SELECT_PHOTO_FROM_SDCARD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionCheckUtils.REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionCheckUtils.startAppDetailSettingIntent(this);
            } else {
                startSelectImage();
            }
        }
    }
}