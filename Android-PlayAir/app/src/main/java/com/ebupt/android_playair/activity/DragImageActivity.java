package com.ebupt.android_playair.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ebupt.android_playair.R;
import com.ebupt.android_playair.jellyViewHelper.TestFragPagerAdapter;
import com.ebupt.android_playair.util.CommonUtils;
import com.ebupt.android_playair.widget.JellyViewPager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class DragImageActivity extends FragmentActivity {
	JellyViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragimage);

		pager = (JellyViewPager) findViewById(R.id.myViewPager1);
		pager.setAdapter(new TestFragPagerAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				switch(state){
				case 1: //正在滑动
					break;
				case 2: //滑动结束
                    break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
			}
			
		});
        pager.setScrollPageToSendMessageListener(new JellyViewPager.ScrollPageToSendMessageListener() {
            @Override
            public void sendMessage(String message) {
//                CommonUtils.sendMessage("message", "ZYKDemo:DragImageActivity:" + message);
                CommonUtils.sendMessage("picture sliding", "");
            }
        });
        IntentFilter intentFilter = new IntentFilter("socketIO");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("test", "收到了" + intent.getExtras().getString("message"));
//            String message = intent.getExtras().getString("message");
//            if(message.equals("up")){
//                if (pager != null)
//                    pager.showPre(true);
//            }else if(message.equals("down")){
//                if (pager != null)
//                    pager.showNext(true);
//            }else if(message.equals("finishDragImageActivity")){
//                finishAcitvity();
//            }
//            if(intent.getExtras().equals(""))
        }
    };

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.closeDrawImage:
                finishAcitvity();
//                CommonUtils.sendMessage("message", "ZYKDemo:DragImageActivity:finishDragImageActivity");
                CommonUtils.sendMessage("exit", "picture");
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
//            CommonUtils.sendMessage("message","ZYKDemo:DragImageActivity:finishDragImageActivity");
            CommonUtils.sendMessage("exit", "picture");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
