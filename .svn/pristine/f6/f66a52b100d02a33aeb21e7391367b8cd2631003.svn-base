package com.sy.mobile.control;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tools.R;

/**
 * 底部导航栏
 * Created by admin on 2018/3/5.
 */
public class BottomBar extends LinearLayout {
    public BottomBar(Context context) {
        super(context);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean isJump = true;

    //字体选中时的颜色 //字体未选中时的颜色
    int sel_color = 0, not_color = 0;
    //选中时的图标
    int[] iocis;
    //未选中的图标
    int[] iocno;
    //当前的计数
    int index;
    ImageView[] images;
    TextView[] textviews;
    TextView[] homesums;

    /**
     * @param iocis   选中时的图标
     * @param iocno   未选中时的图标
     * @param content 文字内容
     */
    public void init(Context context, int[] iocis, int[] iocno, String[] content) {
//        setOrientation(LinearLayout.HORIZONTAL);
        removeAllViews();
        this.iocis = iocis;
        this.iocno = iocno;
        //判断是否相同
        if (iocis == null || iocno == null || content == null || iocis.length != iocno.length || iocno.length != content.length)
            return;
        images = new ImageView[iocis.length];
        textviews = new TextView[iocis.length];
        homesums = new TextView[iocis.length];
        for (int i = 0; i < iocis.length; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_item, null);
            ImageView homeioc = (ImageView) view.findViewById(R.id.homeioc);
            TextView homemess = (TextView) view.findViewById(R.id.homemess);
            TextView homesum = (TextView) view.findViewById(R.id.homesum);
            //初始化的时候 第一个默认为选中
            if (i == 0) {
                homeioc.setImageResource(iocis[i]);
                if (sel_color != 0)
                    homemess.setTextColor(sel_color);
            } else {
                homeioc.setImageResource(iocno[i]);
                if (not_color != 0)
                    homemess.setTextColor(not_color);
            }
            homemess.setText(content[i]);
            //设置view为一比一
            LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
            final int test = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null)
                        viewOnclick.viewOnclick(test);
                    if (isJump)
                        shouButton(test);
                }
            });
            images[i] = homeioc;
            textviews[i] = homemess;
            homesums[i] = homesum;
            view.setLayoutParams(params);
            addView(view);
        }
    }

    /**
     * 设置点击是否变
     *
     * @param isJump
     */
    public void setJump(boolean isJump) {
        this.isJump = isJump;
    }

    /**
     * 显示红点
     *
     * @param page
     * @param number
     */
    public void setRedNumber(int page, int number) {
        if (number < 1) {
            homesums[page].setVisibility(View.GONE);
        } else {
            homesums[page].setText(number + "");
            homesums[page].setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示红点
     *
     * @param number
     */
    public void setRedNumber(int[] number) {
        for (int i = 0; i < number.length; i++) {
            setRedNumber(i, number[i]);
        }
    }

    public interface ViewOnclick {
        void viewOnclick(int page);
    }

    ViewOnclick viewOnclick;

    /**
     * 点击事件
     *
     * @param viewOnclick
     */
    public void setViewOnclick(ViewOnclick viewOnclick) {
        this.viewOnclick = viewOnclick;
    }

    /**
     * 设置字体颜色
     *
     * @param sel_color 选中时的颜色
     * @param not_color 未选中时的颜色
     */
    public void setTextColor(int sel_color, int not_color) {
        this.sel_color = sel_color;
        this.not_color = not_color;
    }

    /**
     * 控制显示
     *
     * @param in
     */
    public void shouButton(int in) {
        images[index].setImageResource(iocno[index]);
        textviews[index].setTextColor(not_color);
        index = in;
        images[index].setImageResource(iocis[index]);
        textviews[index].setTextColor(sel_color);
    }
}
