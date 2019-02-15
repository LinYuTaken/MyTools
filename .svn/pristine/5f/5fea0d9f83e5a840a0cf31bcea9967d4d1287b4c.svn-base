package com.sy.mobile.analytical;

import android.R.bool;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import com.wxample.data.MyData;

/**
 * 动画
 *
 * @author Administrator
 */
public class MyAnimation {
    View view;
    //控件高宽
    int topcenint = 0, width = 0;
    //修改后的宽高
    int test = 0, testwidth = 0;
    //透明度
    int transp = 0;
    int shuzi = 0, testsum = 0;
    MarginLayoutParams headerLayoutParams;
    boolean ishuo = true;

    boolean isImplement = false;
    /**
     * 伸张
     */
    public static final int DONE = 1;
    /**
     * 收缩
     */
    public static final int CONTRACTION = 2;
    /**
     * 渐变消失
     */
    public static final int GRADIENTFAGE = 3;
    /**
     * 渐变出现
     */
    public static final int GRADIENTAPPEAR = 4;
    /**
     * 移动
     */
    public static final int MOBILE = 5;

    /**
     * 数字读取效果
     */
    public static final int SUM = 6;

    /**
     * 速度
     */
    private int SPEED = 20;

    /**
     * 间隔
     */
    private int interval = 20;

    public MyAnimation(View view) {
        this.view = view;
        headerLayoutParams = (MarginLayoutParams) view.getLayoutParams();
    }

    public MyAnimation() {

    }

    public void setHeigth(int he) {
        this.topcenint = he;
    }

    /**
     * 开始动画
     *
     * @param type
     */
    public void animationT(final int type) {
        if (ishuo) {
            view.setVisibility(View.VISIBLE);
            getMak();
            test = topcenint;
            ishuo = false;
            //如果是需要伸张的记录高度后把控件变成0
            if (type == DONE) {
                headerLayoutParams.height = 0;
                test = 0;
                view.setLayoutParams(headerLayoutParams);
            }
            //如果是渐变消失的，要先设置透明度
            if (type == GRADIENTFAGE) {
                transp = 255;
            }
        }
        //如果是渐变消失或者出现，要设置点击开关
        if (type == GRADIENTFAGE) {
            view.setEnabled(false);
        } else if (type == GRADIENTAPPEAR) {
            view.setEnabled(true);
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    switch (type) {
                        case CONTRACTION:
                            test -= SPEED;
                            break;
                        case DONE:
                            test += SPEED;
                            break;
                        case GRADIENTFAGE:
                            transp -= SPEED;
                            break;
                        case GRADIENTAPPEAR:
                            transp += SPEED;
                            break;
                        default:
                            break;
                    }
                    han.sendEmptyMessage(type);

                    if (type == CONTRACTION || type == DONE) {
                        if (test <= 0 || test > topcenint) {
                            if (completion != null)
                                SystemClock.sleep(200);
                            completion.OnCompletion();
                            break;
                        }
                    } else if (type == GRADIENTFAGE || type == GRADIENTAPPEAR) {
                        if (transp <= 0 || transp > 255) {
                            if (completion != null)
                                SystemClock.sleep(200);
                            completion.OnCompletion();
                            break;
                        }
                    }
                }
            }

            ;
        }.start();
    }

    /**
     * 数字增加动画
     *
     * @param maxSum
     */
    public void animationSum(int maxSum) {
        testsum = 0;
        shuzi = maxSum;
        if (maxSum == 0){
            han2.sendEmptyMessage(0);
            if (completion != null)
                completion.OnCompletion();
            return;
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    testsum += SPEED;
                    han2.sendEmptyMessage(0);
                    if (testsum > shuzi) {
                        if (completion != null)
                            completion.OnCompletion();
                        break;
                    }
                }
            }

            ;
        }.start();
    }

    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DONE:
                case CONTRACTION:
                    if (test <= 0) {
                        test = 0;
                    } else if (test > topcenint) {
                        test = topcenint;
                    }
                    headerLayoutParams.height = test;
                    view.setLayoutParams(headerLayoutParams);
                    break;
                case GRADIENTFAGE:
                case GRADIENTAPPEAR:
                    if (transp <= 0) {
                        transp = 0;
                    } else if (transp > 255) {
                        transp = 255;
                    }
                    view.getBackground().setAlpha(transp);
                    break;

                default:
                    break;
            }

        }

        ;
    };

    Handler han2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (testsum > shuzi) {
                testsum = shuzi;
            }
            TextView vi = (TextView) view;
            vi.setText(testsum + "");
        }

        ;
    };

    private void getMak() {
        if (topcenint != 0) {
            return;
        }
        topcenint = view.getHeight();
        width = view.getWidth();
    }

    /**
     * 获取控件当前状态
     *
     * @return
     */
    public int getState() {
        int i = 0;
        getMak();
        if (view.getVisibility() == View.GONE || view.getHeight() == 0) {
            i = CONTRACTION;
        } else {
            i = DONE;
        }
        return i;
    }

    public interface Completion {
        void OnCompletion();
    }

    Completion completion;

    public void setCompletion(Completion completion) {
        this.completion = completion;
    }

    /**
     * 设置速度
     */
    public void setSpeed(int speed) {
        SPEED = speed;
    }

    /**
     * 设置间隔
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

}
