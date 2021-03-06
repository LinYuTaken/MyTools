package com.sy.mobile.analytical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.example.g.SoundMeter;
import com.example.tools.R;
import com.sy.mobile.analytical.ApiSpeech.onVoiceReply;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 语音识别
 */
public class CustomerVoiceChat extends Activity {
	EditText huifunei;
	ListView cainilv;
	TextView yuyintubiao,tishi,tishisb;
	private ChatMsgViewAdapter mAdapter;
	String apikey="0fr85ihSFGf2Ge2lLUrOtaD5",sekey="voMlre1KckHzHlPmemqymZzcKFvN6aCv";
	boolean isk = true;

	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	LinearLayout yuyinlie,yysb;
	private SoundMeter mSensor = new SoundMeter();
	//语音路径
	String lujnme = "";
	String yin = "";
	ApiSpeech as;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setView();
		initData();
		initY();
	}

	private void setView(){
		huifunei = (EditText) findViewById(R.id.huifunei);
		cainilv = (ListView) findViewById(R.id.cainilv);
		yuyinlie = (LinearLayout) findViewById(R.id.yuyinlie);
		tishi = (TextView) findViewById(R.id.tishi);
		yuyintubiao = (TextView) findViewById(R.id.yuyintubiao);
		yysb = (LinearLayout) findViewById(R.id.yysb);
		tishisb = (TextView) findViewById(R.id.tishisb);

		findViewById(R.id.star).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isk){
					as.onStartListening();
					tishisb.setText("请说话");
					isk = false;
				}else{
					Toast.makeText(CustomerVoiceChat.this, "已开始录音", Toast.LENGTH_LONG).show();
				}

			}
		});

		findViewById(R.id.end).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				as.onStopListening();
			}
		});

		findViewById(R.id.cenct).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				as.onCancel();
			}
		});

		findViewById(R.id.add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(yuyinlie.getVisibility() == View.VISIBLE){
					yuyinlie.setVisibility(View.GONE);
				}else{
					View view = getWindow().peekDecorView();
					if (view != null) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					yuyinlie.setVisibility(View.VISIBLE);
					yysb.setVisibility(View.GONE);
				}
			}
		});
		findViewById(R.id.huifu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				send();
			}
		});

		findViewById(R.id.yuyin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(yysb.getVisibility() == View.VISIBLE){
					yysb.setVisibility(View.GONE);
				}else{
					View view = getWindow().peekDecorView();
					if (view != null) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					yysb.setVisibility(View.VISIBLE);
					yuyinlie.setVisibility(View.GONE);
				}
			}
		});

		huifunei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				yuyinlie.setVisibility(View.GONE);
				yysb.setVisibility(View.GONE);
			}
		});

		yuyintubiao.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(CustomerVoiceChat.this, "没有sd卡,请插入sd卡", Toast.LENGTH_LONG).show();
					return false;
				}
				switch (arg1.getAction()) {
					case MotionEvent.ACTION_DOWN:
						yuyintubiao.setBackgroundResource(R.drawable.luyinan);
//					tishi.setText("正在录音");
						lujnme = SystemClock.currentThreadTimeMillis()+ ".amr";
						mSensor.start(lujnme,tishi);
						break;
					case MotionEvent.ACTION_UP:
						yuyintubiao.setBackgroundResource(R.drawable.luyin);
						tishi.setText("按下录音");
						yuyinlie.setVisibility(View.GONE);
//					ccc.setVisibility(View.VISIBLE);
//					shic.setText(mSensor.stop()+"s");
						mSensor.stop();
//					try {
//						yin = mSensor.readStream(lujnme);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
						ChatMsgEntity entity = new ChatMsgEntity();
						entity.setDate(getDate());
						entity.setName("某某");
						entity.setMsgType(false);
//					entity.setTime(time+"\"");
						entity.setText(lujnme);
						mDataArrays.add(entity);
						mAdapter.notifyDataSetChanged();
						cainilv.setSelection(cainilv.getCount() - 1);
						break;
					default:
						break;
				}

				return true;
			}
		});
	}
	private String[] msgArray = new String[] { "一花一世界","一木一浮生","一草一天堂","一叶一如来","一砂一极乐","一方一净土"};
	private String[] dataArray = new String[] { "2012-10-31 18:00",
			"2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",
			"2012-10-31 18:30", "2012-10-31 18:35"};
	private final static int COUNT = 6;

	public void initData() {
		for (int i = 0; i < COUNT; i++) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName("客户");
				entity.setMsgType(true);
			} else {
				entity.setName("自己");
				entity.setMsgType(false);
			}
			entity.setText(msgArray[i]);
			mDataArrays.add(entity);
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		cainilv.setAdapter(mAdapter);
	}
	private void initY(){
		as = new ApiSpeech(apikey, sekey, this);
		as.setReply(new onVoiceReply() {

			@Override
			public void onReply(String content, int stau) {
				// TODO Auto-generated method stub
				switch (stau) {
					case VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS:
					case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
						huifunei.setText(content);
						isk = true;
						tishisb.setText("未开始");
						break;
					case -1:
						Toast.makeText(CustomerVoiceChat.this, content, Toast.LENGTH_LONG).show();
						tishisb.setText("未开始");
						isk = true;
						break;
					case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START:
						tishisb.setText("说话中");
						break;
					case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
						tishisb.setText("正在识别..");
						break;
					case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
						tishisb.setText("未开始");
						isk = true;
						break;
					default:
						break;
				}
			}

			@Override
			public void onCurrent(long vol) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 发送消息
	 */
	private void send() {
		String contString = huifunei.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("自己");
			entity.setMsgType(false);
			entity.setText(contString);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			huifunei.setText("");
			cainilv.setSelection(cainilv.getCount() - 1);
		}
	}
	/**
	 * 获取时间
	 * @return
	 */
	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}
}
