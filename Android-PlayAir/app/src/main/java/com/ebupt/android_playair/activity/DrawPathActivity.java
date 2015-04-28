package com.ebupt.android_playair.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ebupt.android_playair.R;
import com.ebupt.android_playair.util.CommonUtils;
import com.ebupt.android_playair.widget.GuaGuaKa;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.UnknownFormatConversionException;

/**
 * Created by Administrator on 2015/4/16.
 */
public class DrawPathActivity extends Activity {

    private GuaGuaKa guaGuaKa;

    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawpath);


        width = CommonUtils.getWindowWidth(this);
        height = CommonUtils.getWindowHeight(this);



        guaGuaKa = (GuaGuaKa)findViewById(R.id.guaguaka);
        guaGuaKa.setonDrawPathListener(new GuaGuaKa.onDrawPathListener() {
            @Override
            public void onDrawPath(int x, int y,int action) {
                CommonUtils.sendMessage("message","ZYKDemo:DrawPathActivity:Position."+((float)x)/width+","+((float)y)/height+","+action);
//                CommonUtils.sendMessage("note footprint", );
            }
        });


        IntentFilter intentFilter = new IntentFilter("DrawPathActivity");
        registerReceiver(broadcastReceiver, intentFilter);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("test", "收到了" + intent.getExtras().getString("message"));
            String message = intent.getExtras().getString("message");
            if (message.equals("clearPath")) {
                if (guaGuaKa != null)
                    guaGuaKa.clearPath();
            } else if (message.equals("finishDrawPathActivity")) {
                finishAcitvity();
            } else if (message.startsWith("Position.")) {
                String positionMessage = message.substring(9);
                String positions[] = positionMessage.split(",");
                String newX = positions[0];
                String newY = positions[1];
                String action = positions[2];
                if (guaGuaKa != null) {
                    if (Integer.parseInt(action) == MotionEvent.ACTION_MOVE) {
                        guaGuaKa.move((int) (Float.parseFloat(newX) * width), (int) (Float.parseFloat(newY) * height));
                    } else if (Integer.parseInt(action) == MotionEvent.ACTION_DOWN) {
                        guaGuaKa.down((int) (Float.parseFloat(newX) * width), (int) (Float.parseFloat(newY) * height));
                    }
                }
                Log.e("test", positions[0] + "," + positions[1]);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.closeDrawPath:
                finishAcitvity();
//                CommonUtils.sendMessage("message", "ZYKDemo:DrawPathActivity:finishDrawPathActivity");
                CommonUtils.sendMessage("exit", "note");
                break;
            case R.id.buttonClear:
                guaGuaKa.clearPath();
                CommonUtils.sendMessage("message", "ZYKDemo:DrawPathActivity:clearPath");
                break;
            default:
                break;
        }
    }

    private void finishAcitvity() {
        setResult(RESULT_OK);
        this.finish();  //finish当前activity
        overridePendingTransition(0,
                0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            finishAcitvity();
//            CommonUtils.sendMessage("message","ZYKDemo:DrawPathActivity:finishDrawPathActivity");
            CommonUtils.sendMessage("exit", "note");
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
