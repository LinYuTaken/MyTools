package com.sy.mobile.control;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tools.R;
import com.wxample.data.MyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 侧边菜单
 * @author Administrator
 *
 */
public class ViewPagerSwitch extends LinearLayout{
	Context context;
	TextView[] textviews;
	//当前的计数
	int index;
	//字体选中时的颜色 //字体未选中时的颜色
	int sel_color = Color.parseColor("#16ADFE"),not_color = Color.parseColor("#999999");

	public interface ViewOnclick{
		void viewOnclick(int page);
	}

	ViewOnclick viewOnclick;

	/**
	 * 点击事件
	 * @param viewOnclick
	 */
	public void setViewOnclick(ViewOnclick viewOnclick){
		this.viewOnclick = viewOnclick;
	}

	public ViewPagerSwitch(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public ViewPagerSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 初始化
	 * @param context
	 * @param name 标题名字
	 * @param list 要显示的内容
	 */
	public void init(Context context, String[] name , List<Fragment> pagerItemList){
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.view_pager_switch, null);
		LinearLayout titleLin = (LinearLayout) view.findViewById(R.id.titleLin);

		//显示标题
		titleLin.removeAllViews();
		textviews = new TextView[name.length];
		for (int i = 0;i<name.length;i++){
			View view_item = LayoutInflater.from(context).inflate(R.layout.view_pager_switch_item,null);
			TextView content = (TextView) view_item.findViewById(R.id.content);
			//初始化的时候 第一个默认为选中
			if(i==0){
				content.setBackgroundResource(R.drawable.tool_titbom);
				content.setTextColor(sel_color);
			}else{
				content.setBackgroundDrawable(null);
				content.setTextColor(not_color);
			}
			content.setText(name[i]);
			//设置view为一比一
			LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
			final int test = i;
			view_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(viewOnclick!=null)
						viewOnclick.viewOnclick(test);
					shouButton(test);
				}
			});
			textviews[i] =content;
			view_item.setLayoutParams(params);
			addView(view);
		}
	}

	/**
	 * 控制显示
	 * @param in
	 */
	private void shouButton(int in) {
		textviews[index].setTextColor(not_color);
		textviews[index].setBackgroundDrawable(null);
		index = in;
		textviews[index].setTextColor(sel_color);
		textviews[index].setBackgroundResource(R.drawable.tool_titbom);
	}
}
