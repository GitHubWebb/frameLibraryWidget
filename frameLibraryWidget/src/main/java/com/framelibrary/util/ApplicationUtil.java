package com.framelibrary.util;

import android.app.Application;
import android.content.SharedPreferences;


/**
 * <pre>
 *      author:         wangweixu
 *      date:           2021-05-25 17:51:08
 *      description:    可以获取Application的单例类
 *      upUser:
 *      upDate:
 *      upRemark:       更新说明:
 *      version:        v1.0
 * </pre>
 */
public class ApplicationUtil {
    //使用volatile修饰 目的是为了在JVM层编译顺序一致
    private static volatile ApplicationUtil applicationUtil = null;

    private Application instance;

    //私有化构造器
    private ApplicationUtil() {
    }

    public static ApplicationUtil getInstance() {
        //第一次校验
        if (applicationUtil == null) {
            synchronized (ApplicationUtil.class) {

                //第二次校验
                if (applicationUtil == null) {
                    applicationUtil = new ApplicationUtil();
                }
            }
        }
        return applicationUtil;
    }

    public void init(Application appContext) {
        this.instance = appContext;
    }


    public Application getApplication() {
        return instance;
    }


}
