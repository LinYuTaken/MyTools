package com.lin.mobile.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.tools.R;
import com.sy.mobile.analytical.ScreenTools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 功能类
 * 
 * @author admin
 * 
 */
public class CalendarShow {
	Calendar calendarDate;
	LinearLayout rowlin;
	LinearLayout container;
	Context context;
	ScreenTools screentools;
	int year,month,date;
	
	public static int LAST_MONTH = 0;
	public static int NEXT_MONTH = 1;
	public static int THIS_MONTH = 2;
	
	public interface SetShowView{
		public View setViews(String cont,int type);
	}
	
	SetShowView setShowView;
	/**
	 * 显示时间
	 * 
	 * @param container
	 *            容器
	 * @param contxt
	 *            当前时间传null为当天
	 */
	public void showCalendar(Context contxt,LinearLayout container) {
		calendarDate = Calendar.getInstance();
		container.removeAllViews();
		this.context = contxt;
		this.container = container;
		screentools = ScreenTools.instance(context);
		// 如果传入的时间不为空把时间传入
		if (year==0&&month==0&&date==0) {
			year = calendarDate.get(Calendar.YEAR);
			month = calendarDate.get(Calendar.MONTH);
			date = calendarDate.get(Calendar.DATE);
		}else{
			calendarDate.set(Calendar.YEAR, year);
			calendarDate.set(Calendar.MONTH, month);
			calendarDate.set(Calendar.DATE, date);
		}
		// 获取当月总天数
		int MaxDate = calendarDate.getActualMaximum(Calendar.DATE);
		// 获取本月的第一天是星期几
		int week = getWeek() - 1;
		// 获取上月最后一天是几号
		int lastDate = getPreviousMonthEnd();
		// 获取本月最后一天是星期几
		// int lastDateWeek = getDefaultDay();

		// int showSumDate = MaxDate + week + (7 - lastDateWeek);
		int index = 0;
		rowlin = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.linlayr,null);
		//由于addview会导致LayoutParams丢失，所以需要重新添加
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, screentools.dip2px(10), 0, 0);
		rowlin.setLayoutParams(lp);
		container.addView(rowlin);
		// 加载上个月最后几天
		for (int i = 0; i < week; i++) {
			index++;
			addLinView(""+(lastDate - (week - (i + 1))),LAST_MONTH);
		}
		// 加载本月天数
		for (int i = 0; i < MaxDate; i++) {
			index++;
			if (index % 7 == 1) {
				addLin();
				addLinView(""+(i + 1),THIS_MONTH);
			} else {
				addLinView(""+(i + 1),THIS_MONTH);
			}
		}
		// 加载下个月开始几天
		for (int i = 0; i < 42 - (MaxDate + week); i++) {
			index++;
			if (index % 7 == 1) {
				addLin();
				addLinView(""+(i + 1),NEXT_MONTH);
			} else {
				addLinView(""+(i + 1),NEXT_MONTH);
			}
		}
	}
	
	/**
	 * 下一个月
	 */
	public void nextMonth(){
		if(month==11){
			month = 0 ;
			year ++;
		}else{
			month ++ ;
		}
		showCalendar(context, container);
	}
	
	/**
	 * 上一个月
	 */
	public void topMonth(){
		if(month==0){
			month = 11 ;
			year --;
		}else{
			month -- ;
		}
		showCalendar(context, container);
	}
	
	/**
	 * 重置时间
	 */
	public void resetTime(){
		year = 0;
		month = 0;
		date = 0;
	}
	
	/**
	 * 返回年份
	 * @return
	 */
	public int getYear(){
		return year;
	}
	
	/**
	 * 返回月份
	 * @return
	 */
	public int getMonth(){
		return month+1;
	}
	
	/**
	 * 返回日期
	 * @return
	 */
	public int getDa(){
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
	 * 添加view
	 * @param cont
	 */
	private void addLinView(String cont,int type){
		if(setShowView==null){
			return ;
		}
		rowlin.addView(setShowView.setViews(cont, type));
	}
	
	/**
	 * 添加行
	 */
	private void addLin(){
		rowlin = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.linlayr,null);
		//由于addview会导致LayoutParams丢失，所以需要重新添加
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT); 
		lp.setMargins(0, screentools.dip2px(10), 0, 0);
		rowlin.setLayoutParams(lp);
		container.addView(rowlin);
	}
	
	/**
	 * 传入view
	 * @param setShowView
	 */
	public void setShowViewInterface(SetShowView setShowView){
		this.setShowView = setShowView;
	}
}
