package com.wwx.app.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.framelibrary.bean.select.select_popdata.SelectPopDataBean;
import com.framelibrary.config.DaemonThreadFactory;
import com.framelibrary.config.FrameLibBaseApplication;
import com.framelibrary.ui.activity.select_photo.MultiImageSelectorActivity;
import com.framelibrary.util.PermissionCheckUtils;
import com.framelibrary.util.StringUtils;
import com.framelibrary.util.UIUtils;
import com.framelibrary.util.dialog.DialogDoNet;
import com.framelibrary.util.filter.text.EmojiFilter;
import com.framelibrary.util.filter.text.TextChangedListener;
import com.framelibrary.util.logutil.LoggerUtils;
import com.framelibrary.util.select.selectdata.SelectPopWindowLevelLinkageData;
import com.framelibrary.util.select.selectphoto.FileUtils;
import com.framelibrary.util.share.DeviceDataShare;
import com.framelibrary.widget.image.ShowImagesDialog;
import com.wwx.app.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final int SELECT_PHOTO_FROM_SDCARD = 1;
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4, new DaemonThreadFactory());
    private Activity mActivity;
    private int openDialogMsgCount = 0; // 点击打开信息dialog次数
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        tvTest = findViewById(R.id.et_test);
        TextView tvTest1 = findViewById(R.id.et_test1);

        InputFilter[] inputFilters = {TextChangedListener.allowEnglishNumWrap, new EmojiFilter()};
        TextChangedListener.inputLimitSpaceWrap(18, inputFilters, (EditText) tvTest1);

//        TextChangedListener.specialStringExcludePointsWatcher(20, (EditText) tvTest1);

        findViewById(R.id.btn_get_img_mime_type).setOnClickListener(this);
        findViewById(R.id.btn_open_spliteditactivity).setOnClickListener(this);
        findViewById(R.id.btn_open_dialog_message).setOnClickListener(this);
        findViewById(R.id.btn_put_sp_test).setOnClickListener(this);
        findViewById(R.id.btn_open_choose_pop_test).setOnClickListener(this);
        findViewById(R.id.btn_open_choose_pop).setOnClickListener(this);
        tvTest.setText("Hello");

        mActivity = this;
        UIUtils.makeTextViewResizable(tvTest, 4, 1, "...");

        UIUtils.setEditAndClickByTextView(tvTest, true, null);
        UIUtils.setEditAndClickByTextView(tvTest1, true, null);
        LoggerUtils.D("activity");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoggerUtils.D("activity1");
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
        urls.add("");
        findViewById(R.id.btn_show_imgs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openPictureChoose();
                /*popupView = new XPopup.Builder(mActivity)
                        .asLoading("正在加载中")
                        .show();*/

//                startSelectImage();
                new ShowImagesDialog(MainActivity.this, urls, 10).show();
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
            LoggerUtils.D(TAG, "onSelectSuccess( resultList = )" + path);
            *//*if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }*//*
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
            LoggerUtils.D(TAG, "onSelectSuccess( media = )" + media);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_img_mime_type:
                FileUtils.getMimeType(new File("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511198824138&di=cec97b6363a1bce28b8499a31b78df83&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F3e282f8762696b0bbb3ed16a5dc193c718e5aff9.jpg"));

                break;
            case R.id.btn_open_spliteditactivity:
                DialogDoNet.startLoadAndCancelable(mActivity, "正在加载中", false);
                startActivity(new Intent(FrameLibBaseApplication.getInstance().getContext(), SplitEditActivity.class));
                break;
            case R.id.btn_open_dialog_message:

                // 循环任务，以上一次任务的结束时间计算下一次任务的开始时间,也就是延迟时间加上任务执行时间就是下一个任务的开始时间
                mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        boolean isOpenCancelable = false;
                        if (openDialogMsgCount % 2 == 0) isOpenCancelable = true;

                        if (openDialogMsgCount >= 50) {
                            DialogDoNet.dismiss(300);
                            return;
                        } else
                            DialogDoNet.startLoadAndCancelable(mActivity, "正在加载中" + openDialogMsgCount++, isOpenCancelable);

                    }
                }, 200, 300, TimeUnit.MILLISECONDS);

                if (openDialogMsgCount >= 50) {
                    mScheduledExecutorService.shutdown();
                }
                break;

            case R.id.btn_put_sp_test:
                DeviceDataShare.getInstance().setStringValueByKey(null, null);
                break;
            case R.id.btn_open_choose_pop_test:
                startActivity(new Intent(this, MainSelectPopActivity.class));
                break;
            case R.id.btn_open_choose_pop:
                /*SelectPopWindowLevelLinkageData.showSelectDataNPicker(this, tvTest, "/",
                        Arrays.asList("请选择", "男", "女"),
                        Arrays.asList("请选择", "男", "女"),
                        Arrays.asList("请选择", "男", "女")
                );*/

                List<SelectPopDataBean> option1Items = new ArrayList<>();

                List<SelectPopDataBean> option2Items = new ArrayList<>();

                List<SelectPopDataBean> option3Items = new ArrayList<>();
                option3Items.add(new SelectPopDataBean(
                        "男20", "男20", null
                ));
                option3Items.add(new SelectPopDataBean(
                        "男21", "男21", null
                ));

                option2Items.add(new SelectPopDataBean(
                        "男10", "男10", option3Items
                ));
                option2Items.add(new SelectPopDataBean(
                        "男11", "男11", option3Items
                ));

                option1Items.add(new SelectPopDataBean(
                        "男", "男", option2Items
                ));

                option2Items = new ArrayList<>();
                option3Items = new ArrayList<>();
                option3Items.add(new SelectPopDataBean(
                        "女20", "女20", null
                ));
                option3Items.add(new SelectPopDataBean(
                        "女21", "女21", null
                ));

                option2Items.add(new SelectPopDataBean(
                        "女10", "女10", option3Items
                ));
                option2Items.add(new SelectPopDataBean(
                        "女11", "女11", option3Items
                ));

                option1Items.add(new SelectPopDataBean(
                        "女", "女", null
                ));


                SelectPopDataBean optionItem = new SelectPopDataBean(StringUtils.getUUID32(), "测试", "测试页面", option1Items);

                SelectPopWindowLevelLinkageData.showSelectDataPicker(this, true, tvTest, "-",
                        optionItem);
                break;
        }
    }
}