package com.ebupt.wifibox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebupt.wifibox.device.DeviceFragment;
import com.ebupt.wifibox.group.GroupFragment;
import com.ebupt.wifibox.settings.SettingsFragment;



public class MainActivity extends Activity implements View.OnClickListener{

    private GroupFragment groupFragment;
    private DeviceFragment deviceFragment;
    private SettingsFragment settingsFragment;

    private View grouplayout;
    private View devicelayout;
    private View settingslayout;

    private TextView grouptext;
    private TextView devicetext;
    private TextView settingstext;

    private FragmentManager fragmentManager;

    private TextView titletext;
    private ImageView titleright;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
        titletext = (TextView) findViewById(R.id.myTitle);

        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);


        IntentFilter login_success = new IntentFilter("login_success");
        registerReceiver(broadcastReceiver, login_success);
        IntentFilter login_error = new IntentFilter("login_error");
        registerReceiver(broadcastReceiver, login_error);
    }

    private void initViews() {
        grouplayout = findViewById(R.id.group_layout);
        devicelayout = findViewById(R.id.device_layout);
        settingslayout = findViewById(R.id.settings_layout);

        grouptext = (TextView) findViewById(R.id.group_text);
        devicetext = (TextView) findViewById(R.id.device_text);
        settingstext = (TextView) findViewById(R.id.settings_text);

        grouplayout.setOnClickListener(this);
        devicelayout.setOnClickListener(this);
        settingslayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_layout:
                setTabSelection(0);
                break;
            case R.id.device_layout:
                setTabSelection(1);
                break;
            case R.id.settings_layout:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    private void clearSelection() {
        grouptext.setTextColor(Color.parseColor("#000000"));
        devicetext.setTextColor(Color.parseColor("#000000"));
        settingstext.setTextColor(Color.parseColor("#000000"));
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                grouptext.setTextColor(Color.parseColor("#50B1DB"));
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                    transaction.add(R.id.content, groupFragment);
                } else {
                    transaction.show(groupFragment);
                }
                titletext.setText("团管理");
                titleright = (ImageView) findViewById(R.id.myDetail);
                titleright.setVisibility(View.VISIBLE);
                titleright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
                break;
            case 1:
                devicetext.setTextColor(Color.parseColor("#50B1DB"));
                if (deviceFragment == null) {
                    deviceFragment = new DeviceFragment();
                    transaction.add(R.id.content, deviceFragment);
                } else {
                    transaction.show(deviceFragment);
                }
                titleright.setVisibility(View.GONE);
                titletext.setText("设备");
                break;
            case 2:
            default:
                settingstext.setTextColor(Color.parseColor("#50B1DB"));
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    transaction.add(R.id.content, settingsFragment);
                } else {
                    transaction.show(settingsFragment);
                }
                titleright.setVisibility(View.GONE);
                titletext.setText("设置");
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (groupFragment != null) {
            transaction.hide(groupFragment);
        }
        if (deviceFragment != null) {
            transaction.hide(deviceFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }
    }


    private void showDialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(MainActivity.this);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.add_group_layout, null);
        dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("login_success")) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("login_error")) {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
