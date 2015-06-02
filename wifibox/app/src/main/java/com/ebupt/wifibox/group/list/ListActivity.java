package com.ebupt.wifibox.group.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.UnVisitorsMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.ftp.FTPUtils;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class ListActivity extends Activity{
    private Intent intent;
    private UploadFragment uploadFragment;
    private SignFragment signFragment;
    private FragmentManager fragmentManager;
    private ImageView tabupload;
    private ImageView tabsign;
    private ImageView download;
    private ImageView groupadd;
    private ImageView addVisitor;
    private Dialog dialog;

    @InjectView(R.id.group_list_upload) ImageView uploadPassport;
    @OnClick(R.id.group_list_upload)
    void uploadpassport() {
        if (downList.getVisibility() == View.VISIBLE) {
            UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
            Networks.userInfos(this, userMSG.getPhone(), "mac", intent.getStringExtra("groupid"));
        } else {
            Networks.uploadPassports(this, intent.getStringExtra("groupid"));
        }
    }

    @InjectView(R.id.group_list_down) ImageView downList;
    @OnClick(R.id.group_list_down)
    void downList() {
        String ServerPath = getResources().getString(R.string.db_path);
        String fileName = "qiandao.db";
        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/qiandao.db";
        if (FTPUtils.isExists(dataPath)) {
            FTPUtils.deleteFiles(dataPath);
        }
        FTPUtils.downloadFileFromFTP(this, ServerPath, fileName);
        Toast.makeText(this, "签到列表已下载", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.list_activity_layout);
        ButterKnife.inject(this);
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

        fragmentManager = getFragmentManager();


        addVisitor = (ImageView) findViewById(R.id.group_list_add);
        addVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        setTabSelection(0);
    }

    private void initViews() {
        tabupload = (ImageView) findViewById(R.id.group_list_upload_button);
        tabupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);
            }
        });
        tabsign = (ImageView) findViewById(R.id.group_list_sign_button);
        tabsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
            }
        });

        download = (ImageView) findViewById(R.id.group_list_down);
        groupadd = (ImageView) findViewById(R.id.group_list_add);
    }

    private void showDialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(ListActivity.this);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.add_visitor_layout, null);
        dialog = new AlertDialog.Builder(ListActivity.this).create();
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
                    Toast.makeText(ListActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passport.getText().toString().equals("")) {
                    Toast.makeText(ListActivity.this, "请输入护照号", Toast.LENGTH_SHORT).show();
                    return;
                }

                UnVisitorsMSG visitorsMSG = new UnVisitorsMSG();
                visitorsMSG.setGroupid(intent.getStringExtra("groupid"));
                visitorsMSG.setName(name.getText().toString());
                visitorsMSG.setPassports(passport.getText().toString());
                visitorsMSG.setBrokerage("0");
                visitorsMSG.saveThrows();
                Intent add = new Intent("addVisitor");
                sendBroadcast(add);
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
                Toast.makeText(ListActivity.this, "当前不支持护照扫描", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);

        switch (index) {
            case 0:
                tabupload.setImageResource(R.drawable.tab_manager_hot);
                if (uploadFragment == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("groupid", intent.getStringExtra("groupid"));
                    uploadFragment = new UploadFragment();
                    uploadFragment.setArguments(bundle);
                    transaction.add(R.id.group_list_fragment, uploadFragment);
                } else {
                    transaction.show(uploadFragment);
                }
                break;
            case 1:
            default:
                tabsign.setImageResource(R.drawable.tab_sign_hot);
                groupadd.setVisibility(View.GONE);
                download.setVisibility(View.VISIBLE);
                if (signFragment == null) {
                    signFragment = new SignFragment();
                    transaction.add(R.id.group_list_fragment, signFragment);
                } else {
                    transaction.show(signFragment);
                }
                break;

        }
        transaction.commit();

    }

    private void clearSelection() {
        tabupload.setImageResource(R.drawable.tab_manager);
        tabsign.setImageResource(R.drawable.tab_sign);
        download.setVisibility(View.GONE);
        groupadd.setVisibility(View.VISIBLE);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (uploadFragment != null) {
            transaction.hide(uploadFragment);
        }
        if (signFragment != null) {
            transaction.hide(signFragment);
        }
    }

}
