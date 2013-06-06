package com.emdoor.autotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	private static SharedPreferences prefs;
	private static Context sContext;
	public static void init(Context context)
	{
		sContext=context;
		prefs =PreferenceManager.getDefaultSharedPreferences(context) ;
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
	
	public static String getServerHost(){
		return prefs.getString("server_host",sContext.getString(R.string.def_server_host));
	}
	
	public static void setServerHost(String host){
		prefs.edit().putString("server_host", host).commit();
	}
	
	public static int getPort()
	{
		return prefs.getInt("server_port", sContext.getResources().getInteger(R.integer.def_server_port));
	}
	
	public static void setPort(int port){
		prefs.edit().putInt("server_port", port).commit();
	}
	
	
}
