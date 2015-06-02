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

import org.apache.commons.net.util.Base64;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoqin on 5/25/15.
 */
public class PollService extends Service{
    private final String TAG = "PollService";
    String time = "30";
    Timer timer;
    Handler handler;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("updateTime");
        IntentFilter beacon = new IntentFilter("beaconmaclist.log");
        IntentFilter assoc = new IntentFilter("assocmaclist.log");
        IntentFilter temp = new IntentFilter("temp.macdetectlog");
        registerReceiver(broadcastReceiver, temp);
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
//        String ServerPath = getResources().getString(R.string.db_path);
//        String fileName = "qiandao.db";
//        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/qiandao.db";
//        if (FTPUtils.isExists(dataPath)) {
//            FTPUtils.deleteFiles(dataPath);
//        }
//        FTPUtils.downloadFileFromFTP(this, ServerPath, fileName);
//        Toast.makeText(this, "签到列表已下载", Toast.LENGTH_LONG).show();
        String ServerPath = "/tmp/beaconmaclist.log";
        String localName = "beaconmaclist.log";
        downloadFile(ServerPath, localName);

        String ServerPath1 = "/tmp/assocmaclist.log";
        String localName1 = "assocmaclist.log";
        downloadFile(ServerPath1, localName1);

        String ServerPath2 = "/tmp/log/*.macdetectlog";
        String localName2 = "temp.macdetectlog";
        downloadFile(ServerPath2, localName2);
    }

    private void downloadFile(String ServerPath, String localName) {
        Log.e(TAG, "downloadFile");
        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/" + localName;
        if (FTPUtils.isExists(dataPath)) {
            FTPUtils.deleteFiles(dataPath);
        }
        FTPUtils.downloadFileFromFTP(this, ServerPath, localName);
        Log.e(TAG, localName + " is download");
    }


    private void readFile(String fileName) {
        Log.e(TAG, "readFile");
        String dataPath = "/mnt/sdcard/" + getApplicationContext().getPackageName() + "/" + fileName;
        File file = new File(dataPath);
        try {
            /* FileInputStream 输入流的对象， */
            FileInputStream fis = new FileInputStream(file);
            /* 准备一个字节数组用户装即将读取的数据 */
            byte[] buffer = new byte[fis.available()];
             /* 开始进行文件的读取 */
            fis.read(buffer);
            /* 关闭流  */
            fis.close();
            byte[] valueDecoded= Base64.decodeBase64(buffer);
            Log.e("EncryptUtil", String.valueOf(valueDecoded.length));
            Log.e("EncryptUtil", new String(valueDecoded));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                readFile("beaconmaclist.log");
            }
            if (intent.getAction().equals("assocmaclist.log")) {
                readFile("assocmaclist.log");
            }
            if (intent.getAction().equals("temp.macdetectlog")) {
                readFile("temp.macdetectlog");
            }
        }
    };

}
