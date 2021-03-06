package com.widget.time;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.example.tools.R;
import com.lin.mobile.entity.TimeEntity;

import android.R.integer;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    public int screenheight;
    @Deprecated
    private boolean hasSelectTime;
    private int START_YEAR = 1950, END_YEAR = 2050;

    private int maxmonth;
    private int maxday;
    private int maxTime;
    private int maxBranch;

    /**
     * 时间格式
     */
    private String timeFormat = "";

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getSTART_YEAR() {
        return START_YEAR;
    }

    public void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public int getEND_YEAR() {
        return END_YEAR;
    }

    public void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public WheelMain(View view) {
        super();
        this.view = view;
        hasSelectTime = false;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectTime) {
        super();
        this.view = view;
        this.hasSelectTime = hasSelectTime;
        setView(view);
    }

    /**
     * 设置当前时间为最大选择限制
     */
    public void maxCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        END_YEAR = calendar.get(Calendar.YEAR);
        maxmonth = calendar.get(Calendar.MONTH) + 1;
        maxday = calendar.get(Calendar.DAY_OF_MONTH);
        maxTime = calendar.get(calendar.HOUR_OF_DAY);
        maxBranch = calendar.get(calendar.MINUTE);
    }

    /**
     * 设置时间格式
     */
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public void initDateTimePicker(int year, int month, int day) {
        this.initDateTimePicker(year, month, day, 0, 0);
    }

    public void initDateTimePicker(Calendar calendar) {
        this.initDateTimePicker(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day, int h, int m) {
        // int year = calendar.get(Calendar.YEAR);
        // int month = calendar.get(Calendar.MONTH);
        // int day = calendar.get(Calendar.DATE);
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
//		wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, maxmonth > 0 ? maxmonth : 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (day != 0) {
            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 ? maxday : 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 ? maxday : 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 ? maxday : 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 ? maxday : 28));
            }
            wv_day.setLabel("日");
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setVisibility(View.GONE);
        }
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);
        //时
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCyclic(true);// 可循环滚动
        wv_hours.setLabel("时");// 添加文字
        wv_hours.setCurrentItem(h);

        //分
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setCyclic(true);// 可循环滚动
        wv_mins.setLabel("分");// 添加文字
        wv_mins.setCurrentItem(m);
        int sum = 0;
        //如果有时间格式，用时间格式的方式
        if (timeFormat != null && timeFormat.length() > 0) {
            //年
            if (timeFormat.contains("yyyy")) {
                sum++;
                wv_year.setVisibility(View.VISIBLE);
            } else
                wv_year.setVisibility(View.GONE);
            //月
            if (timeFormat.contains("MM")) {
                wv_month.setVisibility(View.VISIBLE);
                sum++;
            } else
                wv_month.setVisibility(View.GONE);
            //日
            if (timeFormat.contains("dd")) {
                wv_day.setVisibility(View.VISIBLE);
                sum++;
            } else
                wv_day.setVisibility(View.GONE);
            //时
            if (timeFormat.contains("HH")) {
                wv_hours.setVisibility(View.VISIBLE);
                sum++;
            } else
                wv_hours.setVisibility(View.GONE);
            //分
            if (timeFormat.contains("mm")) {
                sum++;
                wv_mins.setVisibility(View.VISIBLE);
            } else
                wv_mins.setVisibility(View.GONE);
        } else {
            if (hasSelectTime) {
                wv_hours.setVisibility(View.VISIBLE);
                wv_mins.setVisibility(View.VISIBLE);
            } else {
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
            }
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && year_num == END_YEAR ? maxday : 31));
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && year_num == END_YEAR ? maxday : 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && year_num == END_YEAR ? maxday : 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && year_num == END_YEAR ? maxday : 28));
                }
                if (maxmonth > 0) {
                    wv_month.setAdapter(new NumericWheelAdapter(1, maxmonth > 0 && year_num == END_YEAR ? maxmonth : 12));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && month_num == maxmonth ? maxday : 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && month_num == maxmonth ? maxday : 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && month_num == maxmonth ? maxday : 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, maxday > 0 && month_num == maxmonth ? maxday : 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime || sum > 3)
            textSize = (screenheight / 100) * 3;
        else
            textSize = (screenheight / 100) * 4;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        int y = (wv_month.getCurrentItem() + 1);
        int r = (wv_day.getCurrentItem() + 1);
        int s = wv_hours.getCurrentItem();
        int f = wv_mins.getCurrentItem();
        String yue = y < 10 ? "0" + y : y + "";
        String ri = r < 10 ? "0" + r : r + "";
        String shi = s < 10 ? "0" + s : s + "";
        String fen = f < 10 ? "0" + f : f + "";
        //如果有时间格式，用时间格式的方式
        if (timeFormat != null && timeFormat.length() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, wv_year.getCurrentItem() + START_YEAR);
            calendar.set(Calendar.MONTH, y - 1);
            calendar.set(Calendar.DAY_OF_MONTH, r);
            calendar.set(Calendar.HOUR_OF_DAY, s);
            calendar.set(Calendar.MINUTE, f);
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
            sb.append(dateFormat.format(calendar.getTime()));
        } else {
            if (!hasSelectTime) {
                if (wv_day.getVisibility() == View.VISIBLE) {
                    sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                            .append(yue).append("-").append(ri);
                } else {
                    sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                            .append(yue);
                }

            } else {
                sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                        .append(yue).append("-").append(ri).append(" ").append(shi)
                        .append(":").append(fen);
            }
        }
        return sb.toString();
    }

    /**
     * 返回详细时间
     *
     * @return
     */
    public TimeEntity getTimeEn() {
        return new TimeEntity(wv_year.getCurrentItem() + START_YEAR,
                wv_month.getCurrentItem() + 1, wv_day.getCurrentItem() + 1,
                wv_hours.getCurrentItem(), wv_mins.getCurrentItem());
    }
}
