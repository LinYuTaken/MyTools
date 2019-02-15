package com.lin.mobile.rgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tools.R;
import com.lin.mobile.entity.City;
import com.lin.mobile.entity.SeriaMap;
import com.sy.mobile.control.MyLetterListView;
import com.sy.mobile.control.MyLetterListView.OnTouchingLetterChangedListener;
/**
 * 选择城市
 * @author admin
 *
 */
public class SelectCity extends Activity {
	private BaseAdapter adapter;
	private ListView personList;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread; // 显示首字母对话框
	private ArrayList<City> allCity_lists; // 所有城市列表
	private ArrayList<City> city_lists;// 城市列表
	ListAdapter.TopViewHolder topViewHolder;
	private String lngCityName = "正在定位所在位置..";
	DBHelper dbHelper;
	EdtextRe edtext = new EdtextRe();
	EditText sh;
	Map<String,String> name = new HashMap<String, String>();
	TextView lng_city;
	LinearLayout yingc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String pkName = this.getPackageName();
		String comString = "/data/data/"+pkName+"/databases/";
		dbHelper = new DBHelper(this,comString);
		yingc = (LinearLayout) findViewById(R.id.yingc);
		if(getIntent().getExtras()==null){
			yingc.setVisibility(View.GONE);
		}else{
			SeriaMap serializableMap = (SeriaMap) getIntent().getExtras().get("orderinfo");
			name = serializableMap.getMap();
		}
		personList = (ListView) findViewById(R.id.list_view);
		allCity_lists = new ArrayList<City>();
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
//		Activity02.setLocateIn(new GetCityName());
		alphaIndexer = new HashMap<String, Integer>();
		lng_city = (TextView) findViewById(R.id.lng_city);
		lng_city.setText(name.get("name"));
		findViewById(R.id.dwdian).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				SeriaMap tmpmap = new SeriaMap();
				Bundle bundle = new Bundle();
				tmpmap.setMap(name);
				bundle.putSerializable("orderinfo", tmpmap);
				resultIntent.putExtras(bundle);
				setResult(100, resultIntent);
				finish();
			}
		});
		handler = new Handler();
		sh = (EditText) findViewById(R.id.sh);
		overlayThread = new OverlayThread();
		personList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
			}
		});
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		sh.addTextChangedListener(edtext);
		personList.setAdapter(adapter);
		initOverlay();
		hotCityInit();
		setAdapter(allCity_lists);
	}

	/**
	 * 热门城市
	 */
	public void hotCityInit() {
		City city;
//		= new City("", "-");
//		allCity_lists.add(city);
//		city = new City("", "-");
//		allCity_lists.add(city);
		city = new City("上海", "","310100","121.47264","31.231707");
		allCity_lists.add(city);
		city = new City("北京", "","110100","116.40529","39.904987");
		allCity_lists.add(city);
		city = new City("广州", "","440100","113.28064","23.125177");
		allCity_lists.add(city);
		city = new City("深圳", "","440300","114.085945","22.547");
		allCity_lists.add(city);
		city = new City("武汉", "","420100","114.29857","30.584354");
		allCity_lists.add(city);
		city = new City("天津", "","120100","117.190186","39.125595");
		allCity_lists.add(city);
		city = new City("西安", "","610100","108.94802","34.26316");
		allCity_lists.add(city);
		city = new City("南京", "","320100","118.76741","32.041546");
		allCity_lists.add(city);
		city = new City("杭州", "","330100","120.15358","30.287458");
		allCity_lists.add(city);
		city = new City("成都", "","510100","104.065735","30.659462");
		allCity_lists.add(city);
		city = new City("重庆", "","500100","106.50496","29.533155");
		allCity_lists.add(city);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	private ArrayList<City> getCityList() {
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(list, comparator);
		return list;
	}

	private void Search(String search){
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city where name like '%"+search+"%'", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getString(5));
				list.add(city);
			}
			cursor.close();
			db.close();
			if(search.length()==0){
				setAdapter(allCity_lists);
			}else{
				((ListAdapter) adapter).setList(list);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * a-z排序
	 */
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}

		}
	};

	private void setAdapter(List<City> list) {
		adapter = new ListAdapter(this, list);
		personList.setAdapter(adapter);
	}

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<City> list;
		final int VIEW_TYPE = 3;

		public ListAdapter(Context context, List<City> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinyi());
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
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
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int type = 0;
			if (position == 0) {
				type = 2;
			} else if (position == 1) {
				type = 1;
			}
			return type;
		}

		@Override
		public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
			return VIEW_TYPE;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
//			int viewType = getItemViewType(position);
//			if (viewType == 1) {
//				if (convertView == null) {
//					topViewHolder = new TopViewHolder();
//					convertView = inflater.inflate(R.layout.frist_list_item,
//							null);
//					topViewHolder.alpha = (TextView) convertView
//							.findViewById(R.id.alpha);
//					topViewHolder.name = (TextView) convertView
//							.findViewById(R.id.lng_city);
//					convertView.setTag(topViewHolder);
//				} else {
//					topViewHolder = (TopViewHolder) convertView.getTag();
//				}
//
//				topViewHolder.name.setText(lngCityName);
//				topViewHolder.alpha.setVisibility(View.VISIBLE);
//				topViewHolder.alpha.setText("定位城市");
//
//			} else if (viewType == 2) {
//				final ShViewHolder shViewHolder;
//				if (convertView == null) {
//					shViewHolder = new ShViewHolder();
//					convertView = inflater.inflate(R.layout.search_item, null);
//					shViewHolder.editText = (EditText) convertView
//							.findViewById(R.id.sh);
//					convertView.setTag(shViewHolder);
//				} else {
//					shViewHolder = (ShViewHolder) convertView.getTag();
//				}
//				shViewHolder.editText.addTextChangedListener(edtext);
//			} else {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView
						.findViewById(R.id.alpha);
				holder.name = (TextView) convertView
						.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//				if (position >= 1) {
			holder.name.setText(list.get(position).getName());
			String currentStr = getAlpha(list.get(position).getPinyi());
			String previewStr = (position - 1) >= 0 ? getAlpha(list
					.get(position - 1).getPinyi()) : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				if (currentStr.equals("#")) {
					currentStr = "热门城市";
				}
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//							Toast.makeText(SelectCity.this,list.get(position).getName(), Toast.LENGTH_LONG).show();
					Intent resultIntent = new Intent();
					SeriaMap tmpmap = new SeriaMap();
					Bundle bundle = new Bundle();
					tmpmap.setMap(list.get(position).getMap());
					bundle.putSerializable("orderinfo", tmpmap);
					resultIntent.putExtras(bundle);
//							resultIntent.putExtra("name",list.get(position).getName());
					setResult(100, resultIntent);
					finish();
				}
			});
//				}
//			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}

		private class TopViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}

		private class ShViewHolder {
			EditText editText;

		}

		public void setList(List<City> list){
			this.list = list;
			notifyDataSetChanged();
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {

		if (str.equals("-")) {
			return "&";
		}
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	private class EdtextRe implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
								  int arg3) {
			// TODO Auto-generated method stub
			Search(arg0.toString());
		}

	}

}