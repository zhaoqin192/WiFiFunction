package com.muggins.ebupt.wififunction.wifiscan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.muggins.ebupt.wififunction.MyApp;
import com.muggins.ebupt.wififunction.R;

/**
 * Created by qin on 2014/12/26.
 */
public class WifiScanActivity extends Activity{
    private WifiInfo wifiInfo;
    private WifiManager wifiManager;
    private Handler handler;
    private List<ScanResult> list;
    private ListView listView;
    private WifiAdmin wifiAdmin;
    private MyAdapter myAdapter;
    private String wifi_name;
    private String bssid;
    private String password;
    private int wifi_level;
    private ProgressBar progressBar;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("X", "wifiscanactivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifiscan);

        listView = (ListView) findViewById(R.id.wifiscan_listview1);
        progressBar = (ProgressBar) findViewById(R.id.wifiscan_progressbar);
        wifiAdmin = new WifiAdmin(this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        init();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //WifiInfo对象包含了当前连接中的相关信息
                wifiInfo = wifiManager.getConnectionInfo();
                Message msg = new Message();
                wifi_name = wifiInfo.getSSID();
                wifi_level = wifiInfo.getRssi();
                bssid = wifiInfo.getBSSID();
                MyApp myApp = (MyApp) getApplicationContext();
                myApp.setKey(bssid);
                if(wifi_level <= 0 && wifi_level >= -50){
                    msg.what = 1;
                    handler.sendMessage(msg);
                }else if(wifi_level < -50 && wifi_level >= -70){
                    msg.what = 2;
                    handler.sendMessage(msg);
                }else if(wifi_level < -70 && wifi_level >= -80){
                    msg.what = 3;
                    handler.sendMessage(msg);
                }else if(wifi_level < -80 && wifi_level >= -100){
                    msg.what = 4;
                    handler.sendMessage(msg);
                }else{
                    msg.what = 5;
                    handler.sendMessage(msg);
                }
            }
        }, 1000 , 10000);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what < 5){
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycle();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                    recycle();
                }else{
                    Toast.makeText(WifiScanActivity.this, "当前没有连接wifi", Toast.LENGTH_SHORT).show();
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult temp = list.get(position);

                LayoutInflater inflater = LayoutInflater.from(WifiScanActivity.this);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(WifiScanActivity.this);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        });
    }

    private void openWifi(){
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    private void init(){
        openWifi();

        wifiInfo = wifiManager.getConnectionInfo();
        list = wifiManager.getScanResults();
        if(list == null){
            Toast.makeText(this, "wifi未打开", Toast.LENGTH_LONG).show();
        }else{
            myAdapter = new MyAdapter(this, list, wifiInfo.getBSSID());
            listView.setAdapter(myAdapter);
        }
    }

    private void recycle(){
        wifiManager.startScan();
        list = wifiManager.getScanResults();
        if(list == null){
            Toast.makeText(this, "wifi未打开", Toast.LENGTH_LONG).show();
        }else{
            myAdapter.refresh(list, bssid);
        }
    }

    public void linkedwifi(View view){
        password = ((EditText) dialog.findViewById(R.id.dialog_pw_edit)).getText().toString();
        wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(wifi_name, password, 3));
        dialog.dismiss();
    }
    public void cancel(View view){
        dialog.dismiss();
    }
    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<ScanResult> list;
        String bssid;
        public MyAdapter(Context context, List<ScanResult> list, String name){
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.bssid = name;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            view = inflater.inflate(R.layout.wifiscan_list, null);
            ScanResult scanResult = list.get(position);
            TextView name = (TextView) view.findViewById(R.id.item_name);
            ImageView state = (ImageView) view.findViewById(R.id.item_state);
            ImageView img = (ImageView) view.findViewById(R.id.item_img);
            name.setText(scanResult.SSID);
            if(bssid.endsWith(scanResult.BSSID)){
                state.setVisibility(View.VISIBLE);
            }else{
                state.setVisibility(View.INVISIBLE);
            }
            if(Math.abs(scanResult.level) > 100){
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_0));
            }else if(Math.abs(scanResult.level) > 80){
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_1));
            }else if(Math.abs(scanResult.level) > 70){
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_1));
            }else if(Math.abs(scanResult.level) > 60){
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_2));
            }else if(Math.abs(scanResult.level) > 50){
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_3));
            }else{
                img.setImageDrawable(getResources().getDrawable(R.drawable.stat_sys_wifi_signal_4));
            }
            return view;
        }
        public void refresh(List<ScanResult> templist, String tempname){
            list = templist;
            bssid = tempname;
            notifyDataSetChanged();
        }
    }

}
