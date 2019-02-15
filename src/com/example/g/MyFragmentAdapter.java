package com.example.g;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * fragment
 * Created by admin on 2018/3/7.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> pagerItemList;
    public MyFragmentAdapter(FragmentManager fm, List<Fragment> pagerItemList) {
        super(fm);
        this.pagerItemList = pagerItemList;
    }

    @Override
    public int getCount() {
        return pagerItemList.size();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < pagerItemList.size())
            fragment = pagerItemList.get(position);
        else
            fragment = pagerItemList.get(0);

        return fragment;

    }
}
