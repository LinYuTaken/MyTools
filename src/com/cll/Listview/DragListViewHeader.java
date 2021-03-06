package com.cll.Listview;



import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tools.R;


public class DragListViewHeader extends LinearLayout {
	private LinearLayout layout;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private TextView hintTextView,timerText;
	private int mState = STATE_NORMAL;

	private Animation rotateUpAnim;
	private Animation rotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	private String ordinaryStr,releaseStr;
	private Context context;

	public DragListViewHeader(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	/**
	 * 初始化组件
	 *
	 * @param context
	 */
	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0);
		layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pagination_listview_header, null);
		addView(layout, lp);
		setGravity(Gravity.BOTTOM);

		arrowImageView = (ImageView) findViewById(R.id.pagination_header_arrow);
		hintTextView = (TextView) findViewById(R.id.pagination_header_hint_textview);
		timerText = (TextView) findViewById(R.id.time);
		progressBar = (ProgressBar) findViewById(R.id.pagination_header_progressbar);

		rotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		rotateUpAnim.setFillAfter(true);

		rotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		rotateDownAnim.setFillAfter(true);
	}

	/**
	 * 修改顶部的提示文字
	 * @param ordinaryStr
	 * @param releaseStr
	 */
	public void setHeaderString(String ordinaryStr,String releaseStr){
		this.ordinaryStr = ordinaryStr;
		this.releaseStr = releaseStr;
		hintTextView.setText(ordinaryStr);
	}

	/**
	 * 设置颜色
	 * @param color
	 */
	public void setHeaderTextColor(int color){
		hintTextView.setTextColor(color);
		timerText.setTextColor(color);
	}

	/**
	 * 设置状态
	 *
	 * @param state
	 */
	public void setState(int state) {
		if (state == mState)
			return;
		// 刷新状态
		if (state == STATE_REFRESHING) {
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			// 显示箭头图片
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
		}

		switch (state) {
			case STATE_NORMAL:
				if (mState == STATE_READY) {
					arrowImageView.startAnimation(rotateDownAnim);
				}
				if (mState == STATE_REFRESHING) {
					arrowImageView.clearAnimation();
				}
				hintTextView.setText(ordinaryStr!=null&&ordinaryStr.length()>0?ordinaryStr:context.getResources().getString(R.string.pagination_header_hint_normal));
				break;
			case STATE_READY:
				if (mState != STATE_READY) {
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(rotateUpAnim);
					hintTextView.setText(releaseStr!=null&&releaseStr.length()>0?releaseStr:context.getResources().getString(R.string.pagination_header_hint_ready));
				}
				break;
			case STATE_REFRESHING:
				hintTextView.setText(R.string.pagination_header_hint_loading);
				break;
			default:
		}

		mState = state;
	}

	/**
	 * 设置显示高度
	 *
	 * @param height
	 */
	public void setVisibleHeight(int height) {
		if (height < 0) {
			height = 0;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
		params.height = height;
		layout.setLayoutParams(params);
	}

	/**
	 * 获取高度
	 */
	public int getVisibleHeight() {
		return layout.getHeight();
	}
	/**
	 * 设置显示时间
	 */
	public void setTimerText(String vale){
		timerText.setText(vale);
	}
}
