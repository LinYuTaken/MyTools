
package com.sy.mobile.analytical;

import com.baidu.voicerecognition.android.Candidate;
import com.baidu.voicerecognition.android.DataUploader;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionClient.VoiceClientStatusChangeListener;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.example.tools.R;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * 使用底层API方式语音识别
 *  
 * @author 
 */
public class ApiSpeech{
//    private ControlPanelFragment mControlPanel;

    private VoiceRecognitionClient mASREngine;
    
    public interface onVoiceReply {
		/**
		 * 返回语音内容和当前状态
		 * @param content
		 */
		public void onReply(String content,int stau);
		
		/**
		 * 音频
		 * @param vol
		 */
		public void onCurrent(long vol);
			
	}
    onVoiceReply vr;
    /** 正在识别中 */
    private boolean isRecognition = false;

    /** 音量更新间隔 */
    private static final int POWER_UPDATE_INTERVAL = 100;

    /** 识别回调接口 */
    private MyVoiceRecogListener mListener = new MyVoiceRecogListener();

    /** 主线程Handler */
    private Handler mHandler = new Handler();;

    /**
     * 结果展示
     */
    private EditText mResult = null;

    /**
     * 音量更新任务
     */
    private Runnable mUpdateVolume = new Runnable() {
        public void run() {
            if (isRecognition) {
                long vol = mASREngine.getCurrentDBLevelMeter();
//                mControlPanel.volumeChange((int) vol);
                mHandler.removeCallbacks(mUpdateVolume);
                mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
            }
        }
    };
    public ApiSpeech(String api_key,String secret_key,Context context){
    	mASREngine = VoiceRecognitionClient.getInstance(context);
        mASREngine.setTokenApis(api_key,secret_key);
    }
    public void setReply(final onVoiceReply vr) {
		this.vr = vr;
    }
    /**
     * 开始
     * @return
     */
    public boolean onStartListening() {
//        mResult.setText(null);
        VoiceRecognitionConfig config = new VoiceRecognitionConfig();
        config.setProp(Config.CURRENT_PROP);
        config.setLanguage(Config.getCurrentLanguage());
        config.enableContacts(); // 启用通讯录
        config.enableVoicePower(Config.SHOW_VOL); // 音量反馈。
        if (Config.PLAY_START_SOUND) {
            config.enableBeginSoundEffect(R.raw.bdspeech_recognition_start); // 设置识别开始提示音
        }
        if (Config.PLAY_END_SOUND) {
            config.enableEndSoundEffect(R.raw.bdspeech_speech_end); // 设置识别结束提示音
        }
        config.setSampleRate(VoiceRecognitionConfig.SAMPLE_RATE_8K); // 设置采样率,需要与外部音频一致
        // 下面发起识别
        int code = mASREngine.startVoiceRecognition(mListener, config);
        if (code != VoiceRecognitionClient.START_WORK_RESULT_WORKING) {
        	//启动失败，返回
//            mResult.setText(getString(R.string.error_start, code));
        	vr.onReply("启动失败",-1);
        } 

        return code == VoiceRecognitionClient.START_WORK_RESULT_WORKING;
    }
    /**
     * 取消
     * @return
     */
    public boolean onCancel() {
        mASREngine.stopVoiceRecognition();
        return true;
    }
    /**
     * 结束
     * @return
     */
    public boolean onStopListening() {
    	//停止
        mASREngine.speakFinish();
        return true;
    }

    /**
     * 重写用于处理语音识别回调的监听器
     */
    class MyVoiceRecogListener implements VoiceClientStatusChangeListener {

        @Override
        public void onClientStatusChange(int status, Object obj) {
            switch (status) {
            // 语音识别实际开始，这是真正开始识别的时间点，需在界面提示用户说话。
                case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
                    isRecognition = true;
                    mHandler.removeCallbacks(mUpdateVolume);
                    mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
                    vr.onReply("", VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING);
                    break;
                case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START: // 检测到语音起点
                	//说话中
                	vr.onReply("",VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START);
                    break;
                // 已经检测到语音终点，等待网络返回
                case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
                	vr.onReply("",VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END);
                	//正在识别
                    break;
                // 语音识别完成，显示obj中的结果
                case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
                    isRecognition = false;
                    updateRecognitionResult(obj);
                    break;
                // 处理连续上屏
                case VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS:
                    updateRecognitionResult(obj);
                    break;
                // 用户取消
                case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
                	vr.onReply("",VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED);
                    isRecognition = false;
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onError(int errorType, int errorCode) {
            isRecognition = false;
            //错误信息
//            mResult.setText(getString(R.string.error_occur, Integer.toHexString(errorCode)));
            vr.onReply("无法解析",-1);
        }

        @Override
        public void onNetworkStatusChange(int status, Object obj) {
            // 这里不做任何操作不影响简单识别
        }
    }

    /**
     * 将识别结果更新到UI上，搜索模式结果类型为List<String>,输入模式结果类型为List<List<Candidate>>
     * 
     * @param result
     */
    private void updateRecognitionResult(Object result) {
        if (result != null && result instanceof List) {
            List results = (List) result;
            if (results.size() > 0) {
                if (results.get(0) instanceof List) {
                    List<List<Candidate>> sentences = (List<List<Candidate>>) result;
                    StringBuffer sb = new StringBuffer();
                    for (List<Candidate> candidates : sentences) {
                        if (candidates != null && candidates.size() > 0) {
                            sb.append(candidates.get(0).getWord());
                        }
                    }
                    vr.onReply(sb.toString(),VoiceRecognitionClient.CLIENT_STATUS_FINISH);
                } else {
                	vr.onReply(results.get(0).toString(),VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS);
                }
            }
        }
    }
    
//    /**
//     * 上传通讯录
//     * */
//    private void uploadContacts(){
//    	DataUploader dataUploader = new DataUploader(ApiDemoActivity.this);
//    	dataUploader.setApiKey(Constants.API_KEY, Constants.SECRET_KEY);
//    	
//    	String jsonString = "[{\"name\":\"兆维\", \"frequency\":1}, {\"name\":\"林新汝\", \"frequency\":2}]";
//    	try{
//    		dataUploader.uploadContactsData(jsonString.getBytes("utf-8"));
//    	}catch (Exception e){
//    		e.printStackTrace();
//    	}
//    }
}
