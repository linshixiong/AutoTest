package com.emdoor.autotest.testcase;

import java.io.FileOutputStream;
import java.io.PrintStream;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Resources;


public class wifi extends Activity implements Runnable {

	private Button wifiCheckOk = null;
	private Button wifiCheckFail = null;
	private Button wifiCheckBack = null;
	private TextView notifaction , messageinfo;
	private boolean isStop = false;
	private boolean wifiCheck = false;
	private Resources res;
	private WifiUtil wifiUtil;
	private Configuration config;
	public StringBuilder sb;
	private String prompt;

	private static final int MESSAGETYPE_01 = 0x0002;
	private static final int MESSAGETYPE_02 = 0x0004;
	Thread T1 = null;
	String tmpS = "";
	ContentResolver cv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wifi);
		
		notifaction = (TextView)findViewById(R.id.noti);
		messageinfo = (TextView)findViewById(R.id.wifiinfo);
		wifiCheckOk = (Button)findViewById(R.id.btnwifiok);
		wifiCheckOk.setVisibility(wifiCheckOk.INVISIBLE);
		wifiCheckFail = (Button)findViewById(R.id.btnwififail);
		wifiCheckBack = (Button)findViewById(R.id.btnwifiback);
		prompt = "please click back -- conn wifi -- test again";
		
		wifiCheckOk.setOnClickListener(linstener);
		wifiCheckFail.setOnClickListener(linstener);
		wifiCheckBack.setOnClickListener(linstener);
		
		config = new Configuration(this);

		wifiUtil = new WifiUtil(this);
		
		T1 = new Thread(this);
		T1.start();
		

	}
	
	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btnwifiok:
				config.setWIFITestOk(1);
				break;
			case R.id.btnwififail:
				config.setWIFITestOk(2);
				break;
			case R.id.btnwifiback:
				config.setWIFITestOk(0);
				break;
			default:
				config.setWIFITestOk(0);
				break;
			}
			isStop = true;
			wifi.this.finish();
		}
		
		
	};	


	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGETYPE_01:
				messageinfo.setText(sb);
				notifaction.setText(prompt);
				break;
			case MESSAGETYPE_02:
				notifaction.setText("pass");
				config.setWIFITestOk(1);
				isStop = true;	
				break;
			}			
			super.handleMessage(message);
			
		}
	};

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}
	
	public void checkWifi(){
		wifiUtil.startScan();		
		sb = wifiUtil.lookUpScan();	
		if(sb.length() > 0){
			wifiCheck = true;
		}
		
	}
	
	public void connDefWifi(){
		for (int i=0;i<wifiUtil.getConfiguration().size();i++)
		{
			wifiUtil.connectConfiguration(i);
		}
		
	}

	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && !isStop) {
			try {
				
				Log.d("wifi" , "isconnect" + wifiUtil.isWifiConnect());

				if (wifiUtil.getWifiList()!=null&&wifiUtil.getWifiList().size()>0) {									
					Message msg_listData = new Message();
					msg_listData.what = MESSAGETYPE_02;
					handler.sendMessage(msg_listData);
					Thread.sleep(2000);
					wifi.this.finish();
				} else {
					if (!wifiUtil.isWifiOpen()) {
						wifiUtil.openWifi();
						Thread.sleep(6000);
						if (!wifiUtil.isWifiOpen()) {
							isStop = true;
							config.setWIFITestOk(2);
							wifi.this.finish();
						}						
					}
					
					checkWifi();
					
					if(wifiCheck){
					Message msg_listData = new Message();
					msg_listData.what = MESSAGETYPE_01;
					handler.sendMessage(msg_listData);
					}					
	
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub		
		super.finish();
	}
}
