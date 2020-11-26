package com.framelibrary.util.request;

import com.framelibrary.util.logutil.LoggerUtils;
import com.framelibrary.util.StringUtils;

/**
 * @author wangwx
 * 表单请求参数
 */
public abstract class RequestParams {

    private static final String TAG = "RequestParams";

    private StringBuffer stringBuffer;

    public RequestParams() {
        stringBuffer = new StringBuffer();
    }

    /**
     * 添加参数
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, Object value) {
        stringBuffer.append("&");
        stringBuffer.append(key);
        stringBuffer.append("=");
        stringBuffer.append(value);
    }

    public abstract void putAccessToken();

    /**
     * 传递结束后调用此方法
     */
    @Override
    public String toString() {
        if (StringUtils.isBlank(stringBuffer.toString())) {
            return "";
        }
        String params = stringBuffer.substring(1, stringBuffer.length());
        LoggerUtils.D("RequestParams======" + params);
        return params;  // 忽略第1位字符&
    }

}
