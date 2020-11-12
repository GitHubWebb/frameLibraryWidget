package com.framelibrary.util.request;

import org.json.JSONArray;

/**
 * @author wangwx
 * 请求工具类
 */

public abstract class RequestUtils {

    private static final String TAG = Class.class.getSimpleName();
    public static int fail_Login = 0; //失败调用标记点

    /**
     * 请求成功返回码
     */

    private static final String REQUEST_SUCCESS_CODE = "0";


    /**
     * 请求失败返回码
     */

    private static final String REQUEST_FAIL_CODE = "1";
    public static JSONArray appShareCallBackJA;  //分享回调
    public static JSONArray appAddWXCard;  //领取微信卡劵
    public static JSONArray appMiniProgram;  //打开微信小程序
    public static boolean reLoginFinished = true;  //登录成功标记点
    private static boolean requestSuccess = true;

    /**
     * 判断是否请求成功
     *
     * @param code 请求结果码
     * @return 是否成功
     */

    public static boolean isRequestSuccess(String code) {
        return REQUEST_SUCCESS_CODE.equals(code);
    }

    public static boolean isRequestFail(String code) {
        return REQUEST_FAIL_CODE.equals(code);
    }

    public static boolean isAccessTokenInvalid(String code) {
        return "99".equals(code);
    }

}

