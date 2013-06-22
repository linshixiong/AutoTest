package com.emdoor.autotest.testcase;

import java.io.File;

import org.w3c.dom.Text;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.DeviceManager;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MrioUsbTest extends Activity {
	private int plugged;

	private TextView tv01, tv02;

	private Button BtnusbOk, BtnusbFail, BtnusbBack;

	private Configuration config;

	private Boolean plugin = false;

	private Boolean removeOk = false;

	private final String TAG = "MrioUsbTest";

	private static final int MESSAGETYPE_01 = 0x0001;

	private static final int MESSAGETYPE_02 = 0x0004;

	private boolean isTFCard;

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.intent.action.MEDIA_EJECT")) {
				Log.d(TAG, "media eject");
				Message msg_listData = new Message();
				msg_listData.what = MESSAGETYPE_02;
				handler.sendMessage(msg_listData);
			}
			if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
				Log.d(TAG, "media mounted");
				Log.d("fadsfsadf", "fdsafads");
				Message msg_listData = new Message();
				msg_listData.what = MESSAGETYPE_01;
				handler.sendMessage(msg_listData);
			}

		}
	};

	@Override
	public void onCreate(Bundle savedinstanceState) {
		super.onCreate(savedinstanceState);
		setContentView(R.layout.usb);
		BtnusbOk = (Button) findViewById(R.id.btnusbok);
		BtnusbFail = (Button) findViewById(R.id.btnusbfail);
		BtnusbBack = (Button) findViewById(R.id.btnusbback);

		config = new Configuration(this);

		BtnusbOk.setVisibility(BtnusbOk.INVISIBLE);

		BtnusbOk.setOnClickListener(listener);
		BtnusbFail.setOnClickListener(listener);
		BtnusbBack.setOnClickListener(listener);
		isTFCard = getIntent().getBooleanExtra("isTFCard", false);
		
		
		((TextView)findViewById(R.id.tvusb)).setText(isTFCard?"SD Card Test":"USB Storage Test");
	}

	private final Button.OnClickListener listener = new Button.OnClickListener() {
		public void onClick(View args0) {

			if (args0.getId() == R.id.btnusbok) {
				if (isTFCard) {
					config.setTFCARDTestOk(1);
				} else {
					config.setUSBTestOk(1);
				}
			}
			if (args0.getId() == R.id.btnusbfail) {
				if (isTFCard) {
					config.setTFCARDTestOk(2);
				} else {
					config.setUSBTestOk(2);
				}
			}
			if (args0.getId() == R.id.btnusbback) {
				if (isTFCard) {
					config.setTFCARDTestOk(0);
				} else {
					config.setUSBTestOk(0);
				}
			}
			finish();
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGETYPE_01:
				if (isTFCard) {
					if (DeviceManager.getInstance(MrioUsbTest.this)
							.isExternalSDCardMounted()) {
						plugin = true;
						((TextView) findViewById(R.id.plugintest))
								.setText("PASS");
						checkIsPass();
					}
				} else {

					if (DeviceManager.getInstance(MrioUsbTest.this)
							.isUSBStorageMounted()) {
						plugin = true;
						((TextView) findViewById(R.id.plugintest))
								.setText("PASS");
						checkIsPass();
					}
				}
				break;
			case MESSAGETYPE_02:

				removeOk = true;
				((TextView) findViewById(R.id.plugouttest)).setText("PASS");
				checkIsPass();

				break;
			}
			super.handleMessage(message);
		}

		private void checkIsPass() {
			// TODO Auto-generated method stub
			if (plugin && removeOk) {
				if (isTFCard) {
					config.setTFCARDTestOk(1);
				} else {
					config.setUSBTestOk(1);
				}

				finish();
			}

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addDataScheme("file");
		registerReceiver(mBatInfoReceiver, intentFilter);
		// Toast.makeText(usbtest.this, "0", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mBatInfoReceiver);
	}

	@SuppressWarnings("unused")
	private void OnDestroy() {
		// unregisterReceiver(mBatInfoReceiver);
		System.gc();
		super.onDestroy();
	}
}
