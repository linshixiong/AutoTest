package com.emdoor.autotest.testcase;



import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import android.net.Uri;


public class HdmiTest extends Activity implements Runnable , SurfaceHolder.Callback{

	private Button btnok, btnfail, btnback;
	
	private Configuration config;
	
	private MediaPlayer mp;
	
	LinearLayout LinearLayout01 = null;
	
	private SurfaceView surfaceView;
	
	private SurfaceHolder surfaceHolder;
	
	private MediaPlayer mediaPlayer;
	
	private static final int MESSAGETYPE_01 = 0x0001;
	
	Thread T1 = null;
	
	Boolean isStop = false;
	
	int i=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.hdmi);
		
		config = new Configuration(this);
		
		btnok = (Button)findViewById(R.id.btnhdmiok);
		btnfail = (Button)findViewById(R.id.btnhdmifail);
		btnback = (Button)findViewById(R.id.btnhdmiback);
		
		btnok.setOnClickListener(linstener);
		btnfail.setOnClickListener(linstener);
		btnback.setOnClickListener(linstener);	
		
		LinearLayout01 = (LinearLayout) findViewById(R.id.hdmi);
		
		//LinearLayout01.setBackgroundColor(Color.RED);
		
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFixedSize(700, 500);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
		//startHdmiTest();
		
		//T1 = new Thread(this);
		//T1.start();
		
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGETYPE_01:
				changeScreenColor();
				break;
			}
			super.handleMessage(message);
		}
	};
	
	void changeScreenColor() {
		// if (flag == 1)
		i++;
		if (i < 1000) {
			switch (i % 5) {
			case 0:
				LinearLayout01.setBackgroundColor(Color.RED);
				break;
			case 1:
				LinearLayout01.setBackgroundColor(Color.GREEN);
				break;
			case 2:
				LinearLayout01.setBackgroundColor(Color.BLUE);
				break;
			case 3:
				LinearLayout01.setBackgroundColor(Color.WHITE);
				break;
			case 6:
				LinearLayout01.setBackgroundColor(Color.BLACK);
				break;
			}
		} else {
			isStop = true;
		}
		
		
	}
	
	private void startHdmiTest() {
		// TODO Auto-generated method stub
		mp = MediaPlayer.create(this,R.raw.speeker_test);
		mp.start();
		mp.setLooping(true);		
		
		
	}	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();		
		isStop = true;		
	}	
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		//if(mp.isPlaying()){
		//	mp.stop();
		//}
		isStop = true;
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			if (!isStop) {
				try {
					Thread.sleep(1000);
					Message msg_listData = new Message();
					msg_listData.what = MESSAGETYPE_01;
					handler.sendMessage(msg_listData);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btnhdmiok:
				config.setHDMITestOk(1);
				break;
			case R.id.btnhdmifail:
				config.setHDMITestOk(2);
				break;
			case R.id.btnhdmiback:
				config.setHDMITestOk(0);
				break;
			default:
				config.setHDMITestOk(0);
				break;
			}
			finish();
		}
		
		
	};
	
	public void Start(){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDisplay(surfaceHolder);
		try
		{
			mediaPlayer.setDataSource(HdmiTest.this, Uri.parse("android.resource://com.emdoor.selftest/"+R.raw.test));			
			mediaPlayer.prepare();			
			mediaPlayer.start();
		}
		catch (Exception e){}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub		
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mediaPlayer.stop();
		
	}

	
}
