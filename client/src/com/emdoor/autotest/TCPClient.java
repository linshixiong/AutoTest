package com.emdoor.autotest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

public class TCPClient implements Runnable {
	private static final String TAG = "TCPClient";
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Handler mHandler;
	private String content = "";
	private String serverIP;
	private int port;
	private boolean bConnected = false;

	public TCPClient(String serverIP, int port, Handler handler) {
		this.mHandler = handler;
		this.serverIP = serverIP;
		this.port = port;

		new Thread(this).start();

	}

	public boolean isConnected() {
		return socket.isConnected();
	}

	public void WriteString(String str) {

		if (str == null) {
			return;
		}
		byte[] b = str.getBytes();
		try {
			dos.write(b);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 显示从socket返回的数据
	public void ReadInt() {
		try {
			System.out.println(dis.readInt());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {
			socket = new Socket(serverIP, port);

			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			this.WriteString("Hello,I'm ready!");
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			while (true) {
				if (socket.isConnected()) {

					content = dis.readLine();
					if (content != null) {
						Message msg = new Message();
						msg.what = Messages.MSG_CMD_RECEIVE;
						msg.obj = content;
						Log.d(TAG, "receive data:" + content);
						mHandler.sendMessage(msg);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				dis.close();
				dos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
