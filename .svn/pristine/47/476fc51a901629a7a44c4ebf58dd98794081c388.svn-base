package com.sy.mobile.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.tools.R;
import com.sy.mobile.net.HttpDream.Cont;
import com.wxample.data.MyData;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧边菜单
 * @author Administrator
 *
 */
public class LietMuen extends LinearLayout{
	Context context;
	MyListViewAdataper mva;
	private int ordinary = Color.WHITE;
	private int click = Color.parseColor("#EBECEB");
	private int index;
	private int textColor = Color.BLACK;
	private int textSize = 14;
	private LinearLayout classlie;
	//选中的
	Map<Integer,Integer> indexcen = new HashMap<Integer, Integer>();
	TextView tsstTxt;
	ListView centlist;
	List<Map<String,String>> teststuList = new ArrayList<Map<String,String>>();

	public interface OnItemClick {

		public void Click(Map<String,String> map);
	}
	OnItemClick onClick;

	public LietMuen(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public LietMuen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
//	public LietMuen(Context context,List<String> list) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		this.list = list;
//		this.context = context;
//		setOnItemClickListener(new ItemClickEvent());
//		mva = new MyListViewAdataper(context, list);
//		setAdapter(mva);
//	}
	/**
	 * 初始化
	 * @param context
	 * @param name 标题名字
	 * @param list 要显示的内容
	 */
	public void ini(Context context,String[] name ,List<Object> list){
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.lietmuen, null);
		classlie = (LinearLayout) view.findViewById(R.id.classlie);
		centlist = (ListView) view.findViewById(R.id.centlist);
//		mva = new MyListViewAdataper(context, list);
		List<Map<String,Object>> rowsList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < name.length; i++) {
			Map<String,Object> tmap = new HashMap<String, Object>();
			tmap.put("name", name[i]);
			tmap.put("conten", list.get(i));
			rowsList.add(tmap);
		}
		LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(linear);

		mva = new MyListViewAdataper(context, new ArrayList<Map<String,String>>());
		centlist.setAdapter(mva);
		centlist.setOnItemClickListener(new ItemClickEvent());
		showLeft(rowsList);
		addView(view);
//		setonvi();
	}

	/**
	 * 显示左边班级
	 */
	private void showLeft(List<Map<String,Object>> rowsList){
		classlie.removeAllViews();
		for (int i = 0; i < rowsList.size(); i++) {
			Map<String,Object> map = rowsList.get(i);
			View view = LayoutInflater.from(context).inflate(R.layout.selectstutext, null);
			final TextView cenReply = (TextView) view.findViewById(R.id.cenReply);
			cenReply.setText(MyData.mToString(map.get("name")));
			//内容集合
			final List<Map<String,String>> stuList = (List<Map<String, String>>) map.get("conten");
			//如果是第一个显示点击状态和加载列表
			if(i==0){
				cenReply.setBackgroundColor(Color.parseColor("#FFFFFF"));
				tsstTxt = cenReply;
				teststuList = stuList;
				mva.assLie(stuList);
				index = 0;
			}
			//初始化选中的
			indexcen.put(i, 0);
			final int test = i;
			cenReply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//点击后显示点击状态和加载列表
					if(tsstTxt!=null)
						tsstTxt.setBackgroundColor(Color.parseColor("#F5F5F5"));
					cenReply.setBackgroundColor(Color.parseColor("#ffffff"));
					tsstTxt = cenReply;
					teststuList = stuList;
					index = test;
					mva.assLie(stuList);
				}
			});
			classlie.addView(view);
		}

	}
	/**
	 * 点击回调
	 * @param callback
	 */
	public void setCall(final OnItemClick callback) {
		this.onClick = callback;
	}
	/**
	 * 设置菜单颜色
	 * @param ordinary 普通状态
	 * @param click 点击状态
	 */
	public void setMuenColor(int ordinary,int click){
		this.ordinary = ordinary;
		this.click = click;
	}
	/**
	 * 设置菜单字体颜色
	 */
	public void setMuenTextColor(int textColor){
		this.textColor = textColor;
		mva.notifyDataSetChanged();
	}
	/**
	 * 设置菜单字体大小
	 */
	public void setMuenTextSize(int textSize){
		this.textSize = textSize;
		mva.notifyDataSetChanged();
	}
	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			indexcen.put(index, arg2);
			mva.notifyDataSetChanged();
			if(onClick!=null)
				onClick.Click(teststuList.get(indexcen.get(index)));
		}
	}

	private class MyListViewAdataper extends BaseAdapter {
		LayoutInflater mInf;
		List<Map<String,String>> temp;

		private MyListViewAdataper(Context context,
								   List<Map<String,String>> list) {
			mInf = LayoutInflater.from(context);
			temp = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return temp.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return temp.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Map<String,String> map = temp.get(position);

			Holder holder = null;

			if (convertView == null) {
				holder = new Holder();
				convertView = mInf.inflate(R.layout.item_list_popwindow, null);
				holder.isju = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.isju.setText(map.get("name"));
//			if(indexcen.get(index)==position){
//				holder.isju.setBackgroundColor(click);
//			}else{
//				holder.isju.setBackgroundColor(ordinary);
//			}
			holder.isju.setTextColor(textColor);
			holder.isju.setTextSize(textSize);
			return convertView;
		}

		/**
		 * 重用的类
		 *
		 * @author Administrator
		 *
		 */
		class Holder {
			public TextView isju;
		}
		/**
		 * list赋值
		 */
		public void assLie(List<Map<String, String>> list){
			this.temp = list;
			notifyDataSetChanged();
		}
	}
	private void setonvi(){
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		});
	}
}
