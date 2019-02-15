package com.example.g;

import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.sy.mobile.control.MyDialog;
import com.wxample.data.MyData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 自定义适配器
 *
 * @param <E>
 */
public abstract class MyBaseAdapter<E> extends BaseAdapter {
    public LayoutInflater mInf;
    public List<E> list;
    public List<E> testList;
    public Activity context;
    private boolean isDeepCopy;
    private ListView baseList;
    public boolean isTos = true;
    ListView cainilv;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCainilv(ListView cainilv) {
        this.cainilv = cainilv;
    }

    /**
     * 是否需要深度复制(用于搜索)
     *
     * @param isDeepCopy
     */
    public void setIsDeepCopy(boolean isDeepCopy) {
        this.isDeepCopy = isDeepCopy;
    }

    /**
     * 添加列
     *
     * @param
     */
    public void addLie(List<E> list) {
        if (list == null || list.size() == 0) {
            MyDialog.showTextToast("暂无更多数据", context);
        } else {
            //是否需要深度复制(用于搜索)
            if (isDeepCopy) {
                try {
                    this.testList.addAll(deepCopy(list));
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * list赋值
     */
    public void assLie(List<E> list) {
        if ((list == null || list.size() == 0) && isTos)
            MyDialog.showTextToast("暂无数据", context);

        if (list != null)
            this.list = list;
        if (list != null && isDeepCopy) {
            try {
                this.testList = deepCopy(list);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assLieComplete();
        notifyDataSetChanged();
    }

    /**
     * list加载临时数据
     */
    public void assTemLie(List<E> list) {
        if ((list == null || list.size() == 0) && isTos)
            MyDialog.showTextToast("暂无数据", context);

        if (list != null)
            this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 初始化数据(需要深度复制为true)
     */
    public void initList() {
        if (testList != null && testList.size() > 0 & isDeepCopy) {
            try {
                this.list = deepCopy(testList);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 是否显示暂无
     *
     * @param isTos
     */
    public void setIsTos(boolean isTos) {
        this.isTos = isTos;
    }

    /**
     * 返回list
     *
     * @return
     */
    public List<E> getList() {
        return list;
    }

    /**
     * 删数据
     */
    public void revem() {
        list.clear();
        //是否需要深度复制(用于搜索)
        if (isDeepCopy) {
            this.testList.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param map
     * @param index
     */
    public void addMap(E map, int index) {
        this.list.add(index, map);
        if (isDeepCopy) {
            this.testList.add(map);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param map
     */
    public void addMap(E map) {
        this.list.add(map);
        if (isDeepCopy) {
            this.testList.add(map);
        }
        notifyDataSetChanged();
    }

    /**
     * 修改
     *
     * @param map
     * @param index
     */
    public void modifyMap(E map, int index) {
        this.list.add(index, map);
        this.list.remove(index + 1);
        //是否需要深度复制(用于搜索)
        if (isDeepCopy) {
            this.testList.add(index, map);
            this.testList.remove(index + 1);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除列
     */
    public void revem(int index) {
        list.remove(index);
        if (isDeepCopy) {
            this.testList.remove(index);
        }
        notifyDataSetChanged();
    }

    /**
     * 搜索(需要开启深度复制)
     *
     * @param ser
     */
    public void searchrList(String ser, String key, int type) {
        //需要保存原始数据的testList有数据才执行
        if ((list == null || testList == null || testList.size() == 0) && !isDeepCopy)
            return;
        list.clear();
        for (int i = 0; i < testList.size(); i++) {
            if (type == MyAdapterType.mapSS) {
                Map<String, String> testmap = (Map<String, String>) testList.get(i);
                if (testmap.size() == 0)
                    list.add(testList.get(i));
                else if (testmap.get(key).contains(ser)) {
                    list.add(testList.get(i));
                }
            } else if (type == MyAdapterType.mapSO) {
                Map<String, Object> testmap = (Map<String, Object>) testList.get(i);
                if (testmap.size() == 0)
                    list.add(testList.get(i));
                else if (testmap.get(key) != null && MyData.mToString(testmap.get(key)).contains(ser)) {
                    list.add(testList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 序列化深复制
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * 设置listview
     */
    public void setListView(ListView listView) {
        this.baseList = listView;
    }

    /**
     * 显示最新一列
     *
     * @param po
     */
    public void showSelection(final int po) {
        if (baseList == null)
            return;
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        baseList.requestFocusFromTouch();
//                        baseList.smoothScrollToPosition(baseList.getHeaderViewsCount() + po);
                        baseList.setSelection(baseList.getHeaderViewsCount() + po);
                    }
                });
            }
        }.start();
    }

    public void showTop(final int po) {
        if (cainilv == null)
            return;
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cainilv.requestFocusFromTouch();
                        cainilv.smoothScrollToPosition(cainilv.getHeaderViewsCount() + po);
                        cainilv.setSelection(cainilv.getHeaderViewsCount() + po);
//                        cainilv.setSelection(po);
                    }
                });
            }
        }.start();
    }


    /**
     * 加载完成
     */
    protected void assLieComplete() {

    }
}
