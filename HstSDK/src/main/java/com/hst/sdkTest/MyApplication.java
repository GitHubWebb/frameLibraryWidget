package com.hst.sdkTest;

import android.app.Application;

import com.inpor.fastmeetingcloud.sdk.HstLoginManager;





/**
 * Created by hst on 2018/5/3.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化视频会议SDK
        HstLoginManager.getInstance().initSdk(getApplicationContext());
    }
}
