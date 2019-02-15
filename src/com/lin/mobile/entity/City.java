package com.lin.mobile.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 城市
 * @author admin
 *
 */
public class City {

	public String name;
	public String pinyi;
	public String number;
	public String longs;
	public String lat;

	public City(String name, String pinyi, String number,String longs,String lat
	) {
		super();
		this.name = name;
		this.pinyi = pinyi;
		this.number = number;
		this.longs = longs;
		this.lat = lat;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLongs() {
		return longs;
	}

	public void setLongs(String longs) {
		this.longs = longs;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public Map<String,String> getMap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("pinyi", pinyi);
		map.put("number", number);
		map.put("long", longs);
		map.put("lat", lat);
		return map;
	}
}
