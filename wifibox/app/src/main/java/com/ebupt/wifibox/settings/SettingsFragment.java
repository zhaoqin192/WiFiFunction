package com.ebupt.wifibox.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.LoginActivity;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DeviceMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.settings.wifi.WifiAdmin;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoqin on 4/15/15.
 */
public class SettingsFragment extends Fragment{
    private View contactslayout;
//    private Button passport;
    private WifiAdmin wifiAdmin;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private String wifi_name;
    private String wifi_mac;
    private Button logout;
    private Button link;
    private Handler handler;
    private DeviceMSG deviceMSG;
    private TextView login_text;
    private TextView wifi_text;
    private boolean flag;
    private ProgressDialog progressDialog;
    private Timer timer;
    private Dialog dialog;
    private TextView timetext;
    private Button timebutton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.settings_layout, container, false);
//        passport = (Button) contactslayout.findViewById(R.id.settings_passport);
//        passport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), OCRActivity.class);
//                startActivity(intent);
//            }
//        });
//        wifiAdmin = new WifiAdmin(getActivity());
        deviceMSG = DataSupport.findFirst(DeviceMSG.class);

        login_text = (TextView) contactslayout.findViewById(R.id.settings_login_text);

        timetext = (TextView) contactslayout.findViewById(R.id.settings_notice_time);
        timebutton = (Button) contactslayout.findViewById(R.id.settings_notice_button);
        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
                UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
            }
        });

        UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
        login_text.setText(userMSG.getPhone());
        wifi_text = (TextView) contactslayout.findViewById(R.id.settings_wifi_text);


        logout = (Button) contactslayout.findViewById(R.id.settings_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        link = (Button) contactslayout.findViewById(R.id.settings_link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceMSG.getLinkflag()) {
                    int wcgID = wifiInfo.getNetworkId();
                    wifiAdmin.disconnectWifi(wcgID);
                    flag = true;
//                    showDialog("正在断开连接...");
                } else {
//                    Log.e("zzzz", deviceMSG.getMacAddress());
//                    Log.e("zzzz", deviceMSG.getPasswd());
//                    wifiAdmin.openWifi();
//                    wifiAdmin.acquireWifiLock();
//                    wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(deviceMSG.getMacAddress(), deviceMSG.getPasswd(), 3));

//                    connection();
//                    showDialog("正在连接设备...");
                    Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                    startActivity(intent);
                    flag = true;
                }
            }
        });

        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiAdmin = new WifiAdmin(wifiManager);




        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        link.setBackgroundResource(R.drawable.btn_unlink_background);
//                        wifi_text.setText(wifi_name.replaceAll("\"", ""));
                        wifi_text.setText(wifi_name);
                        if (flag) {
//                            progressDialog.hide();
                            flag = false;
                        }
                        break;
                    case 1:
                        link.setBackgroundResource(R.drawable.btn_link_background);
                        wifi_text.setText("未连接指定设备");
                        if (flag) {
//                            progressDialog.hide();
                            flag = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        };


        return contactslayout;
    }

    private void showDialog(String str) {
        //创建ProgressDialog对象
        progressDialog = new ProgressDialog(getActivity());
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 标题
//        progressDialog.setTitle("提示");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage(str);
        // 设置ProgressDialog 标题图标
//        progressDialog.setIcon(R.drawable.a);
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(true);
        //设置ProgressDialog 的一个Button
//        progressDialog.setButton("确定", new SureButtonListener());
        // 让ProgressDialog显示
        progressDialog.show();
    }

    private void connection() {
        String networkSSID = "66:51:7e:38:e9:80";
        String networkPW = "1082325588";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\"" + networkPW + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int temp = wifiManager.addNetwork(conf);
        Log.e("addNetwork", temp + "");

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                Log.e("xxx", "connect");
                wifiManager.disconnect();
                wifiManager.enableNetwork(temp, true);
                Log.e("networkId", i.networkId + "");
//                wifiManager.saveConfiguration();
                wifiManager.reconnect();
                break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wifiInfo = wifiManager.getConnectionInfo();
                wifi_name = wifiInfo.getSSID();
                wifi_mac = wifiInfo.getBSSID();
                if (wifi_mac != null) {
                    Log.e("xxx", wifi_mac);
                    if (wifi_mac.equals(deviceMSG.getMacAddress())) {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                        deviceMSG.setLinkflag(true);
                        deviceMSG.saveThrows();
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        deviceMSG.setLinkflag(false);
                        deviceMSG.saveThrows();
                    }
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    deviceMSG.setLinkflag(false);
                    deviceMSG.saveThrows();
                }

            }
        }, 1000, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void showdialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.dialog_changetime_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final BootstrapEditText edit = (BootstrapEditText) layout.findViewById(R.id.changetime_edit);

        Button yes = (Button) layout.findViewById(R.id.changetime_group_ok);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit.getText().equals("")) {
                    UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
                    userMSG.setNoticetime(edit.getText().toString());
                    userMSG.saveThrows();
                    timetext.setText(userMSG.getNoticetime() + "分钟");
                    Intent intent = new Intent("updateTime");
                    getActivity().sendBroadcast(intent);
                }
                dialog.hide();
            }
        });

        Button no = (Button) layout.findViewById(R.id.changetime_group_cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

    }
}
