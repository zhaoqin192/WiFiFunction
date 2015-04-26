package com.ebupt.android_playair.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation") public class GuaGuaKa extends View
{
	private Paint mOutterPaint;
	private Path mPath;
	private Canvas mCanvas;
	private Bitmap mBitmap;

    private int width;
    private int height;

	private int mLastX;
	private int mLastY;

    private boolean isClear = false;

    public interface onDrawPathListener{
        void onDrawPath(int x, int y, int action);
    }

    private onDrawPathListener listener;

    public void setonDrawPathListener(onDrawPathListener listener){
        this.listener = listener;
    }

	public GuaGuaKa(Context context)
	{
		this(context, null);
	}

	public GuaGuaKa(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public GuaGuaKa(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = getMeasuredWidth();
		height = getMeasuredHeight();
		// ��ʼ�����ǵ�bitmap
		mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

		// ���û���path���ʵ�һЩ����
		setupOutPaint();

		Paint tempPaint = new Paint();
		tempPaint.setColor(Color.parseColor("#ffffff"));
		mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30,
				tempPaint);

	}



	/**
	 * ���û���path���ʵ�һЩ����
	 */
	private void setupOutPaint()
	{
		mOutterPaint.setColor(Color.parseColor("#ff0000"));
		mOutterPaint.setAntiAlias(true);
		mOutterPaint.setDither(true);
		mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
		mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
		mOutterPaint.setStyle(Style.FILL);
		mOutterPaint.setStrokeWidth(20);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();

		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:

			mLastX = x;
			mLastY = y;
			mPath.moveTo(mLastX, mLastY);
            listener.onDrawPath(x,y,MotionEvent.ACTION_DOWN);
			break;
		case MotionEvent.ACTION_MOVE:

			int dx = Math.abs(x - mLastX);
			int dy = Math.abs(y - mLastY);

			if (dx > 3 || dy > 3)
			{
				mPath.lineTo(x, y);
                listener.onDrawPath(x,y,MotionEvent.ACTION_MOVE);
			}

			mLastX = x;
			mLastY = y;
			break;
		}
			invalidate();
		return true;

	}

    public void move(int x, int y){
        int dx = Math.abs(x - mLastX);
        int dy = Math.abs(y - mLastY);

        if (dx > 3 || dy > 3)
        {
            mPath.lineTo(x, y);
        }

        mLastX = x;
        mLastY = y;

        invalidate();
    }

    public void down(int x, int y) {
        mLastX = x;
        mLastY = y;
        mPath.moveTo(mLastX, mLastY);
        invalidate();
    }


    @Override
	protected void onDraw(Canvas canvas)
	{
        if(isClear){
            mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            Paint tempPaint = new Paint();
            tempPaint.setColor(Color.parseColor("#ffffff"));
            mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30,
                    tempPaint);

            canvas.drawBitmap(mBitmap, 0, 0, null);
            mPath = new Path();
            isClear=false;
        }else {
            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
	}

	private void drawPath()
	{
		mOutterPaint.setStyle(Style.STROKE);
		mCanvas.drawPath(mPath, mOutterPaint);
	}

	/**
	 * ����һЩ��ʼ������
	 */
	private void init()
	{
		mOutterPaint = new Paint();
		mPath = new Path();
	}

    public void clearPath(){
        isClear=true;
        invalidate();
    }
}
