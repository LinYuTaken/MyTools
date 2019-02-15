package com.lin.mobile.rgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 城市数据库操作
 * @author admin
 *
 */
public class CitySqlOperation {
	Activity ac ;
	DBHelper dbHelper;
	public CitySqlOperation(Activity conAc){
		ac = conAc;
		String pkName =  conAc.getPackageName();
		String comString = "/data/data/"+pkName+"/databases/";
		dbHelper = new DBHelper(conAc,comString);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据城市id获取区
	 * @param cityId
	 * @return
	 */
	public List<Map<String,String>> queryAreaByCityId(String cityId){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from region where number='" + cityId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("code", cursor.getString(cursor.getColumnIndex("code")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("long", cursor.getString(cursor.getColumnIndex("long")));
			map.put("lat", cursor.getString(cursor.getColumnIndex("lat")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			list.add(map);
		}
		cursor.close();
		db.close();
		return list;
	}


	/**
	 * 根据城市名称获取城市信息
	 * @param name
	 * @return
	 */
	public Map<String,String> queryCityByName(String name){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(name!=null&&name.length()>1)
			name = name.substring(0,name.length()-1);
		Map<String,String> map = new HashMap<String, String>();
		String sql = "select * from city where name like '%"+name+"%'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			map.put("pinyin", cursor.getString(cursor.getColumnIndex("pinyin")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("long", cursor.getString(cursor.getColumnIndex("long")));
			map.put("lat", cursor.getString(cursor.getColumnIndex("lat")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
		}
		cursor.close();
		db.close();
		return map;
	}

	/**
	 * 根据区id获取区信息
	 * @param cityId
	 * @return
	 */
	public Map<String,String> queryAreaByID(String id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Map<String,String> map = new HashMap<String, String>();
		String sql = "select * from region where code like '"+id+"'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			map.put("code", cursor.getString(cursor.getColumnIndex("code")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("long", cursor.getString(cursor.getColumnIndex("long")));
			map.put("lat", cursor.getString(cursor.getColumnIndex("lat")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
		}
		cursor.close();
		db.close();
		return map;
	}

}
