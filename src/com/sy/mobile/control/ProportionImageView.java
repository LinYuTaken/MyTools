package com.sy.mobile.control;

import com.wxample.data.MyData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

/**
 * 宽高比例大小图片
 * @author admin
 *
 */
public class ProportionImageView extends ImageView{
	//控件高宽
	int topcenint = 0,width = 0;
	public ProportionImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 按照宽度设置比例设置高度
	 */
	public void setWProportion(double wpr,double hpr){
		getMak();
		MarginLayoutParams headerLayoutParams = (MarginLayoutParams) getLayoutParams();
		headerLayoutParams.height = MyData.mToInt(MyData.mul(MyData.div(width, wpr, 1), hpr));
		setLayoutParams(headerLayoutParams);
	}
	/**
	 * 按照高度设置比例设置宽度
	 */
	public void setHProportion(double wpr,double hpr){
		getMak();
		MarginLayoutParams headerLayoutParams = (MarginLayoutParams) getLayoutParams();
		headerLayoutParams.width = MyData.mToInt(MyData.mul(MyData.div(topcenint, hpr, 1), wpr));
		setLayoutParams(headerLayoutParams);
	}

	/**
	 * 获取控件当前的宽高
	 */
	private void getMak(){
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		measure(w, h);
		topcenint = getMeasuredHeight();
		width = getMeasuredWidth();
	}
}
