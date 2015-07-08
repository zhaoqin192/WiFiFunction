package com.ebupt.wifibox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ebupt.wifibox.databases.BrokenData;
import com.ebupt.wifibox.databases.DeviceMSG;
import com.ebupt.wifibox.databases.DownVisitorMSG;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.MessageTable;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.ftp.FTPUtils;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import Decoder.BASE64Decoder;

/**
 * Created by zhaoqin on 5/25/15.
 */
public class PollService extends Service{
    private final String TAG = "PollService";
    String time = "30";
    Timer timer;
    Handler handler;
    MyApp myApp;
    List<BrokenData> list;
    List<DownVisitorMSG> downList;
    long currentTimeMillis;
    private WifiInfo wifiInfo;
    private WifiManager wifiManager;
    private String wifi_name;
    private String wifi_mac;
    private DeviceMSG deviceMSG;
    private List<String> fileList = new ArrayList<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = (MyApp) getApplicationContext();

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<DeviceMSG> listdevice = DataSupport.findAll(DeviceMSG.class);
        if (listdevice.size() != 0) {
            deviceMSG = listdevice.get(0);
        }
        pollInterface();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        timer.cancel();
                        UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
                        time = userMSG.getNoticetime();
                        pollInterface();
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("updateTime");
        IntentFilter beacon = new IntentFilter("beaconmaclist.log");
        IntentFilter assoc = new IntentFilter("assocmaclist.log");
        IntentFilter getfileList = new IntentFilter("getfileList");
        IntentFilter macdetect0 = new IntentFilter("macdetect0.log");
        IntentFilter macdetect1 = new IntentFilter("macdetect1.log");
        IntentFilter macdetect2 = new IntentFilter("macdetect2.log");
        IntentFilter check = new IntentFilter("check");
        registerReceiver(broadcastReceiver, check);
        registerReceiver(broadcastReceiver, macdetect2);
        registerReceiver(broadcastReceiver, macdetect1);
        registerReceiver(broadcastReceiver, macdetect0);
        registerReceiver(broadcastReceiver, getfileList);
        registerReceiver(broadcastReceiver, assoc);
        registerReceiver(broadcastReceiver, beacon);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private void pollInterface() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Networks.getTours(PollService.this);
                getList();
            }
        };
        timer.schedule(timerTask, Integer.parseInt(time) * 60 * 1000 / 3, Integer.parseInt(time) * 60 * 1000 / 3);
    }

    private void getList() {
        wifiInfo = wifiManager.getConnectionInfo();
        wifi_name = wifiInfo.getSSID();
        wifi_mac = wifiInfo.getBSSID();
        if (wifi_mac != null) {
            Log.e("xxx", wifi_mac);
            if (wifi_mac.equals(deviceMSG.getMacAddress())) {
                myApp.wifiConnectFlag = true;
            } else {
                myApp.wifiConnectFlag = false;
            }
        } else {
            myApp.wifiConnectFlag = false;
        }

//        Log.e(TAG, "getList");
        if (myApp.wifiConnectFlag) {
            list = new ArrayList<>();
            downList = new ArrayList<>();

            currentTimeMillis = System.currentTimeMillis() / 1000;
            Log.e("CurrentTimeMils", String.valueOf(currentTimeMillis / 1000));

            downList = DataSupport.findAll(DownVisitorMSG.class);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String ServerPath = "/tmp/beaconmaclist.log";
                    String localName = "beaconmaclist.log";
                    downloadFile(ServerPath, localName);
                    String ServerPath1 = "/tmp/assocmaclist.log";
                    String localName1 = "assocmaclist.log";
                    downloadFile(ServerPath1, localName1);
                    FTPUtils.downloadFileFromFTPBySuffix(PollService.this, "/tmp/log", "macdetectlog");
                }
            }).start();
        }
    }

    private void downloadFile(String ServerPath, String localName) {
        Log.e(TAG, "add " + localName);
        fileList.add(localName);
        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/" + localName;
        if (FTPUtils.isExists(dataPath)) {
            FTPUtils.deleteFiles(dataPath);
        }
        FTPUtils.downloadFileFromFTP(this, ServerPath, localName);
    }

    private void readFile(final String fileName) {
        Log.e(TAG, "read " + fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader is;
                try {
                    is = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/mnt/sdcard/" + PollService.this.getPackageName() + "/" + fileName))));
                    while (is.readLine() != null) {
                        String s = decryptBASE64(is.readLine());
                        Pattern p = Pattern.compile("\\#");
                        if (!s.equals("") && s != null) {
                            String[] str = p.split(s);
                            if (fileName.equals("assocmaclist.log")) {
                                BrokenData brokenData = new BrokenData();
                                brokenData.setMac(str[0]);
                                brokenData.setMac(str[1]);
                                list.add(brokenData);
                            } else {
                                BrokenData brokenData = new BrokenData();
                                brokenData.setMac(str[0]);
                                brokenData.setTime(Long.parseLong(str[2]));
                                list.add(brokenData);
                            }
                        }
                    }
                    Log.e(TAG, "remove " + fileName);
                    fileList.remove(fileName);
                    if (fileList.size() == 0) {
                        analysize();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String decryptBASE64(String key) throws IOException {
        if (key == null || key.length() < 1) {
            return "";
        }
        return new String(new BASE64Decoder().decodeBuffer(key));
    }

    private void analysize() {
        Log.e(TAG, "list " + list.size());
        Log.e(TAG, "downlist " + downList.size());
        List<BrokenData> temp = new ArrayList<>();
        //get mac list for exist in t minutes
        for (BrokenData brokenData : list) {
            Log.e("filter2", (currentTimeMillis + " currentTimeMills"));
            Log.e("filter2", brokenData.getTime() + " getTime");
            if (currentTimeMillis - brokenData.getTime() < Long.parseLong(time) * 60 * 1000) {
                temp.add(brokenData);
            }
        }

        //update DownVisitorMSG status
        int count = 0;
        for (DownVisitorMSG downVisitorMSG : downList) {
            downVisitorMSG.setStatus("offline");
            downVisitorMSG.saveThrows();
            for (BrokenData brokenData : temp) {
                if (downVisitorMSG.getMac().equals(brokenData.getMac())) {
                    downVisitorMSG.setStatus("online");
                    downVisitorMSG.saveThrows();
                    count++;
                    break;
                }
            }
        }

        if (count < downList.size()) {
            int diff = downList.size() - count;
            MessageTable messageTable = new MessageTable();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            messageTable.setTime(simpleDateFormat.format(new Date()));
            messageTable.setStatus(true);
            StringBuffer str = new StringBuffer("已发现");
            str.append(diff);
            str.append("个设备连续");
            str.append(time);
            str.append("分钟不在wifi范围内，该设备可能关闭wifi功能或关机");
            messageTable.setContent(str.toString());
            messageTable.setType("alarm");
            List<GroupMSG> groupMSGs = DataSupport.findAll(GroupMSG.class);
            int groupSize = groupMSGs.size();
            for (int i = 0; i < groupSize; i++) {
                if (!groupMSGs.get(i).getInvalid()) {
                    messageTable.setGroupid(groupMSGs.get(i).getGroup_id());
                    break;
                }
            }
            messageTable.saveThrows();
            Intent intent = new Intent("updateBadge");
            sendBroadcast(intent);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updateTime")) {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
            if (intent.getAction().equals("beaconmaclist.log")) {
                Log.e("Count", "beaconmaclist");
                readFile("beaconmaclist.log");
            }
            if (intent.getAction().equals("assocmaclist.log")) {
                Log.e("Count", "assocmaclist");
                readFile("assocmaclist.log");
            }
            if (intent.getAction().equals("getfileList")) {
                int size = myApp.fileList.size();
                for (int i = 0; i < size; i++) {
                    String ServerPath = "/tmp/log/" + myApp.fileList.get(i);
                    String localName = "macdetect" + i + ".log";
                    downloadFile(ServerPath, localName);
                }
            }
            if (intent.getAction().equals("macdetect0.log")) {
                Log.e("Count", "macdetect0");
                readFile("macdetect0.log");
            }
            if (intent.getAction().equals("macdetect1.log")) {
                Log.e("Count", "macdetect1");
                readFile("macdetect1.log");
            }
            if (intent.getAction().equals("macdetect2.log")) {
                Log.e("Count", "macdetect2");
                readFile("macdetect2.log");
            }
            if (intent.getAction().equals("check")) {
                getList();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
