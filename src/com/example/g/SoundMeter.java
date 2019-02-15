package com.example.g;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.sy.mobile.analytical.ScreenTools;
import com.sy.mobile.control.MyDialog;
import com.wxample.data.MyData;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 语音录制
 *
 * @author Administrator
 *
 */
public class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	private Timer timer;
	private int i = 0;
	private TextView xisn;
	int audio = MediaRecorder.AudioEncoder.AMR_NB;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	Context context;

	public interface OnComple {
		public void onCompletion();
	}

	OnComple con;
	/**
	 * 是否太短
	 */
	boolean issto = false;

	/**
	 * 开始录制
	 *
	 * @param name
	 *            路径名称
	 */
	public void start(String name, TextView xisn) {
		if (issto)
			return;
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		String lu = android.os.Environment.getExternalStorageDirectory()
				+ "/luyin/";
		File destDir = new File(
				android.os.Environment.getExternalStorageDirectory() + "/luyin");
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(audio);
			mRecorder.setOutputFile(lu + name);
			try {
				mRecorder.prepare();
				mRecorder.start();
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				this.xisn = xisn;
				i = 0;
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						han.sendEmptyMessage(0);
					}
				}, 0, 1000);
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	Handler han = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (i != 60) {
				i++;
				if (issto) {
					if (xisn != null)
						xisn.setText("请重试");
				} else {
					if (xisn != null && mRecorder != null)
						xisn.setText(i + " 〃");
				}
				if (issto && i > 1) {
					issto = false;
					stop();
				}
			} else {
				stop();
				i = 0;
			}
			return false;
		}
	});

	/**
	 * 出错时停止
	 *
	 * @param issto
	 */
	public void setIssto() {
		// if(timer!=null)
		// return;
		// new Thread(){
		// public void run() {
		// while (!issto) {
		// SystemClock.sleep(1000);
		// han.sendEmptyMessage(0);
		// }
		// };
		// }.start();
		issto = false;
		i = 0;
		mRecorder = null;
	}

	/**
	 * 停止录音
	 *
	 * @return 录音的秒数
	 */
	public boolean stop() {
		if (mRecorder == null)
			return false;
		if (issto)
			return false;
		int ii = i;
		if (ii < 2) {
			issto = true;
			return false;
		} else {
			if (mRecorder != null) {
				mRecorder.setOnErrorListener(null);
				mRecorder.setPreviewDisplay(null);
				// mRecorder.setOnInfoListener(null);
				try {
					mRecorder.stop();
					mRecorder.release();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mRecorder = null;
				if (xisn != null)
					xisn.setText("按下录音");
			}
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			i = 0;
			return true;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 开始
	 */
	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	/**
	 * 获取录音音量
	 *
	 * @return
	 */
	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}

	/**
	 * 设置音频编码器
	 *
	 * @param audio
	 */
	public void setAudioEncoder(int audio) {
		this.audio = audio;
	}

	/**
	 * 播放语音
	 *
	 * @Description
	 * @param name
	 */
	public void playMusic(String name, Context context) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			// File file = new File(name);
			// FileInputStream fis = new FileInputStream(file);
			// mMediaPlayer.setDataSource(fis.getFD());
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					if (con != null)
						con.onCompletion();
				}
			});
			// mMediaPlayer.setOnTimedTextListener(listener)
		} catch (Exception e) {
			e.printStackTrace();
			MyDialog.showTextToast("语音播放失败", context);
			if (con != null)
				con.onCompletion();
		}
	}

	public void stopMusic(){
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
	}

	/**
	 * 播放完成
	 *
	 * @param con
	 */
	public void setOnComple(OnComple con) {
		this.con = con;
	}

	public String readStream(String path) throws Exception {
		File file = new File(path);
		InputStream inStream = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		String mImage = new String(Base64.encode(data, 0));
		outStream.close();
		inStream.close();
		return mImage;
	}

	/**
	 * 获取语音的时间
	 * @param path
	 * @return
	 */
	public void getMaxTime(final String path, final TextView time, final LinearLayout lin,Context context){
		this.context = context;
		new Thread(){
			@Override
			public void run() {
				MediaPlayer player = new MediaPlayer();
				try {
					player.setDataSource(path);  //recordingFilePath（）为音频文件的路径
					player.prepare();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				double duration = MyData.div(player.getDuration(),1000,2);//获取音频的时间;
				player.release();//记得释放资源
				Map<String,Object> test = new HashMap<String,Object>();
				test.put("text",time);
				test.put("lin",lin);
				test.put("data",duration);
				Message message = new Message();
				message.obj = test;
				message.what = 0;
				max.sendMessage(message);
			}
		}.start();
	}
	Handler max  = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			Map<String,Object> test = (Map<String, Object>) msg.obj;
			TextView time = (TextView) test.get("text");
			time.setText(test.get("data")+"'");
//			showLinLength(MyData.mToDouble(test.get("data")),(LinearLayout) test.get("lin"));
			return false;
		}
	});

	/**
	 * 控件长度
	 */
	private void showLinLength(Double dou ,LinearLayout lin){
		double length = MyData.mul(dou,3.5);
		//设定最小和最大限度
		if(length<50)
			length = 50;
		if(length>200)
			length = 200;
		ScreenTools screentools = ScreenTools.instance(context);
		//设置控件长度
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) lin.getLayoutParams();
		linearParams.width = screentools.dip2px(MyData.mToInt(length));
		lin.setLayoutParams(linearParams);
//		ViewGroup.MarginLayoutParams headerLayoutParams = (ViewGroup.MarginLayoutParams) lin
//				.getLayoutParams();
//		headerLayoutParams.width = screentools.dip2px(MyData.mToInt(length));
//		lin.setLayoutParams(headerLayoutParams);
	}
}
