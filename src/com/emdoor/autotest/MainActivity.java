package com.emdoor.autotest;

import java.util.List;

import javax.xml.transform.Result;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	private  WifiHelper mWifiHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntentFilter filter=new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		this.registerReceiver(wifiBroadcastReceiver, filter);
		mWifiHelper=WifiHelper.getInstance(this);
		if(mWifiHelper.isWifiEnabled())
		{
			mWifiHelper.turnOnWifi();
			
		}else{
			mWifiHelper.scanAPList();
		}
	}

	
	
	@Override
	protected void onDestroy() {
		this.unregisterReceiver(wifiBroadcastReceiver);
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    private BroadcastReceiver wifiBroadcastReceiver=new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
				
				 List<ScanResult> results=mWifiHelper.getScanResultList();
				
				 for (ScanResult scanResult : results) {
					AccessPoint accessPoint=new AccessPoint(context,scanResult);
					 
					 //int security=AccessPoint.getSecurity(scanResult); 
					//Log.d(TAG, "scanResult,SSID="+scanResult.SSID+",security="+security+",id="+scanResult.capabilities);
				}
				 
				 List<WifiConfiguration> configs=mWifiHelper.getConfiguredNetworks();
				 
				 for (WifiConfiguration config : configs) {
					AccessPoint accessPoint=new AccessPoint(context, config);
				}
				 
			}
			
		}
    	
    };

}
