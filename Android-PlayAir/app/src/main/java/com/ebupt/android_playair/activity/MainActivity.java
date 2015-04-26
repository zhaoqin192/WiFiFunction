package com.ebupt.android_playair.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ebupt.android_playair.R;
import com.ebupt.android_playair.Service.CommunicationService;
import com.ebupt.android_playair.util.CommonUtils;


/**
 * Created by Administrator on 2015/4/2.
 */
public class MainActivity extends Activity {
   //屏幕宽高
    private int screenWidth;
    private int screenHeight;

    private RelativeLayout container;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView imgCover;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenWidth = CommonUtils.getWindowWidth(this);
        screenHeight = CommonUtils.getWindowHeight(this);

        container = (RelativeLayout) findViewById(R.id.container);

        imgCover = new ImageView(this);



        img3 = new ImageView(this);
        RelativeLayout.LayoutParams params3 = initParams();
        params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        img3.setBackgroundResource(R.drawable.fragment3);
        container.addView(img3, params3);

        img4 = new ImageView(this);
        RelativeLayout.LayoutParams params4 = initParams();
        params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        img4.setBackgroundResource(R.drawable.fragment4);
        container.addView(img4, params4);

        img1 = new ImageView(this);
        RelativeLayout.LayoutParams params1 = initParams();
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        img1.setBackgroundResource(R.drawable.fragment1);
        container.addView(img1, params1);

        img2 = new ImageView(this);
        RelativeLayout.LayoutParams params2 = initParams();
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        img2.setBackgroundResource(R.drawable.fragment2);
        container.addView(img2, params2);

        Log.e("test","通知栏高度为:"+CommonUtils.getStatusBarHeight(this));

        startService(new Intent(this, CommunicationService.class));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("test","收到了"+intent.getExtras().getString("message"));
                if(intent.getExtras().getString("message").equals("startActivity1"))
                    startActivity1();
                if(intent.getExtras().getString("message").equals("startActivity2"))
                    startActivity2();
                if(intent.getExtras().getString("message").equals("startActivity3"))
                    startActivity3();
                if(intent.getExtras().getString("message").equals("startActivity4"))
                    startActivity4();
            }
        },new IntentFilter("MainActivity"));

    }



    @Override
    protected void onResume() {
        super.onResume();

        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtils.sendMessage("message","ZYKDemo:MainActivity:startActivity1");
                startActivity1();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtils.sendMessage("message","ZYKDemo:MainActivity:startActivity2");
                startActivity2();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtils.sendMessage("message","ZYKDemo:MainActivity:startActivity3");
                startActivity3();
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CommonUtils.sendMessage("message","ZYKDemo:MainActivity:startActivity4");
                startActivity4();
            }
        });
    }


    private void startActivity4() {
        if(imgCover.getParent()==container)
            container.removeView(imgCover);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params5.width = screenWidth / 2;
        params5.height = screenHeight / 2;
        imgCover.setBackgroundResource(R.drawable.fragment4);
        container.addView(imgCover, params5);
        imgCover.setPivotX(imgCover.getLayoutParams().width);
        imgCover.setPivotY(imgCover.getLayoutParams().height);
        imgCover.invalidate();
        loadAnimatorEnlarge(imgCover, 4);
        loadAnimatorShrink(img1, img2, img3, img4);
    }

    private void startActivity3() {
        if(imgCover.getParent()==container)
            container.removeView(imgCover);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params5.width = screenWidth / 2;
        params5.height = screenHeight / 2;
        imgCover.setBackgroundResource(R.drawable.fragment3);
        container.addView(imgCover, params5);
        imgCover.setPivotX(0);
        imgCover.setPivotY(imgCover.getLayoutParams().height);
        imgCover.invalidate();
        loadAnimatorEnlarge(imgCover, 3);
        loadAnimatorShrink(img1, img2, img3, img4);
    }

    private void startActivity2() {
        if(imgCover.getParent()==container)
            container.removeView(imgCover);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params5.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params5.width = screenWidth / 2;
        params5.height = screenHeight / 2;
        imgCover.setBackgroundResource(R.drawable.fragment2);
        container.addView(imgCover, params5);
        imgCover.setPivotX(imgCover.getLayoutParams().width);
        imgCover.setPivotY(0);
        imgCover.invalidate();
        loadAnimatorEnlarge(imgCover, 2);
        loadAnimatorShrink(img1, img2, img3, img4);
    }

    private void startActivity1() {
        if(imgCover.getParent()==container)
            container.removeView(imgCover);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params5.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params5.width = screenWidth / 2;
        params5.height = screenHeight / 2;
        imgCover.setBackgroundResource(R.drawable.fragment1);
        container.addView(imgCover, params5);
        imgCover.setPivotX(0);
        imgCover.setPivotY(0);
        imgCover.invalidate();
        loadAnimatorEnlarge(imgCover, 1);
        loadAnimatorShrink(img1, img2, img3, img4);
    }

    private RelativeLayout.LayoutParams initParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.width = screenWidth / 2;
        params.height = screenHeight / 2;
        return params;
    }

    private void loadAnimatorEnlarge(final ImageView imgCover, final int i) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imgCover, "aaa", 1.0f, 2.0f);
        animator1.setTarget(imgCover);
        animator1.setDuration(700);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (Float) valueAnimator.getAnimatedValue();
                imgCover.setScaleX(cVal);
                imgCover.setScaleY(cVal);
            }
        });
        animator1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                Handler handler = new Handler();
                Intent intent;
                switch (i) {
                    case 1:
                        intent = new Intent(MainActivity.this, DragImageActivity.class);
                        MainActivity.this.startActivityForResult(intent, 1);
                        overridePendingTransition(0, 0);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                        MainActivity.this.startActivityForResult(intent, 2);
                        overridePendingTransition(0, 0);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, DrawPathActivity.class);
                        MainActivity.this.startActivityForResult(intent, 3);
                        overridePendingTransition(0, 0);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, DragImageActivity.class);
                        MainActivity.this.startActivityForResult(intent, 4);
                        overridePendingTransition(0, 0);
                        break;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        container.removeView(imgCover);
                    }
                }, 300);

            }
        });
        animator1.start();
    }

    private void loadImgCoverShrink(final ImageView imgCover){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imgCover, "aaa", 1.0f, 0.5f);
        animator1.setTarget(imgCover);
        animator1.setDuration(700);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (Float) valueAnimator.getAnimatedValue();
                imgCover.setScaleX(cVal);
                imgCover.setScaleY(cVal);
            }
        });
        animator1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                        container.removeView(imgCover);
            }
        });
        animator1.start();
    }

    private void loadAnimatorShrink(final ImageView img1,
                                    final ImageView img2, final ImageView img3,
                                    final ImageView img4) {
        animShrink(img1);
        animShrink(img2);
        animShrink(img3);
        animShrink(img4);

    }

    private void loadAnimatorShrinkBack(final ImageView img1,
                                        final ImageView img2, final ImageView img3,
                                        final ImageView img4){
        animLarge(img1);
        animLarge(img2);
        animLarge(img3);
        animLarge(img4);
    }

    private void animLarge(final ImageView img){
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "zyk", 0.0f, 1.0f);
        animator2.setDuration(700);
//        animator2.setStartDelay(100);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (Float) valueAnimator.getAnimatedValue();
                img.setScaleX(cVal);
                img.setScaleY(cVal);
            }
        });
        animator2.start();
    }

    private void animShrink(final ImageView img) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img, "zyk", 1.0f, 0.0f);
        animator1.setDuration(700);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (Float) valueAnimator.getAnimatedValue();
                img.setScaleX(cVal);
                img.setScaleY(cVal);
            }
        });
        animator1.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animator1.start();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==1) {
            if(img1!=null&&img2!=null&&img3!=null&&img4!=null)
                loadAnimatorShrinkBack(img1,img2,img3,img4);
            if(imgCover.getParent()==container)
                container.removeView(imgCover);
            RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params5.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params5.width = screenWidth ;
            params5.height = screenHeight ;
            imgCover.setBackgroundResource(R.drawable.fragment1);
            container.addView(imgCover, params5);
            imgCover.setPivotX(0);
            imgCover.setPivotY(0);
            imgCover.invalidate();
            loadImgCoverShrink(imgCover);
        }
        if (resultCode==RESULT_OK&&requestCode==2) {
            if(img1!=null&&img2!=null&&img3!=null&&img4!=null)
                loadAnimatorShrinkBack(img1,img2,img3,img4);
            if(imgCover.getParent()==container)
                container.removeView(imgCover);
            RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params5.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params5.width = screenWidth ;
            params5.height = screenHeight ;
            imgCover.setBackgroundResource(R.drawable.fragment2);
            container.addView(imgCover, params5);
            imgCover.setPivotX(imgCover.getLayoutParams().width);
            imgCover.setPivotY(0);
            imgCover.invalidate();
            loadImgCoverShrink(imgCover);
        }
        if (resultCode==RESULT_OK&&requestCode==3) {
            if(img1!=null&&img2!=null&&img3!=null&&img4!=null)
                loadAnimatorShrinkBack(img1,img2,img3,img4);
            if(imgCover.getParent()==container)
                container.removeView(imgCover);
            RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params5.width = screenWidth ;
            params5.height = screenHeight ;
            imgCover.setBackgroundResource(R.drawable.fragment3);
            container.addView(imgCover, params5);
            imgCover.setPivotX(0);
            imgCover.setPivotY(imgCover.getLayoutParams().height);
            imgCover.invalidate();
            loadImgCoverShrink(imgCover);
        }
        if (resultCode==RESULT_OK&&requestCode==4) {
            if(img1!=null&&img2!=null&&img3!=null&&img4!=null)
                loadAnimatorShrinkBack(img1,img2,img3,img4);
            if(imgCover.getParent()==container){
                container.removeView(imgCover);
            }
            RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params5.width = screenWidth ;
            params5.height = screenHeight ;
            imgCover.setBackgroundResource(R.drawable.fragment4);
            container.addView(imgCover, params5);
            imgCover.setPivotX(imgCover.getLayoutParams().width);
            imgCover.setPivotY(imgCover.getLayoutParams().height);
            imgCover.invalidate();
            loadImgCoverShrink(imgCover);
        }
    }
}
