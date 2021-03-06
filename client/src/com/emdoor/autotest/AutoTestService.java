package com.emdoor.autotest;

import java.io.IOException;
import java.text.BreakIterator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class AutoTestService extends Service {

	protected static final String TAG = "AutoTestService";
	private static TCPClient client;
	private PowerManager pm;
	private WakeLock wl;
	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}
	
	
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		if(pm!=null){
			wl=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
		}
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.connectServer();
		if(wl!=null&&!wl.isHeld()){
			wl.acquire();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	@Override
	public void onDestroy() {
		if(wl!=null&&wl.isHeld()){
			wl.release();
		}
		super.onDestroy();
	}



	private void connectServer() {
		if (client == null || !client.isConnected()) {
			String host = Settings.getServerHost();
			int port = Settings.getPort();
			client = new TCPClient(host, port, this, handler);
		}
	}
	
	public static boolean isConnected(){
		if(client==null){
			return false;
		}
		return client.isConnected();
	}
	
	public static void disconnect(Context context){
		if(client!=null){
			client.Disconnect();
			client=null;
		}
		
		Intent intent=new Intent(Intents.ACTION_TCP_CONNECT_STATE_CHANGE);
		context.sendBroadcast(intent);
		
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent=null;
			switch (msg.what) {
			case Messages.MSG_WIFI_ENABLED:

				break;
			case Messages.MSG_CONNECT_SUCCUSS:
				intent=new Intent(Intents.ACTION_TCP_CONNECT_STATE_CHANGE);
				AutoTestService.this.sendBroadcast(intent);
				break;
			case Messages.MSG_CONNECT_ERROR:
				String error=msg.obj==null?"connection disconnect": msg.obj.toString();
				Toast.makeText(AutoTestService.this,error, Toast.LENGTH_SHORT).show();
				intent=new Intent(Intents.ACTION_TCP_CONNECT_STATE_CHANGE);
				AutoTestService.this.sendBroadcast(intent);
				break;
			case Messages.MSG_CMD_RECEIVE:
				String cmd = msg.obj.toString();
				byte[] data = Commands.getInstance(AutoTestService.this,handler).excute(
						cmd);
				if (data != null) {
					Log.d(TAG, "command excute result=" + data.length);
					client.WriteByteArray(data);
				}
				break;
			case Messages.MSG_PHOTO_TAKEN:
				byte[] photo=(byte[])msg.obj;
				if(photo!=null){
					byte[] output=Utils.getResponeData(Commands.deviceIndex,0, photo);
					client.WriteByteArray(output);
				}else {
					byte[] output=Utils.getResponeData(Commands.deviceIndex,-1, "Camera Catch ERROR\r\n");
					client.WriteByteArray(output);
				}
				
				break;
			case Messages.MSG_CHANGE_WIFI:
				intent=new Intent(Intents.ACTION_WIFI_AP_CHANGE);
				sendBroadcast(intent);
				disconnect(AutoTestService.this);
				break;
			case Messages.MSG_FACTORY_RESET:
				Log.d(TAG,"send android.intent.action.MASTER_CLEAR");
				sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
				break;
			case Messages.MSG_SCREEN_OFF:
				
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

}
