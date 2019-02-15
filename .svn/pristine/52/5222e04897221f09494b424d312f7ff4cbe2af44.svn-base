package com.lin.mobile.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.tools.R;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sy.mobile.control.MyDialog;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wxample.data.MyData;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

/**
 * 分享
 * 
 * @author Administrator
 * 
 */
public class AndroidShare{
	// List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	Activity context;
	String msgTitle = "fasdfasd", msgText = "分享", imageurl = "", sharUrl = "";
	ArrayList<String> im = new ArrayList<String>();
	static int IO_BUFFER_SIZE = 2 * 1024;
	private Dialog pdialog;
	MyDialog md = new MyDialog();

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	// ;
	public Oauth2AccessToken mAccessToken;
	/**
	 * qq分享类
	 */
	public static Tencent mTencent;
	/**
	 * 微信分享类
	 */
	public static IWXAPI wxapi;

	/**
	 * 微博分享类
	 */
	// public static IWeiboShareAPI mWeibo;

	/**
	 * @param context
	 * @param msgTitle 分享标题
	 * @param msgText 分享内容
	 * @param imageurl 分享图片地址
	 * @param sharUrl 分享点击跳转地址
	 * @param qq_id 
	 * @param wx_id
	 */
	public AndroidShare(Activity context, String msgTitle, String msgText,
			String imageurl, String sharUrl,String qq_id,String wx_id) {
		this.msgTitle = msgTitle;
		this.msgText = msgText;
		// this.imageurl = Data.urlto + imageurl;
		this.sharUrl = sharUrl;
		this.imageurl = imageurl;
		Log.i("sho", msgTitle + ":" + msgText + ":" + imageurl + ":" + sharUrl);
		// 腾讯qq类初始化
		mTencent = Tencent.createInstance(qq_id, context.getApplicationContext());
		// 微信类初始化
		wxapi = WXAPIFactory.createWXAPI(context.getApplicationContext(),
				wx_id, false);
		wxapi.registerApp(wx_id);
		// 微博类初始化
		// mAuthInfo = new AuthInfo(this, Data.WEIBO_ID,
		// Data.REDIRECT_URL, Data.SCOPE);
		// mWeibo = WeiboShareSDK.createWeiboAPI(this, Data.WEIBO_ID);
		// mWeibo.registerApp();
		// 第一次启动本应用，AccessToken 不可用
//		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		init(context);
	}

	private void init(Activity context) {
		this.context = context;

		View view = LayoutInflater.from(context).inflate(R.layout.shareview,
				null);
		view.findViewById(R.id.heib).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDialog.closeDialog();
			}
		});
		view.findViewById(R.id.qq).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickShare();
			}
		});
		view.findViewById(R.id.wx).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WXFenShare();
			}
		});
		view.findViewById(R.id.wb).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareToQzone();
			}
		});
		view.findViewById(R.id.pyq).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WXShare();
			}
		});
		md.showCustom(context, view);
		// list.clear();
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("packageName", "com.tencent.mm");
		// map.put("paTitle", "微信");
		// map.put("activityName", "com.tencent.mm.ui.tools.ShareImgUI");
		// list.add(map);
		// map = new HashMap<String, String>();
		// map.put("packageName", "com.tencent.mm");
		// map.put("paTitle", "微信朋友圈");
		// map.put("activityName", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		// list.add(map);
		// map = new HashMap<String, String>();
		// map.put("packageName", "com.tencent.mobileqq");
		// map.put("paTitle", "腾讯qq");
		// map.put("activityName",
		// "com.tencent.mobileqq.activity.JumpActivity");
		// list.add(map);
		// map = new HashMap<String, String>();
		// map.put("packageName", "com.sina.weibo");
		// map.put("paTitle", "新浪微博");
		// map.put("activityName", "com.sina.weibo.EditActivity");
		// list.add(map);
	}

	public boolean isAvilible(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (((PackageInfo) pinfo.get(i)).packageName
					.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

	// @SuppressLint("NewApi")
	// public int getScreenOrientation() {
	// int landscape = 0;
	// int portrait = 1;
	// Point pt = new Point();
	// getWindow().getWindowManager().getDefaultDisplay().getSize(pt);
	// int width = pt.x;
	// int height = pt.y;
	// return width > height ? portrait : landscape;
	// }

	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// ShareItem share = (ShareItem) this.mListData.get(position);
	// shareMsg(getContext(), "分享到...", this.msgText, this.mImgPath, share);
	// }

	private void shareMsg(Map<String, String> share) {
		if (share.get("packageName") != null
				&& !isAvilible(context, share.get("packageName"))) {
			Toast.makeText(context, "请先安装" + share.get("paTitle"),
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent("android.intent.action.SEND");
		// if ((imgPath == null) || (imgPath.equals(""))) {
		intent.setType("text/plain");
		// } else {
		// File f = new File(imgPath);
		// if ((f != null) && (f.exists()) && (f.isFile())) {
		// intent.setType("image/png");
		// intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		// }
		// }

		intent.putExtra(Intent.EXTRA_HTML_TEXT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (share.get("packageName") != null) {
			intent.setComponent(new ComponentName(share.get("packageName"),
					share.get("activityName")));
			context.startActivity(intent);
		} else {
			context.startActivity(Intent.createChooser(intent, msgTitle));
		}
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.heib:
//			dismiss();
//			break;
//		case R.id.qq:
//			onClickShare();
//			// shareMsg(list.get(2));
//			break;
//		case R.id.wx:
//			// shareMsg(list.get(0));
//			WXFenShare();
//			break;
//		case R.id.pyq:
//			// shareMsg(list.get(1));
//			WXShare();
//			break;
//		case R.id.wb:
//			// shareMsg(list.get(3));
//			// reqMsg();
//			// WBShare();
//			shareToQzone();
//			break;
//		default:
//			break;
//		}
//	}

	/**
	 * qq分享
	 */
	private void onClickShare() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, msgTitle);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, msgText);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, sharUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageurl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "");
		// params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
		// QQ分享要在主线程做
		// ThreadManager.getMainHandler().post(new Runnable() {
		//
		// @Override
		// public void run() {
		// if (null != mTencent) {
		mTencent.shareToQQ(context, params, qqShareListener);
		// }
		// }
		// });
		MyDialog.closeDialog();
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			// if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
			// Util.toastMessage(QQShareActivity.this, "onCancel: ");
			// }
			if (mTencent != null) {
				mTencent.releaseResource();
			}
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			// Util.toastMessage(QQShareActivity.this, "onComplete: " +
			// response.toString());
			if (mTencent != null) {
				mTencent.releaseResource();
			}
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			// Util.toastMessage(QQShareActivity.this, "onError: " +
			// e.errorMessage, "e");
			if (mTencent != null) {
				mTencent.releaseResource();
			}
		}
	};

	/**
	 * 微信朋友圈分享
	 */
	private void WXShare() {
		if (!MyData.isAvilible(context, "com.tencent.mm")) {
			Toast.makeText(context, "请先安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		pdialog = ProgressDialog.show(context, "请稍等", "正在处理图片");
		new Thread() {
			public void run() {
				try {
					Bitmap bmp = returnBitMap(imageurl);
					// BitmapFactory.decodeResource(context.getResources(),
					// R.drawable.abaose);
					Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150,
							true);
					bmp.recycle();
					Message mess = new Message();
					mess.obj = thumbBmp;
					mess.what = 0;
					handler.dispatchMessage(mess);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	/**
	 * 微信分享
	 */
	private void WXFenShare() {
		if (!MyData.isAvilible(context, "com.tencent.mm")) {
			Toast.makeText(context, "请先安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		pdialog = ProgressDialog.show(context, "请稍等", "正在处理图片");
		new Thread() {
			public void run() {
				try {
					Bitmap bmp = returnBitMap(imageurl);
//					Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), shareContent.getPicResource());
					// BitmapFactory.decodeResource(context.getResources(),
					// R.drawable.abaose);
					// bmp.recycle();
					Message mess = new Message();
					mess.obj = getBitmapBytes(bmp, false);
					mess.what = 1;
					handler.dispatchMessage(mess);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			};
		}.start();
	}

	// 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
	private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
		if (bitmap == null) {
			return null;
		}
		Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		int i;
		int j;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			i = bitmap.getWidth();
			j = bitmap.getWidth();
		} else {
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
		while (true) {
			localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
					80, 80), null);
			if (paramBoolean)
				bitmap.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
				// F.out(e);
			}
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
	}

	/**
	 * 微博分享
	 */
	private void WBShare() {
		// MainActivity.mAccessToken =
		// AccessTokenKeeper.readAccessToken(context);

		if (mAccessToken.isSessionValid()) {
			// TODO发微博
			// reqMsg();
		} else {
			/** 不使用SSO方式进行授权验证 */
			// mWeibo.anthorize(AppMain.this, new AuthDialogListener());

			/** 使用SSO方式进行授权验证 */
			// mSsoHandler = new SsoHandler(this, mWeibo);
			// mSsoHandler.authorizeClientSso(new AuthListener());
			// context.startActivity(new Intent(context, OpenSq.class));
			// mSsoHandler.authorize(new AuthDialogListener(), null);
		}
		MyDialog.closeDialog();
	}

	/**
	 * 向weibo 客户端注册发送一个携带：文字、图片等数据
	 * 
	 */
	// public void reqMsg() {
	//
	// // weiboApi.registerApp();
	//
	// /* 图片对象 */
	// ImageObject imageobj = new ImageObject();
	//
	// // if (bitmap != null) {
	// imageobj.setImageObject(BitmapFactory.decodeResource(
	// context.getResources(), R.drawable.abaose));
	// // }
	//
	// /* 微博数据的message对象 */
	// WeiboMultiMessage multmess = new WeiboMultiMessage();
	// TextObject textobj = new TextObject();
	// textobj.text = "这是我的测试微博分享消息，大家看的到吗？";
	//
	// multmess.textObject = textobj;
	// multmess.imageObject = imageobj;
	// /* 微博发送的Request请求 */
	// SendMultiMessageToWeiboRequest multRequest = new
	// SendMultiMessageToWeiboRequest();
	// multRequest.multiMessage = multmess;
	// // 以当前时间戳为唯一识别符
	// multRequest.transaction = String.valueOf(System.currentTimeMillis());
	// MainActivity.mWeibo.sendRequest(MainActivity.acti, multRequest);
	// }

	/**
	 * qq空间分享
	 */
	private void shareToQzone() {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, msgTitle);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, msgText);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, sharUrl);
		ArrayList imageUrls = new ArrayList();
		// imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
		imageUrls.add(imageurl);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
				QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
		mTencent.shareToQzone(context, params, qqShareListener);
		MyDialog.closeDialog();
	}

	public Bitmap returnBitMap(String url) {
		// url =
		// "http://125.68.187.13/bdo2o/bdadmin/upload/60bfa13898d04ad9b83999d0179d29b6.jpg";
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;

			conn = (HttpURLConnection) myFileUrl.openConnection();

			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (pdialog != null) {
				pdialog.dismiss();
			}
			if (msg.what == 0) {
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = sharUrl;
				WXMediaMessage wxmsg = new WXMediaMessage(webpage);
				wxmsg.title = msgTitle;
				wxmsg.description = msgText;
				if (msg.obj != null)
					wxmsg.setThumbImage((Bitmap) msg.obj);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = String.valueOf(System.currentTimeMillis());
				req.message = wxmsg;
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
				wxapi.sendReq(req);
			} else {
				String url = sharUrl;// 收到分享的好友点击信息会跳转到这个地址去
				WXWebpageObject localWXWebpageObject = new WXWebpageObject();
				localWXWebpageObject.webpageUrl = url;
				WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
				localWXMediaMessage.title = msgTitle;// 不能太长，否则微信会提示出错。
				localWXMediaMessage.description = msgText;
				localWXMediaMessage.mediaObject = localWXWebpageObject;
				if (msg.obj != null)
					localWXMediaMessage.thumbData = (byte[]) msg.obj;
				// BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.abaose), false);
				SendMessageToWX.Req localReq = new SendMessageToWX.Req();
				localReq.transaction = System.currentTimeMillis() + "";
				localReq.message = localWXMediaMessage;
				wxapi.sendReq(localReq);
			}
			MyDialog.closeDialog();
			return false;
		}
	});
}
