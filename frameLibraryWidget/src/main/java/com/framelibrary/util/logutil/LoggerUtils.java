package com.framelibrary.util.logutil;


import android.text.TextUtils;

import com.framelibrary.util.Constant;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * 由原本的使用底层log框架更改为Logger
 *
 * @author wangweixu
 * @link "https://github.com/orhanobut/logger"
 * @Date 2020年11月26日17:26:28
 * @see "https://www.cnblogs.com/bdsdkrb/p/10038732.html"
 */
public class LoggerUtils {
    private final static int I = 1, D = 2, E = 3, JSON = 4, XML = 5;
    private static boolean isHideAllLog = false;
    private static boolean isRelease = false;

    private static void print(int mod, String tag, String msg) {
        if (isHideAllLog) {
            return;
        }
        if (isRelease && mod < E) {
            return;
        }

        StringBuffer msgSB = new StringBuffer(" \n");
        msg = msgSB.append(msg).toString();
        switch (mod) {
            case I:
                Logger.i(msg);
//                Log.i(tag, msg);
                break;
            case D:
                Logger.d(msg);
//                Log.d(tag, msg);
                break;
            case E:
                Logger.e(msg);
//                Log.e(tag, msg);
                break;
            case JSON:
                Logger.json(msg);
                break;
            case XML:
                Logger.xml(msg);
                break;
        }
    }

    public static void I(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        Logger.t(tag);
        if (message != null) {
            print(I, tag, message);
        }
    }

    public static void D(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        Logger.t(tag);
        if (message != null) {
            print(D, tag, message);
        }
    }

    public static void E(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        Logger.t(tag);
        if (message != null) {
            print(E, tag, message);
        }
    }

    public static void JSON(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        Logger.t(tag);
        if (message != null) {
            print(JSON, tag, message);
        }
    }

    public static void XML(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        Logger.t(tag);
        if (message != null) {
            print(XML, tag, message);
        }
    }


    /**
     * 避免e.printStackTrace()堆栈过多造成锁死
     *
     * @param e          报错Exception
     * @param methodName 报错方法名
     */
    public static void printStackToLog(Exception e) {
        printStackToLog(e, "");
    }

    /**
     * 避免e.printStackTrace()堆栈过多造成锁死
     *
     * @param e          报错Exception
     * @param methodName 报错方法名
     */
    public static void printStackToLog(Exception e, String methodName) {
        printStackToLog(e, methodName, "execute fail!");
    }

    /**
     * 避免e.printStackTrace()堆栈过多造成锁死
     *
     * @param e          报错Exception
     * @param methodName 报错方法名
     */
    public static void printStackToLog(Exception e, String methodName, String... paramsDes) {
        if (e == null)
            return;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();

        String tag = generateTag();
        Logger.t(tag);
        print(E, tag, methodName + " , " + Arrays.toString(paramsDes) + " ,e=" + exception);
    }

    /**
     * 得到tag（所在类.方法（L:行））
     *
     * @return
     */
    private static String generateTag() {

        return generateTag(5);
    }

    /**
     * 得到tag（所在类.方法（L:行））
     *
     * @return
     */
    private static String generateTag(int index) {
        StackTraceElement[] stackTraceElementArr = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElementArr[index];

        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = "%s.%s(L:%d)";
        tag = String.format(tag, callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()));
        //给tag设置前缀
        tag = TextUtils.isEmpty(Constant.LOG_TAG_PREFIX) ? tag : Constant.LOG_TAG_PREFIX + ":" + tag;
        return tag;
    }


    /**
     * 获取相关调用栈的信息，并且打印相关日志及代码行数；
     * <p>
     * <p>
     * 相关调用栈的信息，按照:类名,方法名,行号等，这样的格式拼接，可以用来定位代码行，
     * 如：
     * at cn.xx.ui.MainActivity.onCreate(MainActivity.java:23) 定位代码行;
     *
     * @param isLinked 是否输出所有相关调用栈的信息；
     */
    private static String logTraceInfo(boolean isLinked) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        if (stes == null) {
            D("logTraceLinkInfo#return#stes == null");
            return "";
        }
        StringBuilder result = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stes.length; i++) {
            StackTraceElement ste = stes[i];
            if (ignorable(ste)) {
                continue;
            }
            sb.append("").append("[Thread:")
                    .append(Thread.currentThread().getName())
                    .append(", at ").append(ste.getClassName())
                    .append(".").append(ste.getMethodName())
                    .append("(").append(ste.getFileName())
                    .append(":").append(ste.getLineNumber())
                    .append(")]");
            String info = sb.toString();
            if (isLinked) {
                if (result == null) {
                    result = new StringBuilder();
                }
                result.append(info).append("\n");
                sb.delete(0, info.length());

            } else {
                return info;
            }
        }
        return result.toString();
    }


    private static boolean ignorable(StackTraceElement ste) {
        return ste.isNativeMethod() ||
                ste.getClassName().equals(Thread.class.getName()) ||
                ste.getClassName().equals(Logger.class.getName());
    }

}