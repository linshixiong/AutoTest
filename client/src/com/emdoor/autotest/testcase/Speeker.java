package com.emdoor.autotest.testcase;


import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Speeker extends Activity implements OnClickListener {

	Button[] btns = new Button[5];
	int[] ids = {R.id.left_control,R.id.right_control,R.id.btn_speeker_1,
			     R.id.btn_speeker_2,R.id.btn_speeker_3};
	Configuration config;
	
	boolean left_enable = true,right_enable = true;
	
	private MediaPlayer mp;   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speeker);
		
		for(int i = 0 ; i < 5; i ++){
			btns[i] = (Button) findViewById(ids[i]);
			btns[i].setOnClickListener(this);
		}
		mp = MediaPlayer.create(this,R.raw.speeker_test);
		config = new Configuration(this);
		mp.start();
		mp.setLooping(true);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_speeker_1:
			config.setSPEAKERTestOk(1);
			finish();
			break;
		case R.id.btn_speeker_2:
			config.setSPEAKERTestOk(2);
			finish();
			break;
		case R.id.btn_speeker_3:
			config.setSPEAKERTestOk(0);
			finish();
			break;
		case R.id.left_control:
			if(left_enable){
				left_enable = false;
				btns[0].setText(getString(R.string.left_speeker_disable));
				mp.setVolume(0.0f, right_enable?1.0f:0.0f);
				
			}else{
				left_enable = true;
				btns[0].setText(getString(R.string.left_speeker_enable));
				mp.setVolume(1.0f, right_enable?1.0f:0.0f);
			}
			
			break;
		case R.id.right_control:
			if(right_enable){
				right_enable = false;
				btns[1].setText(getString(R.string.right_speeker_disable));
				mp.setVolume(left_enable?1.0f:0.0f, 0.0f);
			}else{
				right_enable = true;
				btns[1].setText(getString(R.string.right_speeker_enable));
				mp.setVolume(left_enable?1.0f:0.0f, 1.0f);
			}
			
			break;
		default:
			break;
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mp.isPlaying()){
			mp.stop();
		}
	}
	
	
	
}
