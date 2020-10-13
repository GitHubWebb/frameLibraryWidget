package com.framelibrary.util.request;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framelibrary.R;
import com.framelibrary.listener.OkHttpRequestListener;
import com.framelibrary.listener.OnRequestResultListener;
import com.framelibrary.util.dialog.DialogUtils;
import com.framelibrary.util.request.RequestParams;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

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

