package com.emdoor.autotest.testcase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	SurfaceHolder holder;
	private float radiusYe = 10.0f;
	private float radiusRed = 4.0f;
	public volatile boolean run = true;

	public void setRun(boolean run) {
		this.run = run;
	}

	private volatile float xOffset, yOffset;

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;

	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = this.getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(new MyThread()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	class MyThread implements Runnable {

		@Override
		public void run() {
			while (run) {
				Canvas canvas = holder.lockCanvas(null);
				canvas.drawColor(Color.BLACK);
				int width = canvas.getWidth();
				int height = canvas.getHeight();
				Paint mPaint = new Paint();
				mPaint.setColor(Color.RED);
				canvas.drawCircle(width / 2, height / 2, radiusRed, mPaint);

				mPaint.setColor(Color.YELLOW);
				canvas.drawCircle(width / 2 - xOffset * 10, height / 2
						- yOffset * 10, radiusYe, mPaint);

				if (xOffset > 8) {
					mPaint.setColor(Color.RED);
					canvas.drawLine(2, 2, 2, 10, mPaint);
				}
				holder.unlockCanvasAndPost(canvas);

			}

		}

	}
}
