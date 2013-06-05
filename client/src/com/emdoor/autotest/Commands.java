package com.emdoor.autotest;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

public class Commands{

	public static final String CMD_GET_VERSION = "Test Version";

	public static final String CMD_GET_WIFI_INFO = "WIFI Level and WIFY Address";

	public static final String CMD_GET_BLE_INFO = "BLE Level and BLE Address";
	public static final String CMD_OPEN_BLE = "BLE Open";
	public static final String CMD_CLOSE_BLE = "BLE Close";

	public static final String CMD_SCREEN_RED = "Screen Red";
	public static final String CMD_SCREEN_GREEN = "Screen Green";
	public static final String CMD_SCREEN_BLUE = "Screen Blue";
	public static final String CMD_SCREEN_BLACK = "Screen Black";
	public static final String CMD_SCREEN_WITHE = "Screen White";
	public static final String CMD_SCREEN_NORMAL = "Screen Normal";

	public static final String CMD_SLEEP = "Android Sleep";
	public static final String CMD_SCREEN_OFF = "Screen Off";
	public static final String CMD_SCREEN_ON = "Screen On";

	public static final String CMD_TAKE_A_PICTURE = "Camera Catch";
	public static final String CMD_RECODE_AUDIO = "Record Audio";
	public static final String CMD_PLAY_AUDIO = "Play Audio";
	public static final String CMD_SET_VOLUME = "Set Volume";

	public static final String CMD_GET_GSENSOR_COORDINATE = "3D XYZ";
	public static final String CMD_SD_WRITE = "SD Write";
	public static final String CMD_SET_TIME = "Time Setup";
	public static final String CMD_CHECK_FILE = "Check File";
	public static final String CMD_SN_WRITE = "SN Write";
	public static final String CMD_SN_READ = "SN Read";
	public static final String CMD_CLEAR_HISTORY = "Clear History";
	public static final String CMD_FACTORY_RESET = "Factory Reset";
	public static final String CMD_TEST_END = "Test End";

	public static final HashMap<String, String> mapCmds = new HashMap<String, String>();

	private static final String TAG = "Commands";

	private static Commands instance;
	private Context mContext;
	
	private Commands(Context context){
		this.mContext=context;
	}
	
	public static Commands getInstance(Context context){
		if(instance==null){
			instance=new Commands(context);
		}
		return instance;
	}
	
 	public  byte[] excute(String cmd) {
		if (cmd.toUpperCase().startsWith(CMD_GET_VERSION.toUpperCase())) {
			return getVersion();
		} else if (cmd.toUpperCase()
				.startsWith(CMD_GET_WIFI_INFO.toUpperCase())) {
			return getWifiInfo();
		} else if (cmd.toUpperCase().startsWith(CMD_GET_BLE_INFO.toUpperCase())) {
			return getBleInfo();
		} else if (cmd.toUpperCase().startsWith(CMD_OPEN_BLE.toUpperCase())) {
			return enableBle(true);
		} else if (cmd.toUpperCase().startsWith(CMD_CLOSE_BLE.toUpperCase())) {
			return enableBle(false);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_RED.toUpperCase())) {
			return showBlankScreen(cmd,Color.RED);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_GREEN.toUpperCase())) {
			return showBlankScreen(cmd,Color.GREEN);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_BLUE.toUpperCase())) {
			return showBlankScreen(cmd,Color.BLUE);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_BLACK.toUpperCase())) {
			return showBlankScreen(cmd,Color.BLACK);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_WITHE.toUpperCase())) {
			return showBlankScreen(cmd,Color.WHITE);
		} else if (cmd.toUpperCase()
				.startsWith(CMD_SCREEN_NORMAL.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(
				CMD_TAKE_A_PICTURE.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(CMD_RECODE_AUDIO.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(CMD_PLAY_AUDIO.toUpperCase())) {
			
		} else if (cmd.toUpperCase().startsWith(CMD_SET_VOLUME.toUpperCase())) {
			return setVolume(cmd);
		} else if (cmd.toUpperCase().startsWith(
				CMD_GET_GSENSOR_COORDINATE.toUpperCase())) {
			return getGsensorCoordinate();
		}

		else if (cmd.toUpperCase().startsWith(CMD_SD_WRITE.toUpperCase())) {
			return writeFileToSdcard(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SET_TIME.toUpperCase())) {
			return setTime(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_CHECK_FILE.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(CMD_SN_WRITE.toUpperCase())) {
			return writeSN(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SN_READ.toUpperCase())) {
			return readSN(cmd);
		} else if (cmd.toUpperCase()
				.startsWith(CMD_CLEAR_HISTORY.toUpperCase())) {
			
		} else if (cmd.toUpperCase()
				.startsWith(CMD_FACTORY_RESET.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(CMD_TEST_END.toUpperCase())) {

		}
		return new byte[100];
	}

	
	private  byte[] getVersion(){
		String version= "Test_Version=1.0\r\n";
		return version.getBytes();
	}
	
	private  byte[] getWifiInfo(){
		String result=String.format("Level=%dDB and Address=%s\r\n", WifiHelper.getInstance(mContext).getWifiSignal(),WifiHelper.getInstance(mContext).getWifiMAC());
		return result.getBytes();
	}
	
	private byte[] getBleInfo(){
		String result=String.format("Level=%dDB and Address=%s\r\n",-50, "12:34:56:78:AB:CD");
		return result.getBytes();
	}
	
	private byte[] enableBle(boolean enable){
		String result=enable? "BLE Open OK\r\n":"BLE Close OK\r\n";
		return result.getBytes();
	}
	
	private byte[] showBlankScreen(String cmd,int color){
		Intent intent=new Intent();
		intent.setClass(mContext, BlankActivity.class);
		intent.putExtra("background_color", color);

		mContext.startActivity(intent);
		String result=cmd+" OK\r\n";
		return result.getBytes();
	}
	
	private byte[] getGsensorCoordinate(){
		int x=0;
		int y=0;
		int z=0;
		String result=String.format("X=%d,Y=%d,Z=%d\r\n", x,y,z);
		return result.getBytes();
	}

	private byte[] setVolume(String cmd){
		String result=cmd+" OK\r\n";
		return result.getBytes();
	}
	
	
	private byte[] writeFileToSdcard(String cmd){
		File sdPatch= Environment.getExternalStorageDirectory();
				
		
		Log.d(TAG, "write file to "+sdPatch);
		return null;
	}
	
	private byte[] writeSN(String cmd){
		return (cmd+" OK\r\n").getBytes();
	}
	
	private byte[] readSN(String cmd){
		return (cmd+" OK\r\n").getBytes();
	}
	
	private byte[] setTime(String cmd){
		return null;
	}
}
