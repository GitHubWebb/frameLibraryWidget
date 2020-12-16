package com.framelibrary.util.share;

import android.content.SharedPreferences;

import com.framelibrary.config.FrameLibBaseApplication;
import com.framelibrary.util.StringUtils;
import com.framelibrary.util.logutil.LoggerUtils;

import java.util.Set;

/**
 * 设备数据存储类SP
 *
 * @author wangweixu
 * @Date 😪2017年12月16日14:58:41
 */
public class DeviceDataShare {
    private static final String TAG = "DeviceDataShare";
    private static DeviceDataShare instance;
    private SharedPreferences sharedPreferences;
    private String allUserAvatarUrl;

    private static void init() {
        instance = new DeviceDataShare();
    }

    public synchronized static DeviceDataShare getInstance() {
        if (instance == null)
            init();
        return instance;
    }

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = FrameLibBaseApplication.getInstance().getSharedPreferences();
        return sharedPreferences;
    }

    //设置启动状态  是否显示引导页
    public void setStartStatus(boolean startStatus) {
        getSharedPreferences().edit().putBoolean("startStatus", startStatus).apply();
    }

    //获取启动状态  是否显示引导页
    public Boolean getStartStatus() {
        return getSharedPreferences().getBoolean("startStatus", true);
    }

    //获取分享赠送课程ID
    public String getGiveCourseID() {
        return getSharedPreferences().getString("giveFriendsCourseID", "");
    }

    //保存是否为新用户状态
    public void setAppNewUserStatus(String newUserStatus) {
        boolean isNewUser = false;
        //是否app新用户 0:否,1:是
        if ("1".equals(newUserStatus))
            isNewUser = true;
        getSharedPreferences().edit().putBoolean("newUserStatus", isNewUser).apply();
    }

    //获取是否为新用户状态
    public boolean getAppNewUserStatus() {
        return getSharedPreferences().getBoolean("newUserStatus", true);
    }


    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    public String getAccessToken() {
        String access_token = getSharedPreferences().getString("access_token", "");
        LoggerUtils.I(" access_token=" + access_token);
        return access_token;
    }

    //存储access_token
    public void setAccessToken(String accessToken) {
        LoggerUtils.I("setAccessToken.--------" + accessToken);
        if (!StringUtils.isBlank(accessToken)) {
            getSharedPreferences().edit().putString("access_token", accessToken).apply();
        }
    }

    //获取存储的网页描述
    public String getHtmlDesc() {
        String htmlDesc = getSharedPreferences().getString("htmlDesc", "");
        return htmlDesc;
    }

    //存储webview html desc
    public void setHtmlDesc(String htmlDesc) {
        LoggerUtils.I("setHtmlDesc.--------" + htmlDesc);
        getSharedPreferences().edit().putString("htmlDesc", htmlDesc).apply();
    }

    //存储activitylistBeans
    public void setActivitylistBeans(Set<String> activitylistBeanSet) {
        getSharedPreferences().edit().putStringSet("activitylistBeans", activitylistBeanSet).apply();
    }


    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    public String getStringValueByKey(String key) {
        if (StringUtils.isBlank(key))
            return "";

        String value = getSharedPreferences().getString(key, "");
        LoggerUtils.I(" value=" + value);
        return value;
    }

    //存储access_token
    public void setStringValueByKey(String key, String value) {
        LoggerUtils.I("setStringValueBy Key.--------" + key+" ,value.--------" + value);
        if (StringUtils.isBlank(value)) return;

        getSharedPreferences().edit().putString(key, value).apply();

    }

}
