package com.framelibrary.util.share;

import android.content.SharedPreferences;

import com.framelibrary.config.FrameLibBaseApplication;
import com.framelibrary.util.StringUtils;
import com.framelibrary.util.logutil.LoggerUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 设备数据存储类SP
 * SharedPreference 提 交 数 据 时 ， 尽 量 使 用 Editor#apply()
 * ，而非Editor#commit()。一般来讲，仅当需要确定提交结果，并据此有后续操作时，才使用 Editor#commit()。
 * <p>
 * 说明：
 * <p>
 * SharedPreference 相关修改使用 apply 方法进行提交会先写入内存，然后异步写入磁盘，commit
 * 方法是直接写入磁盘。如果频繁操作的话 apply 的性能会优于 commit，apply会将最后修改内容写入磁盘。
 * 但是如果希望立刻获取存储操作的结果，并据此做相应的其他操作，应当使用 commit。
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
     * 获取选中的selectPop下标
     *
     * @param key "SelectPopData"+SelectPopDataBean.getId();
     * @return 下标
     */
    public String getSelectPopStringValueByKey(String key) {
        key = "SelectPopData:" + key;
        return getStringValueByKey(key);
    }

    /**
     * 获取选中的selectPop下标
     *
     * @param key   "SelectPopData"+SelectPopDataBean.getId();
     * @param value options1 , options2 , options3 ....
     * @return 下标
     */
    public void setSelectPopStringValueByKey(String key, String value) {
        key = "SelectPopData:" + key;
        setStringValueByKey(key, value);
    }

    /**
     * 根据key获取value
     *
     * @return
     */
    public String getStringValueByKey(String key) {
        if (StringUtils.isBlank(key))
            return "";

        String value = getSharedPreferences().getString(key, "");
        LoggerUtils.I(" value=" + value);
        return value;
    }

    //存储根据key,存储value
    public void setStringValueByKey(String key, String value) {
        LoggerUtils.I("setStringValueBy Key.--------" + key + " ,value.--------" + value);
        if (StringUtils.isBlank(value)) return;

        getSharedPreferences().edit().putString(key, value).apply();

    }

    // 删除所有SelectPop选中项缓存数据
    public void removeBySelectPopDataAll() {
        Iterator<? extends Map.Entry<String, ?>> entryIterator = getSharedPreferences().getAll().entrySet().iterator();
        while (entryIterator.hasNext()) {
            String key = entryIterator.next().getKey();
            if (StringUtils.isBlank(key))
                continue;

            if (key.indexOf("SelectPopData:") != -1)
                removeByKey(key);
        }
    }

    // 删除所有数据
    public void removeByAll() {
        Iterator<? extends Map.Entry<String, ?>> entryIterator = getSharedPreferences().getAll().entrySet().iterator();
        while (entryIterator.hasNext()) {
            String key = entryIterator.next().getKey();
            removeByKey(key);
        }
    }

    // 删除所有数据
    public void removeAll() {
        getSharedPreferences().edit().clear().apply();
    }

    // 根据Key删除
    public void removeByKey(String key) {

        if (StringUtils.isBlank(key))
            return;

        LoggerUtils.D("删除了key:" + key);
        getSharedPreferences().edit().remove(key).apply();
    }

}
