package com.framelibrary.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 时间工具类
 *
 * @author wangweixu
 */
public class DateUtils {

    /**
     * time of last click
     */
    private static long lastClickTime;

    /**
     * switch of limit for click
     */
    private static boolean clickLimit = false;
    private static int intervalTime = 500; //间隔时长

    public static void setIntervalTime(int intervalTime) {
        DateUtils.intervalTime = intervalTime;
    }

    public static void setClickLimit(boolean clickLimitParam) {
        clickLimit = clickLimitParam;
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD > intervalTime)
            lastClickTime = time;
        if (!clickLimit)
            return timeD < 0;
        return timeD <= intervalTime;
    }

    /**
     * 精确到天的时间规则
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 默认格式化时间规则
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

    /**
     * 时间格式
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault());

    /**
     * 精确到天的时间规则 年 月 日
     */
    public static final String DATE_PATTERN_STR = "yyyy年MM月dd日";

    public static final int ERROR_PARSE_VALUE = -1;
    /**
     * 仅带分秒的时间
     */
    public static final String PATTERN_ONLY_MS_SS = "mm:ss";
    /**
     * 带毫秒的时间
     */
    public static final String PATTERN_HAS_SS = "yyyy-MM-dd HH:mm:ss:SS";

    private static final SimpleDateFormat hourTimeFormat = new SimpleDateFormat("yyyy-MM-ddHH");


    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    //	private static final String ONE_DAY_AGO = "天前";
    //	private static final String ONE_MONTH_AGO = "月前";
    //	private static final String ONE_YEAR_AGO = "年前";


    /**
     * 格式化时间转换为日期 2003-01-01 00:00:00 转换为 2003-01-01
     *
     * @param time 时间
     * @return 日期
     */
    public static String formatTimeToDate(String time) {
        try {
            if (StringUtils.isBlank(time))
                return "";
            Date date = simpleDateFormat.parse(time);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化时间转换为日期 2003-01-01 00:00:00 转换为 2003-01-01
     *
     * @param time 时间
     * @return 日期
     */
    public static String formatTimeToStr(Date time) {
        if (null == time)
            return "";
        return dateFormat.format(time);
    }


    /**
     * 时间日期转换
     *
     * @param string 需要转换的字符串
     * @return 结果
     */

    public static String birthdayDateConvert(String string, boolean showDay) {
        if (StringUtils.isBlank(string)) return "";
        StringBuilder result = new StringBuilder();
        if (string.contains("-")) {
            String[] strings = string.split("-");
            if (strings.length > 0) {
                result.append(strings[0]).append("年").append(strings[1]).append("月");
                if (showDay) {
                    String day = strings[2];
                    String[] dayTime = day.split(" ");
                    result.append(dayTime[0]).append("日");
                    try {
                        result.append(dayTime[1]);
                    } catch (Exception e) {
                    }
                }
            } else {
                //没有时间  将 - 忽略显示
                result.append("");
            }
        } else {
            int yearIndex = string.indexOf("年");
            if (yearIndex > 0) {
                result.append(string.substring(0, yearIndex)).append("-");
            }
            int monthIndex = string.indexOf("月");
            if (monthIndex > 0) {
                if (monthIndex - yearIndex == 2) {
                    result.append("0");
                }
                result.append(string.substring(yearIndex + 1, monthIndex)).append("-");

            }
            result.append("01");
        }
        String time = "";
        int existDay = result.lastIndexOf("日");
        if (existDay > 0)
            time = result.substring(0, existDay + 1);
        else
            time = result.toString();

        return time;
    }

    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getDaySDFTime(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) return "";
        String timeStr = dateStr;
        try {
            Date currenTimeZone = simpleDateFormat.parse(dateStr);
            Calendar calendar_old = Calendar.getInstance();
            calendar_old.setTime(currenTimeZone);
            Calendar calendar_now = Calendar.getInstance();
            int calendarYear_now = calendar_now.get(Calendar.YEAR);
            int calendarYear_old = calendar_old.get(Calendar.YEAR);
            int calendarMonth_now = calendar_now.get(Calendar.MONTH) + 1;
            int calendarMonth_old = calendar_old.get(Calendar.MONTH) + 1;
            int calendarDate_now = calendar_now.get(Calendar.DATE);
            int calendarDate_old = calendar_old.get(Calendar.DATE);
            int calendarhour_of_day_old = calendar_old.get(Calendar.HOUR_OF_DAY);
            int calendarminute_old = calendar_old.get(Calendar.MINUTE);

            if (calendarYear_old == calendarYear_now) {
                if (calendarMonth_old == calendarMonth_now) {
                    if (calendarDate_old == calendarDate_now) {
                        timeStr = (calendarhour_of_day_old < 10 ? "0" + calendarhour_of_day_old : calendarhour_of_day_old) + ":" +
                                (calendarminute_old < 10 ? "0" + calendarminute_old : calendarminute_old);
                        return timeStr;
                    } else {
                        if (calendarDate_now - (calendar_old.get(Calendar.DATE)) == 1) {
                            timeStr = "昨天";
                        }

                    }
                    return (calendarMonth_old < 10 ? "0" + calendarMonth_old : calendarMonth_old) + "-" + (calendarDate_old < 10 ? "0" + calendarDate_old : calendarDate_old)
                            + " " + (calendarhour_of_day_old < 10 ? "0" + calendarhour_of_day_old : calendarhour_of_day_old) + ":" +
                            (calendarminute_old < 10 ? "0" + calendarminute_old : calendarminute_old);
                }
                return (calendarMonth_old < 10 ? "0" + calendarMonth_old : calendarMonth_old) + "-" + (calendarDate_old < 10 ? "0" + calendarDate_old : calendarDate_old)
                        + " " + (calendarhour_of_day_old < 10 ? "0" + calendarhour_of_day_old : calendarhour_of_day_old) + ":" +
                        (calendarminute_old < 10 ? "0" + calendarminute_old : calendarminute_old);
            }

        } catch (ParseException e) {
            return "";
        }
        return timeStr;
    }

    // 根据时间 str 判断 是否传入时间为过期时间
    public static boolean isExpireTime(String expireTimeStr) {
        long expireTimeParse = parse(expireTimeStr);
        long currentTimeMillis = System.currentTimeMillis();
        if (expireTimeParse > currentTimeMillis) {
            return false;
        }
        return true;
    }


    /**
     * @param startDate 开始时间,endDate 结束时间
     * @return dateMap
     * @author wangwx 计算日期 返回Map startDate 比 endDate 早 xx年xx月xx天xx小时xx分钟前
     */
    public static Map<String, Integer> compareToGetMap(Date startDate, Date endDate) {
        Map<String, Integer> dateMap = new HashMap();

        // if (startDate.after(endDate)) {
        // return "0分钟前";
        // }
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(endDate);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(startDate);

        int yearAgo = nowCal.get(Calendar.YEAR) - dateCal.get(Calendar.YEAR);
        int monthAgo = nowCal.get(Calendar.MONTH) - dateCal.get(Calendar.MONTH);
        if (monthAgo < 0) {
            yearAgo--;
            monthAgo += 12;
        }
        int dayAgo = nowCal.get(Calendar.DAY_OF_MONTH) - dateCal.get(Calendar.DAY_OF_MONTH);
        if (dayAgo < 0) {
            monthAgo--;
            dayAgo += dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (monthAgo < 0) {
                yearAgo--;
                monthAgo += 12;
            }
        }
        int hourAgo = nowCal.get(Calendar.HOUR_OF_DAY) - dateCal.get(Calendar.HOUR_OF_DAY);
        if (hourAgo < 0) {
            dayAgo--;
            hourAgo += 24;
            if (dayAgo < 0) {
                monthAgo--;
                dayAgo += dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (monthAgo < 0) {
                    yearAgo--;
                    monthAgo += 12;
                }
            }
        }
        int minuteAgo = nowCal.get(Calendar.MINUTE) - dateCal.get(Calendar.MINUTE);
        if (minuteAgo < 0) {
            hourAgo--;
            minuteAgo += 60;
            if (hourAgo < 0) {
                dayAgo--;
                hourAgo += 24;
                if (dayAgo < 0) {
                    monthAgo--;
                    dayAgo += dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (monthAgo < 0) {
                        yearAgo--;
                        monthAgo += 12;
                    }
                }
            }
        }

        dateMap.put("year", yearAgo < 1 ? 0 : (yearAgo));
        dateMap.put("month", monthAgo < 1 ? 0 : (monthAgo));
        dateMap.put("day", dayAgo < 1 ? 0 : (dayAgo));
        dateMap.put("hour", hourAgo < 1 ? 0 : (hourAgo));
        dateMap.put("minute", minuteAgo < 1 ? 0 : (minuteAgo));

        return dateMap;
    }


    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }


    /**
     * 获取指定小时
     * eg : hour=9 获取当天九点的时间对象
     *
     * @param
     * @return
     */
    public static Date getHourOfDay(Integer hour, Integer min, Integer second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 获取指定小时
     * eg : hour=9 获取当天九点的时间对象
     *
     * @param
     * @return
     */
    public static Date getHourOfDay(Date date, Integer hour, Integer min, Integer second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }


    public static String long2Time(long times, int start, int end) {
        return String.valueOf(new Timestamp(times)).substring(start, end);
    }

    /**
     * 显示X小时前/X分钟前/X秒前,超过一天显示具体日期时间
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        /*if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }*/
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }

        //超过一天显示具体日期时间
        return getDate(date, simpleDateFormat);

		/*if (delta < 48L * ONE_HOUR) {
			return "昨天";
		}
		if (delta < 30L * ONE_DAY) {
			long days = toDays(delta);
			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}
		if (delta < 12L * 4L * ONE_WEEK) {
			long months = toMonths(delta);
			return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
		} else {
			long years = toYears(delta);
			return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
		}*/
    }

    /**
     * 判断两个日期是否在同一天
     *
     * @param date1
     * @param date2
     * @return true 是  false 不是
     */
    public static boolean isSameDate(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);
        return isSameDate;
    }

    /**
     * 获取两个日期之间的所有日期 (不包括开始和结束日期)
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static List<Date> getBetweenDates(Date start, Date end) {

        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    public static String getDayOfWeek() {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return
     */
    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if (start.getTime() >= end.getTime()) {
                return null;
            }

            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate() {
        return getDate(new Date());
    }

    public static String getDate(String format) {
        return getDate(new Date(), format);
    }

    public static String getDate(Date date) {
        return getDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDate(Date date, String format) {
        if (date == null)
            return "-";

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return getDate(date, formatter);
    }

    public static String getDate(Date date, SimpleDateFormat formatter) {
        if (date == null)
            return "-";

        String datenewformat = formatter.format(date);
        return datenewformat;
    }

    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public static String formatTime(long millisecond) {
        int minute;//分钟
        int second;//秒数
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    public static String format(long milliseconds) {
        return formatDateByPattern(milliseconds, DEFAULT_PATTERN);
    }

    public static long parse(String string) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault());
            return dateFormat.parse(string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ERROR_PARSE_VALUE;
    }

    /**
     * 根据转换类型 修改时间
     *
     * @param dateStr
     * @return
     */
    public static String parseDateByFormat(String dateStr, String patternStr) {
        try {
            Date currenTimeZone = dateFormat.parse(dateStr);
            return formatDateByPattern(currenTimeZone.getTime(), patternStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    //时间转换 yyyy-MM-dd

    public static String formatDatePattern(long milliseconds) {
        return formatDateByPattern(milliseconds, DATE_PATTERN);
    }
    //根据 指定时间转换格式 从时间戳转换String

    public static String formatDateByPattern(long milliseconds, String datePattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
        return dateFormat.format(milliseconds);
    }

    public static String formatDateHasMs(long milliseconds) {
        return formatDateByPattern(milliseconds, PATTERN_HAS_SS);
    }

    public static String formatDateOnlyMs(long milliseconds) {
        return formatDateByPattern(milliseconds, PATTERN_ONLY_MS_SS);
    }

    public static String formatHourTime(long time) {
        String result = hourTimeFormat.format(new Date(time));
        return result;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    /*private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }*/

    public static void main(String[] args) {
        System.out.println(formatHourTime(System.currentTimeMillis() - 60 * 60 * 1000));
    }

}
