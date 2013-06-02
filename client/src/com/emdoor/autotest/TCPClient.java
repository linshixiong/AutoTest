package com.emdoor.autotest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	public TCPClient(String serverIP, int port) {
		try {
			socket = new Socket(serverIP, port);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}



	public void WriteString(String str) {
	
		if(str==null){
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

}
