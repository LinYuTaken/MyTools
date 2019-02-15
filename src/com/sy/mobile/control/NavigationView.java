package com.sy.mobile.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.tools.R;
import com.lin.mobile.entity.Navig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sy.mobile.analytical.ScreenTools;
import com.sy.mobile.picture.ImagePagerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * 导航栏
 *
 * @author Administrator
 */
public class NavigationView extends RelativeLayout {
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter; //图片
    private MyFragmentPagerAdapter myAdapter;//碎片
    private List<Navig> list = new ArrayList<Navig>();
    private List<ImageView> imageViews = new ArrayList<ImageView>(); // 滑动的图片集合
    private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();//滑动碎片集合
    private List<TextView> textViews = new ArrayList<TextView>();//计数红点集合
    private FragmentActivity context;
    int index = 0;
    ScreenTools screenTools;
    //翻页时间
    int timenumb = 4000;
    //自定义计数器点点
    private int selectImage, notsetlectImage;
    /**
     * 页码
     */
    int indexto = 1;
    Timer timer;
    String[] tturl;
    /**
     * 页间距
     */
    int pageMargin;
    /**
     * 默认图片
     */
    String morenioc = "";
    //是否需要计数条
    boolean ispage;
    //是否切换的时候有动画
    boolean isSwitch;
    //是否有翻页效果
    boolean isEffect;
    /**
     * 是否需要循环滑动
     */
    boolean isRe;

    ScaleType scaleType;

    public NavigationView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        // intent();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        // intent();
    }

    public void init(FragmentActivity context) {
        this.context = context;
        screenTools = ScreenTools.instance(context);
        selectImage = R.drawable.ysechek;
        notsetlectImage = R.drawable.nochek;
        mViewPager = new ViewPager(context);
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Calendar c = Calendar.getInstance();
        mViewPager.setId("ADVP".hashCode());
    }

    /**
     * 滑动回调
     *
     * @author admin
     */
    public interface onViewPagerRoll {
        public void onRoll(int position, Navig navig);
    }

    onViewPagerRoll ovpr;

    /**
     * 滑动回调
     *
     * @param ovpr
     */
    public void setOnViewPagerRool(onViewPagerRoll ovpr) {
        this.ovpr = ovpr;
    }

    /**
     * 设置默认图片(如果没有图片时，并且没有默认图片，控件不会显示)
     *
     * @param mo
     */
    public void setMorenString(String mo) {
        morenioc = mo;
    }

    /**
     * 设置自定义计数器点点
     *
     * @param yss 选中状态的
     * @param no  未选中的
     */
    public void setSelectImage(int yss, int no) {
        selectImage = yss;
        notsetlectImage = no;
    }

    /**
     * 初始化
     *
     * @param lists 内容list
     */
    public void intent(List<Navig> lists) {
        if (lists == null)
            return;
        this.list = new ArrayList<>();
        this.list.addAll(lists);
        //设置默认图片
        if (list.size() == 0 && morenioc.length() > 0) {
            Navig na = new Navig();
            na.setImageurl(morenioc);
            na.setIntent(true);
            this.list.add(na);
        }
        //如果没用内容，隐藏控件
        if (this.list.size() == 0) {
            setVisibility(View.GONE);
            return;
        }
        //清除
        closCoo();
        if (list.size() > 1) {
            isRe = true;
            //如果要循环滚动复制开头和结尾，形成循环的效果 如果是碎片，需要自己传 直接复制的话会报错
            if (list.get(0).getFragment() == null) {
                list.add(list.get(0));
                list.add(0, list.get(list.size() - 2));
            }
        } else {
            isRe = false;
        }
        if (list.get(0).getFragment() != null) {
            //显示碎片
            generateFragment();
        } else {
            //显示图片内容
            generateImage(lists);
        }
        //设置view间的间距
        if (pageMargin != 0)
            mViewPager.setPageMargin(pageMargin);
        //是否有滑动效果
        if (isEffect)
            mViewPager.setPageTransformer(true, new MyTransformation());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // Log.i("hua", "选择" + position);
                //切换计数点
                index = position;
                indexto = position;
                if (!ispage)
                    chcks(position);
                if (ovpr != null)
                    ovpr.onRoll(position, list.get(position));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // Log.i("hua", "滑动"+arg1);
            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // Log.i("hua", "状态" + position);
                if (position == 0) {
                    pageRe();
                    starRe();
                } else {
                    stopRe();
                }
            }
        });
        addView(mViewPager);
        //是否需要计数条
        if (!ispage)
            addView(generateCount());
        mViewPager.setCurrentItem(1, false);
        starRe();
    }

    /**
     * 生成图片
     *
     * @param lists
     */
    private void generateImage(List<Navig> lists) {
        List<Navig> testlist = new ArrayList<Navig>();
        testlist.addAll(lists);
        //获取图片地址
        tturl = new String[testlist.size()];
        for (int i = 0; i < testlist.size(); i++) {
            tturl[i] = testlist.get(i).getImageurl();
        }
        // 生成显示的图
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //这里显示图的时候，如果有预览图显示预览图
            ImageLoader.getInstance().displayImage(list.get(i).getImageurlThumbnail() != null
                    && list.get(i).getImageurlThumbnail().length() > 2
                    ? list.get(i).getImageurlThumbnail() : list.get(i).getImageurl(), imageView);
//			if(ss==null)
//				imageView.setScaleType(ScaleType.CENTER_CROP);//CENTER_INSIDE
//			else
            //设置图片显示模式
            if (scaleType == null)
                imageView.setScaleType(ScaleType.FIT_XY);
            else {
                imageView.setScaleType(scaleType);
            }
//			imageView.setBackgroundColor(Color.WHITE);
            imageViews.add(imageView);
        }
        mAdapter = new MyPagerAdapter(imageViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(imageViews.size() - 1);
    }

    /**
     * 生成碎片
     */
    private void generateFragment() {
        pagerItemList = new ArrayList<Fragment>();
        for (int i = 0; i < list.size(); i++) {
            pagerItemList.add(list.get(i).getFragment());
        }
        myAdapter = new MyFragmentPagerAdapter(context.getSupportFragmentManager(), pagerItemList);
        mViewPager.setAdapter(myAdapter);
        mViewPager.setOffscreenPageLimit(pagerItemList.size() - 1);
    }

    /**
     * 生成计数条
     */
    private View generateCount() {
        //计数条
        LinearLayout chen = new LinearLayout(context);
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, screenTools.dip2px(9));
        linearParams.rightMargin = 10;
        linearParams.bottomMargin = 7;
        linearParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        linearParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        chen.setLayoutParams(linearParams);
        chen.setOrientation(LinearLayout.HORIZONTAL);
        // 生成计数器
        int siz = 0;
        if (isRe) {
            siz = list.size() - 2;
        } else {
            siz = list.size();
        }
        //生成计数条里面的点点
        for (int i = 0; i < siz; i++) {
            TextView textView =
//					(TextView) LayoutInflater.from(context).inflate(R.layout.textma,null);
                    new TextView(context);
            textView.setBackgroundResource(notsetlectImage);
            RelativeLayout.LayoutParams linear = new RelativeLayout.LayoutParams(screenTools.dip2px(10), screenTools.dip2px(5));
            textView.setLayoutParams(linear);
            chen.addView(textView);
            textViews.add(textView);
        }
        textViews.get(0).setBackgroundResource(selectImage);
        return chen;
    }


    /**
     * 图片页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        private List<ImageView> mViewList;

        MyPagerAdapter(List<ImageView> viewList) {
            mViewList = viewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = mViewList.get(position);
            if (list.get(position).getOnClickListener() != null) {
                view.setOnClickListener(list.get(position).getOnClickListener());
            } else {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int tein = position;
                        //记录点开大图要显示第几张图
                        if (tein != 0)
                            tein--;
                        Intent intent = new Intent();
                        //判断点击后是跳转图片大图还是跳转页面
                        if (list.get(position).isIntent()) {
                            intent = list.get(position).getIntent();
                        } else {
                            //因为做了轮播在首末加了两个多的图，所以需要设置下
                            if (position > tturl.length)
                                tein = tturl.length;
                            intent.setClass(context, ImagePagerActivity.class);
                            // 传入图片路径数组
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, tturl);
                            //传入要显示第几张图
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, tein);
                        }
                        if (intent != null)
                            context.startActivity(intent);
                        else {
                            MyDialog.showTextToast(list.get(position).getIntentString(), context);
                        }
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    /**
     * fragment适配
     */
//    public class MyAdapter extends FragmentPagerAdapter {
//        ArrayList<Fragment> pagerItemList;
//        public MyAdapter(FragmentManager fm ,ArrayList<Fragment> pagerItemList) {
//            super(fm);
//            this.pagerItemList = pagerItemList;
//        }
//
//        @Override
//        public int getCount() {
//            return pagerItemList.size();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            Fragment fragment = null;
//            if (position < pagerItemList.size())
//                fragment = pagerItemList.get(position);
//            else
//                fragment = pagerItemList.get(0);
//
//            return fragment;
//        }
//    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    /**
     * 设置图片显示格式
     *
     * @param scaleType
     */
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    /**
     * 隐藏计数条
     */
    public void hidePageRoad() {
        ispage = true;
    }

    /**
     * 设置页间距
     *
     * @param pageMargin
     */
    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    /**
     * 是否需要翻页动画
     *
     * @param isEffect
     */
    public void isEffect(boolean isEffect) {
        this.isEffect = isEffect;
    }

    /**
     * 设置是否需要滑动效果
     */
    public void toggleAnimation(boolean issw) {
        isSwitch = issw;
    }

    public void setTimenumb(int timenumb) {
        this.timenumb = timenumb;
    }

    /**
     * 循环滚动
     */
    private void pageRe() {
        //为了循环滚动，在第一个和最后一个加了相同的布局，所以转到这里的时候要跳转
        if (isRe) {
            //如果是最后一个，切换到第一个
            if (index == list.size() - 1) {
                mViewPager.setCurrentItem(1, isSwitch);
            } else if (index == 0) {
                //如果是第一个切换到最后一个
                mViewPager.setCurrentItem(list.size() - 2, isSwitch);
            }
        }
    }

    /**
     * 修改计数器
     */
    private void chcks(int po) {
        //为了循环滚动，在第一个和最后一个加了相同的布局所以0和最后一个的时候不做操作
        if (po == 0 || po == list.size() - 1 || isRe == false) {
            return;
        }
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setBackgroundResource(notsetlectImage);
        }
        textViews.get(po - 1).setBackgroundResource(selectImage);
    }

    /**
     * 开始
     */
    public void starRe() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(0);
            }
        }, timenumb, timenumb);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(android.os.Message msg) {
            //翻页
            indexto++;
            //如果已经最后一页了，跳到第一页
            if (indexto == list.size() - 1) {
                indexto = 1;
            }
            if (mViewPager != null) {
                mViewPager.setCurrentItem(indexto, isSwitch);
            } else {
                stopRe();
                indexto = 1;
            }
            return false;
        }
    });


    /**
     * 停止
     */
    public void stopRe() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 清除
     */
    public void closCoo() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        index = 0;
        indexto = 1;
        imageViews.clear();
        textViews.clear();
//        mViewPager = null;
        mAdapter = null;
        removeAllViews();
    }
}
