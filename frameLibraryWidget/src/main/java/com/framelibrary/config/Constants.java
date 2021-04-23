package com.framelibrary.config;

import com.framelibrary.R;
import com.framelibrary.util.Constant;

/**
 * @author wangwx
 * 2016年8月20日下午5:52:46
 */
public class Constants {

    public static final String CONNECT_MESSAGE_FAIL = "网络连接失败，请稍后重试";
    public static final String PARSE_DATA_EXCEPTION = "解析数据异常";
    public static final String DATA_EXCEPTION = "数据异常";
    public static final String DATA_EMPTY = "数据为空";
    public static final String COMMIT_DATA_SUCCESS = "提交成功";
    public static final String authid = Constant.LOG_PRINT ? "messaging" : "0d16b28bf191407982abae326aba505d";
    public static final String authSecret = Constant.LOG_PRINT ? "messaging" : "057260d6e9074ebeb3f254cb5b11ebc4";
    //组件所属组件帐户ID
    public static final String moduleid = "21683ae92402466985907465536cd07a";
    public static final String wxappid = "wx22384af3f3afb99e";
    /**
     * 微信支付结果action
     */
    public static final String WXPAYRESULT_ACTION = "payresult_action";
    /**
     * 刷新
     */
    public static final int REFRESH_TYPE = 1;
    /**
     * 加载
     */
    public static final int LOAD_TYPE = 2;
    public static final long SESSION_REFRESHTIME = 1 * 60 * 60 * 1000;
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY_WEIBO = "4010190639";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL_WEIBO = "https://api.weibo.com/oauth2/default.html";
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE_WEIBO =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    public static final String CODE_KEY = "errcode";
    public static final String ERROR_MSG_KEY = "msg";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String LOCAL_PATH = "codingman/";
    //JS native name
    public static final String js_native_name = "__zqcmyj_jsapi";
    public static final long PLAYTIMEMINITIME = 5000;////单位毫秒---上传小课播放时长的最小限时
    // 主工程编译的appName
    public static String MAIN_PROJECT_BUILD_APP_NAME = "frameLibrary";
    // 主工程编译的appIcon
    public static int MAIN_PROJECT_BUILD_IC_LAUNCHER = R.drawable.icon_loading_dialog;
    public static String jumpPackageName = "com.zqcm.ysp";
    // com.kx.bapp 调起应用包名  type为类型 1 授权登陆    user_package_name:使用者包名
    public static String mJumpLisenceUri = "zqcm://" + jumpPackageName + "/sign?type=1&user_package_name=com.zqcm.yj";


}
