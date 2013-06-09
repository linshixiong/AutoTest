package com.emdoor.autotest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class CameraTestActivity extends Activity {

	private CameraView cameraView;
	public static Handler handler;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置横屏模式以及全屏模式
		cameraView=new CameraView(this);
		setContentView(cameraView); // 设置View
		
		takePhoto();
	}
	
	private void takePhoto(){
		new Thread(){

			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cameraView.takePicture(handler);
				super.run();
			}
			
		}.start();
	}

}