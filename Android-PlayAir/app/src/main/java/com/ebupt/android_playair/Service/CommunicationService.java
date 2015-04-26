package com.ebupt.android_playair.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2015/4/16.
 */
public class CommunicationService extends Service {

    public static Socket mSocket;

    private Intent intent;


    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Thread( new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONObject obj = new JSONObject(data.getString("message"));
                        String completeMessage = obj.getString("message");
                        String messages[] = completeMessage.split(":");
                        if(messages[0].equals("ZYKDemo")){
                            intent = new Intent(messages[1]);
                            intent.putExtra("message",messages[2]);
                            sendBroadcast(intent);
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            }).start();
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();

        mSocket.connect();
        mSocket.on("new message", onNewMessage);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
