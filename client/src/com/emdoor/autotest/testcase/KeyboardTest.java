package com.emdoor.autotest.testcase;

import java.io.FileOutputStream;
import java.io.PrintStream;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.R.color;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.content.res.Resources;

public class KeyboardTest extends Activity {

	Button keyboardCheckOk, keyboardCheckFail ,keyboardCheckBack ,volup ,voldown;
	Button restwheel;
	int i = 0;
	Boolean isclick1 = false, isclick2 = false;
	private Configuration config;
	


	@Override
	public void onCreate(Bundle savedinstanceState) {

		super.onCreate(savedinstanceState);

		setContentView(R.layout.keyboard);

		keyboardCheckOk = (Button)findViewById(R.id.btnkeyboardok);
		keyboardCheckFail = (Button)findViewById(R.id.btnkeyboardfail);
		keyboardCheckBack = (Button)findViewById(R.id.btnkeyboardback);
		volup = (Button)findViewById(R.id.volumeup);
		voldown = (Button)findViewById(R.id.volumedown);
		config = new Configuration(this);
		
		keyboardCheckOk.setOnClickListener(linstener);
		keyboardCheckFail.setOnClickListener(linstener);
		keyboardCheckBack.setOnClickListener(linstener);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		while (true) {
			switch (keyCode) {	
			case KeyEvent.KEYCODE_VOLUME_UP:
				 volup.setBackgroundColor(color.background_light);
				 break;	
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				 voldown.setBackgroundColor(color.background_light);
				 break;					
			}
			return true;
		}
		
	}
	
	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btnkeyboardok:
				config.setKEYBOARDTestOk(1);
				break;
			case R.id.btnkeyboardfail:
				config.setKEYBOARDTestOk(2);
				break;
			case R.id.btnkeyboardback:
				config.setKEYBOARDTestOk(0);
				break;
			default:
				config.setKEYBOARDTestOk(0);
				break;
			}
			KeyboardTest.this.finish();
		}
		
		
	};	
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		while (true) {
			switch (keyCode) {	
			case KeyEvent.KEYCODE_VOLUME_UP:
				 volup.setBackgroundColor(Color.GREEN);
				 if(!isclick1){
					 i++;
					 if(i==2){
						 config.setKEYBOARDTestOk(1);
						 KeyboardTest.this.finish();
					 }
				 }
				 isclick1=true;
				 break;	
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				 voldown.setBackgroundColor(Color.GREEN);
				 if(!isclick2){
					 i++;
					 if(i==2){
						 config.setKEYBOARDTestOk(1);
						 KeyboardTest.this.finish();
					 }
				 }
				 isclick2=true;
				 break;			
			}
			return true;
		}
	}

	public void onDestory() {
		System.gc();
		super.onDestroy();
	}
}