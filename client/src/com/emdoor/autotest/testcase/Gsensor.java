package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Gsensor extends Activity implements OnClickListener,
		SensorEventListener {

	Button failBtn, backBtn;
	MySurfaceView myView;
	Configuration config;
	TextView curTxt;
	SensorManager mSensorManager;
	Sensor sensor;
	StringBuffer curResult = new StringBuffer();
	String[] resultStrs = { "x axis points up(x:9,y:0,z:0)",
			"y axis points up(x:0,y:9,z:0)", "z axis points up(x:0,y:0,z:9)" };
	boolean leftOk = false, rightOk = false, topOk = false,bottomOK=false,centerOK=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsensor);
		myView = (MySurfaceView) findViewById(R.id.surfaceview);
		config = new Configuration(this);
		failBtn = (Button) findViewById(R.id.btn_gsensor_2);
		backBtn = (Button) findViewById(R.id.btn_gsensor_3);
		failBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		curTxt = (TextView) findViewById(R.id.textview_current_value);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_gsensor_2:
			config.setGSENSORTestOk(2);
			break;
		case R.id.btn_gsensor_3:
			config.setGSENSORTestOk(0);
			break;
		default:
			break;
		}
		finish();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int x, y, z;
		x = (int) event.values[0];
		y = (int) event.values[1];
		z = (int) event.values[2];
		curTxt.setText("x:" + x + ",y:" + y + ",z:" + z);
		myView.setxOffset(x);
		myView.setyOffset(y);

		if (x >= 8&&!leftOk) {
			findViewById(R.id.left).setVisibility(View.VISIBLE);
			leftOk=true;
		}
		
		if(x<=-8&&!rightOk){
			findViewById(R.id.right).setVisibility(View.VISIBLE);
			rightOk=true;
		}
		
		if (y >= 8&&!topOk) {
			findViewById(R.id.top).setVisibility(View.VISIBLE);
			topOk = true;
		}
		
		if(y<=-8&&!bottomOK){
			findViewById(R.id.bottom).setVisibility(View.VISIBLE);
			bottomOK=true;
		}
		
		if (z >= 8&&!centerOK) {
			centerOK = true;		
		}

		if(leftOk&&rightOk&&topOk&&bottomOK&&centerOK){
			config.setGSENSORTestOk(1);
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		myView.setRun(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		myView.setRun(false);
	}

}
