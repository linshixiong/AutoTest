package com.emdoor.autotest;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnCancelListener {
	protected static final String TAG = "MainActivity";
	private WifiHelper mWifiHelper;

	private TextView mTextOutput;
	private ScrollView mScrollView;
	private ProgressDialog progress;
	private KeyguardManager keyguardManager;
	private KeyguardLock keyguardLock;
	private boolean isTargetAPExist;
	private TCPClient client;
	private boolean isTargetWifiConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		this.registerReceiver(wifiBroadcastReceiver, filter);
		mWifiHelper = WifiHelper.getInstance(this);
		mTextOutput = (TextView) findViewById(R.id.text_output);
		mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
		keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyguardLock = keyguardManager.newKeyguardLock(TAG);
		progress = new ProgressDialog(this);
		progress.setTitle("正在连接网络");
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(true);
		progress.setOnCancelListener(this);
		this.connectWifi();
	}

	@Override
	protected void onResume() {

		keyguardLock.disableKeyguard();

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(wifiBroadcastReceiver);

		// keyguardLock.reenableKeyguard();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_connect:
			this.connectWifi();

			// mWifiHelper.scanAPList();
			// connectNewAP();
			
			break;
		case R.id.menu_clean:
			mTextOutput.setText("");
			break;
		case R.id.menu_settings:
			Intent intent = new Intent();
			intent.setClass(this, BlankActivity.class);
			intent.putExtra("background_color", Color.BLACK);

			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void showInstallProgress() {

		progress.show();
	}

	@Override
	public void onCancel(DialogInterface arg0) {

		this.finish();
	}

	private void connectWifi() {

		if (!mWifiHelper.isWifiEnabled()) {
			progress.setTitle("正在打开WFI");
			progress.show();
			mWifiHelper.turnOnWifi();
			return;
		}
		 isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
		if (isTargetWifiConnected) {
			progress.dismiss();
			connectServer();
			return;
		}
		isTargetAPExist = mWifiHelper.isTargetAPExist();
		Log.d(TAG,"isTargetAPExist:"+isTargetAPExist);
		progress.show();
		if (!isTargetAPExist) {
			mWifiHelper.scanAPList();
			return;
		}
		if (mWifiHelper.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
			progress.setTitle("正在连接到指定热点");

			mWifiHelper.connectWifi();
		}
	}

	private void connectServer() {
		if (client == null) {
			client = new TCPClient("192.168.1.17", 8080,handler);

		
		}
		

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case Messages.MSG_WIFI_ENABLED:

				break;
			case Messages.MSG_CMD_RECEIVE:
				mTextOutput.append("收到消息："+msg.obj.toString()+"\n");
				mScrollView.scrollTo(0, mScrollView.getBottom());
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	private BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent
					.getAction())) {
				isTargetAPExist = mWifiHelper.isTargetAPExist();
				isTargetWifiConnected=mWifiHelper.isTargetWifiConnected();
				if(isTargetAPExist&&isTargetWifiConnected){
					//MainActivity.this.connectWifi();
					progress.dismiss();
					
				}

			} else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {

				if (mWifiHelper.getWifiManager().isWifiEnabled()) {

					isTargetWifiConnected=mWifiHelper.isTargetWifiConnected();
					Log.d(TAG, "WIFI_STATE_ENABLED,isTargetWifiConnected="+isTargetWifiConnected);
					if(isTargetWifiConnected){
						progress.dismiss();
						
					}
					//MainActivity.this.connectWifi();
				}
			}

		}

	};

}
