package com.emdoor.autotest.testcase;

import java.util.HashMap;
import java.util.Iterator;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GpsTest extends Activity {
	
	private Button gpsCheckOk = null;
	private Button gpsCheckFail = null;
	private Button gpsCheckBack = null;
	private Configuration config;
	private TextView textView_gps_msg = null;
	
	private LocationManager mLocationManager;
	GpsStatus gpsStatus;
	private MystatusListener statusListener;
	HashMap<Integer, Integer> passSatellites = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> Satellites = new HashMap<Integer, Integer>();
	String msg;
	
	private final int CN_PASS = 38;
	private final int needMinCount = 3;
	private final int MESSAGE_STATE = 1;
	private final int MESSAGE_OK = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gps);
		
		gpsCheckOk = (Button)findViewById(R.id.btn_gps_1);
		gpsCheckFail = (Button)findViewById(R.id.btn_gps_2);
		gpsCheckBack = (Button)findViewById(R.id.btn_gps_3);
		textView_gps_msg = (TextView)findViewById(R.id.textView_gps_msg);	
		config = new Configuration(this);	
		statusListener = new MystatusListener();
		
		//gpsCheckOk.setVisibility(gpsCheckOk.INVISIBLE);
		
		gpsCheckOk.setOnClickListener(linstener1);
		gpsCheckFail.setOnClickListener(linstener1);
		gpsCheckBack.setOnClickListener(linstener1);	
		
		Log.d("gps","=======" + Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
		
	
		startGPSTest();		
	}
	
	private void startGPSTest() {
		// TODO Auto-generated method stub
		
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiManager.setWifiEnabled(false);
		if(BluetoothAdapter.getDefaultAdapter()!=null){
			BluetoothAdapter.getDefaultAdapter().disable();
		}
		try {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(this.statusListener);
		try{
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, locationListener);
		}catch(Exception e){
			textView_gps_msg.setText(R.string.nogpshw);			
		}
				
	}
	
	//位置监听
	LocationListener locationListener = new LocationListener() {
		//GPS状态变化时触发
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		//GPS开启时触发
		public void onProviderEnabled(String provider) {
		}
		//GPS禁用时触发
		public void onProviderDisabled(String provider) {
		}
		//位置信息变化时触发
		public void onLocationChanged(Location location) {
		}

	};	
	

	class MystatusListener implements GpsStatus.Listener {

		public void onGpsStatusChanged(int event) {		
			
			switch (event) {
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				GpsStatus gpsStatus=mLocationManager.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                msg = "Satellites:\n";
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) { 
                	count ++;
                    GpsSatellite gpsSatellite = iters.next();
                   
                    int snr = (int) gpsSatellite.getSnr();
					int prn = gpsSatellite.getPrn();
					if (snr < 0) {
						continue;
					}					
					msg += "" + prn + "(" + snr + ")\n";
					
					//保存卫星数据，prn对应卫星标号，snr表示信号值
					Satellites.put(prn, snr);
					if (snr >= CN_PASS) {
						passSatellites.put(prn, snr);
					}else{
						passSatellites.remove(prn);
					}	 
                }                
                int passCount = passSatellites.size();
				msg += "Passed Satellites:" + passCount;
				handler.sendEmptyMessage(MESSAGE_STATE);
				
				if(passCount >= needMinCount){
					handler.sendEmptyMessageDelayed(MESSAGE_OK, 3000);
				}
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:			
				break;
			default:
				break;
			}
		}
	}
	
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {			
			switch(message.what){
			case MESSAGE_OK:				
				gpsTestOK();
				break;
			case MESSAGE_STATE:
				textView_gps_msg.setText(msg);
				break;
			}
			super.handleMessage(message);
			
		}		
	};
	
	private void gpsTestOK() {
		// TODO Auto-generated method stub
		config.setGPSTestOk(1);
		GpsTest.this.finish();
		
	}
	
	public void onPause(){	
		Log.d("gps test" , "pause");
		mLocationManager.removeGpsStatusListener(statusListener);
		mLocationManager.removeUpdates(locationListener);
		try {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onPause();
	}
	
	public void onStop(){	
		Log.d("gps test" , "stop");
		try {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
	}
	
	public void onDestroy(){	
		Log.d("gps test" , "destroy");
		try {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}
	

	private final OnClickListener linstener1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.btn_gps_1:
				config.setGPSTestOk(1);
				break;
			case R.id.btn_gps_2:
				config.setGPSTestOk(2);
				break;
			case R.id.btn_gps_3:
				config.setGPSTestOk(0);
				break;
			default:
				config.setGPSTestOk(0);
				break;
			}
			//unregisterReceiver(mReceiver);
			GpsTest.this.finish();
		}
		
		
	};	

}
