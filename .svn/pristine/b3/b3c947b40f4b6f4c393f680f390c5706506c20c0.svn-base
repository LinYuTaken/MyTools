package com.sy.moblie.json;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Data;
import com.longevitysoft.android.xml.plist.domain.Date;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.False;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.longevitysoft.android.xml.plist.domain.Real;
import com.longevitysoft.android.xml.plist.domain.True;

/**
 * xml文件解析
 * @author admin
 *
 */
public class XmlAnalytical {
	Map<String,Object> cenMap = new HashMap<String,Object>();
	public interface AnalyComp{
		public void Complete(Object centen);
	}
	AnalyComp anal;

	/**
	 * 从assets文件获取xml
	 */
	public void setDataAssete(Activity con ,String name){
		try {
			xmlAn(con.getAssets().open(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 解析xml
	 * @param is
	 */
	public void xmlAn(final InputStream is){
		new Thread(new Runnable() {
			@Override
			public void run() {
				PListXMLParser parser = new PListXMLParser();                // 基于SAX的实现
				PListXMLHandler handler = new PListXMLHandler();
				parser.setHandler(handler);
//
				try {
					parser.parse(is);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
//		      Dict root = (Dict) actualPList.getRootElement();
				PListObject rootElement = actualPList.getRootElement();
				cenMap.put("conten", parsePListObject(rootElement));
				Log.i("xmlcon", cenMap.toString());
				XmlAnalytical.this.handler.sendEmptyMessage(0);
			}

		}).start();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(anal!=null)
				anal.Complete(cenMap.get("conten"));
		};
	};

	public void setAnalyComp(AnalyComp anal){
		this.anal = anal;
	}

	private Object parsePListObject(PListObject pListObject) {
		if (pListObject instanceof Dict) {
			Dict dict = (Dict) pListObject;
			return parseDict(dict);
		}else if ( pListObject instanceof Array) {
			Array array = (Array) pListObject;
			return parseArray(array);
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.Date) {
			Date date = (Date) pListObject;
			parseDate(date);
			return "";
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.String) {
			com.longevitysoft.android.xml.plist.domain.String myString = (com.longevitysoft.android.xml.plist.domain.String) pListObject;
			return myString.getValue();
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.Integer) {
			com.longevitysoft.android.xml.plist.domain.Integer myInteger = (com.longevitysoft.android.xml.plist.domain.Integer) pListObject;
			return myInteger.getValue();
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.Real) {
			Real myReal = (Real) pListObject;
			return myReal.getValue();
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.False) {
			False myFalse = (False) pListObject;
			return myFalse.getValue();
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.True) {
			True myTrue = (True) pListObject;
			return myTrue.getValue();
		}else if (pListObject instanceof com.longevitysoft.android.xml.plist.domain.Data) {
			Data data = (Data) pListObject;
			return data.getValue();
		}
		return "";

	}
	private void parseDate(Date date) {
		java.util.Date value = date.getValue();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(value);
	}

	/**
	 * 解析Array类型
	 * @param noteArray
	 */
	private List<Object> parseArray(Array noteArray) {
		List<Object> list = new ArrayList<Object>();
		for(int k=0;k<noteArray.size();k++) {
			PListObject pListObject = noteArray.get(k);
			list.add(parsePListObject(pListObject));
		}
		return list;
	}

	/**
	 * 解析Dict类型
	 * @param dict
	 */
	private Map<String,Object> parseDict(Dict dict) {
		Map<String,PListObject> notes = dict.getConfigMap();
		Map<String,Object> conMap = new HashMap<String,Object>();
		Set<String> keySet = notes.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String tempKey = iterator.next();
			PListObject pListObject = notes.get(tempKey);
//            sb.append(tempKey + " : ");//文件中的节点名称
			conMap.put(tempKey, parsePListObject(pListObject));
		}
		return conMap;
	}
}
