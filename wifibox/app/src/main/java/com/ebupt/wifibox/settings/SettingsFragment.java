package com.ebupt.wifibox.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.LoginActivity;
import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DeviceMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.settings.wifi.WifiAdmin;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoqin on 4/15/15.
 */
public class SettingsFragment extends Fragment{
    private View contactslayout;
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
    private Timer timer;
    private Dialog dialog;
    private TextView timetext;
    private Button timebutton;
    private MyApp myApp;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.settings_layout, container, false);
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
                UserMSG user = DataSupport.findFirst(UserMSG.class);
                user.setAuto(false);
                user.saveThrows();
                startActivity(intent);
                getActivity().finish();
            }
        });

        link = (Button) contactslayout.findViewById(R.id.settings_link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myApp.wifiConnectFlag) {
                    disconnect();
                } else {
                    scanWIFI();
                }
            }
        });

        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        myApp = (MyApp) getActivity().getApplicationContext();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        link.setBackgroundResource(R.drawable.btn_unlink_background);
                        wifi_text.setText(wifi_name.replace("\"", ""));
                        if (flag) {
                            flag = false;
                        }
                        break;
                    case 1:
                        link.setBackgroundResource(R.drawable.btn_link_background);
                        wifi_text.setText("未连接指定设备");
                        if (flag) {
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


    private void connection(String networkSSID) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
//        conf.preSharedKey = "\"" + networkPW + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int temp = wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }

    private void disconnect() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int networkid = wifiInfo.getNetworkId();
        wifiManager.disableNetwork(networkid);
        wifiManager.disconnect();
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
                        myApp.wifiConnectFlag = true;
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        myApp.wifiConnectFlag = false;
                    }
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    myApp.wifiConnectFlag = false;
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

    private void scanWIFI() {
        List<ScanResult> results = wifiManager.getScanResults();
        List<DeviceMSG> list = DataSupport.findAll(DeviceMSG.class);
        if (list.size() != 0) {
            DeviceMSG deviceMSG = list.get(0);
            Log.e("ScanResult", deviceMSG.getMacAddress());
            for (ScanResult scanResult : results) {
                if (deviceMSG.getMacAddress().equals(scanResult.BSSID)) {
                    connection(scanResult.SSID);
                    break;
                }
            }
        } else {
            Toast.makeText(getActivity(), "没有获取到设备信息", Toast.LENGTH_SHORT).show();
        }
    }
}
