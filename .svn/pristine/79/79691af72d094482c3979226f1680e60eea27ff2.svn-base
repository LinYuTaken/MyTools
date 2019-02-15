package com.sy.mobile.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带中划线的
 * @author admin
 *
 */
public class FlagTextView extends TextView{


	public FlagTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FlagTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FlagTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 倾斜度45,上下左右居中
		getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		super.onDraw(canvas);
	}
}
