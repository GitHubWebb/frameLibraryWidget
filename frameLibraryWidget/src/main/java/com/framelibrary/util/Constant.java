package com.framelibrary.util;

/**
 * @author wangwx
 * 2016年8月20日下午5:52:46
 */
public class Constant {

    /**
     * 日志输出控制，上线时修改为false
     */
    public static boolean LOG_PRINT = true;

    /**
     * 日志输出Tag前缀
     */
    public static String LOG_TAG_PREFIX = "wangwx";

    public static boolean isUpload = true;

    public static boolean isFloatWindow = false;

    /**
     * 正式环境appcode
     */
    public static final String NORMAL_APPCODE = "MSCANNER-MOBILE";


    /**
     * 正式环境appcode
     */
    /*public static final String NORMAL_APPCODE = "MSCANNER-123";*/

    /**
     * 测试环境appcode
     */
    public static final String TEST_APPCODE = "MSCANNER-TEST";

    /**
     * 保存崩溃日志文件夹
     */
    public static final String errorLogDirectory = "MexpressScanner";
    /**
     * 保存请求日志文件夹
     */
    public static final String requestLogDirectory = "MexpressScannerRequest";

    public static final String scanner_file_name = "scanner.txt";

    /**
     * 正常日志保存的文件名
     */
    public static final String LOGCAT_FILE_NAME = "logcat.txt";

    /**
     * 请求日志
     */
    public static final String REQUEST_FILE_NAME = "request.txt";

    /**
     * ping 日志文件夹
     */
    public static final String PING_FILE_DIRECTORY = "PingFileDir";

    /**
     * ping 日志
     */
    public static final String PING_FILE_NAME = "ping.txt";

    /**
     * APP下载文件名
     */
    public static final String APP_FILE_NAME = "mexpress-scanner.apk";

    /**
     * ACCESS_TOKEN（key）
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 相册保存到本地文件夹
     */
    public static final String PHOTO_DIRECTORY = "MexpressScannerPhotos";


    /**
     * 补单操作签字图片保存本地文件夹
     */
    public static final String MAKEUP_PHOTO_DIRECTORY = "MexpressScannerMakeup";


    public static final String MAKEUP_PHOTO_TMPDIR = "MexpressScannerMakeupTmp";

    /**
     * 网络请求成功码
     */
    public static final String SUCCESS_CODE = "A00006";
    /**
     * 网络请求失败码
     */
    public static final String FAIL_CODE = "A00005";

    /**
     * 网络请求错误码
     */
    public static final String ERROR_CODE = "A00008";

    /**
     * 网络请求提示语
     */
    public static final String CONNECT_MESSAGE = "正在通信中...";

    public static final String CONNECT_MESSAGE_FAIL = "网络连接失败，请稍后重试";

    public static final String PARSE_DATA_EXCEPTION = "解析数据异常";

    public static final String DATA_EXCEPTION = "数据异常";

    public static final String DATA_EMPTY = "数据为空";

    public static final String COMMIT_DATA_SUCCESS = "提交成功";

    /**
     * 授权开发者ID
     */
    public static final String AUTHID = "ncpa";
    /**
     * 授权开发者ID
     */
    public static final String AUTHSECRET = "ncpa";
    /**
     * 连接失败
     */
    public static String CONNECT_FAIL = "通讯失败，请检查网络连接！";
    /**
     * 提交失败
     */
    public static final String COMMIT_FAIL = "提交失败，请重试！";
    /**
     * 柜组类型
     */
    public static final String CABINET_SCAN_TYPE = "CABINET";
    /**
     * 货架类型
     */
    public static final String SHELF_SCAN_TYPE = "SHELF";
    /**
     * 所有类型
     */
    public static final String ALL_SCAN_TYPE = "ALL_SHELF";

    /**
     * 提示语显示时间
     */
    public static final long SHOW_VIEW_TIME = 10 * 1000;

    /**
     * AccessToken刷新Tag
     */
    public static final String REFRESG_ACCESS_TOKEN_TAG = "refresh_access_token";

    /**
     * 普通请求TAG
     */
    public static final String REQUEST_NORMAL_TAG = "request_normal_tag";


    /**
     * 扫码广播action
     */
    public static final String SCANNER_QCODE_ACTION = "sacnner_qcode_action";

    /**
     * 扫码key
     */
    public static final String SCANNER_QCODE_KEY = "scanner_qcode";


    /**
     * 待交付
     */
    public static final String WAIT_TAKEOVER = "WAITTAKEOVER";

    /**
     * 交付
     */
    public static final String TO_TAKEOVER = "TOTAKEOVER";
    /**
     * 待退回
     */
    public static final String WAIT_RETURN = "WAITRETURN";
    /**
     * 退回
     */
    public static final String TO_RETURN = "TORETURN";

    public static long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启

    /**
     * 下拉刷新操作类型
     */
    public static final int REFRESH_OPERATION_TYPE = 0;

    /**
     * 上拉加载操作类型
     */
    public static final int LOAD_OPERATION_TYPE = 1;

    public static void main(String[] args) {
        String st = "125.jpg";
        System.out.println(st.substring(0, st.lastIndexOf(".jpg")));

    }
}
