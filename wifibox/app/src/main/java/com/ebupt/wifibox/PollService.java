package com.ebupt.wifibox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoqin on 5/25/15.
 */
public class PollService extends Service{
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

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updateTime")) {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);

            }
        }
    };

}
