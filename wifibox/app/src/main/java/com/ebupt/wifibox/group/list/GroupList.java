package com.ebupt.wifibox.group.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;
import com.ebupt.wifibox.networks.Networks;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.acl.Group;
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
    private BootstrapButton upload;
    private BootstrapButton sign;
    private MyApp myApp;
    private StringBuffer str1;
    private StringBuffer str2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_layout);

        initViews();

        datalist = new ArrayList<>();

        VisitorsMSG visitorsMSG = null;

        str1 = new StringBuffer("[");
        for (int i = 0; i < 4; i++) {
            visitorsMSG = new VisitorsMSG();
            datalist.add(visitorsMSG);
            str1.append("{\"name\":");
            str1.append("\"张三\",");
            str1.append("\"passport\":");
            if (i != 3) {
                str1.append("\"123456\"},");
            } else {
                str1.append("\"123456\"}");
            }
        }
        str1.append("]");
        Log.e("xxx", str1.toString());


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

    private void initViews() {
        myApp = (MyApp) getApplicationContext();
        listView = (ExpandableListView) findViewById(R.id.group_list_expand);
        listView.setGroupIndicator(null);

        upload = (BootstrapButton) findViewById(R.id.group_list_upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Networks.passports(GroupList.this, myApp.phone, "mac", "tourid", "gs", str1.toString());
            }
        });

        sign = (BootstrapButton) findViewById(R.id.group_list_sign_button);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str2 = new StringBuffer("[");
                for (int i = 0; i < 4; i++) {
                    str2.append("{\"names\":");
                    str2.append("\"张三\",");
                    str2.append("\"phone\":");
                    str2.append("\"123456\",");
                    str2.append("\"mac\":");

                    if (i != 3) {
                        str2.append("\"123456\"},");
                    } else {
                        str2.append("\"123456\"}");
                    }
                }
                str2.append("]");

                Log.e("yyy", str2.toString());
                Networks.userInfos(GroupList.this, myApp.phone, "mac", "tourid", "gs", str2.toString());
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

        BootstrapEditText name = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_name);
        BootstrapEditText passport = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_passport);

        BootstrapButton ok = (BootstrapButton) layout.findViewById(R.id.add_visitor_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisitorsMSG visitorsMSG = new VisitorsMSG();
                datalist.add(visitorsMSG);
                adapter.notifyDataSetChanged();
                Toast.makeText(GroupList.this, "添加成功", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });

        BootstrapButton cancel = (BootstrapButton) layout.findViewById(R.id.add_visitor_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }
}
