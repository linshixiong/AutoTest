package com.emdoor.autotest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AutoTestService extends Service {

	protected static final String TAG = "AutoTestService";
	private TCPClient client;
	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private void connectServer() {
		if (client == null || !client.isConnected()) {
			String host = getString(R.string.def_server_host);
			int port = getResources().getInteger(R.integer.def_server_port);
			client = new TCPClient(host, port, this, handler);

		}

	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case Messages.MSG_WIFI_ENABLED:

				break;
			case Messages.MSG_CONNECT_SUCCUSS:
				//button.setText("ÕýÔÚ²âÊÔ");
				//button.setEnabled(false);
				//menu.getItem(0).setVisible(true);
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
