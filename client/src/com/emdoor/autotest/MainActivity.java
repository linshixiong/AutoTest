package com.emdoor.autotest;

import java.text.SimpleDateFormat;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		SensorListener {
	protected static final String TAG = "MainActivity";
	private WifiHelper mWifiHelper;
	private SensorManager sm;
	private boolean isTargetAPExist;
	private TCPClient client;
	private boolean isTargetWifiConnected;
	private LinearLayout progressLayout;
	private LinearLayout operateLayout;
	private Button button;
	private TextView textStatus;
	private Menu menu;

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
		this.registerReceiver(wifiBroadcastReceiver, filter);
		mWifiHelper = WifiHelper.getInstance(this);
		// sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

		// int sensorType = Sensor.TYPE_ACCELEROMETER;
		isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();

		progressLayout.setVisibility(isTargetWifiConnected ? View.GONE
				: View.VISIBLE);
		operateLayout.setVisibility(isTargetWifiConnected ? View.VISIBLE
				: View.GONE);
		if (!isTargetWifiConnected) {
			this.connectWifi();
		} else {
			textStatus.setText("");
			textStatus.append("网络名: " + getString(R.string.def_wifi_ssid));
			textStatus.append("\n");
			textStatus.append("服务器: " + getString(R.string.def_server_host)
					+ ":"
					+ getResources().getInteger(R.integer.def_server_port));
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

	private void connectServer() {
		if (client == null || !client.isConnected()) {
			String host = getString(R.string.def_server_host);
			int port = getResources().getInteger(R.integer.def_server_port);
			client = new TCPClient(host, port, this, handler);

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case Messages.MSG_WIFI_ENABLED:

				break;
			case Messages.MSG_CONNECT_SUCCUSS:
				button.setText("正在测试");
				button.setEnabled(false);
				menu.getItem(0).setVisible(true);
				break;
			case Messages.MSG_CMD_RECEIVE:
				String cmd = msg.obj.toString();
				byte[] data = Commands.getInstance(MainActivity.this).excute(
						cmd);
				if(data!=null){
					Log.d(TAG, "command excute result=" + data.length);
					client.WriteByteArray(data);
				}
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
				isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
				if (isTargetAPExist && isTargetWifiConnected) {
					// MainActivity.this.connectWifi();

				}

			} else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {

				if (mWifiHelper.getWifiManager().isWifiEnabled()) {

					isTargetWifiConnected = mWifiHelper.isTargetWifiConnected();
					Log.d(TAG, "WIFI_STATE_ENABLED,isTargetWifiConnected="
							+ isTargetWifiConnected);
					if (isTargetWifiConnected) {

					}
					// MainActivity.this.connectWifi();
				}
			}

		}

	};



	@Override
	public void onClick(View v) {
		connectServer();

	}

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub

	}

}
