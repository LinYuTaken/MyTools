package com.sy.mobile.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.tools.R;
import com.lin.mobile.album.Picker;
import com.lin.mobile.album.PicturePickerUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 选择图片
 *
 * @author admin
 *
 */
public class SelectPicture {
	private String strImgPath;

	/**
	 * 拍照
	 */
	public static final int CAMERA_WITH_DATA = 1001;

	/**
	 * 单选相册
	 */
	public static final int PHOTO_PICKED_WITH_DATA = 1002;

	/**
	 * 多选相册
	 */
	public static final int PHOTO_PICKED_WITH_DATAS = 1003;

	Activity con;

	private boolean isdas = false;

	private int maxint = 9;

	/**
	 * 显示选择图片方式弹出框
	 */
	public void showPictureSelect(Activity context) {
		con = context;
		LayoutInflater li = LayoutInflater.from(context);
		final Dialog dialog = new Dialog(context, R.style.shareDialogTheme);
		View view = li.inflate(R.layout.selectpicture, null);
		RelativeLayout hei = (RelativeLayout) view.findViewById(R.id.hei);
		TextView paizhao = (TextView) view.findViewById(R.id.paizhao);
		TextView xiangce = (TextView) view.findViewById(R.id.xiangce);
		TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
		hei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		paizhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				photograph();
				dialog.dismiss();
			}
		});
		xiangce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				photx();
				dialog.dismiss();
			}
		});
		quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	/**
	 * 是否启动多选相册
	 * @param isdas
	 */
	public void setAlbums(boolean isdas){
		this.isdas = isdas;
	}

	/**
	 * 设置多选相册最多能选多少张图片
	 * @param isdas
	 */
	public void setMaxNumber(int maxint){
		this.maxint = maxint;
	}

	/**
	 * 拍照
	 */
	public void photograph() {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 存放照片的文件夹
		strImgPath = Environment.getExternalStorageDirectory().toString()
				+ "/media/";
		// 照片命名
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + ".jpg";
		File out = new File(strImgPath);
		if (!out.exists()) {
			// 创建文件夹
			out.mkdirs();
		}
		out = new File(strImgPath, fileName);
		// 该照片的绝对路径
		strImgPath = strImgPath + fileName;
		Uri uri = Uri.fromFile(out);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			Uri fileUri = FileProvider.getUriForFile(con, con.getPackageName()+".provider", out);//android 7.0以上
			imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			grantUriPermission(con, fileUri, imageCaptureIntent);
		}
		con.startActivityForResult(imageCaptureIntent, CAMERA_WITH_DATA);
	}

	private static void grantUriPermission(Context context, Uri fileUri, Intent intent) {
		List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resInfoList) {
			String packageName = resolveInfo.activityInfo.packageName;
			context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
	}

	/**
	 * 相册选择
	 */
	public void photx() {
		if(!isdas){
			Intent i = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			con.startActivityForResult(i, PHOTO_PICKED_WITH_DATA);
		}else{
			Picker.from(con).count(maxint).enableCamera(false)
					.forResult(PHOTO_PICKED_WITH_DATAS);
		}
	}



	/**
	 * 获取图片地址
	 *
	 * @param data
	 * @param type
	 * @return
	 */
	public String getStrImgPath(Intent data, int type) {
		// 图片选择返回不等于空操作图片
		if (type == PHOTO_PICKED_WITH_DATA && data != null) {
			strImgPath = "";
			String picturePath = selectImage(con, data);
			if (picturePath.indexOf("/") > 0)
				strImgPath = picturePath.substring(picturePath.indexOf("/"),
						picturePath.length());
			else
				strImgPath = picturePath;
		} else if(type == PHOTO_PICKED_WITH_DATA&& data == null){
			strImgPath = "";
		}

		//多选相册
		if(type == PHOTO_PICKED_WITH_DATAS && data != null){
			strImgPath = "";
			List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
			for (Uri u : mSelected) {
				strImgPath += selectImage(con, u)+",";
			}
			if(strImgPath.length()>1)
				strImgPath = strImgPath.substring(0,strImgPath.length()-1);
			strImgPath.replace("null", "");
		} else if (type == PHOTO_PICKED_WITH_DATAS&& data == null){
			strImgPath = "";
		}

		// 如果拍照的图片不存在，设置路径为空
		if (type == CAMERA_WITH_DATA) {
			try {
				File f = new File(strImgPath);
				if (!f.exists())
					strImgPath = "";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (strImgPath == null||strImgPath.length()==0)
			return "";
		if(type == CAMERA_WITH_DATA || type == PHOTO_PICKED_WITH_DATA){
			int du = readPictureDegree(strImgPath);
			if (du != 0) {
				strImgPath = rotaingImageView(du, strImgPath);
			}
		}
		return strImgPath;
	}

	/**
	 * @param context
	 * @param data
	 * @return
	 */
	public String selectImage(Context context, Uri selectedImage) {
		String picturePath = "";
		// Log.e(TAG, selectedImage.toString());
		try {
			if (selectedImage != null) {
				String uriStr = selectedImage.toString();
				String path = uriStr.substring(10, uriStr.length());
				if (path.startsWith("com.sec.android.gallery3d")) {
					// Log.e(TAG,
					// "It's auto backup pic path:"+selectedImage.toString());
					return "";
				}
			}

			String uriStr = selectedImage.toString();
			if (uriStr.indexOf("file:") != -1) {
				picturePath = uriStr;
			} else {
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (picturePath.indexOf("/") > 0)
			picturePath = picturePath.substring(picturePath.indexOf("/"),
					picturePath.length());
		return picturePath;
	}

	public String selectImage(Context context, Intent data) {
		Uri selectedImage = data.getData();
		String picturePath = "";
		// Log.e(TAG, selectedImage.toString());
		try {
			if (selectedImage != null) {
				String uriStr = selectedImage.toString();
				String path = uriStr.substring(10, uriStr.length());
				if (path.startsWith("com.sec.android.gallery3d")) {
					// Log.e(TAG,
					// "It's auto backup pic path:"+selectedImage.toString());
					return "";
				}
			}

			String uriStr = selectedImage.toString();
			if (uriStr.indexOf("file:") != -1) {
				picturePath = uriStr;
			} else {
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picturePath;
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 *
	 * @param angle
	 *
	 * @param bitmap
	 *
	 * @return Bitmap
	 */
	public static String rotaingImageView(int angle, String fele) {
		String feile = "";
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(
				"file://" + fele);
		// Imagelo
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		if (bitmap != null) {
			StringBuffer stringBuffer = new StringBuffer(fele);
			feile = stringBuffer.insert(fele.indexOf("."), "test").toString();
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			saveBitmap2file(resizedBitmap, feile);
		}
		return feile;
	}

	static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp.compress(format, quality, stream);
	}
}
