package com.ebupt.wifibox.group.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

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
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.group_list_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
        TextView titletext = (TextView) findViewById(R.id.myTitle);
        intent = getIntent();
        titletext.setText(intent.getStringExtra("name"));
        TextView backtext = (TextView) findViewById(R.id.myBack_text);
        backtext.setVisibility(View.VISIBLE);
        backtext.setText("团管理");
        backtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView back = (ImageView) findViewById(R.id.myBack);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViews();

        datalist = new ArrayList<>();

        datalist = DataSupport.findAll(VisitorsMSG.class);
        if (datalist.size() != 0) {
            for (VisitorsMSG visitorsMSG : datalist) {
                datalist.add(0, visitorsMSG);
            }
        }
//        VisitorsMSG visitorsMSG = null;
//        str1 = new StringBuffer("[");
//        for (int i = 0; i < 4; i++) {
//            visitorsMSG = new VisitorsMSG();
//            datalist.add(visitorsMSG);
//            str1.append("{\"name\":");
//            str1.append("\"张三\",");
//            str1.append("\"passport\":");
//            if (i != 3) {
//                str1.append("\"123456\"},");
//            } else {
//                str1.append("\"123456\"}");
//            }
//        }
//        str1.append("]");
//        Log.e("xxx", str1.toString());


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

        final BootstrapEditText name = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_name);
        final BootstrapEditText passport = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_passport);

        Button ok = (Button) layout.findViewById(R.id.add_visitor_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(GroupList.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passport.getText().toString().equals("")) {
                    Toast.makeText(GroupList.this, "请输入护照号", Toast.LENGTH_SHORT).show();
                    return;
                }

                VisitorsMSG visitorsMSG = new VisitorsMSG();
                visitorsMSG.setGroupid(intent.getStringExtra("groupid"));
                visitorsMSG.setName(name.getText().toString());
                visitorsMSG.setPassports(passport.getText().toString());
                visitorsMSG.saveThrows();
                datalist.add(0, visitorsMSG);
                adapter.notifyDataSetChanged();
                Toast.makeText(GroupList.this, "添加成功", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });

        Button cancel = (Button) layout.findViewById(R.id.add_visitor_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        ImageView passportButton = (ImageView) layout.findViewById(R.id.add_visitor_passport_button);
        passportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupList.this, "当前不支持护照扫描", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
