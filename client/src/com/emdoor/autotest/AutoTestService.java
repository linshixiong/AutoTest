package com.emdoor.autotest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class AutoTestService extends Service {

	protected static final String TAG = "AutoTestService";
	private static TCPClient client;
	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.connectServer();
		return super.onStartCommand(intent, flags, startId);
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

			switch (msg.what) {
			case Messages.MSG_WIFI_ENABLED:

				break;
			case Messages.MSG_CONNECT_SUCCUSS:
				Intent intent=new Intent(Intents.ACTION_TCP_CONNECT_STATE_CHANGE);
				AutoTestService.this.sendBroadcast(intent);
				break;
			case Messages.MSG_CONNECT_ERROR:
				String error=msg.obj.toString();
				Toast.makeText(AutoTestService.this,error, Toast.LENGTH_SHORT).show();
				break;
			case Messages.MSG_CMD_RECEIVE:
				String cmd = msg.obj.toString();
				byte[] data = Commands.getInstance(AutoTestService.this).excute(
						cmd);
				if (data != null) {
					Log.d(TAG, "command excute result=" + data.length);
					client.WriteByteArray(data);
				}
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

}
