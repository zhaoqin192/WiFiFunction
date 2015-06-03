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
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = (MyApp) getApplicationContext();

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
        IntentFilter downList = new IntentFilter("downList");
        registerReceiver(broadcastReceiver, downList);
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
            }
        };
        timer.schedule(timerTask, 1000, Integer.parseInt(time) * 60 * 1000);
    }

    private void getList() {
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

                FTPUtils.downloadFileFromFTPBySuffix(PollService.this, "/tmp/log", "macdetectlog");

                String ServerPath1 = "/tmp/assocmaclist.log";
                String localName1 = "assocmaclist.log";
                downloadFile(ServerPath1, localName1);
            }
        }).start();
    }

    private void downloadFile(String ServerPath, String localName) {
        String dataPath = "/mnt/sdcard/" + this.getPackageName() + "/" + localName;
        if (FTPUtils.isExists(dataPath)) {
            FTPUtils.deleteFiles(dataPath);
        }
        FTPUtils.downloadFileFromFTP(this, ServerPath, localName);
    }

    private void readFile(final String fileName) {
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
                    Log.e("ListCount", list.size() + " before");
                    int size = list.size();
                    int outsize = downList.size();
                    List<BrokenData> temp = new ArrayList<>();
                    for (int i = 0; i < outsize; i++) {
                        DownVisitorMSG outData = downList.get(i);
                        for (int j = 0; j < size; j++) {
                            BrokenData data = list.get(j);
                            if (data.getMac().equals(outData.getMac())) {
                                Log.e("PhoneTime", data.getTime() + "");
                                if (data.getTime() >= (currentTimeMillis - Long.parseLong(time) * 60)) {
                                    temp.add(data);
                                }
                            }
                        }
                    }
                    list = temp;
                    int listSize = list.size();
                    Log.e("ListCount", list.size() + " after");

                    for (int i = 0; i < outsize; i++) {
                        DownVisitorMSG outData = downList.get(i);
                        for (int j = 0; j < listSize; j++) {
                            BrokenData data = list.get(j);
                            if (outData.getMac().equals(data.getMac())) {
                                outData.setStatus("online");
                            }
                            outData.saveThrows();
                        }
                    }


                    if (fileName.equals("assocmaclist.log")) {
                        if (temp.size() < outsize) {
                            myApp.viewCount++;
                            int diff = outsize - temp.size();
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
            if (intent.getAction().equals("downList")) {
                getList();
            }
        }
    };

}
