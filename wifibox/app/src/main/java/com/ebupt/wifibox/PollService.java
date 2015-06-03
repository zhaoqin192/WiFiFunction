package com.ebupt.wifibox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ebupt.wifibox.databases.BrokenData;
import com.ebupt.wifibox.databases.DownVisitorMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.ftp.FTPUtils;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    List<BrokenData> localList;
    long currentTimeMillis;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = (MyApp) getApplicationContext();
        list = new ArrayList<>();
        localList = new ArrayList<>();


        pollInterface();
        getList();

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
                        getList();
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("updateTime");
        IntentFilter beacon = new IntentFilter("beaconmaclist.log");
        IntentFilter assoc = new IntentFilter("assocmaclist.log");
        IntentFilter getfileList = new IntentFilter("getfileList");
        IntentFilter macdetect0 = new IntentFilter("macdetect0");
        IntentFilter macdetect1 = new IntentFilter("macdetect1");
        IntentFilter macdetect2 = new IntentFilter("macdetect2");
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
                Networks.pollGroup(PollService.this);
            }
        };
        timer.schedule(timerTask, 1000, Integer.parseInt(time) * 60 * 1000);
    }

    private void getList() {

        currentTimeMillis = System.currentTimeMillis();
        Log.e("CurrentTimeMils", String.valueOf(currentTimeMillis / 1000));

        List<DownVisitorMSG> downList = DataSupport.findAll(DownVisitorMSG.class);
        int size = downList.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                BrokenData brokenData = new BrokenData();
                brokenData.setMac(downList.get(i).getMac());
                brokenData.setTime(currentTimeMillis / 1000);
                localList.add(brokenData);
            }
        }

        String ServerPath = "/tmp/beaconmaclist.log";
        String localName = "beaconmaclist.log";
        downloadFile(ServerPath, localName);

        String ServerPath1 = "/tmp/assocmaclist.log";
        String localName1 = "assocmaclist.log";
        downloadFile(ServerPath1, localName1);

        FTPUtils.downloadFileFromFTPBySuffix(this, "/tmp/log", "macdetectlog");

    }

    private void downloadFile(String ServerPath, String localName) {
        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/" + localName;
        if (FTPUtils.isExists(dataPath)) {
            FTPUtils.deleteFiles(dataPath);
        }
        FTPUtils.downloadFileFromFTP(this, ServerPath, localName);
    }

    private void readFile(String fileName) {
        BufferedReader is;
        try {
            is = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/mnt/sdcard/" + this.getPackageName() + "/" + fileName))));
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
            Log.e("ListCount", list.size() + " before");
            int size = list.size();
            int outsize = localList.size();
            List<BrokenData> temp = new ArrayList<>();
            for (int i = 0; i < outsize; i++) {
                BrokenData outData = localList.get(i);
                for (int j = 0; j < size; j++) {
                    BrokenData data = list.get(j);
                    if (data.getMac().equals(outData.getMac())) {
                        if (data.getTime() >= (currentTimeMillis - Long.parseLong(time) * 60)) {
                            temp.add(data);
                        }
                    }
                }
            }
            list = temp;
            Log.e("ListCount", list.size() + " after");

            if (temp.size() < outsize) {
                Intent intent = new Intent("BrokenMessage");
                sendBroadcast(intent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decryptBASE64(String key) throws IOException {
        if (key == null || key.length() < 1) {
            return "";
        }
        return new String(new BASE64Decoder().decodeBuffer(key));
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
                    Log.e(TAG, myApp.fileList.get(i));
                    String ServerPath = "/tmp/log/" + myApp.fileList.get(i);
                    String localName = "macdetect" + i;
                    downloadFile(ServerPath, localName);
                }
            }
            if (intent.getAction().equals("macdetect0")) {
                Log.e("Count", "macdetect0");
                readFile("macdetect0");
            }
            if (intent.getAction().equals("macdetect1")) {
                Log.e("Count", "macdetect1");
                readFile("macdetect1");
            }
            if (intent.getAction().equals("macdetect2")) {
                Log.e("Count", "macdetect2");
                readFile("macdetect2");
            }
        }
    };

}
