package com.lin.mobile.convenient;

import com.wxample.data.MyData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间
 * Created by admin on 2018/7/6.
 */
public class MyDateFun {
    /**
     * 一分钟的毫秒值
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;


    /**
     * 毫秒转换 文字 x天x小时x分x秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 9) {
            sb.append(day + "天");
        } else if (day < 10) {
            sb.append("0" + day + "天");
        } else {
            sb.append("00" + "天");
        }

        if (hour > 9) {
            sb.append(hour + "小时");
        } else if (hour < 10) {
            sb.append("0" + hour + "小时");
        } else {
            sb.append("00" + "小时");
        }

        if (minute > 9) {
            sb.append(minute + "分");
        } else if (minute < 10) {
            sb.append("0" + minute + "分");
        } else {
            sb.append("00" + "分");
        }
        if (second > 9) {
            sb.append(second + "秒");
        } else if (second < 10) {
            sb.append("0" + second + "秒");
        } else {
            sb.append("00" + "秒");
        }
        if (milliSecond > 0) {
            // sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

    /**
     * 离当前时间差多少天
     *
     * @param endTime
     * @param format
     * @return
     */
    public static String formatTime(String endTime, String format) {
        long starTimeLong = System.currentTimeMillis();
        long timePassed = getTimerLongs(endTime, format) - starTimeLong;
        return formatTime(timePassed, "day");
    }

    /**
     * 毫秒转化时分秒
     *
     * @param ms
     * @param ip 返回那个标志
     * @return
     */
    public static String formatTime(Long ms, String ip) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String sb = "";
        if (ip.equals("day")) {
            if (day < 0) {
                day = 0l;
            }
            if (hour > 0)
                day++;
            sb = MyData.mToInt(day) + "";
        }
        if (ip.equals("times")) {
            sb = hour + "";
        }
        if (ip.equals("branch")) {
            sb = minute + "";
        }
        if (ip.equals("second")) {
            sb = second + "";
        }
        return sb.toString();
    }

    /**
     * 文字转long
     *
     * @param stringTimer 时间
     * @param format      时间格式 （如 yyyy年MM月dd日）
     * @return
     */
    public static Long getTimerLongs(String stringTimer, String format) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 得到指定模范的时间
        try {
            Date d1 = sdf.parse(stringTimer);
            return d1.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sdf = null;
        return 0l;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str, String formatString) {
        if (str == null)
            return new Date();
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * long转换日期文字
     *
     * @param time
     * @param formatString
     * @return
     */
    public static String longToString(Long time, String formatString) {
        Date date;
        String str = "";
        try {
            date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            str = sdf.format(date);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 日期格式转换
     *
     * @param current 当前格式 如(yyyy-MM-dd)
     * @param trans   转换后的格式(yyyy年MM月dd日)
     * @param time    时间
     * @return
     */
    public static String timeFormatTransformation(String current, String trans,
                                                  String time) {
        if (current == null || trans == null || time == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(current);
        SimpleDateFormat sdf2 = new SimpleDateFormat(trans);
        String str = "";
        try {
            Date date = new Date(sdf.parse(time).getTime());
            str = sdf2.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str = time;
        }
        return str;
    }

    /**
     * 根据传入的时间算出距今有多久
     */
    public static String refreshUpdatedAtValue(String timer, String format) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - getTimerLongs(timer, format);
        long timeIntoFormat;
        String updateAtValue = "";
        if (timePassed < ONE_MINUTE || timePassed == currentTime) {
            updateAtValue = "刚刚";
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            updateAtValue = timeIntoFormat + "分钟前";
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            updateAtValue = timeIntoFormat + "小时前";
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            updateAtValue = timeIntoFormat + "天前";
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            updateAtValue = timeIntoFormat + "个月前";
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            updateAtValue = timeIntoFormat + "年前";
        }
        return updateAtValue;
    }

    /**
     * 根据传入的时间算出距今还有有多久
     *
     * @param timer   开始时间
     * @param endtime 结束时间 在结束时间前都会返回现在
     * @return
     */
    public static String refreshUpdatedValue(String timer, String endtime, String format) {
        long currentTime = System.currentTimeMillis();
        long timePassed = getTimerLongs(timer, format) - currentTime;
        long timeIntoFormat;
        long timeend = 0;
        if (endtime != null)
            timeend = currentTime - getTimerLongs(endtime, format);
        String updateAtValue = "";
        if (timePassed < ONE_MINUTE || timeend > 0) {
            updateAtValue = "现在";
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            updateAtValue = timeIntoFormat + "分钟后";
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            updateAtValue = timeIntoFormat + "小时后";
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            updateAtValue = timeIntoFormat + "天后";
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            updateAtValue = timeIntoFormat + "个月后";
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            updateAtValue = timeIntoFormat + "年后";
        }
        return updateAtValue;
    }

    /**
     * 根据传入的时间算出距今还有有多久
     *
     * @param timer
     * @return
     */
    public static String refreshUpdatedValue(String timer, String format) {
        return refreshUpdatedValue(timer, null, format);
    }


    /**
     * 获取时间
     *
     * @param format
     * @param datePlus
     * @return
     */
    private static String getCurrentDate(String format, Date datePlus) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(datePlus);
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 获取修改时间
     *
     * @param format
     * @param days   加减天数
     * @return
     */
    public static String datePlus(String format, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DATE, days);
        return getCurrentDate(format, cal.getTime());
    }

    /**
     * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
     *
     * @param date null代表当前
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 时间比较
     *
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 如果第一个时间大返回true 否则返回false
     */
    public static boolean timeCompare(String time1, String time2, String format) {
        if (getTimerLongs(time1, format) - getTimerLongs(time2, format) > 0)
            return true;
        else
            return false;
    }

    /**
     * 两个日期相差不能大于多少天
     *
     * @param time1  结束日期
     * @param time2  开始日期
     * @param format 格式
     * @param day    天
     * @return
     */
    public static boolean timeComparejudge(String time1, String time2, String format, int day) {
        if ((getTimerLongs(time2, format) - getTimerLongs(time1, format)) <= (ONE_DAY * day))
            return true;
        else
            return false;
    }
}
