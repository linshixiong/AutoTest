package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ThreeGTest extends Activity {
	
	
	private Button tgCheckOk = null;
	private Button tgCheckFail = null;
	private Button tgCheckBack = null;
	private Configuration config;
	private TextView textView_threeg_msg = null;
	
	
	IntentFilter mFilter;
	private ConnectivityManager connectivityManager;
	private NetworkInfo networkInfo;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo = connectivityManager.getActiveNetworkInfo();
				
				analyseNetwork(networkInfo);

				
			}
		}		
	};
	
	private void analyseNetwork(NetworkInfo networkInfo) {
		// TODO Auto-generated method stub
		if(networkInfo != null){
			String name = networkInfo.getTypeName();
			String state = networkInfo.getState().toString();
			String reason = networkInfo.getReason().toString();
			String extra = networkInfo.getExtraInfo().toString();
			// type: mobile[UMTS], state: CONNECTED/CONNECTED, reason: dataEnabled, extra: 3gnet, roaming: false, failover: true, isAvailable: true
			textView_threeg_msg.setText("[network name = " + name + "],[network state = " 
					+ state + "],[network reason = " + reason + "],[network extra = " + extra + "]");
		    if (name.equals("mobile") && state.equals("CONNECTED") && reason.equals("simLoaded")
		    		&& extra.equals("3gnet")){
		    	Message msg_listData = new Message();
				msg_listData.what = 1;
				handler.sendMessageDelayed(msg_listData, 3000);
		    }
		
		}else{
			
			textView_threeg_msg.setText(R.string.threegtestinfo);
		}
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			threeGTestOK();
			super.handleMessage(message);
			
		}
	};


	
	private void threeGTestOK() {
		// TODO Auto-generated method stub
		config.setEVDOTestOk(1);
		//unregisterReceiver(mReceiver);
		ThreeGTest.this.finish();		
		
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.threeg);
		
		tgCheckOk = (Button)findViewById(R.id.btn_threeg_1);
		tgCheckFail = (Button)findViewById(R.id.btn_threeg_2);
		tgCheckBack = (Button)findViewById(R.id.btn_threeg_3);
		textView_threeg_msg = (TextView)findViewById(R.id.textView_threeg_msg);
		config = new Configuration(this);		       		
		
		tgCheckOk.setVisibility(tgCheckOk.INVISIBLE);
		
		tgCheckOk.setOnClickListener(linstener1);
		tgCheckFail.setOnClickListener(linstener1);
		tgCheckBack.setOnClickListener(linstener1);	
		NetworkInfo networkInfo = null;
		analyseNetwork(networkInfo);

	}
	
	public void onResume(){	
		super.onResume();
		mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
		registerReceiver(mReceiver, mFilter);		
	}
	

	private final OnClickListener linstener1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btn_threeg_1:
				config.setEVDOTestOk(1);
				break;
			case R.id.btn_threeg_2:
				config.setEVDOTestOk(2);
				break;
			case R.id.btn_threeg_3:
				config.setEVDOTestOk(0);
				break;
			default:
				config.setEVDOTestOk(0);
				break;
			}
			//unregisterReceiver(mReceiver);
			ThreeGTest.this.finish();
		}
		
		
	};	
	
	public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }



}
