package com.emdoor.autotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings {
	private static final String TAG = null;
	private static SharedPreferences prefs;
	private static Context sContext;
	public static void init(Context context)
	{
		sContext=context;
		prefs =PreferenceManager.getDefaultSharedPreferences(context) ;
	}
	
	public static void reset(){
		prefs.edit().clear().commit();
	}
	
	
	public static String getSSID(){
		return prefs.getString("ssid",sContext.getString(R.string.def_wifi_ssid));
	}
	
	public static void setSSID(String ssid){
		prefs.edit().putString("ssid", ssid).commit();
	}
	
	public static String getPwd(){
		return prefs.getString("password",sContext.getString(R.string.def_wifi_pwd));
	}
	
	public static void setPwd(String pwd){
		prefs.edit().putString("password", pwd).commit();
	}
	
	public static String getServerHost(int deviceIndex){
		
		String host=sContext.getResources().getStringArray(R.array.def_server_host_list)[deviceIndex];
		return prefs.getString("server_host_"+deviceIndex,host);
	}
	
	public static void setServerHost(int deviceIndex,String host){
		prefs.edit().putString("server_host_"+deviceIndex, host).commit();
	}
	
	public static int getPort(int deviceIndex)
	{
		int port=0;
		try{
			port=sContext.getResources().getIntArray(R.array.def_server_port_list)[deviceIndex];
		}catch(Exception e){
			e.printStackTrace();
			port=3001;
		}
		return prefs.getInt("server_port_"+deviceIndex, port);
	}
	
	public static void setPort(int deviceIndex,int port){
		prefs.edit().putInt("server_port_"+deviceIndex, port).commit();
	}
	
	public static void setBLEDeviceMAC(String mac){
		prefs.edit().putString("ble_device_mac", mac).commit();
	}
	
	public static String getBLEDeviceMAC(){
		return prefs.getString("ble_device_mac",sContext.getString(R.string.def_ble_device_mac));
	}
}
