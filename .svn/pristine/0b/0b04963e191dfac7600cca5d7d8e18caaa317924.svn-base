package com.sy.mobile.control;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tools.R;
import com.lin.mobile.entity.TimeEntity;
import com.sy.mobile.net.HttpDream.Cont;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

/**
 * 滚动时间控件
 *
 * @author Administrator
 */
public class Wheel {
    /**
     * 显示年月日时分
     */
    public final static boolean HAVETIME = true;
    /**
     * 显示年月日
     */
    public final static boolean NOTIME = false;
    Context context;
    /**
     * 不要日期
     */
    boolean notDay;
    /**
     * 是否限制当前时间为最大时间
     */
    boolean maxcu;

    public Wheel(Context context) {
        this.context = context;
    }

    public Wheel(Context context, boolean notDay) {
        this.context = context;
        this.notDay = notDay;
    }

    WheelMain wheelMain;
    View timepickerview;
    TextView showText;

    public interface OnCheckOcl {
        /**
         * @param 返回数据
         */
        public void Check(String content);
    }

    public interface OnCheckOclById {
        /**
         * @param 返回数据
         */
        public void Check(String content, int id);
    }

    public interface OnCheckOclByEn {
        /**
         * @param 返回数据
         */
        public void Check(String content, TimeEntity timeentity);
    }


    OnCheckOclById callbacktoid;
    OnCheckOcl callback;
    OnCheckOclByEn onceckonbyen;

    /**
     * 显示时间选择器
     *
     * @param v 要显示的控件
     * @param i true 时
     */
    @Deprecated
    public void showTime(final TextView v, Boolean i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        timepickerview = inflater.inflate(R.layout.timepicker, null);
        showText = v;
        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        wheelMain = new WheelMain(timepickerview, i);
        wheelMain.screenheight = screenInfo.getHeight();
        //限制
        if (maxcu)
            wheelMain.maxCurrentTime();
        String time = showText.getText().toString();
        Calendar calendar = Calendar.getInstance();

        if (JudgeDate.isDate(time, "yyyy-MM-dd") || JudgeDate.isDate(time, "yyyy-MM-dd HH:mm") || JudgeDate.isDate(time, "yyyy-MM")) {
            try {
                if (i) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    calendar.setTime(dateFormat.parse(time));
                } else if (notDay == false) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    calendar.setTime(dateFormat.parse(time));
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                    calendar.setTime(dateFormat.parse(time));
                }
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day;
        if (notDay) {
            day = 0;
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (i) {
            int when = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            wheelMain.initDateTimePicker(year, month, day, when, minute);
        } else {
            wheelMain.initDateTimePicker(year, month, day);
        }
        showView();
    }

    /**
     * 显示时间选择窗口
     *
     * @param v
     * @param format 时间格式 （yyyy年MM月dd日 HH时mm分）
     */
    public void showTime(final TextView v, String format) {
        LayoutInflater inflater = LayoutInflater.from(context);
        timepickerview = inflater.inflate(R.layout.timepicker, null);
        showText = v;
        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.setTimeFormat(format);
        wheelMain.screenheight = screenInfo.getHeight();
        //限制
        if (maxcu)
            wheelMain.maxCurrentTime();
        String time = showText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        //如果有时间，传入时间
        if (time.length() > 0) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                calendar.setTime(dateFormat.parse(time));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        wheelMain.initDateTimePicker(calendar);
        showView();
    }


    /**
     * 显示弹窗
     */
    private void showView() {
        new AlertDialog.Builder(context)
                .setTitle("选择时间")
                .setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showText.setText(wheelMain.getTime());
                        if (callback != null)
                            callback.Check(wheelMain.getTime());
                        if (callbacktoid != null)
                            callbacktoid.Check(wheelMain.getTime(), showText.getId());
                        if (onceckonbyen != null)
                            onceckonbyen.Check(wheelMain.getTime(), wheelMain.getTimeEn());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    /**
     * 只能选择当前时间以后
     */
    public void setMaxCurrentTime() {
        maxcu = true;
    }

    /**
     * 点击回调
     *
     * @param callback
     */
    public void setOnCheckTheReturnValue(final OnCheckOcl callback) {
        this.callback = callback;
    }

    /**
     * 点击回调加返回点击id
     *
     * @param callback
     */
    public void setOnCheckTheReturnValueToid(final OnCheckOclById callbacktoid) {
        this.callbacktoid = callbacktoid;
    }

    /**
     * 点击返回字符加实体类
     *
     * @param callbacktoid
     * @param timeentity
     */
    public void setOnCheckTheReturnValueToEn(final OnCheckOclByEn callbacktoid) {
        this.onceckonbyen = callbacktoid;
    }

}
