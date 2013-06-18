package com.emdoor.autotest.testcase;


import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.DeviceManager;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Sdcard extends Activity implements OnClickListener {

	
	Button passBtn,failBtn,backBtn;
	Configuration config;
	TextView msgTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcard);
		config = new Configuration(this);
		passBtn = (Button) findViewById(R.id.btn_sdcard_1);
		failBtn = (Button) findViewById(R.id.btn_sdcard_2);
		backBtn = (Button) findViewById(R.id.btn_sdcard_3);
		passBtn.setOnClickListener(this);
		failBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		msgTxt = (TextView) findViewById(R.id.TextView_sdcard_01);
		
		
		if(DeviceManager.getInstance(null).isExternalSDCardMounted()){		
			Log.e("FAS", ".......................");
			String path = "/storage/external_storage/sdcard1/";
			StatFs fs = new StatFs(path);
			StringBuffer sb = new StringBuffer();
			sb.append("Total Block: ").append(fs.getBlockCount()).append("\n")
			.append("Block size: ").append(fs.getBlockSize()).append("\n")
			.append("Free Blocks: ").append(fs.getFreeBlocks()).append("\n");
			msgTxt.setText(sb.toString());
			
			config.setTFCARDTestOk(1);
			
		}else{
			config.setTFCARDTestOk(2);
		}
		
		new Thread(){
		   public void run(){
		       try{
				sleep(3000);
				finish();
			   }catch(Exception e){
			   }
		   }
		}.start();
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_sdcard_1:
			config.setTFCARDTestOk(1);
			break;
		case R.id.btn_sdcard_2:
			config.setTFCARDTestOk(2);
			break;
		case R.id.btn_sdcard_3:
			config.setTFCARDTestOk(0);
			break;
		default:
			break;
		}
		finish();
	}

}
