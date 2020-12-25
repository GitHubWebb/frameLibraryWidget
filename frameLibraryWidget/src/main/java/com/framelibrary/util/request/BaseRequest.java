package com.framelibrary.util.request;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * request基类
 */

public class BaseRequest {

    private static BaseRequest instance;
    private OkHttpClient okHttpClient;

    private BaseRequest() {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
    }

    public static BaseRequest getInstance() {
        if (instance == null) {
            synchronized (BaseRequest.class) {
                instance = new BaseRequest();
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Call post() {


        return null;
    }


}
