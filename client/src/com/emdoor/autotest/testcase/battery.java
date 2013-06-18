package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class battery extends Activity {

	private int intLevel;
	private int intScale;
	private int plugged;
	private Button batteryCheckOk = null;
	private Button batteryCheckFail = null;
	private Button batteryCheckBack = null;
	private Configuration config;
	
	private Boolean isCharge = false;

	/* BroadcastReceiver */
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				intLevel = intent.getIntExtra("level", 0);
				intScale = intent.getIntExtra("scale", 100);
				plugged = intent.getIntExtra("plugged", 1);
				onBatteryInfoReceiver(intLevel, intScale, plugged);
			}

		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		

		setContentView(R.layout.battery_info);
		//PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		

		registerReceiver(mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		batteryCheckOk = (Button) findViewById(R.id.btnbatteryok);
		batteryCheckFail = (Button) findViewById(R.id.btnbatteryfail);
		batteryCheckBack = (Button) findViewById(R.id.btnbatteryback);
		
		config = new Configuration(this);
		
		batteryCheckOk.setOnClickListener(linstener);
		batteryCheckFail.setOnClickListener(linstener);
		batteryCheckBack.setOnClickListener(linstener);
		
	}

	public void onBatteryInfoReceiver(int intLevel, int intScale, int plugged) {

		((TextView)findViewById(R.id.voltage_count)).setText(intScale+"mA");
		((TextView)findViewById(R.id.current_count)).setText(intLevel+"mA");
		((TextView)findViewById(R.id.capacity_count)).setText("" + String.valueOf(intLevel * 100 / intScale)+ "%");
		if (plugged == 0){
			if(isCharge){
				((TextView)findViewById(R.id.plug_state)).setText("unpluging");
				((TextView)findViewById(R.id.charging_state)).setText("Discharging");
				config.setBATTERYTestOk(1);
				battery.this.finish();
				
			}else{
				((TextView)findViewById(R.id.plug_state)).setText("is pluging");
				((TextView)findViewById(R.id.charging_state)).setText("is charging");
			}
		}
		if (plugged == 1){
			isCharge = true;
			((TextView)findViewById(R.id.charging_state)).setText("is charging");
			((TextView)findViewById(R.id.plug_state)).setText("pluging");
		}else{
			((TextView)findViewById(R.id.charging_state)).setText("Discharging");
			((TextView)findViewById(R.id.plug_state)).setText("unpluging");
		}
			
		
	}
	
	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
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

	@Override
	public void onDestroy() {
		unregisterReceiver(mBatInfoReceiver);
		System.gc();
		super.onDestroy();
	}
}