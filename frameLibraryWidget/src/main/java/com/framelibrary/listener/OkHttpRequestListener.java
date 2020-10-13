package com.framelibrary.listener;

import com.framelibrary.bean.BaseRespBean;

import okhttp3.Call;

/**
 * 请求接口回调监听
 */

public interface OkHttpRequestListener {

    void onResponse(Call call, BaseRespBean baseRespBean, String result);
    void onFail(Call call, Exception e);

}
