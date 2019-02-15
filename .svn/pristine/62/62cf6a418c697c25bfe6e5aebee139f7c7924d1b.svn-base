package com.sy.mobile.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.g.MyBaseAdapter;
import com.example.tools.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 通讯录列表
 * Created by admin on 2018/7/27.
 */

public class MailListView extends RelativeLayout {
    protected Context context;
    protected ListView listView;
    protected MyBaseAdapter adapter;
    protected List<Map<String, Object>> contactList;
    protected Sidebar sidebar;

    public MailListView(Context context) {
        super(context);
        init(context, null);
    }

    public MailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public MailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.contact_list, this);
        listView = findViewById(R.id.list);
        sidebar = findViewById(R.id.sidebar);
    }

//    /*
//     * init view
//     */
//    public void setData(List<Map<String, Object>> contactList) {
//        this.contactList = contactList;
//        adapter.assLie(contactList);
//    }

    public void setAdapter(MyBaseAdapter myBaseAdapter) {
        adapter = myBaseAdapter;
        listView.setAdapter(adapter);
        sidebar.setListView(listView);
    }
}
