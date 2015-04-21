package com.ebupt.wifibox.group.list;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;
import com.ebupt.wifibox.group.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupList extends Activity{
    private ExpandableListView listView;
    private List<VisitorsMSG> datalist;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_layout);

        listView = (ExpandableListView) findViewById(R.id.group_list_expand);
        datalist = new ArrayList<>();

        VisitorsMSG visitorsMSG = null;
        for (int i = 0; i < 4; i++) {
            visitorsMSG = new VisitorsMSG();
            datalist.add(visitorsMSG);
        }

        adapter = new ListAdapter(this, datalist);
        listView.setAdapter(adapter);
    }
}
