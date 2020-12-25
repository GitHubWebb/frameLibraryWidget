package com.framelibrary.util;

import android.text.TextUtils;

import com.framelibrary.util.logutil.LoggerUtils;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 *
 * @author wangweixu
 */
public class StringUtils {
    private static String TAG = Class.class.getName();

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * (这里的100就是2位小数点,如果要其它位,如4位,这里两个100改成10000)
     * float 四舍五入 小数点后两位
     */
    public static Float roundingModeScale(float currentFloat) {
        float returnFloat = (float) (Math.round(currentFloat * 100)) / 100;
        return returnFloat;
    }

    public static String getUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        LoggerUtils.D("getUUID32 , UUID=" + uuid);
        return uuid;
    }

    //分转元
    public static String fenToYuan(int fen) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(fen / 100f);
    }

    public static String formatString(String string, int maxLength) {
        if (!StringUtils.isBlank(string) && string.length() > maxLength)
            return string.substring(0, maxLength);
        return string;
    }

    /**
     * 方法名:         calcFloatValue
     * 方法功能描述:   float类型的加减乘除，避免Java 直接计算错误
     *
     * @param:     type:   加：add   减：  subtract  乘：multiply 除：divide
     *      
     */
    public static float calcFloatValue(float t1, float t2, String type) {
        BigDecimal a = new BigDecimal(String.valueOf(t1));
        BigDecimal b = new BigDecimal(String.valueOf(t2));
        float retValue = 0f;
        switch (type) {
            case "add":
                retValue = a.add(b).floatValue();
                break;
            case "subtract":
                retValue = a.subtract(b).floatValue();
                break;
            case "multiply":
                retValue = a.multiply(b).floatValue();
                break;
            case "divide":
                retValue = a.divide(b).floatValue();
                break;
        }
        return retValue;
    }

    /**
     * 方法名:         calcDoubleValue
     * 方法功能描述:   double类型的加减乘除，避免Java 直接计算错误
     *      
     */
    public static double calcDoubleValue(double t1, double t2, String type) {
        BigDecimal a = new BigDecimal(String.valueOf(t1));
        BigDecimal b = new BigDecimal(String.valueOf(t2));
        double retValue = 0f;
        switch (type) {
            case "add":
                retValue = a.add(b).doubleValue();
                break;
            case "subtract":
                retValue = a.subtract(b).doubleValue();
                break;
            case "multiply":
                retValue = a.multiply(b).doubleValue();
                break;
            case "divide":
                retValue = a.divide(b).doubleValue();
                break;
        }
        return retValue;
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0 || "".equals(str) || "null".equals(str));
    }

    /**
     * is null or its length is 0
     * <p>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     * <p>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return
     * {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * <p>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str
                .toString()));
    }

    /**
     * capitalize first letter
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * get innerHtml from href
     * <p>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern
                .compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source
                .replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <p>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <p>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 时：分：秒
     *
     * @param second
     * @return
     */
    public static String timeToString(int second) {
        StringBuffer buffer = new StringBuffer();
        int hour = second / 3600;
        int min = (second - hour * 3600) / 60;
        int sec = (second - hour * 3600 - min * 60);
        if (hour < 10) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (min < 10) {
            buffer.append("0");
        }
        buffer.append(min).append(":");
        if (sec < 10) {
            buffer.append("0");
        }
        buffer.append(sec);
        return buffer.toString();
    }

    /**
     * 时：分
     *
     * @param second
     * @return
     */
    public static String timeToStringSF(int second) {
        StringBuffer buffer = new StringBuffer();
        int hour = second / 3600;
        int min = (second - hour * 3600) / 60;
//		int sec = (second - hour * 3600 - min * 60);
        if (hour < 10) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (min < 10) {
            buffer.append("0");
        }
        buffer.append(min);
        return buffer.toString();
    }

    /**
     * 天：时：分：秒
     *
     * @param second
     * @return
     */
    public static String timeToString1(int second, int day, int hour, int min,
                                       int sec) {
        StringBuffer buffer = new StringBuffer();
        day = second / (3600 * 24);//
        hour = (second - day * 24 * 3600) / 3600;
        min = (second - day * 24 * 3600 - hour * 3600) / 60;
        sec = (second - day * 24 * 3600 - hour * 3600 - min * 60);
        if (day < 10) {
            buffer.append("0");
        }
        buffer.append(day).append(":");
        if (hour < 10) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (min < 10) {
            buffer.append("0");
        }
        buffer.append(min).append(":");
        if (sec < 10) {
            buffer.append("0");
        }
        buffer.append(sec);
        return buffer.toString();
    }

    /**
     * @param second
     * @return
     */
    public static String timeToString1(int second, int hour, int min, int sec) {
        StringBuffer buffer = new StringBuffer();
        hour = second / 3600;
        min = (second - hour * 3600) / 60;
        sec = (second - hour * 3600 - min * 60);
        if (hour < 10) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (min < 10) {
            buffer.append("0");
        }
        buffer.append(min)
        /*.append(":") ;
         if (sec < 10) {
		 buffer.append("0");
		 }
		 buffer.append(sec)*/;
        return buffer.toString();
    }

    // 电话号码验证
    public static boolean checkPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        return checkPhone(phone, regex);
    }

    private static boolean checkPhone(String phone, String regEx) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    /**
     * 创建订单电话校验
     *
     * @param phone 手机号码
     * @return
     */
    public static boolean checkCreateOrderPhone(String phone) {
        String regEx = "^[1][12345678][0-9]{9}$";
        return checkPhone(phone, regEx);
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str 如果都是数字，返回true，否则返回false
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isBlank(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 检查手机号
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        return !StringUtils.isBlank(mobile) && mobile.length() == 11 && mobile.startsWith("1") && TextUtils.isDigitsOnly(mobile);
    }

    public static boolean isGoodJSON(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //数字转字母 1-26 ： A-Z
    public static String numberToLetter(int num) {
        if (num <= 0) {
            return null;
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter = ((char) (num % 26 + (int) 'A')) + letter;
            num = (num - num % 26) / 26;
        } while (num > 0);

        return letter;
    }

    //字母转数字  A-Z ：1-26
    public static int letterToNumber(String letter) {
        int length = letter.length();
        int num = 0;
        int number = 0;
        for (int i = 0; i < length; i++) {
            char ch = letter.charAt(length - i - 1);
            num = ch - 'A' + 1;
            num *= Math.pow(26, i);
            number += num;
        }
        return number;
    }

    /**
     * 获取url对应的域名
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        String result = "";
        int j = 0, startIndex = 0, endIndex = 0;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                j++;
                if (j == 2)
                    startIndex = i;
                else if (j == 3)
                    endIndex = i;
            }

        }
        result = url.substring(startIndex + 1, endIndex);
        return result;
    }

    /**
     * 参数串取字段值
     * params 如：a=1&b=sdfsdf&c=&d=2234
     *
     * @param params 字符值来源
     * @param key    根据key取值
     * @return 得到的值, 不存在为null
     * @author wangwx
     */
    public static String getParamsValue(String params, String key) {
        if (StringUtils.isEmpty(params))
            return null;

        int paramspos = params.indexOf("?");
        String params_string = paramspos >= 0 ? params.substring(paramspos + 1) : params;

        for (String paramitem : params_string.split("&")) {
            if (StringUtils.isEmpty(paramitem))
                continue;

            String[] kvp = paramitem.split("=");
            if (kvp.length < 2)
                continue;

            if (key.equals(kvp[0]))
                return kvp[1];
        }

        return null;
    }

}
