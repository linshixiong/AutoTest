package com.emdoor.autotest;

import java.util.HashMap;
import java.util.List;

import javax.xml.transform.Result;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	private WifiHelper mWifiHelper;
	private HashMap<String, AccessPoint> apMap;
	private TextView mTextOutput;
	private ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		this.registerReceiver(wifiBroadcastReceiver, filter);
		mWifiHelper = WifiHelper.getInstance(this);
		mTextOutput = (TextView) findViewById(R.id.text_output);
		mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
		apMap = new HashMap<String, AccessPoint>();
		/*
		 * if (!mWifiHelper.isWifiEnabled()) { mWifiHelper.turnOnWifi(); } else
		 * { mWifiHelper.scanAPList(); }
		 */

	}

	@Override
	protected void onResume() {
		View v = findViewById(R.id.status_image);
		// v.setAlpha(1);
		/*
		 * int titleId = Resources.getSystem().getIdentifier(
		 * "action_bar_title", "id", "android"); TextView yourTextView =
		 * (TextView) findViewById(titleId); yourTextView.setText("hello");
		 * yourTextView.setTextColor(Color.RED);
		 */
		super.onResume();
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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_connect:
			connect();
			// mScrollView.scrollTo(0, mScrollView.getBottom());
			break;
		case R.id.menu_clean:
			mTextOutput.setText("");
			break;
		case R.id.menu_settings:
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void connect() {
		AccessPoint accessPoint = apMap.get("emdoor_soft");

		if (accessPoint == null) {
			return;
		}

		WifiConfiguration config = accessPoint.getConfig();
		if (config == null) {
			config=new WifiConfiguration();
		}
		config.SSID="\"emdoor_soft\"";
		Log.d(TAG, "connecting to emdoor_soft");


		String password = "\"emdoor1234567890\"";
		
		config.preSharedKey = password;
		config.hiddenSSID = true;

		config.status = WifiConfiguration.Status.ENABLED;

		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		int netId= mWifiHelper.getWifiManager().addNetwork(config);
		Log.d(TAG, "add network id="+netId);
		boolean success = mWifiHelper.getWifiManager().enableNetwork(
				netId, true);
		Log.d(TAG, "success=" + success);

	}

	private BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent
					.getAction())) {

				List<ScanResult> results = mWifiHelper.getScanResultList();

				for (ScanResult scanResult : results) {
					AccessPoint accessPoint = new AccessPoint(context,
							scanResult);
					apMap.put(accessPoint.ssid, accessPoint);

				}

				List<WifiConfiguration> configs = mWifiHelper
						.getConfiguredNetworks();

				for (WifiConfiguration config : configs) {
					AccessPoint accessPoint = new AccessPoint(context, config);

					apMap.put(accessPoint.ssid, accessPoint);
				}

			}

		}

	};

}
