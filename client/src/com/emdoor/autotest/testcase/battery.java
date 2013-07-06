package com.emdoor.autotest.testcase;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import com.emdoor.autotest.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class battery extends Activity {

	protected static final String TAG = "battery";

	private Button batteryCheckOk = null;
	private Button batteryCheckFail = null;
	private Button batteryCheckBack = null;
	private Configuration config;

	private Boolean isCharge = false;
	private Timer mTimer;
	/* BroadcastReceiver */
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 0);
				int plugged = intent.getIntExtra("plugged", 1);
				int voltage = intent.getIntExtra("voltage", 0);

				// Log.v("status", String.valueOf(status));
				// Log.v("health", String.valueOf(health));
				// Log.v("present", String.valueOf(present));
				// Log.v("level", String.valueOf(level));
				// Log.v("scale", String.valueOf(scale));
				// Log.v("voltage", String.valueOf(voltage));
				onBatteryInfoReceiver(level, voltage, scale, plugged);
			}

		}
	};

	@Override
	protected void onResume() {

		super.onResume();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.battery_info);

		registerReceiver(mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		batteryCheckOk = (Button) findViewById(R.id.btnbatteryok);
		batteryCheckFail = (Button) findViewById(R.id.btnbatteryfail);
		batteryCheckBack = (Button) findViewById(R.id.btnbatteryback);

		config = new Configuration(this);

		batteryCheckOk.setOnClickListener(linstener);
		batteryCheckFail.setOnClickListener(linstener);
		batteryCheckBack.setOnClickListener(linstener);
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}

	private void updateCurrntNow() {
		((TextView) findViewById(R.id.current_count)).setText(getCurrentNow()
				+ "mA");
	}

	public void onBatteryInfoReceiver(int intLevel, int voltage, int current,
			int plugged) {

		((TextView) findViewById(R.id.voltage_count)).setText(voltage + "mV");
		((TextView) findViewById(R.id.current_count)).setText(getCurrentNow()
				+ "mA");
		((TextView) findViewById(R.id.capacity_count)).setText(getEnergyFullDesign()+"mAh");
		((TextView) findViewById(R.id.percent_count)).setText(intLevel + "%");
		// ((TextView)
		// findViewById(R.id.capacity_count)).setText(getEnergyFullDesign());
		if (plugged == 0) {
			if (isCharge) {
				((TextView) findViewById(R.id.plug_state)).setText("unpluging");
				((TextView) findViewById(R.id.charging_state))
						.setText("Discharging");

			} else {
				((TextView) findViewById(R.id.plug_state))
						.setText("is pluging");
				((TextView) findViewById(R.id.charging_state))
						.setText("Charging");
			}
		}
		if (plugged == 1) {
			isCharge = true;
			((TextView) findViewById(R.id.charging_state)).setText("Charging");
			((TextView) findViewById(R.id.plug_state)).setText("pluging");
		} else {
			((TextView) findViewById(R.id.charging_state))
					.setText("Discharging");
			((TextView) findViewById(R.id.plug_state)).setText("unpluging");
		}

	}

	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnbatteryok:
				config.setBATTERYTestOk(1);
				break;
			case R.id.btnbatteryfail:
				config.setBATTERYTestOk(2);
				break;
			case R.id.btnbatteryback:
				config.setBATTERYTestOk(0);
				break;
			default:
				config.setBATTERYTestOk(0);
				break;
			}
			battery.this.finish();
		}

	};

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateCurrntNow();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private String getEnergyFullDesign() {
		File file = new File(
				"sys/class/power_supply/battery/energy_full_design");
		return Utils.readTextFromFile(file);
	}

	private int getCurrentNow() {
		File file = new File("sys/class/power_supply/battery/current_now");
		int currentNow = Integer.parseInt(Utils.readTextFromFile(file));
		return currentNow / 1000;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBatInfoReceiver);
		System.gc();
		mTimer.cancel();
		super.onDestroy();
	}
}