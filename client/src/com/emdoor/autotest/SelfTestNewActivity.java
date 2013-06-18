package com.emdoor.autotest;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.provider.Settings;
import android.os.PowerManager;



import com.emdoor.autotest.R;
import com.emdoor.autotest.testcase.*;

public class SelfTestNewActivity extends Activity {
	/** Called when the activity is first created. */

	MyButton lcd, touch, wifi, bluetooth, gps, evdo, usb, camera, gsensor,
			light, speaker, mic, tfcard, hdmi, battery, keyboard, version,
			brightness;
	Button test, close, clear, uninstall, recovery;
	Configuration config;
	Intent intent;
	String TAG = "SelfTestNewActivity";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lcd = (MyButton) findViewById(R.id.lcd);
		touch = (MyButton) findViewById(R.id.touch);
		wifi = (MyButton) findViewById(R.id.wifi);
		bluetooth = (MyButton) findViewById(R.id.bluetooth);
		gps = (MyButton) findViewById(R.id.gps);
		evdo = (MyButton) findViewById(R.id.evdo);
		usb = (MyButton) findViewById(R.id.usb);
		camera = (MyButton) findViewById(R.id.camera);
		gsensor = (MyButton) findViewById(R.id.gsensor);
		light = (MyButton) findViewById(R.id.lightsensor);
		speaker = (MyButton) findViewById(R.id.speaker);
		mic = (MyButton) findViewById(R.id.mic);
		tfcard = (MyButton) findViewById(R.id.tfcard);
		hdmi = (MyButton) findViewById(R.id.hdmi);
		battery = (MyButton) findViewById(R.id.battery);
		keyboard = (MyButton) findViewById(R.id.keyboard);
		version = (MyButton) findViewById(R.id.version);
		brightness = (MyButton) findViewById(R.id.brightness);
		config = new Configuration(this);

		test = (Button) findViewById(R.id.test);
		close = (Button) findViewById(R.id.close);
		clear = (Button) findViewById(R.id.clear);
		uninstall = (Button) findViewById(R.id.uninstall);
		recovery = (Button) findViewById(R.id.recovery);
		test.setOnClickListener(buttonClick);
		close.setOnClickListener(buttonClick);
		clear.setOnClickListener(buttonClick);
		uninstall.setOnClickListener(buttonClick);
		recovery.setOnClickListener(buttonClick);

		refuseView();

		setClick();
		
		Settings.System.putInt(this.getContentResolver(), "hdmi_auto_switch",
                    1);

	}

	private final OnClickListener buttonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.test:
				startSelftest();
				break;
			case R.id.close:
				stopTheView();
				break;
			case R.id.clear:
				clearData();
				break;
			case R.id.uninstall:
				uninstallMySelf();
				break;
			case R.id.recovery:
				startRecoveryMode();
				break;
			default:
				break;
			}

		}

		private void startRecoveryMode() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(SelfTestNewActivity.this);
			builder.setTitle("reboot for recovery mode");
			builder.setMessage("Are you sure you want to reboot for recovery mode?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {							
							PowerManager pm = (PowerManager) SelfTestNewActivity.this.getSystemService(Context.POWER_SERVICE);
							pm.reboot("recovery");
						}
					});
			builder.setNeutralButton("cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();
			
		}

		private void uninstallMySelf() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(SelfTestNewActivity.this);
			builder.setTitle("Uninstall");
			builder.setMessage("Are you sure you want to uninstall?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Uri packageURI = Uri.parse("package:com.emdoor.selftest"); 
							Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI); 
							startActivity(uninstallIntent); 

						}
					});
			builder.setNeutralButton("cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();
			
		}

		private void clearData() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(SelfTestNewActivity.this);
			builder.setTitle("Clear the data");
			builder.setMessage("Are you sure you want to clear?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SelfTestNewActivity.this.config.resetData();
							SelfTestNewActivity.this.refuseView();

						}
					});
			builder.setNeutralButton("cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();
			
		}

		private void stopTheView() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(SelfTestNewActivity.this);
			builder.setTitle("EXIT");
			builder.setMessage("Are you sure you want to exit?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SelfTestNewActivity.this.finish();

						}
					});
			builder.setNeutralButton("cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();

		}

		private void startSelftest() {
			// TODO Auto-generated method stub
			
			if (config.isBRIGHTNESSDefSet()) {
				testForBRIGHTNESS();
			}
			if (config.isVERSIONDefSet()) {
				testForVERSION();
			}
			if (config.isKEYBOARDDefSet()) {
				testForKEYBOARD();
			}
			if (config.isBATTERYDefSet()) {
				testForBATTERY();
			}			
			if (config.isTFCARDDefSet()) {
				testForSDCARD();
			}
			if (config.isSPEAKERDefSet()) {
				testForSPEAKER();
			}
			if (config.isGSENSORDefSet()) {
				testForGSENSOR();
			}
			if (config.isCAMERADefSet()) {
				testForCAMERA();
			}
			if (config.isUSBDefSet()) {
				testForUSB();
			}
			if (config.isEVDODefSet()) {
				testForEVDO();
			}
			if (config.isGPSDefSet()) {
				testForGPS();
			}
			if (config.isBLUETOOTHDefSet()) {
				testForBLUETOOTH();
			}			
			if (config.isWIFIDefSet()) {
				testForWIFI();
			}	
			if (config.isTOUCHDefSet()) {
				testForTOUCH();
			}
			if (config.isLCDDefSet()) {
				testForLCD();
			}
			

		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "STATE=" + config.isLCDTestOk());
		refuseView();
	}

	private void setClick() {
		// TODO Auto-generated method stub
		lcd.setOnClickListener(myOnClickLinstener);
		lcd.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lcd.setOnClickCheckBoxState(v);
				config.setLCDDef(lcd.isCheck);
			}

		});
		touch.setOnClickListener(myOnClickLinstener);
		touch.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				touch.setOnClickCheckBoxState(v);
				config.setTOUCHDef(touch.isCheck);
			}

		});
		wifi.setOnClickListener(myOnClickLinstener);
		wifi.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifi.setOnClickCheckBoxState(v);
				config.setWIFIDef(wifi.isCheck);
			}

		});
		bluetooth.setOnClickListener(myOnClickLinstener);
		bluetooth.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bluetooth.setOnClickCheckBoxState(v);
				config.setBLUETOOTHDef(bluetooth.isCheck);
			}

		});
		gps.setOnClickListener(myOnClickLinstener);
		gps.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gps.setOnClickCheckBoxState(v);
				config.setGPSDef(gps.isCheck);
			}

		});
		evdo.setOnClickListener(myOnClickLinstener);
		evdo.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				evdo.setOnClickCheckBoxState(v);
				config.setEVDODef(evdo.isCheck);
			}

		});
		usb.setOnClickListener(myOnClickLinstener);
		usb.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				usb.setOnClickCheckBoxState(v);
				config.setUSBDef(usb.isCheck);
			}

		});
		camera.setOnClickListener(myOnClickLinstener);
		camera.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera.setOnClickCheckBoxState(v);
				config.setCAMERADef(camera.isCheck);
			}

		});
		gsensor.setOnClickListener(myOnClickLinstener);
		gsensor.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gsensor.setOnClickCheckBoxState(v);
				config.setGSENSORDef(gsensor.isCheck);
			}

		});
		light.setOnClickListener(myOnClickLinstener);
		speaker.setOnClickListener(myOnClickLinstener);
		speaker.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				speaker.setOnClickCheckBoxState(v);
				config.setSPEAKERDef(speaker.isCheck);
			}

		});
		mic.setOnClickListener(myOnClickLinstener);
		mic.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mic.setOnClickCheckBoxState(v);
				config.setMICDef(mic.isCheck);
			}

		});
		tfcard.setOnClickListener(myOnClickLinstener);
		tfcard.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tfcard.setOnClickCheckBoxState(v);
				config.setTFCARDDef(tfcard.isCheck);
			}

		});
		hdmi.setOnClickListener(myOnClickLinstener);
		hdmi.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hdmi.setOnClickCheckBoxState(v);
				config.setHDMIDef(hdmi.isCheck);
			}

		});
		battery.setOnClickListener(myOnClickLinstener);
		battery.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				battery.setOnClickCheckBoxState(v);
				config.setBATTERYDef(battery.isCheck);
			}

		});
		keyboard.setOnClickListener(myOnClickLinstener);
		keyboard.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				keyboard.setOnClickCheckBoxState(v);
				config.setKEYBOARDDef(keyboard.isCheck);
			}

		});
		version.setOnClickListener(myOnClickLinstener);
		version.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				version.setOnClickCheckBoxState(v);
				config.setVERSIONDef(version.isCheck);
			}

		});
		brightness.setOnClickListener(myOnClickLinstener);
		brightness.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				brightness.setOnClickCheckBoxState(v);
				config.setBRIGHTNESSDef(brightness.isCheck);
			}

		});

	}

	private final OnClickListener myOnClickLinstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.lcd:
				testForLCD();
				break;
			case R.id.touch:
				testForTOUCH();
				break;
			case R.id.wifi:
				testForWIFI();
				break;
			case R.id.bluetooth:
				testForBLUETOOTH();
				break;
			case R.id.battery:
				testForBATTERY();
				break;
			case R.id.keyboard:
				testForKEYBOARD();
				break;
			case R.id.version:
				testForVERSION();
				break;
			case R.id.camera:
				testForCAMERA();
				break;
			case R.id.usb:
				testForUSB();
				break;
			case R.id.brightness:
				testForBRIGHTNESS();
				break;
			case R.id.gsensor:
				testForGSENSOR();
				break;
			case R.id.tfcard:
				testForSDCARD();
				break;
			case R.id.speaker:
				testForSPEAKER();
				break;				
			case R.id.mic:
				testForMIC();
				break;
			case R.id.hdmi:
				testForHDMI();
				break;
			case R.id.gps:
				testForGPS();
				break;
			case R.id.evdo:
				testForEVDO();
				break;
			}
		}

	};

	private void testForLCD() {
		intent = new Intent(SelfTestNewActivity.this, lcd.class);
		startActivity(intent);
	}

	private void testForTOUCH() {
		intent = new Intent(SelfTestNewActivity.this, touchscreentest.class);
		startActivity(intent);
	}

	private void testForWIFI() {
		intent = new Intent(SelfTestNewActivity.this, wifi.class);
		startActivity(intent);
	}

	private void testForUSB() {
		intent = new Intent(SelfTestNewActivity.this, MrioUsbTest.class);
		startActivity(intent);
	}

	private void testForCAMERA() {
		intent = new Intent(SelfTestNewActivity.this, CameraTest.class);
		startActivity(intent);
	}

	private void testForBRIGHTNESS() {
		intent = new Intent(SelfTestNewActivity.this, Brightness.class);
		startActivity(intent);
	}

	private void testForGSENSOR() {
		intent = new Intent(SelfTestNewActivity.this, Gsensor.class);
		startActivity(intent);
	}

	private void testForSDCARD() {
		intent = new Intent(SelfTestNewActivity.this, Sdcard.class);
		startActivity(intent);
	}

	private void testForSPEAKER() {
		intent = new Intent(SelfTestNewActivity.this, Speeker.class);
		startActivity(intent);
	}

	private void testForBLUETOOTH() {
		intent = new Intent(SelfTestNewActivity.this, BluetoothTest.class);
		startActivity(intent);
	}

	private void testForBATTERY() {
		intent = new Intent(SelfTestNewActivity.this, battery.class);
		startActivity(intent);
	}

	private void testForVERSION() {
		intent = new Intent(SelfTestNewActivity.this, VersionTest.class);
		startActivity(intent);
	}

	private void testForKEYBOARD() {
		intent = new Intent(SelfTestNewActivity.this, KeyboardTest.class);
		startActivity(intent);
	}
	
	private void testForMIC() {
		intent = new Intent(SelfTestNewActivity.this, SoundRecorder.class);
		startActivity(intent);
	}
	
	private void testForHDMI(){
		intent = new Intent(SelfTestNewActivity.this, HdmiTest.class);
		startActivity(intent);
	}
	
	private void testForGPS(){
		intent = new Intent(SelfTestNewActivity.this, GpsTest.class);
		startActivity(intent);
	}
	
	private void testForEVDO(){
		intent = new Intent(SelfTestNewActivity.this, ThreeGTest.class);
		startActivity(intent);
	}

	private void refuseView() {
		// TODO Auto-generated method stub
		lcd.setCheckBoxState(config.isLCDDefSet());
		setBackgroundforMyView(lcd, config.isLCDTestOk());

		touch.setCheckBoxState(config.isTOUCHDefSet());
		setBackgroundforMyView(touch, config.isTOUCHTestOk());

		wifi.setCheckBoxState(config.isWIFIDefSet());
		setBackgroundforMyView(wifi, config.isWIFITestOk());

		bluetooth.setCheckBoxState(config.isBLUETOOTHDefSet());
		setBackgroundforMyView(bluetooth, config.isBLUETOOTHTestOk());

		gps.setCheckBoxState(config.isGPSDefSet());
		setBackgroundforMyView(gps, config.isGPSTestOk());

		evdo.setCheckBoxState(config.isEVDODefSet());
		setBackgroundforMyView(evdo, config.isEVDOTestOk());
		
		usb.setCheckBoxState(config.isUSBDefSet());
		setBackgroundforMyView(usb, config.isUSBTestOk());

		camera.setCheckBoxState(config.isCAMERADefSet());
		setBackgroundforMyView(camera, config.isCAMERATestOk());

		gsensor.setCheckBoxState(config.isGSENSORDefSet());
		setBackgroundforMyView(gsensor, config.isGSENSORTestOk());

		speaker.setCheckBoxState(config.isSPEAKERDefSet());
		setBackgroundforMyView(speaker, config.isSPEAKERTestOk());

		mic.setCheckBoxState(config.isMICDefSet());
		setBackgroundforMyView(mic, config.isMICTestOk());

		tfcard.setCheckBoxState(config.isTFCARDDefSet());
		setBackgroundforMyView(tfcard, config.isTFCARDTestOk());

		hdmi.setCheckBoxState(config.isHDMIDefSet());
		setBackgroundforMyView(hdmi, config.isHDMITestOk());

		battery.setCheckBoxState(config.isBATTERYDefSet());
		setBackgroundforMyView(battery, config.isBATTERYTestOk());

		keyboard.setCheckBoxState(config.isKEYBOARDDefSet());
		setBackgroundforMyView(keyboard, config.isKEYBOARDTestOk());

		version.setCheckBoxState(config.isVERSIONDefSet());
		setBackgroundforMyView(version, config.isVERSIONTestOk());

		brightness.setCheckBoxState(config.isBRIGHTNESSDefSet());
		setBackgroundforMyView(brightness, config.isBRIGHTNESSTestOk());

	}

	public void setBackgroundforMyView(MyButton myButton, int isOK) {
		switch (isOK) {
		case 0:
			myButton.setBackgroundColor(color.background_light);
			break;
		case 1:
			myButton.setBackgroundColor(Color.GREEN);
			break;
		case 2:
			myButton.setBackgroundColor(Color.RED);
			break;
		default:
			myButton.setBackgroundColor(0);
			break;
		}

	}
}