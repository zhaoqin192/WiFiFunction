package com.ebupt.android_playair.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ebupt.android_playair.R;
import com.ebupt.android_playair.util.CommonUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.DanmakuFactory;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

public class VideoPlayActivity extends Activity {

    private SurfaceView surface_view;                           /* 播放视频载体 */
    private MediaPlayer mediaPlayer;                            /* 播放器 */
    private SurfaceHolder surface_holder;                       /* Surface 控制器 */
    private boolean isStartPlaying;                             /* 是否开始了播放 */
    private boolean isShowDanmu = true;                         //是否显示弹幕

    private IDanmakuView mDanmakuView;
    private BaseDanmakuParser mParser;

    private EditText et;
    private ImageView check;
    private ImageView playbutton;

    private Handler handler;

    private SeekBar seekbar;
    private TextView currentTime;
    private TextView maxTime;

    private Boolean flag;
    private Boolean isPlay;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        initViews();
        initDanmuView();
        initData();


        IntentFilter intentFilter = new IntentFilter("VideoPlayActivity");
        registerReceiver(broadcastReceiver, intentFilter);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        int a = msg.getData().getInt("1");
                        int b = msg.getData().getInt("2");
                        set(b, a);
                }
            }
            private void set(int progress, int max) {
                // TODO 自动生成的方法存根
                if(currentTime!=null)
                    currentTime.setText(toTime(progress));
                if(maxTime!=null)
                    maxTime.setText( toTime(max));
            }
            private String toTime(int progress) {
                // TODO 自动生成的方法存根
                StringBuffer sb = new StringBuffer();
                int s = (progress / 1000) % 60;
                int m = progress / 60000;
                sb.append(m).append(":");
                if (s < 10) {
                    sb.append(0);
                }
                sb.append((progress / 1000) % 60);
                return sb.toString();
            }
        };

        seekbar.setEnabled(false);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO 自动生成的方法存根
                if (mediaPlayer != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO 自动生成的方法存根
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO 自动生成的方法存根
            }
        });
    }
    private void initViews(){
        surface_view = (SurfaceView) findViewById(R.id.surface_view);
        mDanmakuView = (IDanmakuView)findViewById(R.id.mydanmuku);
        et = (EditText)findViewById(R.id.edittext);
        check = (ImageView) findViewById(R.id.check);
        playbutton =  (ImageView) findViewById(R.id.toggleplay);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        currentTime = (TextView)findViewById(R.id.currenttime);
        maxTime = (TextView)findViewById(R.id.maxtime);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
    }


    private BaseDanmakuParser createParser(InputStream stream) {

        if(stream==null){
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }


        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    private void initDanmuView() {
        DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN, 3);
        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mDanmakuView.setCallback(new DrawHandler.Callback() {

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(mParser);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    private void initData() {
/* 获取并设置 SurfaceHolder 对象 */
        surface_holder = surface_view.getHolder();                      /* 根据 SurfaceView 组件, 获取 SurfaceHolder 对象 */
        surface_holder.setFixedSize(160, 128);                          /* 设置视频大小比例 */
        surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);/* 设置视频类型 */
        isPlay=false;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.toggleplay:
                if(isPlay==false) {
                    playVideo();
                    playbutton.setImageResource(R.drawable.pause);
//                    CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:play");
                    CommonUtils.sendMessage("video play", "");
                    isPlay=true;
                }
                else {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        if (!(mDanmakuView == null || !mDanmakuView.isPrepared()))
                            mDanmakuView.pause();
                        playbutton.setImageResource(R.drawable.play);
//                        CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:pause");
                        CommonUtils.sendMessage("video pause", "");
                        isPlay=false;
                    }
                }
                break;
            case R.id.quickbackward:
                if (mediaPlayer != null) {
                    int progress = mediaPlayer.getCurrentPosition();
                    progress = progress-mediaPlayer.getDuration()/10;
                    seekbar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                    CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:quickbackward");
                }
                break;
            case R.id.quickforward:
                if (mediaPlayer != null) {
                    int progress = mediaPlayer.getCurrentPosition();
                    progress = progress+mediaPlayer.getDuration()/10;
                    seekbar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                    CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:quickforward");
                }
                break;
            case R.id.check:
                if(!isShowDanmu){
                if (mDanmakuView == null || !mDanmakuView.isPrepared())
                    return;
                mDanmakuView.show();
                    CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:show");
                    check.setImageResource(R.drawable.check);
                    isShowDanmu=true;
                }
                else if(isShowDanmu) {
                    if (mDanmakuView == null || !mDanmakuView.isPrepared())
                        return;
                    mDanmakuView.hide();
                    CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:hide");
                    check.setImageResource(R.drawable.uncheck);
                    isShowDanmu = false;
                }
                break;
            case R.id.danmu:
                if (mDanmakuView == null || !mDanmakuView.isPrepared())
                    return;
                if(et.getText()==null||et.getText().equals(""))
                    return;
                sendDanmu(et.getText().toString());
//                CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:danmu." + et.getText().toString());
                CommonUtils.sendMessage("video barrage", et.getText().toString());
                et.setText("");
                break;
            case R.id.close:
                finishActivity();
//                CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:finishVideoPlayActivity");
                CommonUtils.sendMessage("exit", "video");
                break;
            default:
                break;
        }
    }

    private void finishActivity() {
        flag=false;
        setResult(RESULT_OK);
        this.finish();  //finish当前activity
        overridePendingTransition(0,
                0);
    }

    private void sendDanmu(String danmuText){
        BaseDanmaku danmaku = DanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = danmuText;
        danmaku.padding = 5;
        danmaku.priority = 1;
        danmaku.time = mDanmakuView.getCurrentTime() + 200;
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        //danmaku.underlineColor = Color.GREEN;

        mDanmakuView.addDanmaku(danmaku);
    }
    private void playVideo() {
        if(isStartPlaying){
            if (!(mDanmakuView == null || !mDanmakuView.isPrepared()))
                mDanmakuView.resume();
            /* 如果已经开始了播放, 就直接开始播放 */
            mediaPlayer.start();
        }else{                                          /* 如果是第一次开始播放, 需要初始化 MediaPlayer 设置监听器等操作 */
            mediaPlayer = new MediaPlayer();            /* 创建 MediaPlayer 对象 */
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);          /* 设置播放音量 */
            mediaPlayer.setDisplay(surface_holder);     /* 设置播放载体 */
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer arg0, int what, int extra) {
                    System.out.println("MediaPlayer 出现错误 what : " + what + " , extra : " + extra);
                    return false;
                }
            });
            if(!seekbar.isEnabled())
                seekbar.setEnabled(true);

            new Thread(){
                public void run() {
                    try {
                       //mediaPlayer.setDataSource(R.raw.guide);
                        mediaPlayer.setDataSource(VideoPlayActivity.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide));
                        mediaPlayer.prepareAsync();
                        Log.e("test",mediaPlayer.getDuration()+"");
                        mediaPlayer.start();

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // TODO 自动生成的方法存根
                                mediaPlayer.start();
                                final int max = mediaPlayer.getDuration();
                                seekbar.setMax(max);
                                //mediaPlayer.seekTo();
                                new Thread() {
                                    public void run() {
                                        flag = true;
                                        while (flag) {
                                            int progress = mediaPlayer.getCurrentPosition();
                                            seekbar.setProgress(progress);
                                            Message message = new Message();
                                            Bundle bundle = new Bundle();
                                            message.setData(bundle);
                                            bundle.putInt("1", max);
                                            bundle.putInt("2", progress);
                                            message.what = 0;
                                            handler.sendMessage(message);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                // TODO 自动生成的 catch 块
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }.start();
                            }
                        });

//                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                seekbar.setProgress(0);
//                                currentTime.setText("0:00");
//                                mediaPlayer.stop();
//                                mediaPlayer.seekTo(0);
////                                Message message = new Message();
////                                Bundle bundle = new Bundle();
////                                message.setData(bundle);
////                                bundle.putInt("1", mediaPlayer.getDuration());
////                                bundle.putInt("2", 0);
////                                message.what = 0;
////                                handler.sendMessage(message);
//                            }
//                        });

                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                flag = false;
                                return false;
                            }
                        });
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                };
            }.start();



            isStartPlaying = true;
        }
    }


    @Override
    protected void onDestroy() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){

            finishActivity();
//            CommonUtils.sendMessage("message", "ZYKDemo:VideoPlayActivity:finishVideoPlayActivity");
            CommonUtils.sendMessage("exit", "video");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("test", "收到了" + intent.getExtras().getString("message"));
            if(intent.getExtras().getString("message").equals("play")){
                if(isPlay==false) {
                    playVideo();
                    playbutton.setImageResource(R.drawable.pause);
                    isPlay=true;
                }
            }
            else if(intent.getExtras().getString("message").equals("pause")){
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    if (!(mDanmakuView == null || !mDanmakuView.isPrepared()))
                        mDanmakuView.pause();
                    playbutton.setImageResource(R.drawable.play);
                    isPlay=false;
                }
            }
            else if(intent.getExtras().getString("message").equals("quickbackward")){
                if (mediaPlayer != null) {
                    int progress = mediaPlayer.getCurrentPosition();
                    progress = progress-mediaPlayer.getDuration()/10;
                    seekbar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                }
            }
            else if(intent.getExtras().getString("message").equals("quickforward")){
                if (mediaPlayer != null) {
                    int progress = mediaPlayer.getCurrentPosition();
                    progress = progress+mediaPlayer.getDuration()/10;
                    seekbar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                }
            }
            else if(intent.getExtras().getString("message").equals("hide")){
                if (!(mDanmakuView == null || !mDanmakuView.isPrepared())){
                    mDanmakuView.hide();
                    check.setImageResource(R.drawable.uncheck);
                    isShowDanmu = false;
                }
            }
            else if(intent.getExtras().getString("message").equals("show")){
                if (!(mDanmakuView == null || !mDanmakuView.isPrepared())) {
                    mDanmakuView.show();
                    check.setImageResource(R.drawable.check);
                    isShowDanmu=true;
                }
            }
            else if(intent.getExtras().getString("message").equals("finishVideoPlayActivity")){
                finishActivity();
            }
            else if(intent.getExtras().getString("message").startsWith("danmu.")){
                sendDanmu(intent.getExtras().getString("message").substring(6));
            }
        }
    };
}

