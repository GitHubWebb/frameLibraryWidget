package com.framelibrary.util;


import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;


public class LogUtils {
    private static boolean isHideAllLog = false;
    private static boolean isRelease = false;
    private final static int I = 1, D = 2, E = 3;

    private static void print(int mod, String tag, String msg) {
        if (isHideAllLog) {
            return;
        }
        if (isRelease && mod < E) {
            return;
        }
        switch (mod) {
            case I:
                Log.i(tag, msg);
                break;
            case D:
                Log.d(tag, msg);
                break;
            case E:
                Log.e(tag, msg);
                break;
        }
    }

    public static void I(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        if (message != null) {
            print(I, tag, message);
        }
    }

    public static void D(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        if (message != null) {
            print(D, tag, message);
        }
    }

    public static void E(String message) {
        if (!Constant.LOG_PRINT)
            return;

        String tag = generateTag();
        if (message != null) {
            print(E, tag, message);
        }
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
        
        print(E, tag, methodName + " , " + Arrays.toString(paramsDes) + " ,e=" + exception);
    }

    /**
     * 得到tag（所在类.方法（L:行））
     *
     * @return
     */
    private static String generateTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[0];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = "%s.%s(L:%d)";
        tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        //给tag设置前缀
        tag = TextUtils.isEmpty(Constant.LOG_TAG_PREFIX) ? tag : Constant.LOG_TAG_PREFIX + ":" + tag;
        return tag;
    }

}