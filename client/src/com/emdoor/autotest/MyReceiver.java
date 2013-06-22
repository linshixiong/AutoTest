package com.emdoor.autotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			
			Intent activty=new Intent();
			activty.setClass(context, MainActivity.class);
			activty.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//context.startActivity(activty);
			
		}
		
	}

}
