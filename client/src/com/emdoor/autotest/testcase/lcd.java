package com.emdoor.autotest.testcase;

import java.io.DataInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.InternalAPI;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class lcd extends Activity implements Runnable {

	String TAG = "LCD";
	LinearLayout LinearLayout01 = null;
	private static final int MESSAGETYPE_01 = 0x0001;
	private boolean isStop = false;
	TextView tv01 ,lcdinfo;
	Button btnlcdok, btnlcdng , btnlcdbk;
	int i = 0;
	int j = 0;
	int flag;
	Thread T1 = null;
	Configuration config;
	
	@Override
	public void onCreate(Bundle savedinstanceState) {
		super.onCreate(savedinstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
				
		setContentView(R.layout.lcd);

		btnlcdok = (Button) findViewById(R.id.btn_lcd_1);
		btnlcdng = (Button) findViewById(R.id.btn_lcd_2);
		btnlcdbk = (Button) findViewById(R.id.btn_lcd_3);
		
		config = new Configuration(this);		

		btnlcdok.setOnClickListener(listener);	
		btnlcdng.setOnClickListener(listener);
		btnlcdbk.setOnClickListener(listener);
		
		tv01 = (TextView) findViewById(R.id.TextView_LCD_01);		
			
		lcdinfo = (TextView) findViewById(R.id.lcdinfo);

		LinearLayout01 = (LinearLayout) findViewById(R.id.screen_lcd);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		LinearLayout01 = (LinearLayout) findViewById(R.id.screen_lcd);

		LinearLayout01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("fdsaf" , ""+j);
				changeSreen();
				
			}
		});

		//T1 = new Thread(this);
		//T1.start();

	}	
	
	private final Button.OnClickListener listener = new Button.OnClickListener() {
		public void onClick(View args0) {

			/*
			 * FileOutputStream out; // declare a // file PrintStream p; //
			 * declare a print
			 * 
			 * try { out = new FileOutputStream("/tmp/lcd01.txt"); p = new
			 * PrintStream(out); p.println("OK"); p.close(); } catch (Exception
			 * e) { e.printStackTrace(); }
			 */
			if(args0.getId() == R.id.btn_lcd_1){
				config.setLCDTestOk(1);
			}
			if(args0.getId() == R.id.btn_lcd_2){
				config.setLCDTestOk(2);
			}
			if(args0.getId() == R.id.btn_lcd_3){
				config.setLCDTestOk(0);
			}

			lcd.this.finish();

		}
	};

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

	void downData() {
		// if (flag == 1)
		i++;
		tv01 = (TextView) findViewById(R.id.TextView_LCD_01);
		LinearLayout01 = (LinearLayout) findViewById(R.id.LinearLayout01);
		Log.i(TAG, "downData is running (" + i + ") ");
		switch (i) {
		case 1:
			LinearLayout01.setBackgroundColor(Color.RED);			
			break;
		case 2:
			LinearLayout01.setBackgroundColor(Color.GREEN);
			break;
		case 3:
			LinearLayout01.setBackgroundColor(Color.BLUE);
			break;
		case 4:
			LinearLayout01.setBackgroundColor(Color.WHITE);
			break;
		case 5:
			LinearLayout01.setBackgroundColor(Color.BLACK);
			//writeSysfs(OSD_BLANK_PATH, "0");
			break;
		default:
			break;
		}
		if (i == 6) {
			isStop = true;
			flag = 3;
			btnlcdok.setVisibility(btnlcdok.VISIBLE);
			btnlcdng.setVisibility(btnlcdok.VISIBLE);
			btnlcdbk.setVisibility(btnlcdok.VISIBLE);
			tv01.setVisibility(btnlcdok.VISIBLE);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGETYPE_01:
				downData();
				break;
			}
			super.handleMessage(message);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (flag == 2)
			i++;
		if (i == 6) {
			flag = 3;
			btnlcdok.setVisibility(btnlcdok.VISIBLE);
			btnlcdng.setVisibility(btnlcdok.VISIBLE);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// isStop = true;

		// T1.stop();

		// T1 = null;

		handler = null;

		i = 0;

		System.gc();
		
		
		/*
		if(SystemProperties.getBoolean("mbx.hideStatusBar.enable",false)!=false)
			InternalAPI.setProperty(null,"mbx.hideStatusBar.enable","false");
		*/
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onPause();
		/*
		if(SystemProperties.getBoolean("mbx.hideStatusBar.enable",false)!=true)
			SystemProperties.set("mbx.hideStatusBar.enable","true");*/	
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();/*
		if(SystemProperties.getBoolean("mbx.hideStatusBar.enable",false)!=false)
			SystemProperties.set("mbx.hideStatusBar.enable","false");*/
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	
	public void changeSreen() {
		// TODO Auto-generated method stub
		j++;
		j = j % 6; 
		if(j != 0){
			btnlcdok.setVisibility(btnlcdok.INVISIBLE);
			btnlcdng.setVisibility(btnlcdok.INVISIBLE);
			btnlcdbk.setVisibility(btnlcdok.INVISIBLE);
			tv01.setVisibility(btnlcdok.INVISIBLE);
			lcdinfo.setVisibility(btnlcdok.INVISIBLE);
		}
		switch (j) {
		case 1:
			LinearLayout01.setBackgroundColor(Color.RED);			
			break;
		case 2:
			LinearLayout01.setBackgroundColor(Color.GREEN);
			break;
		case 3:
			LinearLayout01.setBackgroundColor(Color.BLUE);
			break;
		case 4:
			LinearLayout01.setBackgroundColor(Color.WHITE);
			break;
		case 5:
			LinearLayout01.setBackgroundColor(Color.BLACK);
			btnlcdok.setVisibility(btnlcdok.VISIBLE);
			btnlcdng.setVisibility(btnlcdok.VISIBLE);
			btnlcdbk.setVisibility(btnlcdok.VISIBLE);
			tv01.setVisibility(btnlcdok.VISIBLE);
			lcdinfo.setVisibility(btnlcdok.VISIBLE);
			break;
		default:
			break;
		}
		
		
	}


}
