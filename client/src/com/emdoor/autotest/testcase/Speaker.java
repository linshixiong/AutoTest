package com.emdoor.autotest.testcase;

import java.io.IOException;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Speaker extends Activity implements OnClickListener {

	Button[] btns = new Button[5];
	int[] ids = { R.id.left_control, R.id.right_control, R.id.btn_speeker_1,
			R.id.btn_speeker_2, R.id.btn_speeker_3 };
	Configuration config;

	boolean left_enable = true, right_enable = true;

	private MediaPlayer mp;
	private boolean isHeadset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speeker);

		isHeadset = getIntent().getBooleanExtra("isHeadset", false);
		if (isHeadset) {
			((TextView) findViewById(R.id.TextView_speaker_title))
					.setText(R.string.headset_title);
		}

		for (int i = 0; i < 5; i++) {
			btns[i] = (Button) findViewById(ids[i]);
			btns[i].setOnClickListener(this);
		}
		if(isHeadset){
			mp = MediaPlayer.create(this, R.raw.speeker_test);
		}else
		{
			mp = MediaPlayer.create(this, R.raw.audio_1k);
		}
		config = new Configuration(this);
	
		mp.setLooping(true);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		if (!isHeadset) {
			findViewById(R.id.left_control).setVisibility(View.GONE);
			findViewById(R.id.right_control).setVisibility(View.GONE);

		} else {
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.intent.action.HEADSET_PLUG");
			registerReceiver(receiver, filter);
		}
		if (!isHeadset) {
			mp.start();
		}
		super.onResume();
	}

	public BroadcastReceiver receiver = new BroadcastReceiver() {

		// private static final String TAG = "HeadsetPlugReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("state")) {
				if (intent.getIntExtra("state", 0) == 0) {
					showDialog();
					
					if (mp.isPlaying()) {
						mp.pause();
					}
					// Toast.makeText(context, "headset not connected",
					// Toast.LENGTH_LONG).show();
				} else if (intent.getIntExtra("state", 0) == 1) {
					
					if(dialog!=null){
						dialog.cancel();
					}
					if (!mp.isPlaying()) {
						mp.start();
					}
					// Toast.makeText(context, "headset connected",
					// Toast.LENGTH_LONG).show();
				}
			}

		}

	};
	private AlertDialog dialog;

	private void showDialog() {
	  dialog=	new AlertDialog.Builder(this)
				.setTitle("Plug In Headset")
				.setMessage(
						"Please plug in headset.")
				.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
							finish();
					}
				})
				.setCancelable(false).show();
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_speeker_1:

			if (isHeadset) {
				config.setHeadsetTestOk(1);
			} else {
				config.setSPEAKERTestOk(1);
			}
			finish();
			break;
		case R.id.btn_speeker_2:
			if (isHeadset) {
				config.setHeadsetTestOk(2);
			} else {
				config.setSPEAKERTestOk(2);
			}
			finish();
			break;
		case R.id.btn_speeker_3:
			if (isHeadset) {
				config.setHeadsetTestOk(0);
			} else {
				config.setSPEAKERTestOk(0);
			}
			finish();
			break;
		case R.id.left_control:
			if (left_enable) {
				left_enable = false;
				btns[0].setText(getString(R.string.left_speeker_disable));
				mp.setVolume(0.0f, right_enable ? 1.0f : 0.0f);

			} else {
				left_enable = true;
				btns[0].setText(getString(R.string.left_speeker_enable));
				mp.setVolume(1.0f, right_enable ? 1.0f : 0.0f);
			}

			break;
		case R.id.right_control:
			if (right_enable) {
				right_enable = false;
				btns[1].setText(getString(R.string.right_speeker_disable));
				mp.setVolume(left_enable ? 1.0f : 0.0f, 0.0f);
			} else {
				right_enable = true;
				btns[1].setText(getString(R.string.right_speeker_enable));
				mp.setVolume(left_enable ? 1.0f : 0.0f, 1.0f);
			}

			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mp.isPlaying()) {
			mp.stop();
		}

		mp.release();
		if (isHeadset) {
			unregisterReceiver(receiver);
		}
	}

}
