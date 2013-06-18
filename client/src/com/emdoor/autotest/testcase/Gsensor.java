package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Gsensor extends Activity implements OnClickListener, SensorEventListener {

	Button failBtn,backBtn;
	MySurfaceView myView;
	Configuration config;
	TextView curTxt,resultTxt;
	SensorManager mSensorManager;
	Sensor sensor;
	StringBuffer curResult = new StringBuffer();
	String[] resultStrs = {"x axis points up(x:9,y:0,z:0)",
						   "y axis points up(x:0,y:9,z:0)",
						   "z axis points up(x:0,y:0,z:9)"};
	boolean xOk = false,yOk = false,zOk = false;
	
	
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
		resultTxt = (TextView) findViewById(R.id.textview_result);
		
		curResult = curResult.append(resultStrs[0]);
		resultTxt.setText(curResult.toString());
		
		mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);   
		sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  	         
        
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
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
		int x,y,z;
		x = (int) event.values[0];
		y = (int) event.values[1];
		z = (int) event.values[2];
		curTxt.setText("x:"+x+",y:"+y+",z:"+z);
		myView.setxOffset(x);
		myView.setyOffset(y);
		if(!xOk){
			if(x == 9 && y == 0 && z == 0){
				xOk = true;
				curResult.append(":pass \n");
				curResult.append(resultStrs[1]);
				resultTxt.setText(curResult.toString());
			}
		}else{
			if(!yOk){
				if(x == 0 && y == 9 && z == 0){
					yOk = true;
					curResult.append(":pass \n");
					curResult.append(resultStrs[2]);
					resultTxt.setText(curResult.toString());
				}
			}else{
				if(!zOk){
					if(x == 0 && y == 0 && z == 9){
						zOk = true;
						curResult.append(":pass \n");
						resultTxt.setText(curResult.toString());
						config.setGSENSORTestOk(1);
						finish();
					}
				}
			}
		}
	}
	
	protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        myView.setRun(false);
    }
    
}
