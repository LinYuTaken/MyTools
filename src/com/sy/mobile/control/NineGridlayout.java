package com.sy.mobile.control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

import com.sy.mobile.analytical.ScreenTools;
import com.sy.mobile.picture.ImagePagerActivity;


/**
 * 朋友圈图片列表多个图片显示
 */
public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//
    private int rows;//
    private List<String> listData;
    private int totalWidth;
    Context context;
    private int mainWidth = 80;

    public NineGridlayout(Context context) {
        super(context);
        ScreenTools screenTools=ScreenTools.instance(getContext());
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(mainWidth);
        this.context = context;
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenTools screenTools=ScreenTools.instance(getContext());
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(mainWidth);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * 设置位置
     * @param mainWidth
     */
    public void setMainWidth(int mainWidth){
        this.mainWidth = mainWidth;
        ScreenTools screenTools=ScreenTools.instance(getContext());
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(mainWidth);
    }

    /**
     * 设置控件的位置
     */
    private void layoutChildrenView(){
        //view的数量
        int childrenCount = listData.size();
        //单个view的宽度    控件的总宽度 减去两个间隔的宽度 除以3
        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        //宽高相同
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //控件的高度 单个view的高度 乘列数 加上列数减一的间隔
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            //把add了的view提取出来
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            //设置要显示的图片
            childrenView.setImageUrl(listData.get(i));

            int[] position = findPosition(i);
            //left 单个view的宽度加上间隔 在 乘 第几列 (如果列的值是0 值就会是0)
            int left = (singleWidth + gap) * position[1];
            // top 单个view的高度加上间隔 在 乘 第几行
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }

    }

    /**
     * 确定view在几行几列
     * @param childNum
     * @return
     */
    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        //遍历每行
        for (int i = 0; i < rows; i++) {
            //遍历每列
            for (int j = 0; j < columns; j++) {
                //如果遍历的view的位置和当前的数相同
                if ((i * columns + j) == childNum) {
                    //第几行乘一列有多少 加上当前行的列数
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 获取图片的间隔
     * @return
     */
    public int getGap() {
        return gap;
    }

    /**
     * 设置图片的间隔
     * @param gap
     */
    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<String> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局 根数图片数量判断有几行几列
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView(i);
                addView(iv,generateDefaultLayoutParams());
                i++;
            }
        } else {
            //原来的图片数量
            int oldViewCount = listData.size();
            //新的图片数量
            int newViewCount = lists.size();
            //如果原来的图片数量大于新的图片数量
            if (oldViewCount > newViewCount) {
                //删除多的图片 从最后一个新图的位置开始 删除的数量为原来图的数量减新的图的数量
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                //如果新的图比原来的图多 新增多出来的图
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView(i);
                    addView(iv,generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    /**
     * 生成一个图片控件
     * @param i
     * @return
     */
    private CustomImageView generateImageView(final int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	//图片点击
            	showPic(listData,i);
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
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
