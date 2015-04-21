package com.ebupt.wifibox.group.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupList extends Activity{
    private ExpandableListView listView;
    private List<VisitorsMSG> datalist;
    private ListAdapter adapter;
    private ImageView addVisitor;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_layout);

        listView = (ExpandableListView) findViewById(R.id.group_list_expand);
        listView.setGroupIndicator(null);
        datalist = new ArrayList<>();

        VisitorsMSG visitorsMSG = null;
        for (int i = 0; i < 4; i++) {
            visitorsMSG = new VisitorsMSG();
            datalist.add(visitorsMSG);
        }

        adapter = new ListAdapter(this, datalist);
        listView.setAdapter(adapter);

        addVisitor = (ImageView) findViewById(R.id.group_list_add);
        addVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(GroupList.this);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.add_visitor_layout, null);
        dialog = new AlertDialog.Builder(GroupList.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);



    }
}
