package com.sy.mobile.control;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sy.mobile.analytical.ScreenTools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;



/**
 * 朋友圈图片列表自适应ImageView
 */
public class CustomImageView extends ImageView {
    private String url;
    private boolean isAttachedToWindow;
    Context context;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMax(context);
    }

    public CustomImageView(Context context) {
        super(context);
        setMax(context);
    }


    private void setMax(Context context){
    	this.context = context;
    	setAdjustViewBounds(true);
    	setClickable(true);
    	ScreenTools screentools = ScreenTools.instance(context);
    	setMaxHeight(screentools.dip2px(300));
    	setMaxWidth(screentools.getScreenWidth() - screentools.dip2px(80));
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable=getDrawable();
                if(drawable!=null) {
                    //按下的时候添加滤镜效果
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //放开的时候关闭滤镜效果
                Drawable drawableUp=getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
//        Picasso.with(getContext()).cancelRequest(this);
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
//                Picasso.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
//            	setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            	ImageLoader.getInstance().displayImage(url, this);
            }
        }
    }
}
