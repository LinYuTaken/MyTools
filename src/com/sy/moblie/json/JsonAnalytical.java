package com.sy.moblie.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * json解析类
 *
 * @author Administrator
 *
 */
public class JsonAnalytical {
	Gson gson = new Gson();
	String[] notString;
	/**
	 * 解析json
	 *
	 * @param list
	 *            json数据
	 * @param cls
	 *            实体类
	 * @return
	 */
	public Object analyJs(String list, Class<?> cls) {
		Object obj = "";

		try {
			obj = gson.fromJson(list, cls);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			try {
				obj = gson.fromJson(list, new TypeToken<List>() {
				}.getType());
			} catch (JsonSyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return obj;
	}
	/**
	 * 设置哪些字段不解析
	 * @param keyName
	 */
	public void setOnt(String... keyName){
		notString = keyName;
	}
	/**
	 * json解析
	 *
	 * @param list
	 *            json数据
	 * @return
	 */
	public Map<String, Object> JsonRe(String list) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (list != null && list.length() > 1) {
				String tet = list.substring(0, 1);
				if (list.length() > 0 && tet.equals("[")) {
					if(list.indexOf("=")!=-1){
						list = "{content=" + list + "}";
					}else{
						list = "{\"content\":" + list + "}";
					}
				}
			}else{
				return map;
			}
			Log.i("json", list);
//			MyJson js = new MyJson(list);
			JSONObject js = new JSONObject(list);
			Iterator<String> itq = js.keys();
			while (itq.hasNext()) {
				String keyq = itq.next();
				String values = js.getString(keyq);
				map.put(keyq, Judge(values,keyq));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return map;
	}

	/**
	 * 判定
	 *
	 * @param values
	 * @return
	 */
	private Object Judge(String values,String key) {
		//如果是不用解析的字段，直接返回
		if(key!=null&&key.length()>0&&notString!=null&&notString.length>0){
			for (int i = 0; i < notString.length; i++) {
				if(key.equals(notString[i])){
					return values;
				}
			}
		}
		String test = "";
		if(values.length()>1)
			test = values.substring(0, 1);
		// 值是list<Map>(只判断前两个字符)
		if (test.indexOf("[{") != -1) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			try {
				JSONArray js = new JSONArray(values);
				for (int i = 0; i < js.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					JSONObject jsob = js.getJSONObject(i);
					Iterator<String> itq = jsob.keys();
					while (itq.hasNext()) {
						String keyq = itq.next();
						String objvalues = jsob.getString(keyq);
						map.put(keyq, Judge(objvalues,keyq));
					}
					list.add(map);
				}
				return list;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return list;
			}
		}
		// 值是list
		else if (test.indexOf("[") != -1) {
			List<Object> list = new ArrayList<Object>();
			try {
				JSONArray js = new JSONArray(values);
				for (int i = 0; i < js.length(); i++) {
					list.add(Judge(js.getString(i),""));
				}
				return list;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return list;
			}
		}
		// 值是Map
		else if (test.indexOf("{") != -1) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject js = new JSONObject(values);
				Iterator<String> itq = js.keys();
				while (itq.hasNext()) {
					String keyq = itq.next();
					String objvalues = js.getString(keyq);
					map.put(keyq, Judge(objvalues,keyq));
				}
				return map;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return map;
			}
		}
		// 值是String
		else {
			return values;
		}
	}
}
