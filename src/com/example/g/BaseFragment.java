package com.example.g;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.sy.mobile.control.MyDialog;
import com.sy.mobile.net.HttpDream;
import com.wxample.data.MyData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class BaseFragment extends Fragment {
    protected HttpDream http;
    private String url;
    protected View mView;
    /**
     * 表示View是否被初始化
     */
    protected boolean isViewInitiated;
    /**
     * 表示对用户是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 表示数据是否初始化
     */
    protected boolean isDataInitiated;
    protected Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mView == null) {
            mContext = getContext();
            mView = View.inflate(mContext, getLayoutId(), null);
            initView(mView);
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    public void setUrl(String url1) {
        url = url1;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    protected abstract void initView(View view);

    protected abstract int getLayoutId();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        baseInit();
        initListener();
        initData();
//        prepareFetchData();
    }

    protected void baseInit() {
    }

    ;

    private void initListener() {
        //判断显示哪个
        http = new HttpDream(url, getActivity());
        http.setOnTheReturnValue(new HttpDream.Cont() {
            @Override
            public void content(Object content, int no) {
                // TODO Auto-generated method stub
                if (content != null) {
                    Map<String, Object> conMap = (Map<String, Object>) content;
                    if (MyData.mToString(conMap.get("statusCode")).equals("0")) {
                        getDataCallback(conMap, no);
                    } else {
                        MyDialog.showTextToast(MyData.mToString(conMap.get("errorMsg")), getActivity());
                        getDataCallbackNot(MyData.mToInt(conMap.get("statusCode")), no);
                    }
                } else {
                    MyDialog.showTextToast("获取信息失败", getActivity());
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
        if (isVisibleToUser && isViewInitiated) {
            soVisual();
        } else if (!isVisibleToUser && isViewInitiated) {
            notVisual();
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    /**
     * 每次可见的时候
     */
    public void soVisual() {

    }

    /**
     * 每次不可见的时候
     */
    public void notVisual() {

    }

    /**
     * 界面第一次可见时
     */
    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /***
     *
     * @param forceUpdate 表示是否在界面可见的时候是否强制刷新数据
     * @return
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            //  界面可见的时候再去加载数据
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract void initData();

    /**
     * 申请权限
     */
    protected void requestRuntimePermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            // 遍历每一个申请的权限，把没有通过的权限放在集合中
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            // 申请权限
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            } else {
                //表示全部都有权限
                granted();
            }
        } else {
            granted();
        }
    }

    /**
     * 申请后的处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            List<String> deniedList = new ArrayList<>();
            // 遍历所有申请的权限，把被拒绝的权限放入集合
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {

                } else {
                    deniedList.add(permissions[i]);
                }
            }
            if (!deniedList.isEmpty()) {
                denied(deniedList);
            } else {
                granted();
            }
        }
    }

    public void granted() {
    }

    public void denied(List<String> deniedList) {
    }

    /**
     * http成功回调
     *
     * @param contMap
     */
    public void getDataCallback(Map<String, Object> contMap, int no) {

    }

    /**
     * http失败
     */
    public void getDataCallbackNot(int state, int no) {
    }

}