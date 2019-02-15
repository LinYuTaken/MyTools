package com.sy.mobile.control;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.g.MyBaseAdapter;
import com.example.tools.R;
import com.lin.mobile.entity.DateSelect;
import com.sy.mobile.analytical.ScreenTools;
import com.wxample.data.MyData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间选择
 * Created by admin on 2017/12/5.
 */

public class DateSelection extends LinearLayout {
    GridView gridView1;
    Calendar calendarDate;
    ScreenTools screentools;
    //当前日历的时间
    int year, month, date, firstWeek = 1;

    public static int LAST_MONTH = 0;
    public static int NEXT_MONTH = 1;
    public static int THIS_MONTH = 2;

    private int layoutId;

    Context context;
    MyBaseAdapter dateSelectionAdapter;
    TextView timers;

    Map<String, Boolean> celectMap = new HashMap<String, Boolean>();
    /**
     * 显示的日期
     */
    List<DateSelect> dateList = new ArrayList<DateSelect>();
    Button determine, reset;

    int MaxDate;
    int week;
    int lastDate;

    /**
     * 显示方式 默认显示全部
     */
    boolean isAll = true;

    public DateSelection(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public DateSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DateSelection(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(layoutId != 0 ? layoutId : R.layout.dateselection, this);
        gridView1 = (GridView) findViewById(R.id.gridView1);
        timers = (TextView) findViewById(R.id.timers);
        determine = (Button) findViewById(R.id.determine);
        reset = (Button) findViewById(R.id.reset);

//        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
//        leftImage = (ImageView) findViewById(R.id.left_image);
//        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
//        rightImage = (ImageView) findViewById(R.id.right_image);
//        titleView = (TextView) findViewById(R.id.title);
//        titleLayout = (RelativeLayout) findViewById(R.id.root);
//        parseStyle(context, attrs);
        findViewById(R.id.topm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topMonth();
            }
        });
        findViewById(R.id.nextm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });
        if (reset != null) {
            findViewById(R.id.reset).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    celectMap.clear();
                    dateSelectionAdapter.notifyDataSetChanged();
                }
            });
        }
        dateSelectionAdapter = new DateSelectionAdapter(context, new ArrayList<DateSelect>());
        gridView1.setAdapter(dateSelectionAdapter);
//        showCalendar();
    }

    /**
     * 获取控件
     *
     * @param id
     * @return
     */
    public View getCustomView(int id) {
        return findViewById(id);
    }

    /**
     * 显示方式
     *
     * @param isAll true 显示全部，false 显示一周
     */
    public void displayMode(boolean isAll) {
        this.isAll = isAll;
        //如果是当月，显示当前周
        Calendar lastDate = Calendar.getInstance();
        if (lastDate.get(Calendar.MONTH) == month) {
            //天数除7 如果有余数的话，在加一
            int t = (lastDate.get(Calendar.DATE) + week) / 7;
            int y = (lastDate.get(Calendar.DATE) + week) % 7 > 0 ? 1 : 0;
            firstWeek = t + y;
            firstWeek = firstWeek == 0 ? 1 : firstWeek;
        } else
            firstWeek = 1;
        showCalendar();
    }

    public boolean getDisplayMode() {
        return isAll;
    }

    /**
     * 隐藏按钮
     */
    public void hideButton() {
        if (determine != null)
            determine.setVisibility(View.GONE);
        if (reset != null)
            reset.setVisibility(View.GONE);
    }

    /**
     * 设置自定义适配器
     */
    public void setCustomAdapter(MyBaseAdapter myBaseAdapter) {
        dateSelectionAdapter = myBaseAdapter;
        gridView1.setAdapter(dateSelectionAdapter);
    }

    /**
     * 传入自定义确认框布局
     * 传入自定义布局，但是必须有 内容控件content,取消控件cancel,确认confirm
     *
     * @param layoutId
     */
    public void setChoiceDialogCustom(int layoutId_) {
        layoutId = layoutId_;
        removeAllViews();
        init(context, null);
    }

    /**
     * 显示时间
     *
     * @param container   容器
     * @param currentDate 当前时间传null为当天
     */
    public void showCalendar() {
        dateList.clear();
        calendarDate = Calendar.getInstance();
        screentools = ScreenTools.instance(context);
        // 如果传入的时间不为空把时间传入
        if (year == 0 && month == 0 && date == 0) {
            year = calendarDate.get(Calendar.YEAR);
            month = calendarDate.get(Calendar.MONTH);
            date = calendarDate.get(Calendar.DATE);
        } else {
            calendarDate.set(Calendar.YEAR, year);
            calendarDate.set(Calendar.MONTH, month);
            calendarDate.set(Calendar.DATE, date);
        }
        // 获取当月总天数
        MaxDate = calendarDate.getActualMaximum(Calendar.DATE);
        // 获取本月的第一天是星期几
        week = getWeek() - 1;
        // 获取上月最后一天是几号
        lastDate = getPreviousMonthEnd();

        // 加载上个月最后几天
        //是否是显示周的模式
        if (isAll) {
            for (int i = 0; i < week; i++) {
                dateList.add(new DateSelect(0, 0, (lastDate - (week - (i + 1))), LAST_MONTH));
            }
        } else {
            //只有第一周才可能有上个月的
            if (firstWeek == 1) {
                for (int i = 0; i < week; i++) {
                    dateList.add(new DateSelect(0, 0, (lastDate - (week - (i + 1))), LAST_MONTH));
                }
            }
        }

        // 加载本月天数
        if (isAll) {
            for (int i = 0; i < MaxDate; i++) {
                dateList.add(new DateSelect(year, month + 1, (i + 1), THIS_MONTH));
            }
        } else {
            //数字是 7天乘星期 在减去7天，表示一星期的开始
            //条件是不大于最大天数 与 数字小于 天数减去上个月的天数
            int t = (7 * firstWeek) - week;
            for (int i = (7 * firstWeek - 7) - (firstWeek != 1 ? week : 0); i < MaxDate && i < t; i++) {
                dateList.add(new DateSelect(year, month + 1, (i + 1), THIS_MONTH));
            }
        }

        // 加载下个月开始几天
        if (isAll) {
            int max = MaxDate + week > 35 ? 42 : 35;
            for (int i = 0; i < max - (MaxDate + week); i++) {
                dateList.add(new DateSelect(0, 0, (i + 1), NEXT_MONTH));
            }
        } else {
            //条件是周数 小于 最大天数加上月的
            for (int i = 0; i < 7 * firstWeek - (MaxDate + week); i++) {
                dateList.add(new DateSelect(0, 0, (i + 1), NEXT_MONTH));
            }
        }
        dateSelectionAdapter.assLie(dateList);
        timers.setText(year + "年" + getMonth() + "月");
    }

    class DateSelectionAdapter extends MyBaseAdapter<DateSelect> {

        public DateSelectionAdapter(Context context, List<DateSelect> list) {
            mInf = LayoutInflater.from(context);
            this.list = list;
//            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final DateSelect map = list.get(position);

            Holder holder = null;

            if (convertView == null) {
                holder = new Holder();
                convertView = mInf.inflate(R.layout.tim_item, null);
                holder.day = (TextView) convertView.findViewById(R.id.day);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.day.setText(map.getDay() + "");
            //时间
            final String times = map.getYear() + "-" + MyData.stringFormat(map.getMonth(), "00")
                    + "-" + MyData.stringFormat(map.getDay(), "00");
            if (map.getType() == LAST_MONTH) {
                holder.day.setTextColor(Color.parseColor("#CCCCCC"));
                holder.day.setBackgroundDrawable(null);
                holder.day.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        topMonth();
                    }
                });
            } else if (map.getType() == THIS_MONTH) {
                if (celectMap.get(times) != null && celectMap.get(times)) {
                    holder.day.setTextColor(Color.parseColor("#ffffff"));
                    holder.day.setBackgroundResource(R.drawable.jin);
                    holder.day.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            celectMap.put(times, false);
                            if (dayClick != null)
                                dayClick.dayClick();
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    holder.day.setTextColor(Color.parseColor("#666666"));
                    holder.day.setBackgroundDrawable(null);
                    holder.day.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            celectMap.put(times, true);
                            if (dayClick != null)
                                dayClick.dayClick();
                            notifyDataSetChanged();
                        }
                    });
                }
            } else if (map.getType() == NEXT_MONTH) {
                holder.day.setTextColor(Color.parseColor("#CCCCCC"));
                holder.day.setBackgroundDrawable(null);
                holder.day.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextMonth();
                    }
                });
            }
            return convertView;
        }

        class Holder {
            TextView day;
        }
    }


    /**
     * 下一个月
     */
    public void nextMonth() {
        //是否周的显示模式
        if (isAll) {
            if (month == 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
        } else {
            //周模式
            if (firstWeek * 7 < MaxDate + week) {
                firstWeek++;
            } else {
                if (month == 11) {
                    month = 0;
                    year++;
                } else {
                    month++;
                }
                firstWeek = 1;
            }
        }
        if (paging != null)
            paging.next(year + "-" + MyData.stringFormat((month + 1), "00") + "-01");
        showCalendar();
    }

    /**
     * 上一个月
     */
    public void topMonth() {
        //是否周的显示模式
        if (isAll) {
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
        } else {
            if (firstWeek > 1) {
                firstWeek--;
            } else {
                if (month == 0) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
                firstWeek = ((lastDate + week) / 7) + ((lastDate + week) % 7 != 0 ? 1 : 0);
            }
        }
        if (paging != null)
            paging.last(year + "-" + MyData.stringFormat((month + 1), "00") + "-01");
        showCalendar();
    }

    /**
     * 重置时间
     */
    public void resetTime() {
        year = 0;
        month = 0;
        date = 0;
        showCalendar();
    }

    /**
     * 返回年份
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * 返回月份
     *
     * @return
     */
    public int getMonth() {
        return month + 1;
    }

    /**
     * 返回日期
     *
     * @return
     */
    public int getDa() {
        return date;
    }

    /**
     * 返回星期
     *
     * @return
     */
    private int getWeek() {
        Calendar lastDate = calendarDate;
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return lastDate.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 上月最后一天
     *
     * @return
     */
    private int getPreviousMonthEnd() {
        Calendar lastDate = calendarDate;
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        return lastDate.get(Calendar.DATE);
    }

    /**
     * 本月最后一天是星期几
     *
     * @return
     */
    private int getDefaultDay() {

        Calendar lastDate = calendarDate;
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        return lastDate.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回选择的map
     *
     * @return
     */
    public Map<String, Boolean> getCelectMap() {
        return celectMap;
    }

    public int getCelectMapInt() {
        int i = 0;
        for (Map.Entry<String, Boolean> entry : celectMap.entrySet()) {
            if (entry.getValue())
                i++;
        }
        return i;
    }

    public String getCelectMapString() {
        String i = "";
        for (Map.Entry<String, Boolean> entry : celectMap.entrySet()) {
            if (entry.getValue())
                i += (entry.getKey() + ",");
        }
        if (i.length() > 2)
            i = i.substring(0, i.length() - 1);
        return i;
    }

    public void setCelectMap(Map<String, Boolean> celectMap) {
        this.celectMap = celectMap;
    }


    public void setDetermineClickListener(OnClickListener listener) {
        determine.setOnClickListener(listener);
    }

    public interface DayClick {
        void dayClick();
    }

    public void setDayClick(DayClick dayClick) {
        this.dayClick = dayClick;
    }

    DayClick dayClick;

    public interface Paging {
        void next(String times);

        void last(String times);
    }

    Paging paging;

    public void setPagin(Paging paging) {
        this.paging = paging;
    }
}
