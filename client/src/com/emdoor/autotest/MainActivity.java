package com.emdoor.autotest;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	protected static final String TAG = "MainActivity";
	private WifiHelper mWifiHelper;
	private boolean isTargetAPExist;
	private boolean isTargetWifiConnected;
	private LinearLayout progressLayout;
	private LinearLayout operateLayout;
	private Button button;
	private TextView textStatus;
	private Menu menu;
	private ConnectivityManager cm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressLayout = (LinearLayout) findViewById(R.id.progress_panel);
		operateLayout = (LinearLayout) findViewById(R.id.operate_panel);
		button = (Button) findViewById(R.id.button);
		textStatus = (TextView) findViewById(R.id.statusText);
		button.setOnClickListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(wifiBroadcastReceiver, filter);
		mWifiHelper = WifiHelper.getInstance(this);
		cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();

		progressLayout.setVisibility(isTargetWifiConnected ? View.GONE
				: View.VISIBLE);
		operateLayout.setVisibility(isTargetWifiConnected ? View.VISIBLE
				: View.GONE);
		if (!isTargetWifiConnected) {
			this.connectWifi();
		} else {
			showButton();
		}
	}

	@Override
	protected void onResume() {
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
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_disconnect:

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

	private void connectWifi() {

		if (!mWifiHelper.isWifiEnabled()) {

			mWifiHelper.turnOnWifi();
			Log.d(TAG, "turn on wifi");
			return;
		}
		isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
		if (isTargetWifiConnected) {

			return;
		}
		isTargetAPExist = mWifiHelper.isTargetAPExist();
		Log.d(TAG, "isTargetAPExist:" + isTargetAPExist);

		if (!isTargetAPExist) {
			mWifiHelper.scanAPList();
			return;
		}
		if (mWifiHelper.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {

			mWifiHelper.connectWifi();
		}
	}

	private BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent
					.getAction())) {
				isTargetAPExist = mWifiHelper.isTargetAPExist();
				Log.d(TAG,"SCAN RESULTS AVAILABLE,isTargetAPExist:"+isTargetAPExist);
				if (isTargetAPExist) {

					isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
					if (!isTargetWifiConnected) {
						connectWifi();

					}
				}

			}  else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {

				NetworkInfo wifi = cm
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (wifi != null && wifi.isConnected()) {
					isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
					Log.d(TAG, "WIFI_STATE_ENABLED,isTargetWifiConnected="
							+ isTargetWifiConnected);
					if (isTargetWifiConnected) {
						showButton();
					}
				}
			}

		}

	};

	private void showButton() {
		operateLayout.setVisibility(View.VISIBLE);
		progressLayout.setVisibility(View.GONE);
		textStatus.setText("");
		textStatus.append("ÍøÂçÃû: " + getString(R.string.def_wifi_ssid));
		textStatus.append("\n");
		textStatus.append("·þÎñÆ÷: " + getString(R.string.def_server_host) + ":"
				+ getResources().getInteger(R.integer.def_server_port));
	}

	@Override
	public void onClick(View v) {
		// connectServer();

	}

}
