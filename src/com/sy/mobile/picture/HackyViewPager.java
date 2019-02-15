package com.sy.mobile.picture;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * 
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * 
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
	private boolean isScroll;

	private static final String TAG = "HackyViewPager";

	public HackyViewPager(Context context) {
		super(context);
	}
	
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			if (isScroll){
				return super.onInterceptTouchEvent(ev);
			}else{
				return false;
			}
		} catch (IllegalArgumentException e) {
			//不理会
			Log.e(TAG,"hacky viewpager error1");
			return false;
		}catch(ArrayIndexOutOfBoundsException e ){
			//不理会
			Log.e(TAG,"hacky viewpager error2");
			return false;
		}
	}

	/**
	 * 1.dispatchTouchEvent一般情况不做处理
	 *,如果修改了默认的返回值,子孩子都无法收到事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);   // return true;不行
	}
	/**
	 * 是否消费事件
	 * 消费:事件就结束
	 * 不消费:往父控件传
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//return false;// 可行,不消费,传给父控件
		//return true;// 可行,消费,拦截事件
		//super.onTouchEvent(ev); //不行,
		//虽然onInterceptTouchEvent中拦截了,
		//但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
		if (isScroll){
			return super.onTouchEvent(ev);
		}else {
			return true;// 可行,消费,拦截事件
		}
	}
	public void setScroll(boolean scroll) {
		isScroll = scroll;
	}

}
