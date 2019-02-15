package com.sy.mobile.control;

/**
 * Created by admin on 2017/12/4.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tools.R;

/**
 * 标题栏
 * title bar
 *
 */
public class TitleBar extends RelativeLayout {

    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage;
    protected TextView titleView;
    protected RelativeLayout titleLayout;
    protected TextView right_text,left_text;
    protected View bomm;

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_title, this);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        rightImage = (ImageView) findViewById(R.id.right_image);
        titleView = (TextView) findViewById(R.id.title);
        titleLayout = (RelativeLayout) findViewById(R.id.root);
        right_text = (TextView) findViewById(R.id.right_text);
        left_text = (TextView) findViewById(R.id.main_left_text);
        bomm = findViewById(R.id.bommView);
        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            String title = ta.getString(R.styleable.TitleBar_titleBarTitle);
            titleView.setText(title);

            Drawable leftDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }

            Drawable background = ta.getDrawable(R.styleable.TitleBar_titleBarBackground);
            if (null != background) {
                titleLayout.setBackgroundDrawable(background);
            }
            ta.recycle();
        }
    }

    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }

    /**
     * 设置左标题点击事件
     * @param listener
     */
    public void setLeftLayoutClickListener(OnClickListener listener) {
        leftLayout.setOnClickListener(listener);
    }

    /**
     * 设置右边的文字，设置文字时会隐藏右边的图标
     * @param text
     */
    public TextView setRightText(String text){
        right_text.setText(text);
        right_text.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.GONE);
        return  right_text;
    }

    /**
     * 设置左边的文字，设置文字时会隐藏右边的图标
     * @param text
     */
    public TextView setLeftText(String text){
        left_text.setText(text);
        left_text.setVisibility(View.VISIBLE);
        leftImage.setVisibility(View.GONE);
        return  left_text;
    }

    public String getRightTextString(){
        return right_text.getText().toString();
    }
    /**
     * 设置右边字的颜色
     * @param text
     */
    public void setRightTextColor(int text){
        right_text.setTextColor(text);
    }

    /**
     * 设置右边的点击事件
     * @param listener
     */
    public void setRightLayoutClickListener(OnClickListener listener) {
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility) {
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility) {
        rightLayout.setVisibility(visibility);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTitleSize(int size) {
        titleView.setTextSize(size);
    }
    /**
     * 设置标题颜色
     */
    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }

    /**
     * 获取标题
     * @return
     */
    public String getTitle() {
        return titleView.getText().toString();
    }

    /**
     * 设置标题栏颜色
     * @param color
     */
    public void setBackgroundColor(int color) {
        titleLayout.setBackgroundColor(color);
    }

    /**
     * 底部的线条是否显示
     * @param visibility
     */
    public void setBottomViewVisibility(int visibility){
        bomm.setVisibility(visibility);
    }

    /**
     * 底部的线条的颜色
     */
    public void setBottomColor(int color){
        bomm.setBackgroundColor(color);
    }
    public RelativeLayout getLeftLayout() {
        return leftLayout;
    }

    public RelativeLayout getRightLayout() {
        return rightLayout;
    }
}