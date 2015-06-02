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

import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.ftp.FTPUtils;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = (MyApp) getApplicationContext();

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
        IntentFilter macdetect1 = new IntentFilter("macdetect1");
        IntentFilter macdetect2 = new IntentFilter("macdetect2");
        IntentFilter macdetect3 = new IntentFilter("macdetect3");
        registerReceiver(broadcastReceiver, macdetect3);
        registerReceiver(broadcastReceiver, macdetect2);
        registerReceiver(broadcastReceiver, macdetect1);
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
        final boolean[] isExist = new boolean[1];
        try {
            is = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/mnt/sdcard/" + this.getPackageName() + "/" + fileName))));
            while (is.readLine() != null) {
                isExist[0] = false;
                String s = decryptBASE64(is.readLine());
                Pattern p = Pattern.compile("\\#");
                if (!s.equals("") && s != null) {
                    String[] str = p.split(s);
                    for (int i = 0; i < str.length; i++) {
                        Log.e("Encry", str[i]);
                    }
                }
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
                readFile("beaconmaclist.log");
            }
            if (intent.getAction().equals("assocmaclist.log")) {
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
            if (intent.getAction().equals("macdetect1")) {
                readFile("macdetect1");
            }
            if (intent.getAction().equals("macdetect2")) {
                readFile("macdetect2");
            }
            if (intent.getAction().equals("macdetect3")) {
                readFile("macdetect3");
            }
        }
    };

}
