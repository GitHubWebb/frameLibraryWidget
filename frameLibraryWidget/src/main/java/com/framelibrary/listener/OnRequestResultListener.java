package com.framelibrary.listener;

/**
 * 请求结果监听
 */

public interface OnRequestResultListener {
    void onSuccess(String result);

    void onFail(String result);
}
