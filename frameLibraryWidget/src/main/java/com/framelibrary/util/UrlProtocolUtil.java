package com.framelibrary.util;

import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Url协议补全类
 *
 * @author wangweixu
 * @Date 2020年11月19日10:39:49
 */
public class UrlProtocolUtil {
    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";
    private String newurl;

    //判断协议 能连接上则协议正确
    public String getProtocolCompletion(String url) {
        if (StringUtils.isBlank(url))
            return null;

        newurl = clearUrl(url);
        url = HTTP + newurl;
        if (startFutureCheckUrl(url)) {
            return url;
        } else {
            url = HTTPS + newurl;
            if (startFutureCheckUrl(url)) {
                return url;
            } else {
                return null;
            }
        }

    }

    //清除URL里多余的符号
    private String clearUrl(String url) {
        if (StringUtils.isBlank(url))
            return null;

        if (url.contains(HTTP)) {
            url = url.substring(url.lastIndexOf(HTTP) + HTTP.length());
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        } else if (url.contains(HTTPS)) {
            url = url.substring(url.lastIndexOf(HTTPS) + HTTPS.length());
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        } else {
            for (int i = 0; i < url.length(); i++) {
                if (url.charAt(i) == '/' || url.charAt(i) == '.'
                        || url.charAt(i) == '\\') {
                } else {
                    url = url.substring(i);
                    return url;
                }
            }
        }
        return url;
    }

    private boolean startFutureCheckUrl(String url) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            JsonObject jsonObject = executorService.submit(new UrlTask(url)).get();
            return jsonObject.has("connectStatus") ? jsonObject.get("connectStatus").getAsBoolean() : false;
        } catch (ExecutionException e) {
//            e.printStackTrace();
            LogUtils.printStackToLog(e, "startFutureCheckUrl");
        } catch (InterruptedException e) {
//            e.printStackTrace();
            LogUtils.printStackToLog(e, "startFutureCheckUrl");
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }

        return false;
    }

    class UrlTask implements Callable<JsonObject> {
        private String taskName;

        UrlTask(String taskName) {
            this.taskName = taskName;
        }

        //是否能连接上
        private boolean exists(String url) {
            if (StringUtils.isBlank(url))
                return false;

            try {
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setConnectTimeout(3000);
                return (con.getResponseCode() == 200);
            } catch (Exception e) {
                return false;
            }
        }

        public JsonObject call() throws Exception {
            LogUtils.D("->" + taskName + "任务开启");
            Long beginTime = System.currentTimeMillis();

            boolean connectStatus = exists(taskName);

            Long endTime = System.currentTimeMillis();

            long time = endTime - beginTime;
            LogUtils.D("->" + taskName + "任务结束");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("taskStatus", "任务已经结束,耗时：" + time + "毫秒");
            jsonObject.addProperty("connectStatus", connectStatus);
            LogUtils.D("->" + jsonObject);

            return jsonObject;
        }
    }
}