package com.emdoor.autotest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class CameraTestActivity extends Activity {

	private CameraView cameraView;
	public static Handler handler;
	private IntentFilter filter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ú���ģʽ�Լ�ȫ��ģʽ
		cameraView = new CameraView(this, handler);
		setContentView(cameraView); // ����View
		filter = new IntentFilter(Intents.ACTION_TAKE_PHOTO);

	}

	@Override
	protected void onResume() {
		registerReceiver(receiver, filter);
		super.onResume();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);
		super.onStop();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intents.ACTION_TAKE_PHOTO.equals(intent.getAction())) {

				if (cameraView != null) {
					cameraView.takePicture();
				}

			}

		}

	};

}