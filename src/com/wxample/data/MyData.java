package com.wxample.data;

//import com.baidu.location.BDLocation;
//import com.sy.mobile.gis.Function;
//import com.sy.mobile.gis.Function.CallBack;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tools.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sy.mobile.control.MyDialog;
import com.sy.mobile.picture.ImagePagerActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author admin
 */
public class MyData {
    private static Toast toast = null;
    /**
     * 等额本息
     */
    public final static int PRINCIPAL = 1;
    /**
     * 等额本金
     */
    public final static int CAPITAI = 2;

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

    // 控件高宽
    int topcenint = 0, width = 0;

    public interface SetWHReturn {
        public void setWHComplete();
    }

    SetWHReturn setwhr;

    /**
     * String
     *
     * @param obj
     */
    public static String mToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 转换double
     * @param obj
     * @return
     */
    public static Double mToDouble(Object obj) {
        return mToDouble(obj, -1);
    }

    /**
     * 转换double 保留几位小数
     * @param obj
     * @param scale
     * @return
     */
    public static Double mToDouble(Object obj, int scale) {
        try {
            if (obj == null) {
                return 0.00;
            }
            BigDecimal b = new BigDecimal(mToString(obj));
            if (scale == -1)
                return b.doubleValue();
            else
                return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.00;
        }
    }

    /**
     * 转换double 显示小数点后两位
     * @param obj
     * @return
     */
    public static String doubleTwo(Object obj) {
        return stringFormat(mToDouble(obj), "0.00");
    }

    /**
     * 转换double 去除小数点后面的0
     * @param object
     * @return
     */
    public static String toDoubleNotD(Object object) {
        String s = MyData.mToString(object);
        if (s.indexOf(".") > 0) {
            //正则表达
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return s;
    }

    /**
     * 去除小数点与后面的零
     *
     * @return
     */
    public static String notEnd(Object object) {
        String s = MyData.mToString(object);
        if (s.indexOf(".") != -1) {
            return s.substring(0, s.indexOf("."));
        } else {
            return s;
        }
    }

    /**
     * 转int
     *
     * @param obj
     * @return
     */
    public static int mToInt(Object obj) {
        int i = 0;
        if (obj == null) {
            return 0;
        }
        try {
            i = Integer.parseInt(mToString(obj));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                Double d_s = new Double((Double) obj);
                i = d_s.intValue();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return i;
    }

    /**
     * 比较
     *
     * @param one
     * @param two
     * @return
     */
    public static boolean equals(Object one, Object two) {
        if (one == null || two == null)
            return false;
        return one.equals(two);
    }

	/* 时间 */

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
     * 毫秒转化时分秒
     *
     * @param ms
     * @param ip 返回那个标志
     * @return
     */
    @Deprecated
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
            sb = day + "";
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
        if (sb.length() == 1) {
            sb = "0" + sb;
        }
        return sb.toString();
    }

    /**
     * 日期转换（yyyy-MM-dd HH:mm:ss）
     *
     * @param timer
     * @return
     */
    @Deprecated
    public static String zTimer(Object timer) {
        String str = "";
        Date date;
        try {
            date = new Date(Long.parseLong(mToString(timer)));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 日期转换 yyyy-MM-dd
     *
     * @param timer
     * @return
     */
    @Deprecated
    public static String zTimer(Object timer, String tupe) {
        String str = "";
        Date date;
        try {
            date = new Date(Long.parseLong(mToString(timer)));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            str = sdf.format(date);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 文字转换long
     *
     * @param stringTimer
     * @return
     */
    @Deprecated
    public static Long getTimerLong(String stringTimer) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 得到指定模范的时间
        try {
            Date d1 = sdf.parse(stringTimer);
            return d1.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 文字转换long
     *
     * @param stringTimer
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
     * 文字转换long
     *
     * @param stringTimer
     * @return
     */
    @Deprecated
    public static Long getTimerLong2(String stringTimer) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 得到指定模范的时间
        try {
            Date d1 = sdf.parse(stringTimer);
            return d1.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return getTimerLong(stringTimer);
        }
//		return 0l;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    @Deprecated
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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
     * 日期转毫秒
     *
     * @param timer
     * @return
     */
    @Deprecated
    public static long timerStringTurnLong(String timer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long tim = 0;
        try {
            tim = sdf.parse(timer).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tim;
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
        }
        return str;
    }

    /**
     * 根据传入的时间算出距今有多久
     */
    public static String refreshUpdatedAtValue(String timer) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - timerStringTurnLong(timer);
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
    public static String refreshUpdatedValue(String timer, String endtime) {
        long currentTime = System.currentTimeMillis();
        long timePassed = timerStringTurnLong(timer) - currentTime;
        long timeIntoFormat;
        long timeend = 0;
        if (endtime != null)
            timeend = currentTime - timerStringTurnLong(endtime);
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
    public static String refreshUpdatedValue(String timer) {
        return refreshUpdatedValue(timer, null);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDate(String format, Date datePlus) {
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
     * 修改日期
     *
     * @param days
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
    public static boolean timeCompare(String time1, String time2) {
        if (timerStringTurnLong(time1) - timerStringTurnLong(time2) > 0)
            return true;
        else
            return false;
    }

    /**
     * 多余切割
     *
     * @param zi
     * @return
     */
    public static String cutting(Object zi) {
        String i = "";
        if (zi == null)
            return i;
        i = zi.toString();
        int ii = i.indexOf(".0");
        if (ii >= 0) {
            i = i.substring(0, ii);
        }
        return i;
    }

    // /**
    // * tos提示框
    // *
    // * @param msg
    // */
    // public static void showTextToast(String msg, Context context) {
    // if (toast == null) {
    // toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    // } else {
    // toast.setText(msg);
    // }
    // toast.show();
    // }

    /**
     * 拨打电话
     *
     * @param phones
     */
    public static void sendPhone(Context context, String phones) {
        if (phones == null) {
            MyDialog.showTextToast("电话号码不存在", context);
            return;
        }
//        if (isMobileNum(phones, context)) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phones);
        intent.setData(data);
        context.startActivity(intent);
//        }
    }

    public static void sendPhone(final Context context, final String phones, boolean isShow) {
        if (phones == null) {
            MyDialog.showTextToast("电话号码不存在", context);
            return;
        }
        MyDialog.createChoiceDialog(context, context.getString(R.string.prompting_phone) + phones, null, null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                MyDialog.closeDialog();
                MyData.sendPhone(context, phones);
            }
        });
    }

    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }

    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        // 当然在此之前，你要判断分母是否为0，
        // 为0你可以根据实际需求做相应的处理
        if (d1 == 0 || d2 == 0)
            return 0;
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 房贷计算
     *
     * @param aa   总金额
     * @param yue  月数
     * @param aal  利率
     * @param type
     */
    public static Map<String, Double> reckon(double aa, int yue, double aal,
                                             int type) {
        Map<String, Double> map = new HashMap<String, Double>();
        aal = (aal / 100) / 12;
        if (type == PRINCIPAL) {
            // // 等额本息还款法:
            // // 每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            //
            // // 总还款 =还款月数×每月月供额

            double yuegong = new BigDecimal(div(
                    mul(aa, mul(aal, Math.pow((1 + aal), yue))),
                    Math.pow((1 + aal), yue) - 1, 2)).setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            double zonghuan = new BigDecimal(mul(yuegong, yue)).setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            double zongli = new BigDecimal(sub(zonghuan, aa)).setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("zonghuan", zonghuan);
            map.put("yue", (double) yue);
            map.put("zongli", zongli);
            map.put("yuegong", yuegong);
            return map;
        } else {
            // 等额本金还款法:
            // 每月月供额=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率

            // 总还款 =〔(贷款本金÷还款月数+贷款本金×月利率)+贷款本金÷还款月数×(1+月利率)〕÷2×还款月数
            double bjyue = new BigDecimal(sum(div(aa, yue, 3),
                    mul(sub(aa, 0), aal)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double bjzong = new BigDecimal(mul(
                    div(sum(sum(div(aa, yue, 2), mul(aa, aal)),
                            mul(div(aa, yue, 2), (1 + aal))), 2, 2), yue))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double bjzongli = new BigDecimal(sub(bjzong, aa)).setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("zonghuan", bjzong);
            map.put("yue", (double) yue);
            map.put("zongli", bjzongli);
            map.put("yuegong", bjyue);
            return map;
        }
    }

    /**
     * 房贷相加
     *
     * @param map1
     * @param map2
     * @return
     */
    public static Map<String, String> reckonSum(Map<String, Double> map1,
                                                Map<String, Double> map2) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("zonghuan",
                mToString(sum(map1.get("zonghuan"), map2.get("zonghuan"))));
        map.put("yue", mToString(map1.get("yue")));
        map.put("zongli",
                mToString(sum(map1.get("zongli"), map2.get("zongli"))));
        map.put("yuegong",
                mToString(sum(map1.get("yuegong"), map2.get("yuegong"))));
        return map;
    }

    /**
     * 把view转换成bitmap
     *
     * @param addViewContent
     * @return
     */
    public static Bitmap getViewBitmap(View addViewContent) {

        Bitmap bitmap = null;
        try {
            addViewContent.setDrawingCacheEnabled(true);

            addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(),
                    addViewContent.getMeasuredHeight());

            addViewContent.buildDrawingCache();
            Bitmap cacheBitmap = addViewContent.getDrawingCache();
            bitmap = Bitmap.createBitmap(cacheBitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * null判断
     *
     * @param cent
     * @return
     */
    public static boolean isNull(Context cont, EditText... cent) {
        for (EditText tes : cent) {
            String arg = tes.getText().toString();
            if (arg != null && arg.length() > 0) {
            } else {
                MyDialog.showTextToast(mToString(tes.getHint()), cont);
                return false;
            }
        }
        return true;
    }

    /**
     * null判断
     *
     * @param cent
     * @return
     */
    public static boolean isNull(Context cont, TextView... cent) {
        for (TextView tes : cent) {
            String arg = tes.getText().toString();
            if (arg != null && arg.length() > 0) {

            } else {
                MyDialog.showTextToast(mToString(tes.getHint()), cont);
                return false;
            }
        }
        return true;
    }

    /**
     * 非空
     *
     * @param cont
     * @return
     */
    public static boolean isNull(String cont) {
        if (cont != null && cont.length() > 0 && !cont.equals("null")) {
            return true;
        } else
            return false;
    }

    /**
     * 传入数组返回list
     *
     * @return
     */
    public static List<String> getList(String[] cont) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < cont.length; i++) {
            list.add(cont[i]);
        }
        return list;
    }

    /**
     * 返回随机字符串
     *
     * @param list
     * @return
     */
    public static String getString(List<String> list) {
        Random rand = new Random();
        int num = rand.nextInt(list.size());
        String re = list.get(num);
        list.remove(num);
        return re;
    }

    /**
     * 返回随机数
     *
     * @param i 范围
     * @return
     */
    public static int getRandomNumber(int i) {
        Random rand = new Random();
        return rand.nextInt(i);
    }

    /**
     * 拨打电话提示框
     *
     * @param con
     * @param phon
     */
    public static void dioPhone(final Context con, final String phon) {
        MyDialog.createChoiceDialog(con, "将拨打电话：" + phon, null, null, null,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        MyData.sendPhone(con, phon);
                        MyDialog.closeDialog();
                    }
                });
    }

    /**
     * 判断手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles, Context con) {
        Pattern p = Pattern.compile("^1[0-9][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        if (m.matches() == false) {
            MyDialog.showTextToast("请输入正确的手机号码", con);
        }
        return m.matches();

    }

    /**
     * 查询是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 字符串格式化
     *
     * @param content 内容
     * @param format  格式
     * @return
     */

    public static String stringFormat(Object content, String format) {
        try {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(content);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 字符串保留小数点后几位
     *
     * @param content
     * @param i
     * @return
     */
    public static String stringFormat(String content, int i) {
        int ind = content.indexOf(".");
        if (content.indexOf(".") != -1) {
            return content.substring(0, ind + i + 1);
        } else {
            return content;
        }
    }

    /**
     * 按照宽度设置比例设置高度
     */
    public void setWProportion(double wpr, double hpr, View view,
                               SetWHReturn setwhr) {
        this.setwhr = setwhr;
        getMak(view, wpr, hpr, "高");

    }

    /**
     * 按照高度设置比例设置宽度
     */
    public void setHProportion(double wpr, double hpr, View view,
                               SetWHReturn setwhr) {
        this.setwhr = setwhr;
        getMak(view, wpr, hpr, "宽");

    }

    /**
     * 获取控件当前的宽高
     */
    private void getMak(final View view, final double wpr, final double hpr,
                        final String type) {
        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = view.getWidth();
                topcenint = view.getHeight();
                // textView.append("\n\n"+imageView.getHeight()+","+imageView.getWidth());
                if (type.equals("高")) {
                    MarginLayoutParams headerLayoutParams = (MarginLayoutParams) view
                            .getLayoutParams();
                    headerLayoutParams.height = MyData.mToInt(MyData.mul(
                            MyData.div(width, wpr, 1), hpr));
                    Log.i("whshow",
                            "宽:"
                                    + width
                                    + "高:"
                                    + MyData.mToInt(MyData.mul(
                                    MyData.div(width, wpr, 1), hpr)));
                    view.setLayoutParams(headerLayoutParams);
                    // stopt();
                    if (setwhr != null) {
                        setwhr.setWHComplete();
                    }
                } else {
                    MarginLayoutParams headerLayoutParams = (MarginLayoutParams) view
                            .getLayoutParams();
                    headerLayoutParams.width = MyData.mToInt(MyData.mul(
                            MyData.div(topcenint, hpr, 1), wpr));
                    Log.i("whshow",
                            "宽:"
                                    + MyData.mToInt(MyData.mul(
                                    MyData.div(topcenint, hpr, 1), wpr))
                                    + "高:" + topcenint);
                    view.setLayoutParams(headerLayoutParams);
                    // stopt();
                    if (setwhr != null) {
                        setwhr.setWHComplete();
                    }
                }

            }
        });
    }

    /**
     * 生成条纹码
     *
     * @param context
     * @param contents
     * @param desiredWidth
     * @param desiredHeight
     * @param displayCode
     * @return
     */
    public static Bitmap creatBarcode(Context context, String contents,
                                      int desiredWidth, int desiredHeight, boolean displayCode) {
        Bitmap ruseltBitmap = null;
        int marginW = 20;
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        if (displayCode) {
            Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
                    * marginW, desiredHeight, context);
            ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
                    0, desiredHeight));
        } else {
            ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
        }

        return ruseltBitmap;
    }

    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    protected static Bitmap creatCodeBitmap(String contents, int width,
                                            int height, Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                          PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = 20;
        Bitmap newBitmap = Bitmap.createBitmap(
                first.getWidth() + second.getWidth() + marginW,
                first.getHeight() + second.getHeight(), Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, marginW, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save();
        cv.restore();

        return newBitmap;
    }


    public static void showPic(String[] strscon, int index, Context context) {
        showPic(strscon, index, context, false);
    }

    /**
     * 显示图片
     *
     * @param strscon
     * @param index
     */
    public static void showPic(String[] strscon, int index, Context context, boolean isDown) {
        Intent intent = new Intent();
        intent.setClass(context, ImagePagerActivity.class);
        // 传入图片路径数组
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, strscon);
        // 传入要显示第几张图
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
        // 是否显示倒计时
        if (isDown)
            intent.putExtra(ImagePagerActivity.EXTRA_COUNT_DOWN, true);
        context.startActivity(intent);
    }

    /**
     * 显示图片
     *
     * @param strscon
     * @param index
     * @param context
     */
    public static void showPic(String strscon, int index, Context context) {
        if (strscon == null)
            return;
        showPic(strscon.split(","), index, context, false);
    }

    public static void showPic(List<String> strscon, String url, int index, Context context, boolean isDown) {
        if (strscon == null)
            return;
        String[] con = new String[strscon.size()];
        for (int i = 0; i < strscon.size(); i++) {
            con[i] = url + strscon.get(i);
        }
        showPic(con, index, context, isDown);
    }

    /**
     * 显示图片
     *
     * @param strscon
     * @param index
     * @param context
     */
    public static void showPic(List<String> strscon, String url, int index, Context context) {
        if (strscon == null)
            return;
        String[] con = new String[strscon.size()];
        for (int i = 0; i < strscon.size(); i++) {
            con[i] = url + strscon.get(i);
        }
        showPic(con, index, context, false);
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * string插入文字
     *
     * @param marStr
     * @param inserStr 要插入的文字
     * @param index    插入的地方
     * @return
     */
    public static String inserString(String marStr, String inserStr, int... index) {
        StringBuilder sb = new StringBuilder(marStr);
        for (int tes : index) {
            if (marStr.length() > tes)
                sb.insert(tes, inserStr);
        }
        return sb.toString();
    }

    /**
     * 保存图片到本地
     *
     * @param filename 文件地址 如果传null默认根目录
     * @param name     文件名字 如果传null默认当前时间long
     * @param bitmap
     */
    public static void saveBitmap(String filename, String name, Bitmap bitmap,
                                  Context cont) {
        if (!Environment.getExternalStorageDirectory().exists()) {
            MyDialog.showTextToast("没有sd卡,请插入sd卡", cont);
            return;
        }
        if (bitmap == null) {
            MyDialog.showTextToast("图片不存在,请确认后重试", cont);
            return;
        }
        String sdCardDir = Environment.getExternalStorageDirectory()
                + (filename == null ? "" : "/" + filename + "/");
        File dirFile = new File(sdCardDir); // 目录转化成文件夹
        if (!dirFile.exists()) { // 如果不存在，那就建立这个文件夹
            dirFile.mkdirs();
        }
        File file = new File(sdCardDir, (name == null ? System.currentTimeMillis() : name) + ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            MyDialog.showTextToast("图片已保存到" + sdCardDir, cont);
            MediaStore.Images.Media.insertImage(cont.getContentResolver(),
                    bitmap, sdCardDir, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            cont.sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置mar
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static String phonePwd(String mobile) {
        String str = "";
        for (int i = 0; i < mobile.length(); i++) {
            if (i == mobile.length() - 11) {
                str += mobile.charAt(i);
            } else if (i == mobile.length() - 10) {
                str += mobile.charAt(i);
            } else if (i == mobile.length() - 9) {
                str += mobile.charAt(i);
            } else if (i == mobile.length() - 3) {
                str += mobile.charAt(i);
            } else if (i == mobile.length() - 2) {
                str += mobile.charAt(i);
            } else if (i == mobile.length() - 1) {
                str += mobile.charAt(i);
            } else {
                str += "*";
            }
        }
        return str;
    }

    public static String formatNum(String num, Boolean kBool, int decimal) {
        return formatNum(num, kBool, decimal, false);
    }

    /**
     * <pre>
     * 数字格式化显示
     * 小于万默认显示 大于万以1.7万方式显示最大是9999.9万
     * 大于亿以1.1亿方式显示最大没有限制都是亿单位
     * make by dongxh 2017年12月28日上午10:05:22
     * </pre>
     *
     * @param num     格式化的数字
     * @param kBool   是否格式化千,为true,并且num大于999就显示999+,小于等于999就正常显示
     * @param decimal 保留几位小数
     * @return
     */
    public static String formatNum(String num, Boolean kBool, int decimal, boolean isS) {
        try {
//            Integer.parseInt(num);
            StringBuffer sb = new StringBuffer();
            if (kBool == null)
                kBool = false;

            BigDecimal b0 = new BigDecimal("1000");
            BigDecimal b1 = new BigDecimal("10000");
            BigDecimal b2 = new BigDecimal("100000000");
            BigDecimal b3 = new BigDecimal(num);

            String formatNumStr = "";
            String nuit = "";

            // 以千为单位处理
            if (kBool) {
                if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                    return "999+";
                }
                return num;
            }

            // 以万为单位处理
            if (b3.compareTo(b1) == -1) {
                sb.append(b3.toString());
            } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                    || b3.compareTo(b2) == -1) {
                formatNumStr = b3.divide(b1).toString();
                nuit = "万";
            } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
                formatNumStr = b3.divide(b2).toString();
                nuit = "亿";
            }
            if (!"".equals(formatNumStr)) {
                int i = formatNumStr.indexOf(".");
                if (i == -1) {
                    sb.append(formatNumStr).append(nuit);
                } else {
                    i = i + 1;
                    String v = formatNumStr.substring(i, i + 1);
//                    if (!v.equals("0")) {
                    //如果输入的保留小数点位数大于当前位数，显示当前位数 不然会越界
                    if (i + decimal > formatNumStr.length())
                        decimal = formatNumStr.length() - i;
                    String data = formatNumStr.substring(0, i + decimal);
                    //四舍五入
                    if (isS) {
                        //加一位用于四舍五入 如果那么多位就算了
                        if ((i + decimal + 1) <= formatNumStr.length()) {
                            data = MyData.div(MyData.mToDouble(formatNumStr.substring(0, (i + decimal + 1))), 1, decimal) + "";
                        }
                    }
                    sb.append(data).append(nuit);
//                    } else {
//                        sb.append(formatNumStr.substring(0, i + 2)).append(nuit);
//                    }
                }
            }
            if (sb.length() == 0)
                return "0";
            return sb.toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }
}
