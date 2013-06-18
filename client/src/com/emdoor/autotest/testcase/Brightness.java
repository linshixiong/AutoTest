package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

public class Brightness extends Activity implements OnClickListener {

	private static final int SET_BRIGHTNESS = 0;
	int brightness = 0;
	int direction = 0;
	Button passBtn,failBtn,backBtn;
	Configuration config;
	ProgressBar proBar;
	
	private int mScreenBrightnessDim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		config = new Configuration(this);
		setContentView(R.layout.brightness);
		passBtn = (Button) findViewById(R.id.btn_brightless_1);
		failBtn = (Button) findViewById(R.id.btn_brightless_2);
		backBtn = (Button) findViewById(R.id.btn_brightless_3);
		
		proBar = (ProgressBar) findViewById(R.id.bright_less_probar);
		
		passBtn.setOnClickListener(this);
		failBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		mHandler.sendMessage(mHandler.obtainMessage(SET_BRIGHTNESS, 0, 0));
		
		mScreenBrightnessDim = 20;
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_brightless_1:
			config.setBRIGHTNESSTestOk(1);
			break;
		case R.id.btn_brightless_2:
			config.setBRIGHTNESSTestOk(2);
			break;
		case R.id.btn_brightless_3:
			config.setBRIGHTNESSTestOk(0);
			break;
		default:
			break;
		}
		finish();
	}
	
	private  Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case SET_BRIGHTNESS:
				proBar.setProgress(brightness);
				Log.e("dd", brightness+"");
				
				WindowManager.LayoutParams lp = getWindow().getAttributes();   
			    lp.screenBrightness = (brightness+mScreenBrightnessDim)/255f;   
			    getWindow().setAttributes(lp);
				
				if(brightness == 235){
					direction = -1;
				}else if(brightness == 0){
					direction = 1;				
				}
				brightness =brightness+direction;
				sendMessageDelayed(obtainMessage(SET_BRIGHTNESS, brightness, 0), 5);
				break;
			default:
				break;
			}
		}
		
		
	};

	@Override
	protected void onPause() {
		mHandler.removeMessages(SET_BRIGHTNESS);
		super.onPause();
	}

}
