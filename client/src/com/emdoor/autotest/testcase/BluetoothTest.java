package com.emdoor.autotest.testcase;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.res.Resources;


public class BluetoothTest extends Activity implements Runnable {

	private Button btCheckOk = null;
	private Button btCheckFail = null;
	private Button btCheckBack = null;
	private TextView notifaction;
	private boolean isStop = false;
	private Configuration config;
	public ProgressBar progressBar;
	private String notifaction_str;
	int count = 0;
	IntentFilter filter;
	
	BluetoothAdapter adapter;

	private static final int MESSAGETYPE_01 = 0x0002;
	private static final int MESSAGETYPE_02 = 0x0004;
	private static final int MESSAGETYPE_03 = 0x0006;
	Thread T1 = null;
	String tmpS = "";
	ContentResolver cv;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("action" , action);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Message msg_listData = new Message();
				msg_listData.what = MESSAGETYPE_03;
				handler.sendMessage(msg_listData);
				BluetoothTest.this.finish();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.bluetooth);
		
		btCheckOk = (Button)findViewById(R.id.btnbtok);
		btCheckFail = (Button)findViewById(R.id.btnbtfail);
		btCheckBack = (Button)findViewById(R.id.btnbtback);
		notifaction = (TextView)findViewById(R.id.bt_notify);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		notifaction_str = "no bluetooth device found!";
		
		config = new Configuration(this);
		filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);  
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);  
		filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);  
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);  
		
		
		btCheckOk.setVisibility(btCheckOk.INVISIBLE);
		
		btCheckOk.setOnClickListener(linstener);
		btCheckFail.setOnClickListener(linstener);
		btCheckBack.setOnClickListener(linstener);
		
	    adapter= BluetoothAdapter.getDefaultAdapter();    
	   
		
		T1 = new Thread(this);
		T1.start();
		

	}
	
	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btnbtok:
				config.setBLUETOOTHTestOk(1);
				break;
			case R.id.btnbtfail:
				config.setBLUETOOTHTestOk(2);
				break;
			case R.id.btnbtback:
				config.setBLUETOOTHTestOk(0);
				break;
			default:
				config.setBLUETOOTHTestOk(0);
				break;
			}
            isStop = true;
			BluetoothTest.this.finish();
		}
		
		
	};


	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGETYPE_01:
				notifaction.setText(notifaction_str);
				config.setBLUETOOTHTestOk(2);
				break;
			case MESSAGETYPE_02:
				notifaction.setText("pass");
				config.setBLUETOOTHTestOk(1);
				isStop = true;	
				break;
			case MESSAGETYPE_03:
				notifaction.setText("ERROR!find bluetooth device time out!");
				config.setBLUETOOTHTestOk(2);
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
	
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted() && !isStop) {
			try {
				if(adapter == null)
				{
					Message msg_listData = new Message();
					msg_listData.what = MESSAGETYPE_01;
					handler.sendMessage(msg_listData);
					Thread.sleep(5000);
					BluetoothTest.this.finish();
				}else{
					if(!adapter.isEnabled()){  
						//Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        //startActivity(intent);	
                        adapter.enable();
                        Thread.sleep(3000);
                         
                    }		
					
			
					Thread.sleep(3000);
                    if((count++) > 20 && adapter.getAddress().equals("00:00:00:00:00:00")){
                    	Message msg_listData = new Message();
    					msg_listData.what = MESSAGETYPE_03;
    					handler.sendMessage(msg_listData);
    					BluetoothTest.this.finish();
                    }else if(adapter.getAddress() != null  && !adapter.getAddress().equals("00:00:00:00:00:00")){
                    	Message msg_listData = new Message();
    					msg_listData.what = MESSAGETYPE_02;
    					handler.sendMessage(msg_listData);
    					BluetoothTest.this.finish();
                    }
				}	
			}catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub		
		super.finish();		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Log.d("fdsa" , "fdasfas");
		//registerReceiver(mReceiver, filter);
	}
}
