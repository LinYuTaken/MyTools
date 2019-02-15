package com.sy.mobile.control;

import java.util.List;

import com.sy.mobile.picture.ImagePagerActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 朋友圈图片管理
 *
 * @author admin
 *
 */
public class ImageListLay extends LinearLayout {
	// 图片路径
	List<String> imageUrl;
	Context context;
	int wh;
	boolean isShowSingle;

	public ImageListLay(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public ImageListLay(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@SuppressLint("NewApi")
	public ImageListLay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * 初始化
	 */
	public void inten(final List<String> imageu, Context cont) {
//		if (imageUrl == null) {
		removeAllViews();
		this.imageUrl = imageu;
		// 如果只有一张图片显示单独的
		if (imageUrl == null || imageUrl.size() == 0)
			return;
		if (imageUrl.size() == 1 && !isShowSingle) {
			CustomImageView custima = new CustomImageView(cont);
			custima.setImageUrl(imageu.get(0));
			custima.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showPic(imageu, 0);
				}
			});
			addView(custima);
		} else {
			NineGridlayout ninegrid = new NineGridlayout(cont);
			if(wh>0)
				ninegrid.setMainWidth(wh);
			ninegrid.setImagesData(imageUrl);
			addView(ninegrid);
		}
//		}
	}

	/**
	 * 设置减少的宽度
	 * @param wh
	 */
	public void setMainWidth(int wh){
		this.wh = wh;
	}

	/**
	 * 只有一张图也不显示大图
	 */
	public void setNotSingle(){
		this.isShowSingle = true;
	}
	/**
	 * 显示图片详情
	 */
	private void showPic(List<String> pictures,int index){
		String[] tturl = new String[pictures.size()];
		for (int i = 0; i < pictures.size(); i++) {
			tturl[i] = pictures.get(i);
		}
		Intent intent = new Intent();
		intent.setClass(context,ImagePagerActivity.class);
		// 传入图片路径数组
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, tturl);
		//传入要显示第几张图
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
		context.startActivity(intent);
	}
}
